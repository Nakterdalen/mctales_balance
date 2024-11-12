package nakterdalen.mctales.balance.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
        return original.call(noDropTag, durability, speed, attackBonus, enchantValue, repairItems);
    }

    // Change Stone
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 1))
    private static ToolMaterial newStoneStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, durability, speed, attackBonus, enchantValue, repairItems);
    }

    // Change Iron
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 2))
    private static ToolMaterial newIronStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, durability, speed, attackBonus, enchantValue, repairItems);
    }

    // Change Diamond
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 3))
    private static ToolMaterial newDiamondStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, durability, speed, attackBonus, enchantValue, repairItems);
    }

    // Change Gold
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 4))
    private static ToolMaterial newGoldStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, durability, speed, attackBonus, enchantValue, repairItems);
    }

    // Change Netherite
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/ToolMaterial;", ordinal = 5))
    private static ToolMaterial newNetheriteStats(TagKey<Block> noDropTag, int durability, float speed, float attackBonus, int enchantValue, TagKey<Item> repairItems, Operation<ToolMaterial> original) {
        return original.call(noDropTag, durability, speed, attackBonus, enchantValue, repairItems);
    }

}