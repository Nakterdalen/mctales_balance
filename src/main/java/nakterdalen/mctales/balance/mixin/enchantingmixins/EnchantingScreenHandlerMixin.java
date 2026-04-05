package nakterdalen.mctales.balance.mixin.enchantingmixins;


import nakterdalen.mctales.balance.enchanting.BalancedEnchantmentHelper;
import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.*;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;


@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantingScreenHandlerMixin extends ScreenHandler implements IEnchantingHandler {

    @Shadow
    @Final
    private ScreenHandlerContext context;

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
            this.newEnchantmentList.clear();
            Optional<RegistryEntryList.Named<Enchantment>> optional = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(EnchantmentTags.IN_ENCHANTING_TABLE);
            if (!enchantItem.isEmpty() && enchantItem.isEnchantable() && optional.isPresent()) {

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
        for (int i = 0; i < 5; i++) {
            System.out.println("" + this.enchantArray[i]);
        }
        System.out.println("Do we ever send -1? " + this.bookCount.get());
        this.sendContentUpdates();
    }

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
