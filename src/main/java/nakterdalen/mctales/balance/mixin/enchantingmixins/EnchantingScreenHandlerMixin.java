package nakterdalen.mctales.balance.mixin.enchantingmixins;


import nakterdalen.mctales.balance.enchanting.BalancedEnchantmentHelper;
import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IdMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.EnchantingTableBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;


@Mixin(EnchantmentMenu.class)
public abstract class EnchantingScreenHandlerMixin extends AbstractContainerMenu implements IEnchantingHandler {

    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Shadow
    @Final
    private DataSlot enchantmentSeed;

    @Shadow
    public void slotsChanged(Container inventory) {}

    @Shadow
    @Final
    private Container enchantSlots;

    @Unique
    public List<List<EnchantmentInstance>> newEnchantmentList = new LinkedList<>();

    @Unique int[] enchantArray = new int[10];

    @Unique
    final DataSlot bookCount = DataSlot.standalone();

    @Shadow @Final private RandomSource random;

    @Unique
    public Runnable activateScroll = () -> {};
    @Unique
    public Runnable bookCounter = () -> {};
    @Unique
    public Runnable transferEnchants = () -> {};

    protected EnchantingScreenHandlerMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void injectConstructor(int syncId, Inventory playerInventory, ContainerLevelAccess context, CallbackInfo ci) {
        for (int i = 0; i < 10; i++) {
            this.addDataSlot(DataSlot.shared(this.enchantArray, i));
        }
        this.addDataSlot(this.bookCount).set(0);
        this.slotsChanged(this.enchantSlots);
    }

    @Inject(method = "clickMenuButton", at = @At(value = "HEAD"), cancellable = true)
    private void buttonClick(Player player, int id, CallbackInfoReturnable<Boolean> cir) {
        int numberOfEnchants = 10;
        for (int i = 1; i <= 10; i++) {
            if (this.enchantArray[i-1] == -1) {
                numberOfEnchants = i - 1;
                break;
            }
        }

        ItemStack enchantItem = this.enchantSlots.getItem(0);
        ItemStack lapis = this.enchantSlots.getItem(1);

        boolean buttonValid = id >= 0 && id <= numberOfEnchants - 1;
        boolean validEnchantItem = enchantItem.isEnchantable();
        boolean lapisRequirement = player.hasInfiniteMaterials() || (id + 1 <= lapis.getCount());
        boolean levelRequirement = player.hasInfiniteMaterials() || (id + 1 <= player.experienceLevel);
        if (!(player instanceof ServerPlayer)) {
            cir.setReturnValue(Boolean.TRUE);
        }

        if (buttonValid && validEnchantItem && lapisRequirement && levelRequirement) {
            this.access.execute((world, pos) -> {
                if (!this.newEnchantmentList.isEmpty() && !this.newEnchantmentList.get(id).isEmpty()) {

                    player.onEnchantmentPerformed(enchantItem, id + 1);
                    if (enchantItem.is(Items.BOOK)) enchantItem.transmuteCopy(Items.ENCHANTED_BOOK);

                    for (EnchantmentInstance entry : this.newEnchantmentList.get(id)) {
                        enchantItem.enchant(entry.enchantment(), entry.level());
                    }

                    lapis.consume(id + 1, player);

                    player.awardStat(Stats.ENCHANT_ITEM);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer)player, enchantItem, id + 1);
                    }
                    this.enchantSlots.setChanged();
                    this.enchantmentSeed.set(player.getEnchantmentSeed());
                    this.slotsChanged(this.enchantSlots);
                    world.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                    cir.setReturnValue(Boolean.TRUE);
                } else  {
                    cir.setReturnValue(Boolean.FALSE);
                }
            });
        } else {
            cir.setReturnValue(Boolean.FALSE);
        }
    }

    @Inject(method = "slotsChanged", at = @At("TAIL"))
    private void updateSlot(Container inventory, CallbackInfo ci) {
        activateScroll.run();
        this.access.execute((world, pos) -> {
            int count = 0;
            for (BlockPos blockPos : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                if (EnchantingTableBlock.isValidBookShelf(world, pos, blockPos)) {
                    count++;
                }
            }
            this.bookCount.set(count/3);
            bookCounter.run();
            this.broadcastChanges();
            ItemStack enchantItem = inventory.getItem(0);
            Optional<HolderSet.Named<Enchantment>> optional = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE);
            this.newEnchantmentList.clear();
            if (!enchantItem.isEmpty() && enchantItem.isEnchantable() && optional.isPresent()) {
                int level = Objects.requireNonNull(enchantItem.get(DataComponents.ENCHANTABLE)).value();
                for (int i = 1; i <= level; i++) {
                    this.newEnchantmentList.add(BalancedEnchantmentHelper.generateEnchantments(this.random, i, enchantItem, optional.get()));
                }
            }

            for (int i = 0; i < 10; i++) {

                if (i < this.newEnchantmentList.size()) {
                    IdMap<Holder<Enchantment>> indexedIterable = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
                    this.enchantArray[i] = indexedIterable.getId(this.newEnchantmentList.get(i).getFirst().enchantment());
                } else {
                    this.enchantArray[i] = -1;
                }
            }
            transferEnchants.run();
        });
        bookCounter.run();
        this.broadcastChanges();
    }

    //ought to try to figure out why I do this
    /*
    @ModifyVariable(method = "getEnchantmentList", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;selectEnchantment(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Ljava/util/List;"), index = 3, argsOnly = true)
    private int modifyLevel(int value) {
        return 10;
    }
     */

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
        this.broadcastChanges();
        return this.bookCount.get();
    }

    @Override
    public int[] balance$transferEnchants() {
        this.broadcastChanges();
        return this.enchantArray;
    }

}
