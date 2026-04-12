package nakterdalen.mctales.balance.food;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.List;

public class BalancedFoodHelper {

    public static Consumable.Builder copyFoodBuilder(Consumable consumable) {
        Consumable.Builder builder = Consumable.builder();

        List<ConsumeEffect> effects = consumable.onConsumeEffects();

        for (ConsumeEffect effect : effects) {
            builder.onConsume(effect);
        }
        builder.animation(consumable.animation());
        builder.sound(consumable.sound());
        builder.hasConsumeParticles(consumable.hasConsumeParticles());

        return builder;
    }

    public static void consumeFood(ItemStack itemStack, LivingEntity entity) {
        if (entity instanceof ServerPlayer && BalancedFoodItems.hasBalancedFoodItem(itemStack.toString())) {
            BalancedFoodManager manager = ((IFoodManager)entity).balance$getFoodManager();
            BalancedFoodItems item = BalancedFoodItems.getBalancedFoodItem(itemStack.toString());
            manager.addFoodValue(item);
        }
    }

}
