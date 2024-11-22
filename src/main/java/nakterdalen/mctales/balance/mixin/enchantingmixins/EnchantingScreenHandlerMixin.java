package nakterdalen.mctales.balance.mixin.enchantingmixins;


import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Util;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantingScreenHandlerMixin {

    @Mutable
    @Final
    @Shadow
    private final Inventory inventory;

    @Mutable
    @Final
    @Shadow
    private final ScreenHandlerContext context;

    @Mutable
    @Final
    @Shadow
    private final Random random;

    @Mutable
    @Final
    @Shadow
    private final Property seed;

    @Mutable
    @Final
    @Shadow
    public final int[] enchantmentPower;

    @Mutable
    @Final
    @Shadow
    public final int[] enchantmentId;

    @Mutable
    @Final
    @Shadow
    public final int[] enchantmentLevel;

    @Shadow
    private List<EnchantmentLevelEntry> generateEnchantments(DynamicRegistryManager registryManager, ItemStack stack, int slot, int level) {
        //dummy
        return null;
    }

    protected EnchantingScreenHandlerMixin(Inventory inventory, ScreenHandlerContext context, Random random, Property seed, int[] enchantmentPower, int[] enchantmentId, int[] enchantmentLevel) {
        this.inventory = inventory;
        this.context = context;
        this.random = random;
        this.seed = seed;
        this.enchantmentPower = enchantmentPower;
        this.enchantmentId = enchantmentId;
        this.enchantmentLevel = enchantmentLevel;
    }

    @Inject(method = "onContentChanged", at = @At(value = "HEAD"), cancellable = true)
    public void onContentChangedMixin(Inventory inventory, CallbackInfo ci) {
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getStack(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.context.run((world, pos) -> {
                    IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getIndexedEntries();
                    int i = 0;

                    for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
                        if (EnchantingTableBlock.canAccessPowerProvider(world, pos, blockPos)) {
                            ++i;
                        }
                    }

                    this.random.setSeed(this.seed.get());

                    int j;
                    for(j = 0; j < 3; ++j) {
                        this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, i, itemStack);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;
                        if (this.enchantmentPower[j] < j + 1) {
                            this.enchantmentPower[j] = 0;
                        }
                    }

                    for(j = 0; j < 3; ++j) {
                        if (this.enchantmentPower[j] > 0) {
                            List<EnchantmentLevelEntry> list = this.generateEnchantments(world.getRegistryManager(), itemStack, j, this.enchantmentPower[j]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentLevelEntry enchantmentLevelEntry = list.get(this.random.nextInt(list.size()));
                                this.enchantmentId[j] = indexedIterable.getRawId(enchantmentLevelEntry.enchantment);
                                this.enchantmentLevel[j] = enchantmentLevelEntry.level;
                            }
                        }
                    }

                    ((EnchantmentScreenHandler)(Object)this).sendContentUpdates();
                });
            } else {
                for(int i = 0; i < 3; ++i) {
                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;
                }
            }
        }
        ci.cancel();
    }

    @Inject(method = "onButtonClick", at = @At("HEAD"), cancellable = true)
    public void onButtonClickMixin(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        if (id >= 0 && id < this.enchantmentPower.length) {
            ItemStack itemStack = this.inventory.getStack(0);
            ItemStack itemStack2 = this.inventory.getStack(1);
            int i = id + 1;
            if ((itemStack2.isEmpty() || itemStack2.getCount() < i) && !player.isInCreativeMode()) {
                cir.setReturnValue(false);
            } else if (this.enchantmentPower[id] <= 0 || itemStack.isEmpty() || (player.experienceLevel < i || player.experienceLevel < this.enchantmentPower[id]) && !player.getAbilities().creativeMode) {
                cir.setReturnValue(false);
            } else {
                this.context.run((world, pos) -> {
                    ItemStack itemStack3 = itemStack;
                    List<EnchantmentLevelEntry> list = this.generateEnchantments(world.getRegistryManager(), itemStack3, id, this.enchantmentPower[id]);
                    if (list != null && !list.isEmpty()) {
                        player.applyEnchantmentCosts(itemStack3, i);
                        if (itemStack3.isOf(Items.BOOK)) {
                            itemStack3 = itemStack.withItem(Items.ENCHANTED_BOOK);
                            this.inventory.setStack(0, itemStack3);
                        }

                        for (EnchantmentLevelEntry enchantmentLevelEntry : list) {
                            itemStack3.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
                        }

                        itemStack2.decrementUnlessCreative(i, player);
                        if (itemStack2.isEmpty()) {
                            this.inventory.setStack(1, ItemStack.EMPTY);
                        }

                        player.incrementStat(Stats.ENCHANT_ITEM);
                        if (player instanceof ServerPlayerEntity) {
                            Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemStack3, i);
                        }

                        this.inventory.markDirty();
                        this.seed.set(player.getEnchantingTableSeed());
                        ((EnchantmentScreenHandler)(Object)this).onContentChanged(this.inventory);
                        world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                    }

                });
                cir.setReturnValue(true);
            }
        } else {
            String var10000 = String.valueOf(player.getName());
            Util.logErrorOrPause(var10000 + " pressed invalid button id: " + id);
            cir.setReturnValue(false);
        }
    }

}
