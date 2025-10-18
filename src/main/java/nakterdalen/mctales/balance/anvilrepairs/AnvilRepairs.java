package nakterdalen.mctales.balance.anvilrepairs;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

public class AnvilRepairs {

    public static int getRepairDurability(ItemStack stack) {
        int currentDamage = stack.getDamage();

        // Netherite
        if (stack.canRepairWith(Items.NETHERITE_INGOT.getDefaultStack())) {
            //return stack.getMaxDamage();
            return 0;
        }

        // Axes & Pickaxes
        if (stack.isIn(ItemTags.AXES) || stack.isIn(ItemTags.PICKAXES)) {
            return currentDamage - (stack.getMaxDamage()/3);
        }

        // Hoes & Swords
        if (stack.isIn(ItemTags.HOES) || stack.isIn(ItemTags.SWORDS)) {
            return currentDamage - (stack.getMaxDamage()/2);
        }

        // Shovels
        if (stack.isIn(ItemTags.SHOVELS)) {
            return 0;
        }

        // Helmets
        if (stack.isIn(ItemTags.HEAD_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/5);
        }

        // Chest plates
        if (stack.isIn(ItemTags.CHEST_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/8);
        }

        // Leggings
        if (stack.isIn(ItemTags.LEG_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/7);
        }

        // Boots
        if (stack.isIn(ItemTags.FOOT_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/4);
        }

        // Shears
        if (stack.isOf(Items.SHEARS) ) {
            return currentDamage - (stack.getMaxDamage()/2);
        }

        // Trident
        if (stack.isOf(Items.TRIDENT)) {
            return currentDamage - (stack.getMaxDamage()/3);
        }

        // Mace
        if (stack.isOf(Items.MACE)) {
            return 0;
        }

        // Elytra
        if (stack.isOf(Items.ELYTRA)) {
            return currentDamage - (stack.getMaxDamage()/4);
        }

        return stack.getDamage();
    }

}
