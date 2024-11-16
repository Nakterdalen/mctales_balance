package nakterdalen.mctales.balance.armor;

import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ArmorWeight {

    public static final Identifier WEIGHT_ID = Identifier.of(MinecraftTalesBalance.MOD_ID, "armor_weight");
    public static final Map<Item, Integer> WEIGHT_MAP = new HashMap<>();
    private static final double TRANSFER_RATE = 0.02;
    public static final EntityAttribute WEIGHT_ATTRIBUTE = (new ClampedEntityAttribute("attribute.weight.armor", 0.0, 0.0, 50.0).setTracked(true));

    static {
        //Leather
        WEIGHT_MAP.put(Items.LEATHER_BOOTS, 0);
        WEIGHT_MAP.put(Items.LEATHER_LEGGINGS, 0);
        WEIGHT_MAP.put(Items.LEATHER_CHESTPLATE, 1);
        WEIGHT_MAP.put(Items.LEATHER_HELMET, 0);
        //Chain
        WEIGHT_MAP.put(Items.CHAINMAIL_BOOTS, 1);
        WEIGHT_MAP.put(Items.CHAINMAIL_LEGGINGS, 1);
        WEIGHT_MAP.put(Items.CHAINMAIL_CHESTPLATE, 2);
        WEIGHT_MAP.put(Items.CHAINMAIL_HELMET, 1);
        //Iron
        WEIGHT_MAP.put(Items.IRON_BOOTS, 1);
        WEIGHT_MAP.put(Items.IRON_LEGGINGS, 3);
        WEIGHT_MAP.put(Items.IRON_CHESTPLATE, 4);
        WEIGHT_MAP.put(Items.IRON_HELMET, 2);
        //Netherite
        WEIGHT_MAP.put(Items.NETHERITE_BOOTS, 1);
        WEIGHT_MAP.put(Items.NETHERITE_LEGGINGS, 3);
        WEIGHT_MAP.put(Items.NETHERITE_CHESTPLATE, 4);
        WEIGHT_MAP.put(Items.NETHERITE_HELMET, 2);
        //Diamond
        WEIGHT_MAP.put(Items.DIAMOND_BOOTS, 1);
        WEIGHT_MAP.put(Items.DIAMOND_LEGGINGS, 2);
        WEIGHT_MAP.put(Items.DIAMOND_CHESTPLATE, 2);
        WEIGHT_MAP.put(Items.DIAMOND_HELMET, 1);
        //Gold
        WEIGHT_MAP.put(Items.GOLDEN_BOOTS, 3);
        WEIGHT_MAP.put(Items.GOLDEN_LEGGINGS, 4);
        WEIGHT_MAP.put(Items.GOLDEN_CHESTPLATE, 5);
        WEIGHT_MAP.put(Items.GOLDEN_HELMET, 3);
        //Turtle
        WEIGHT_MAP.put(Items.TURTLE_HELMET, 1);
    }

    public static double getArmorWeight(Item item) {
        if (WEIGHT_MAP.containsKey(item)) {
            return -WEIGHT_MAP.get(item)*TRANSFER_RATE;
        }
        return 0;
    }

    public static void registerWeightAttribute() {
        Registry.register(Registries.ATTRIBUTE, WEIGHT_ID, WEIGHT_ATTRIBUTE);
    }
}
