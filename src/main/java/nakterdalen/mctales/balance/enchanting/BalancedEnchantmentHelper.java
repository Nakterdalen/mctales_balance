package nakterdalen.mctales.balance.enchanting;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class BalancedEnchantmentHelper {

    public static int getEnchantmentlevels(ItemStack item) {
        int level = 0;
        for (RegistryEntry<Enchantment> entry : EnchantmentHelper.getEnchantments(item).getEnchantments()) {
            level += EnchantmentHelper.getLevel(entry, item);
        }
        return level;
    }

}
