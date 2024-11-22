package nakterdalen.mctales.balance.enchanting;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;

public class BalancedEnchantmentHelper {

    public static int getEnchantmentlevels(ItemStack item) {
        int level = 0;
        for (RegistryEntry<Enchantment> entry : EnchantmentHelper.getEnchantments(item).getEnchantments()) {
            level += EnchantmentHelper.getLevel(entry, item);
        }
        return level;
    }

    public static List<EnchantmentLevelEntry> cutEnchantmentList(List<EnchantmentLevelEntry> returnList, int enchantability) {

        int totalEnchants = 0;
        List<EnchantmentLevelEntry> enchantmentList = Lists.newArrayList();

        for (EnchantmentLevelEntry entry : returnList) {
            if (totalEnchants < enchantability) {
                int enchantLevel = Math.min(entry.level, enchantability - totalEnchants);
                EnchantmentLevelEntry newEntry = new EnchantmentLevelEntry(entry.enchantment, enchantLevel);
                enchantmentList.add(newEntry);
                totalEnchants += enchantLevel;
            }
        }
        return enchantmentList;
    }
}
