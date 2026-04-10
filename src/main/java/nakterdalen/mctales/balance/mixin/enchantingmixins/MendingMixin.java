package nakterdalen.mctales.balance.mixin.enchantingmixins;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class MendingMixin {

    @Inject(method = "modifyDurabilityToRepairFromXp", at = @At(value = "RETURN"), cancellable = true)
    private static void reduceMendingRepair(ServerLevel world, ItemStack stack, int baseRepairWithXp, CallbackInfoReturnable<Integer> cir) {
        int level = 0;
        for (Holder<Enchantment> entry : EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet()) {
            level += EnchantmentHelper.getItemEnchantmentLevel(entry, stack);
        }
        cir.setReturnValue(cir.getReturnValue() * 3 / (2 + Math.max(level, 4)));
    }

}
