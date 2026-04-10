package nakterdalen.mctales.balance.anvilrepairs;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AnvilRepairs {

    public static int getRepairDurability(ItemStack stack) {
        int currentDamage = stack.getDamageValue();

        // Netherite
        if (stack.isValidRepairItem(Items.NETHERITE_INGOT.getDefaultInstance())) {
            //return stack.getMaxDamage();
            return 0;
        }

        // Axes & Pickaxes
        if (stack.is(ItemTags.AXES) || stack.is(ItemTags.PICKAXES)) {
            return currentDamage - (stack.getMaxDamage()/3);
        }

        // Hoes & Swords
        if (stack.is(ItemTags.HOES) || stack.is(ItemTags.SWORDS)) {
            return currentDamage - (stack.getMaxDamage()/2);
        }

        // Shovels
        if (stack.is(ItemTags.SHOVELS)) {
            return 0;
        }

        // Helmets
        if (stack.is(ItemTags.HEAD_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/5);
        }

        // Chest plates
        if (stack.is(ItemTags.CHEST_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/8);
        }

        // Leggings
        if (stack.is(ItemTags.LEG_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/7);
        }

        // Boots
        if (stack.is(ItemTags.FOOT_ARMOR)) {
            return currentDamage - (stack.getMaxDamage()/4);
        }

        // Shears
        if (stack.is(Items.SHEARS) ) {
            return currentDamage - (stack.getMaxDamage()/2);
        }

        // Trident
        if (stack.is(Items.TRIDENT)) {
            return currentDamage - (stack.getMaxDamage()/3);
        }

        // Mace
        if (stack.is(Items.MACE)) {
            return 0;
        }

        // Elytra
        if (stack.is(Items.ELYTRA)) {
            return currentDamage - (stack.getMaxDamage()/4);
        }

        return stack.getDamageValue();
    }

}
