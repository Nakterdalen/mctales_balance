package nakterdalen.mctales.balance.armor;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.Map;

public enum ArmorStats{
    DEFAULT(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 0);
        map.put(EquipmentType.LEGGINGS, 0);
        map.put(EquipmentType.CHESTPLATE, 0);
        map.put(EquipmentType.HELMET, 0);
        map.put(EquipmentType.BODY, 0);
    })),
    LEATHER(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 0);
        map.put(EquipmentType.LEGGINGS, 0);
        map.put(EquipmentType.CHESTPLATE, 1);
        map.put(EquipmentType.HELMET, 0);
        map.put(EquipmentType.BODY, 0);
    })),
    CHAIN(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 1);
        map.put(EquipmentType.LEGGINGS, 1);
        map.put(EquipmentType.CHESTPLATE, 2);
        map.put(EquipmentType.HELMET, 1);
        map.put(EquipmentType.BODY, 0);
    })),
    IRON(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 1);
        map.put(EquipmentType.LEGGINGS, 3);
        map.put(EquipmentType.CHESTPLATE,4);
        map.put(EquipmentType.HELMET, 2);
        map.put(EquipmentType.BODY, 0);
    })),
    NETHERITE(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 1);
        map.put(EquipmentType.LEGGINGS, 3);
        map.put(EquipmentType.CHESTPLATE, 4);
        map.put(EquipmentType.HELMET, 2);
        map.put(EquipmentType.BODY, 0);
    })),
    DIAMOND(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 1);
        map.put(EquipmentType.LEGGINGS, 2);
        map.put(EquipmentType.CHESTPLATE, 2);
        map.put(EquipmentType.HELMET, 1);
        map.put(EquipmentType.BODY, 0);
    })),
    GOLD(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 3);
        map.put(EquipmentType.LEGGINGS,4);
        map.put(EquipmentType.CHESTPLATE, 5);
        map.put(EquipmentType.HELMET,3);
        map.put(EquipmentType.BODY, 0);
    })),
    TURTLE(Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
        map.put(EquipmentType.BOOTS, 0);
        map.put(EquipmentType.LEGGINGS, 0);
        map.put(EquipmentType.CHESTPLATE, 0);
        map.put(EquipmentType.HELMET, 1);
        map.put(EquipmentType.BODY, 0);
    }));

    private final Map<EquipmentType, Integer> weight;

    ArmorStats(Map<EquipmentType, Integer> weight) {
        this.weight = weight;
    }

    private static final double TRANSFER_RATE = 0.02;


    public static double getAppropriateWeight(EquipmentType type, ArmorMaterial material) {
        assert getMaterial(material) != null;
        return transformWeight(getMaterial(material).weight.get(type));
    }

    private static ArmorStats getMaterial(ArmorMaterial material) {
        if (material.equals(ArmorMaterials.LEATHER)) {
            return LEATHER;
        } else if (material.equals(ArmorMaterials.CHAIN)) {
            return CHAIN;
        } else if (material.equals(ArmorMaterials.IRON)) {
            return IRON;
        } else if (material.equals(ArmorMaterials.NETHERITE)) {
            return NETHERITE;
        } else if (material.equals(ArmorMaterials.DIAMOND)) {
            return DIAMOND;
        } else if (material.equals(ArmorMaterials.GOLD)) {
            return GOLD;
        } else if (material.equals(ArmorMaterials.TURTLE_SCUTE)) {
            return TURTLE;
        }
        return DEFAULT;
    }

    private static double transformWeight(int weight) {
        return weight*-TRANSFER_RATE;
    }
}
