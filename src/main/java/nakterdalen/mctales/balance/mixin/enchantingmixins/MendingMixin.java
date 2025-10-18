package nakterdalen.mctales.balance.mixin.enchantingmixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class MendingMixin {

    @Inject(method = "getRepairWithExperience", at = @At(value = "RETURN"), cancellable = true)
    private static void reduceMendingRepair(ServerWorld world, ItemStack stack, int baseRepairWithXp, CallbackInfoReturnable<Integer> cir) {
        int level = 0;
        for (RegistryEntry<Enchantment> entry : EnchantmentHelper.getEnchantments(stack).getEnchantments()) {
            level += EnchantmentHelper.getLevel(entry, stack);
        }
        cir.setReturnValue(cir.getReturnValue() * 3 / (2 + Math.max(level, 4)));
    }

}
