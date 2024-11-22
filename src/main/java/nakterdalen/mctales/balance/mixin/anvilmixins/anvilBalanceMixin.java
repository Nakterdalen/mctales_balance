package nakterdalen.mctales.balance.mixin.anvilmixins;

import nakterdalen.mctales.balance.anvilrepairs.AnvilRepairs;
import nakterdalen.mctales.balance.enchanting.BalancedEnchantmentHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
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

@Mixin(AnvilScreenHandler.class)
public abstract class anvilBalanceMixin extends ForgingScreenHandler {

    @Final
    @Shadow
    private Property levelCost;

    @Shadow private int repairItemUsage;

    @Shadow @Nullable private String newItemName;

    //will be ignored
    public anvilBalanceMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V", ordinal = 0))
    private void changeDurability(ItemStack instance, int damage) {
        instance.setDamage(AnvilRepairs.getRepairDurability(instance));
    }

    @Inject(method = "updateResult", at = @At("TAIL"))
    private void changeAmount(CallbackInfo ci){
        ItemStack inputItem = input.getStack(0);
        ItemStack outputItem = output.getStack(0);
        if (outputItem.isEmpty()) {
            return;
        }
        int xpCost = BalancedEnchantmentHelper.getEnchantmentlevels(outputItem);

        //int squaredXPCost = xpCost*xpCost;

        levelCost.set(xpCost);
        inputItem.set(DataComponentTypes.REPAIR_COST, 0);
    }

    @Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
    private void canTakeOut(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
        //replaces no xp cost:
        boolean b1 = repairItemUsage > 0 || !Objects.requireNonNull(newItemName).isEmpty() || levelCost.get() > 0;
        cir.setReturnValue((player.isInCreativeMode() || player.experienceLevel >= levelCost.get()) && b1);
    }
}
