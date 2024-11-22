package nakterdalen.mctales.balance.mixin.enchantingmixins;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import nakterdalen.mctales.balance.enchanting.BalancedEnchantmentHelper;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
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
public abstract class EnchantabilityCapMixin {

    @Shadow
    private static ComponentType<ItemEnchantmentsComponent> getEnchantmentsComponentType(ItemStack stack) {
        return null;
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private static void enchantmentCap(ItemStack stack, ItemEnchantmentsComponent enchantments, CallbackInfo ci) {
        int maxEnchants = Objects.requireNonNull(stack.getComponents().get(DataComponentTypes.ENCHANTABLE)).value();
        int currentEnchants = 0;
        ItemEnchantmentsComponent.Builder newComponentBuilder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);

        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : enchantments.getEnchantmentEntries()) {
            if (currentEnchants < maxEnchants) {
                int enchantLevel = Math.min(entry.getIntValue(), maxEnchants - currentEnchants);
                newComponentBuilder.add(entry.getKey(), enchantLevel);
                currentEnchants += enchantLevel;
            }
        }
        stack.set(getEnchantmentsComponentType(stack), newComponentBuilder.build());
        ci.cancel();
    }

    @Inject(method = "generateEnchantments", at = @At("RETURN"), cancellable = true)
    private static void capEnchantmentList(Random random, ItemStack stack, int level, Stream<RegistryEntry<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        List<EnchantmentLevelEntry> returnList = cir.getReturnValue();
        int enchantability = Objects.requireNonNull(stack.getComponents().get(DataComponentTypes.ENCHANTABLE)).value();
        cir.setReturnValue(BalancedEnchantmentHelper.cutEnchantmentList(returnList, enchantability));
    }

}
