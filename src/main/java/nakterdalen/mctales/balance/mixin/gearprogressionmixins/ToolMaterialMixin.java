package nakterdalen.mctales.balance.mixin.gearprogressionmixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.enchanting.Enchantability;
import nakterdalen.mctales.balance.toolprogression.GearStats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToolMaterial.class)
public abstract class ToolMaterialMixin {

    // Change Wood
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagKey;IFFILnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/ToolMaterial;", ordinal = 0))
    private static ToolMaterial newWoodStats(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(incorrectBlocksForDrops, GearStats.WOOD_DURABILITY, GearStats.WOOD_SPEED, attackDamageBonus, Enchantability.WOOD_ENCHANTABILITY, repairItems);
    }

    // Change Stone
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagKey;IFFILnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/ToolMaterial;", ordinal = 1))
    private static ToolMaterial newStoneStats(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(incorrectBlocksForDrops, GearStats.STONE_DURABILITY, GearStats.STONE_SPEED, attackDamageBonus, Enchantability.STONE_ENCHANTABILITY, repairItems);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagKey;IFFILnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/ToolMaterial;", ordinal = 2))
    private static ToolMaterial newCopperStats(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(incorrectBlocksForDrops, GearStats.COPPER_DURABILITY, GearStats.COPPER_SPEED, attackDamageBonus, Enchantability.COPPER_ENCHANTABILITY, repairItems);
    }

    // Change Iron
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagKey;IFFILnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/ToolMaterial;", ordinal = 3))
    private static ToolMaterial newIronStats(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(incorrectBlocksForDrops, GearStats.IRON_DURABILITY, GearStats.IRON_SPEED, attackDamageBonus, Enchantability.IRON_ENCHANTABILITY, repairItems);
    }

    // Change Diamond
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagKey;IFFILnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/ToolMaterial;", ordinal = 4))
    private static ToolMaterial newDiamondStats(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(incorrectBlocksForDrops, GearStats.DIAMOND_DURABILITY, GearStats.DIAMOND_SPEED, GearStats.DIAMOND_ATTACK, Enchantability.DIAMOND_ENCHANTABILITY, repairItems);
    }

    // Change Gold
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagKey;IFFILnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/ToolMaterial;", ordinal = 5))
    private static ToolMaterial newGoldStats(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(incorrectBlocksForDrops, GearStats.GOLD_DURABILITY, GearStats.GOLD_SPEED, GearStats.GOLD_ATTACK, Enchantability.GOLD_ENCHANTABILITY, repairItems);
    }

    // Change Netherite
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/tags/TagKey;IFFILnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/ToolMaterial;", ordinal = 6))
    private static ToolMaterial newNetheriteStats(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(incorrectBlocksForDrops, GearStats.NETHERITE_DURABILITY, GearStats.NETHERITE_SPEED, GearStats.NETHERITE_ATTACK, Enchantability.NETHERITE_ENCHANTABILITY, repairItems);
    }
}
