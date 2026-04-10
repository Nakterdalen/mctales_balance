package nakterdalen.mctales.balance.mixin.armormixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.armor.ArmorStats;
import nakterdalen.mctales.balance.enchanting.Enchantability;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.EnumMap;
import java.util.Map;

@Mixin(ArmorMaterials.class)
public interface ArmorStatMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 0))
    private static ArmorMaterial changeLeather(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        Map<ArmorType, Object> newMap = Util.make(new EnumMap<>(ArmorType.class), (map) -> {
            map.put(ArmorType.BOOTS, ArmorStats.LEATHER_BOOTS_PROTECTION);
            map.put(ArmorType.LEGGINGS, ArmorStats.LEATHER_LEGGINGS_PROTECTION);
            map.put(ArmorType.CHESTPLATE, ArmorStats.LEATHER_CHEST_PROTECTION);
            map.put(ArmorType.HELMET, ArmorStats.LEATHER_HELMET_PROTECTION);
            map.put(ArmorType.BODY, ArmorStats.LEATHER_DOG_PROTECTION);
        });
        return original.call(ArmorStats.LEATHER_DURABILITY, newMap, Enchantability.LEATHER_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 1))
    private static ArmorMaterial changeCopper(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        Map<ArmorType, Object> newMap = Util.make(new EnumMap<>(ArmorType.class), (map) -> {
            map.put(ArmorType.BOOTS, ArmorStats.COPPER_BOOTS_PROTECTION);
            map.put(ArmorType.LEGGINGS, ArmorStats.COPPER_LEGGINGS_PROTECTION);
            map.put(ArmorType.CHESTPLATE, ArmorStats.COPPER_CHEST_PROTECTION);
            map.put(ArmorType.HELMET, ArmorStats.COPPER_HELMET_PROTECTION);
            map.put(ArmorType.BODY, ArmorStats.COPPER_DOG_PROTECTION);
        });
        return original.call(ArmorStats.COPPER_DURABILITY, newMap, Enchantability.COPPER_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 2))
    private static ArmorMaterial changeChain(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        Map<ArmorType, Object> newMap = Util.make(new EnumMap<>(ArmorType.class), (map) -> {
            map.put(ArmorType.BOOTS, ArmorStats.CHAIN_BOOTS_PROTECTION);
            map.put(ArmorType.LEGGINGS, ArmorStats.CHAIN_LEGGINGS_PROTECTION);
            map.put(ArmorType.CHESTPLATE, ArmorStats.CHAIN_CHEST_PROTECTION);
            map.put(ArmorType.HELMET, ArmorStats.CHAIN_HELMET_PROTECTION);
            map.put(ArmorType.BODY, ArmorStats.CHAIN_DOG_PROTECTION);
        });
        return original.call(ArmorStats.CHAIN_DURABILITY, newMap, Enchantability.CHAIN_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 3))
    private static ArmorMaterial changeIron(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        Map<ArmorType, Object> newMap = Util.make(new EnumMap<>(ArmorType.class), (map) -> {
            map.put(ArmorType.BOOTS, ArmorStats.IRON_BOOTS_PROTECTION);
            map.put(ArmorType.LEGGINGS, ArmorStats.IRON_LEGGINGS_PROTECTION);
            map.put(ArmorType.CHESTPLATE, ArmorStats.IRON_CHEST_PROTECTION);
            map.put(ArmorType.HELMET, ArmorStats.IRON_HELMET_PROTECTION);
            map.put(ArmorType.BODY, ArmorStats.IRON_DOG_PROTECTION);
        });
        return original.call(ArmorStats.IRON_DURABILITY, newMap, Enchantability.IRON_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 7))
    private static ArmorMaterial changeNetherite(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        Map<ArmorType, Object> newMap = Util.make(new EnumMap<>(ArmorType.class), (map) -> {
            map.put(ArmorType.BOOTS, ArmorStats.NETHERITE_BOOTS_PROTECTION);
            map.put(ArmorType.LEGGINGS, ArmorStats.NETHERITE_LEGGINGS_PROTECTION);
            map.put(ArmorType.CHESTPLATE, ArmorStats.NETHERITE_CHEST_PROTECTION);
            map.put(ArmorType.HELMET, ArmorStats.NETHERITE_HELMET_PROTECTION);
            map.put(ArmorType.BODY, ArmorStats.NETHERITE_DOG_PROTECTION);
        });
        return original.call(ArmorStats.NETHERITE_DURABILITY, newMap, Enchantability.NETHERITE_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 5))
    private static ArmorMaterial changeDiamond(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        Map<ArmorType, Object> newMap = Util.make(new EnumMap<>(ArmorType.class), (map) -> {
            map.put(ArmorType.BOOTS, ArmorStats.DIAMOND_BOOTS_PROTECTION);
            map.put(ArmorType.LEGGINGS, ArmorStats.DIAMOND_LEGGINGS_PROTECTION);
            map.put(ArmorType.CHESTPLATE, ArmorStats.DIAMOND_CHEST_PROTECTION);
            map.put(ArmorType.HELMET, ArmorStats.DIAMOND_HELMET_PROTECTION);
            map.put(ArmorType.BODY, ArmorStats.DIAMOND_DOG_PROTECTION);
        });
        return original.call(ArmorStats.DIAMOND_DURABILITY, newMap, Enchantability.DIAMOND_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 4))
    private static ArmorMaterial changeGold(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        Map<ArmorType, Object> newMap = Util.make(new EnumMap<>(ArmorType.class), (map) -> {
            map.put(ArmorType.BOOTS, ArmorStats.GOLD_BOOTS_PROTECTION);
            map.put(ArmorType.LEGGINGS, ArmorStats.GOLD_LEGGINGS_PROTECTION);
            map.put(ArmorType.CHESTPLATE, ArmorStats.GOLD_CHEST_PROTECTION);
            map.put(ArmorType.HELMET, ArmorStats.GOLD_HELMET_PROTECTION);
            map.put(ArmorType.BODY, ArmorStats.GOLD_DOG_PROTECTION);
        });
        return original.call(ArmorStats.GOLD_DURABILITY, newMap, Enchantability.GOLD_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/item/equipment/ArmorMaterial;", ordinal = 6))
    private static ArmorMaterial changeTurtle(int durability, Map<ArmorType, Integer> old_map, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> modelId, Operation<ArmorMaterial> original) {
        return original.call(ArmorStats.TURTLE_DURABILITY, old_map, Enchantability.TURTLE_ENCHANTABILITY, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

}
