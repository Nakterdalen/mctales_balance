package nakterdalen.mctales.balance.enchanting;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.random.Random;

import java.util.LinkedList;
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
                int enchantLevel = Math.min(entry.level(), enchantability - totalEnchants);
                EnchantmentLevelEntry newEntry = new EnchantmentLevelEntry(entry.enchantment(), enchantLevel);
                enchantmentList.add(newEntry);
                totalEnchants += enchantLevel;
            }
        }
        return enchantmentList;
    }

    public static List<EnchantmentLevelEntry> generateEnchantments(Random random, int level, ItemStack stack, RegistryEntryList.Named<Enchantment> entryList) {
        List<EnchantmentLevelEntry> enchantList = new LinkedList<>();

        EnchantmentLevelEntry firstEntry = EnchantmentHelper.generateEnchantments(random, stack, 30, entryList.stream()).getFirst();
        firstEntry = new EnchantmentLevelEntry(firstEntry.enchantment(), 1);
        enchantList.add(firstEntry);
        int totalLevel = 1;
        while (totalLevel < level) {
            List<Float> probList = new LinkedList<>();
            enchantList.forEach((entry) ->probList.add(random.nextFloat()));
            for (int i = 0; i < enchantList.size(); i++) {
                float probIncrease = stack.isOf(Items.BOOK) ? 1.0f : 0.7f;
                if (enchantList.get(i).enchantment().value().getMaxLevel() != enchantList.get(i).level() && probList.get(i) < probIncrease) {
                    enchantList.set(i, new EnchantmentLevelEntry(enchantList.get(i).enchantment(), enchantList.get(i).level()+1));
                    totalLevel++;
                    break;
                } else {
                    List<RegistryEntry<Enchantment>> filterList = new LinkedList<>();
                    enchantList.forEach(e -> filterList.add(e.enchantment()));
                    List<EnchantmentLevelEntry> interList = EnchantmentHelper.generateEnchantments(random, stack, 30, entryList
                            .stream().filter(entry -> EnchantmentHelper.isCompatible(filterList, entry)));
                    if (!interList.isEmpty()) {
                        EnchantmentLevelEntry midEntry = interList.getFirst();
                        midEntry = new EnchantmentLevelEntry(midEntry.enchantment(), 1);
                        enchantList.add(midEntry);
                        totalLevel++;
                        break;
                    }
                }
            }
        }
        return enchantList;
    }

}
