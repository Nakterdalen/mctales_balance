package nakterdalen.mctales.balance.enchanting;

import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;


public class BalancedEnchantmentHelper {

    private static final float HIGHERLEVELCHANCE = 0.5f;
    private static final float HIGHERLEVELBOOKCHANCE = 0.9f;

    public static int getEnchantmentlevels(ItemStack item) {
        int level = 0;
        for (Holder<Enchantment> entry : EnchantmentHelper.getEnchantmentsForCrafting(item).keySet()) {
            level += EnchantmentHelper.getItemEnchantmentLevel(entry, item);
        }
        return level;
    }

    public static List<EnchantmentInstance> cutEnchantmentList(List<EnchantmentInstance> returnList, int enchantability) {

        int totalEnchants = 0;
        List<EnchantmentInstance> enchantmentList = Lists.newArrayList();

        for (EnchantmentInstance entry : returnList) {
            if (totalEnchants < enchantability) {
                int enchantLevel = Math.min(entry.level(), enchantability - totalEnchants);
                EnchantmentInstance newEntry = new EnchantmentInstance(entry.enchantment(), enchantLevel);
                enchantmentList.add(newEntry);
                totalEnchants += enchantLevel;
            }
        }
        return enchantmentList;
    }

    public static List<EnchantmentInstance> generateEnchantments(RandomSource random, int level, ItemStack stack, HolderSet.Named<Enchantment> entryList) {
        List<EnchantmentInstance> enchantList = new LinkedList<>();

        EnchantmentInstance firstEntry = EnchantmentHelper.selectEnchantment(random, stack, 30, entryList.stream()).getFirst();
        firstEntry = new EnchantmentInstance(firstEntry.enchantment(), 1);
        enchantList.add(firstEntry);
        int totalLevel = 1;
        while (totalLevel < level) {
            List<Float> probList = new LinkedList<>();
            enchantList.forEach( _ -> probList.add(random.nextFloat()));
            boolean hasAdded = false;
            for (int i = 0; i < enchantList.size(); i++) {
                float probIncrease = stack.is(Items.BOOK) ? HIGHERLEVELBOOKCHANCE : HIGHERLEVELCHANCE;
                if (enchantList.get(i).enchantment().value().getMaxLevel() != enchantList.get(i).level()) {
                    if (probList.get(i) <= probIncrease) {
                        enchantList.set(i, new EnchantmentInstance(enchantList.get(i).enchantment(), enchantList.get(i).level()+1));
                        totalLevel++;
                        hasAdded = true;
                        break;
                    }
                }
            }
            if (!hasAdded) {
                List<Holder<Enchantment>> filterList = new LinkedList<>();
                enchantList.forEach(e -> filterList.add(e.enchantment()));
                List<EnchantmentInstance> interList = EnchantmentHelper.selectEnchantment(random, stack, 30, entryList
                        .stream().filter(entry -> EnchantmentHelper.isEnchantmentCompatible(filterList, entry)));
                if (!interList.isEmpty()) {
                    EnchantmentInstance midEntry = interList.getFirst();
                    midEntry = new EnchantmentInstance(midEntry.enchantment(), 1);
                    enchantList.add(midEntry);
                    totalLevel++;
                }
            }
        }
        return enchantList;
    }

}
