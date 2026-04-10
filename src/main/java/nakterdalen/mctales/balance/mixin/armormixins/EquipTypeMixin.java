package nakterdalen.mctales.balance.mixin.armormixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.ArmorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorType.class)
public abstract class EquipTypeMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/world/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/world/item/equipment/ArmorType;", ordinal = 0))
    private static ArmorType changeHat(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<ArmorType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 5, name);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/world/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/world/item/equipment/ArmorType;", ordinal = 1))
    private static ArmorType changeChest(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<ArmorType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 8, name);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/world/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/world/item/equipment/ArmorType;", ordinal = 2))
    private static ArmorType changeLegs(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<ArmorType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 7, name);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/world/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/world/item/equipment/ArmorType;", ordinal = 3))
    private static ArmorType changeBoots(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<ArmorType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 4, name);
    }
}
