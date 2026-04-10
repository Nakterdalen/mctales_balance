package nakterdalen.mctales.balance.mixin.enchantingmixins;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nakterdalen.mctales.balance.enchanting.BalancedEnchantmentHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Shadow
    private static DataComponentType<ItemEnchantments> getComponentType(ItemStack stack) {
        return null;
    }

    @Inject(method = "setEnchantments", at = @At("HEAD"), cancellable = true)
    private static void enchantmentCap(ItemStack stack, ItemEnchantments enchantments, CallbackInfo ci) {
        int maxEnchants = Objects.requireNonNull(stack.getComponents().get(DataComponents.ENCHANTABLE)).value();
        int currentEnchants = 0;
        ItemEnchantments.Mutable newComponentBuilder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            if (currentEnchants < maxEnchants) {
                int enchantLevel = Math.min(entry.getIntValue(), maxEnchants - currentEnchants);
                newComponentBuilder.upgrade(entry.getKey(), enchantLevel);
                currentEnchants += enchantLevel;
            }
        }
        stack.set(getComponentType(stack), newComponentBuilder.toImmutable());
        ci.cancel();
    }

    @Inject(method = "selectEnchantment", at = @At("RETURN"), cancellable = true)
    private static void capEnchantmentList(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> returnList = cir.getReturnValue();
        int enchantability = Objects.requireNonNull(stack.getComponents().get(DataComponents.ENCHANTABLE)).value();
        cir.setReturnValue(BalancedEnchantmentHelper.cutEnchantmentList(returnList, enchantability));
    }

}
