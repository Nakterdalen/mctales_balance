package nakterdalen.mctales.balance.mixin.gearprogressionmixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.enchanting.Enchantability;
import nakterdalen.mctales.balance.toolprogression.GearStats;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToolMaterial.class)
public abstract class ToolMaterialMixin {

    // Change Wood
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 0))
    private static ToolMaterial newWoodStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, GearStats.WOOD_DURABILITY, GearStats.WOOD_SPEED, attackBonus, Enchantability.WOOD_ENCHANTABILITY, repairItems);
    }

    // Change Stone
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 1))
    private static ToolMaterial newStoneStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, GearStats.STONE_DURABILITY, GearStats.STONE_SPEED, attackBonus, Enchantability.STONE_ENCHANTABILITY, repairItems);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 2))
    private static ToolMaterial newCopperStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, GearStats.COPPER_DURABILITY, GearStats.COPPER_SPEED, attackBonus, Enchantability.COPPER_ENCHANTABILITY, repairItems);
    }

    // Change Iron
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 3))
    private static ToolMaterial newIronStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, GearStats.IRON_DURABILITY, GearStats.IRON_SPEED, attackBonus, Enchantability.IRON_ENCHANTABILITY, repairItems);
    }

    // Change Diamond
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 4))
    private static ToolMaterial newDiamondStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, GearStats.DIAMOND_DURABILITY, GearStats.DIAMOND_SPEED, GearStats.DIAMOND_ATTACK, Enchantability.DIAMOND_ENCHANTABILITY, repairItems);
    }

    // Change Gold
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 5))
    private static ToolMaterial newGoldStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, GearStats.GOLD_DURABILITY, GearStats.GOLD_SPEED, attackBonus, Enchantability.GOLD_ENCHANTABILITY, repairItems);
    }

    // Change Netherite
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 6))
    private static ToolMaterial newNetheriteStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, GearStats.NETHERITE_DURABILITY, GearStats.NETHERITE_SPEED, GearStats.NETHERITE_ATTACK, Enchantability.NETHERITE_ENCHANTABILITY, repairItems);
    }
}
