package nakterdalen.mctales.balance.mixin.armormixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.equipment.EquipmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EquipmentType.class)
public abstract class EquipTypeMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/item/equipment/EquipmentType;", ordinal = 0))
    private static EquipmentType changeHat(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<EquipmentType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 5, name);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/item/equipment/EquipmentType;", ordinal = 1))
    private static EquipmentType changeChest(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<EquipmentType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 8, name);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/item/equipment/EquipmentType;", ordinal = 2))
    private static EquipmentType changeLegs(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<EquipmentType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 7, name);
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;ILnet/minecraft/entity/EquipmentSlot;ILjava/lang/String;)Lnet/minecraft/item/equipment/EquipmentType;", ordinal = 3))
    private static EquipmentType changeBoots(String enumName, int enumNumber, EquipmentSlot equipmentSlot, int baseDamage, String name, Operation<EquipmentType> original) {
        return original.call(enumName, enumNumber, equipmentSlot, 4, name);
    }
}
