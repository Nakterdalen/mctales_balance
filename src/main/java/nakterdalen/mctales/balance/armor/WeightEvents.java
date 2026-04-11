package nakterdalen.mctales.balance.armor;

import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class WeightEvents {

    private static final String MOD_ID = MinecraftTalesBalance.MOD_ID;

    public static void registerWeightEvents() {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(
                ArmorWeight.WEIGHT_MAP.keySet()::contains, (builder, item) -> {
                    ItemAttributeModifiers component = item.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
                    ItemAttributeModifiers.Builder componentBuilder = ItemAttributeModifiers.builder();
                    if (component == null) {
                        return;
                    }
                    for (ItemAttributeModifiers.Entry entry : component.modifiers()) {
                        componentBuilder.add(entry.attribute(),entry.modifier(), entry.slot());
                    }
                    builder.set(DataComponents.ATTRIBUTE_MODIFIERS, componentBuilder
                            .add(Attributes.MOVEMENT_SPEED,
                                    new AttributeModifier(Identifier.fromNamespaceAndPath(MOD_ID, "movement_penalty"), ArmorWeight.getArmorWeight(item), AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                    component.modifiers().getFirst().slot())
                            .build());}));
    }
}
