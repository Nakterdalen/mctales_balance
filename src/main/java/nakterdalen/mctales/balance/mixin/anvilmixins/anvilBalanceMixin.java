package nakterdalen.mctales.balance.mixin.anvilmixins;

import nakterdalen.mctales.balance.anvilrepairs.AnvilRepairs;
import nakterdalen.mctales.balance.enchanting.BalancedEnchantmentHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(AnvilMenu.class)
public abstract class anvilBalanceMixin extends ItemCombinerMenu {

    @Final
    @Shadow
    private DataSlot cost;

    @Shadow private int repairItemCountCost;

    @Shadow @Nullable private String itemName;

    //will be ignored
    public anvilBalanceMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context, ItemCombinerMenuSlotDefinition forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Redirect(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", ordinal = 0))
    private void changeDurability(ItemStack instance, int damage) {
        instance.setDamageValue(AnvilRepairs.getRepairDurability(instance));
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void changeAmount(CallbackInfo ci){
        ItemStack inputItem = inputSlots.getItem(0);
        ItemStack outputItem = resultSlots.getItem(0);
        if (outputItem.isEmpty()) {
            return;
        }
        int xpCost = BalancedEnchantmentHelper.getEnchantmentlevels(outputItem);

        //int squaredXPCost = xpCost*xpCost;

        this.cost.set(xpCost);
        inputItem.set(DataComponents.REPAIR_COST, 0);
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void canTakeOut(Player player, boolean present, CallbackInfoReturnable<Boolean> cir) {
        //replaces no xp cost:
        boolean b1 = this.repairItemCountCost > 0 || !Objects.requireNonNull(this.itemName).isEmpty() || this.cost.get() > 0;
        cir.setReturnValue((player.hasInfiniteMaterials() || player.experienceLevel >= this.cost.get()) && b1);
    }
}
