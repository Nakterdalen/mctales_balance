package nakterdalen.mctales.balance.mixin.armormixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.armor.ArmorStats;
import nakterdalen.mctales.balance.enchanting.Enchantability;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.EnumMap;
import java.util.Map;

@Mixin(ArmorMaterials.class)
public interface ArmorStatMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 0))
    private static ArmorMaterial changeLeather(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, ArmorStats.LEATHER_BOOTS_PROTECTION);
            map.put(EquipmentType.LEGGINGS, ArmorStats.LEATHER_LEGGINGS_PROTECTION);
            map.put(EquipmentType.CHESTPLATE, ArmorStats.LEATHER_CHEST_PROTECTION);
            map.put(EquipmentType.HELMET, ArmorStats.LEATHER_HELMET_PROTECTION);
            map.put(EquipmentType.BODY, ArmorStats.LEATHER_DOG_PROTECTION);
        });
        return original.call(ArmorStats.LEATHER_DURABILITY, newMap, Enchantability.LEATHER_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 1))
    private static ArmorMaterial changeChain(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, ArmorStats.CHAIN_BOOTS_PROTECTION);
            map.put(EquipmentType.LEGGINGS, ArmorStats.CHAIN_LEGGINGS_PROTECTION);
            map.put(EquipmentType.CHESTPLATE, ArmorStats.CHAIN_CHEST_PROTECTION);
            map.put(EquipmentType.HELMET, ArmorStats.CHAIN_HELMET_PROTECTION);
            map.put(EquipmentType.BODY, ArmorStats.CHAIN_DOG_PROTECTION);
        });
        return original.call(ArmorStats.CHAIN_DURABILITY, newMap, Enchantability.CHAIN_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 2))
    private static ArmorMaterial changeIron(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, ArmorStats.IRON_BOOTS_PROTECTION);
            map.put(EquipmentType.LEGGINGS, ArmorStats.IRON_LEGGINGS_PROTECTION);
            map.put(EquipmentType.CHESTPLATE, ArmorStats.IRON_CHEST_PROTECTION);
            map.put(EquipmentType.HELMET, ArmorStats.IRON_HELMET_PROTECTION);
            map.put(EquipmentType.BODY, ArmorStats.IRON_DOG_PROTECTION);
        });
        return original.call(ArmorStats.IRON_DURABILITY, newMap, Enchantability.IRON_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 6))
    private static ArmorMaterial changeNetherite(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, ArmorStats.NETHERITE_BOOTS_PROTECTION);
            map.put(EquipmentType.LEGGINGS, ArmorStats.NETHERITE_LEGGINGS_PROTECTION);
            map.put(EquipmentType.CHESTPLATE, ArmorStats.NETHERITE_CHEST_PROTECTION);
            map.put(EquipmentType.HELMET, ArmorStats.NETHERITE_HELMET_PROTECTION);
            map.put(EquipmentType.BODY, ArmorStats.NETHERITE_DOG_PROTECTION);
        });
        return original.call(ArmorStats.NETHERITE_DURABILITY, newMap, Enchantability.NETHERITE_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 4))
    private static ArmorMaterial changeDiamond(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, ArmorStats.DIAMOND_BOOTS_PROTECTION);
            map.put(EquipmentType.LEGGINGS, ArmorStats.DIAMOND_LEGGINGS_PROTECTION);
            map.put(EquipmentType.CHESTPLATE, ArmorStats.DIAMOND_CHEST_PROTECTION);
            map.put(EquipmentType.HELMET, ArmorStats.DIAMOND_HELMET_PROTECTION);
            map.put(EquipmentType.BODY, ArmorStats.DIAMOND_DOG_PROTECTION);
        });
        return original.call(ArmorStats.DIAMOND_DURABILITY, newMap, Enchantability.DIAMOND_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 3))
    private static ArmorMaterial changeGold(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, ArmorStats.GOLD_BOOTS_PROTECTION);
            map.put(EquipmentType.LEGGINGS, ArmorStats.GOLD_LEGGINGS_PROTECTION);
            map.put(EquipmentType.CHESTPLATE, ArmorStats.GOLD_CHEST_PROTECTION);
            map.put(EquipmentType.HELMET, ArmorStats.GOLD_HELMET_PROTECTION);
            map.put(EquipmentType.BODY, ArmorStats.GOLD_DOG_PROTECTION);
        });
        return original.call(ArmorStats.GOLD_DURABILITY, newMap, Enchantability.GOLD_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 5))
    private static ArmorMaterial changeTurtle(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        return original.call(ArmorStats.TURTLE_DURABILITY, old_map, Enchantability.TURTLE_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

}
