package nakterdalen.mctales.balance.mixin.enchantingmixins;


import nakterdalen.mctales.balance.enchanting.BalancedEnchantmentHelper;
import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;


@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantingScreenHandlerMixin extends ScreenHandler implements IEnchantingHandler {

    @Shadow
    @Final
    private ScreenHandlerContext context;

    @Shadow
    @Final
    private final Property seed = Property.create();

    @Shadow
    public void onContentChanged(Inventory inventory) {}

    @Shadow
    @Final
    private Inventory inventory;

    @Unique
    public List<List<EnchantmentLevelEntry>> newEnchantmentList = new LinkedList<>();

    @Unique int[] enchantArray = new int[10];

    @Unique
    final Property bookCount = Property.create();

    @Shadow @Final private Random random;

    @Unique
    public Runnable activateScroll = () -> {};
    @Unique
    public Runnable bookCounter = () -> {};
    @Unique
    public Runnable transferEnchants = () -> {};

    protected EnchantingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void injectConstructor(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        for (int i = 0; i < 10; i++) {
            this.addProperty(Property.create(this.enchantArray, i));
        }
        this.addProperty(this.bookCount).set(0);
    }

    @Inject(method = "onButtonClick", at = @At(value = "HEAD"), cancellable = true)
    private void buttonClick(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        int numberOfEnchants = 10;
        for (int i = 1; i <= 10; i++) {
            if (this.enchantArray[i-1] == -1) {
                numberOfEnchants = i - 1;
                break;
            }
        }

        ItemStack enchantItem = this.inventory.getStack(0);
        ItemStack lapis = this.inventory.getStack(1);

        boolean buttonValid = id >= 0 && id <= numberOfEnchants - 1;
        boolean validEnchantItem = enchantItem.isEnchantable();
        boolean lapisRequirement = player.isInCreativeMode() || (id + 1 <= lapis.getCount());
        boolean levelRequirement = player.isInCreativeMode() || (id + 1 <= player.experienceLevel);
        if (!(player instanceof ServerPlayerEntity)) {
            cir.setReturnValue(Boolean.TRUE);
        }

        if (buttonValid && validEnchantItem && lapisRequirement && levelRequirement) {
            this.context.run((world, pos) -> {
                if (!this.newEnchantmentList.isEmpty() && !this.newEnchantmentList.get(id).isEmpty()) {

                    player.applyEnchantmentCosts(enchantItem, id + 1);
                    if (enchantItem.isOf(Items.BOOK)) enchantItem.withItem(Items.ENCHANTED_BOOK);

                    for (EnchantmentLevelEntry entry : this.newEnchantmentList.get(id)) {
                        enchantItem.addEnchantment(entry.enchantment(), entry.level());
                    }

                    lapis.decrementUnlessCreative(id + 1, player);

                    player.incrementStat(Stats.ENCHANT_ITEM);
                    if (player instanceof ServerPlayerEntity) {
                        Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchantItem, id + 1);
                    }
                    this.inventory.markDirty();
                    this.seed.set(player.getEnchantingTableSeed());
                    this.onContentChanged(this.inventory);
                    world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                    cir.setReturnValue(Boolean.TRUE);
                } else  {
                    cir.setReturnValue(Boolean.FALSE);
                }
            });
        } else {
            cir.setReturnValue(Boolean.FALSE);
        }
    }

    @Inject(method = "onContentChanged", at = @At("TAIL"))
    private void updateActivateScroll(Inventory inventory, CallbackInfo ci) {
        activateScroll.run();
        this.context.run((world, pos) -> {
            int count = 0;
            for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
                if (EnchantingTableBlock.canAccessPowerProvider(world, pos, blockPos)) {
                    count++;
                }
            }
            this.bookCount.set(count/3);
            bookCounter.run();
            this.sendContentUpdates();
            ItemStack enchantItem = inventory.getStack(0);
            Optional<RegistryEntryList.Named<Enchantment>> optional = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(EnchantmentTags.IN_ENCHANTING_TABLE);
            if (!enchantItem.isEmpty() && enchantItem.isEnchantable() && optional.isPresent()) {
                this.newEnchantmentList.clear();
                int level = Objects.requireNonNull(enchantItem.get(DataComponentTypes.ENCHANTABLE)).value();
                for (int i = 1; i <= level; i++) {
                    this.newEnchantmentList.add(BalancedEnchantmentHelper.generateEnchantments(this.random, i, enchantItem, optional.get()));
                }
            }

            for (int i = 0; i < 10; i++) {

                if (i < this.newEnchantmentList.size()) {
                    IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getIndexedEntries();
                    this.enchantArray[i] = indexedIterable.getRawId(this.newEnchantmentList.get(i).getFirst().enchantment());
                } else {
                    this.enchantArray[i] = -1;
                }
            }
            transferEnchants.run();
        });
        bookCounter.run();
        this.sendContentUpdates();
    }

    //ought to try to figure out why I do this
    @ModifyVariable(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Ljava/util/List;"), index = 3, argsOnly = true)
    private int modifyLevel(int value) {
        return 10;
    }

    @Override
    public void balance$setEnchantingListener(Runnable activateScroll) {
        this.activateScroll = activateScroll;
    }
    @Override
    public void balance$getBookCount(Runnable count) {
        this.bookCounter = count;
    }

    @Override
    public void balance$getEnchants(Runnable enchants) {
        this.transferEnchants = enchants;
    }

    @Override
    public int balance$getCount() {
        this.sendContentUpdates();
        return this.bookCount.get();
    }

    @Override
    public int[] balance$transferEnchants() {
        this.sendContentUpdates();
        return this.enchantArray;
    }

}
