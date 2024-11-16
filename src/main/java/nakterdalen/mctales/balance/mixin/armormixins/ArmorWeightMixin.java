package nakterdalen.mctales.balance.mixin.armormixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.MinecraftTalesBalance;
import nakterdalen.mctales.balance.armor.ArmorStats;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorMaterial.class)
public class ArmorWeightMixin {

    @WrapOperation(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;build()Lnet/minecraft/component/type/AttributeModifiersComponent;"))
    private AttributeModifiersComponent addWeight(AttributeModifiersComponent.Builder instance, Operation<AttributeModifiersComponent> original, EquipmentType equipmentType) {
        Identifier identifier = Identifier.of(MinecraftTalesBalance.MOD_ID, "weight." + equipmentType.getName());
        AttributeModifierSlot attributeModifierSlot = AttributeModifierSlot.forEquipmentSlot(equipmentType.getEquipmentSlot());
        return instance.add(EntityAttributes.MOVEMENT_SPEED,
                new EntityAttributeModifier(identifier, ArmorStats.getAppropriateWeight(equipmentType, (ArmorMaterial) (Object)this), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                attributeModifierSlot).build();
    }
}
