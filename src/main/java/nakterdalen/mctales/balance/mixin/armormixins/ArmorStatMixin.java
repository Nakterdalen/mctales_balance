package nakterdalen.mctales.balance.mixin.armormixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
            map.put(EquipmentType.BOOTS, 1);
            map.put(EquipmentType.LEGGINGS, 2);
            map.put(EquipmentType.CHESTPLATE, 3);
            map.put(EquipmentType.HELMET, 1);
            map.put(EquipmentType.BODY, 3);
        });
        return original.call(26, newMap, enchantmentValue, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 1))
    private static ArmorMaterial changeChain(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, 1);
            map.put(EquipmentType.LEGGINGS, 4);
            map.put(EquipmentType.CHESTPLATE, 5);
            map.put(EquipmentType.HELMET, 2);
            map.put(EquipmentType.BODY, 4);
        });
        return original.call(32, newMap, enchantmentValue, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 2))
    private static ArmorMaterial changeIron(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, 2);
            map.put(EquipmentType.LEGGINGS, 5);
            map.put(EquipmentType.CHESTPLATE, 6);
            map.put(EquipmentType.HELMET, 2);
            map.put(EquipmentType.BODY, 5);
        });
        return original.call(42, newMap, enchantmentValue, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 6))
    private static ArmorMaterial changeNetherite(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, 2);
            map.put(EquipmentType.LEGGINGS, 6);
            map.put(EquipmentType.CHESTPLATE, 7);
            map.put(EquipmentType.HELMET, 3);
            map.put(EquipmentType.BODY, 11);
        });
        return original.call(54, newMap, enchantmentValue, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 4))
    private static ArmorMaterial changeDiamond(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, 3);
            map.put(EquipmentType.LEGGINGS, 6);
            map.put(EquipmentType.CHESTPLATE, 8);
            map.put(EquipmentType.HELMET, 3);
            map.put(EquipmentType.BODY, 11);
        });
        return original.call(74, newMap, enchantmentValue, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 3))
    private static ArmorMaterial changeGold(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        Map<EquipmentType, Object> newMap = Util.make(new EnumMap<>(EquipmentType.class), (map) -> {
            map.put(EquipmentType.BOOTS, 1);
            map.put(EquipmentType.LEGGINGS, 3);
            map.put(EquipmentType.CHESTPLATE, 5);
            map.put(EquipmentType.HELMET, 2);
            map.put(EquipmentType.BODY, 7);
        });
        return original.call(13, newMap, enchantmentValue, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(ILjava/util/Map;ILnet/minecraft/registry/entry/RegistryEntry;FFLnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/Identifier;)Lnet/minecraft/item/equipment/ArmorMaterial;", ordinal = 5))
    private static ArmorMaterial changeTurtle(int durability, Map<EquipmentType, Integer> old_map, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, Identifier modelId, Operation<ArmorMaterial> original) {
        return original.call(40, old_map, enchantmentValue, equipSound, 0f, knockbackResistance, repairIngredient, modelId);
    }

}
