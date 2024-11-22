package nakterdalen.mctales.balance.mixin.enchantingmixins;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

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

}
