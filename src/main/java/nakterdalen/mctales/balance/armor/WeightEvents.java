package nakterdalen.mctales.balance.armor;

import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class WeightEvents {

    private static final String MOD_ID = MinecraftTalesBalance.MOD_ID;

    public static void registerWeightEvents() {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(
                item -> item instanceof ArmorItem, (builder, item) -> {
                    AttributeModifiersComponent component = item.getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
                    AttributeModifiersComponent.Builder componentBuilder = AttributeModifiersComponent.builder();
                    if (component == null) {
                        return;
                    }
                    for (AttributeModifiersComponent.Entry entry : component.modifiers()) {
                        componentBuilder.add(entry.attribute(),entry.modifier(), entry.slot());
                    }
                    builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, componentBuilder
                            .add(EntityAttributes.MOVEMENT_SPEED,
                                    new EntityAttributeModifier(Identifier.of(MOD_ID, "movement_penalty"), ArmorWeight.getArmorWeight(item), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                    component.modifiers().getFirst().slot())
                            .add(RegistryEntry.of(ArmorWeight.WEIGHT_ATTRIBUTE),
                                    new EntityAttributeModifier(Identifier.of(MOD_ID, "weight"), ArmorWeight.WEIGHT_MAP.get(item), EntityAttributeModifier.Operation.ADD_VALUE),
                                    component.modifiers().getFirst().slot())
                            .build());}));

    }
}
