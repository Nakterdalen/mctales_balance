package nakterdalen.mctales.balance.food;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;
import java.util.Objects;

public class FoodEvents {

    public static void registerMineFoodEvent() {
        PlayerBlockBreakEvents.AFTER.register( (_, player, _, _, _) -> {
            if (player instanceof ServerPlayer && !player.isSpectator()) {
                ((IFoodManager)player).balance$mineHunger();
            }
        });

        AttackEntityCallback.EVENT.register( (player, _, _, _, _) -> {
            if (player instanceof ServerPlayer && !player.isSpectator()) {
                ((IFoodManager)player).balance$mineHunger();
            }
            return InteractionResult.PASS;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register( (_, newPlayer, _) -> {
            BalancedFoodManager newManager = new BalancedFoodManager();
            newPlayer.setAttached(BalancedFoodManager.FOOD_ATTACHMENT, newManager);
            ((IFoodManager)newPlayer).balance$setFoodManager(newManager);
        });

        DefaultItemComponentEvents.MODIFY.register( mod ->
                mod.modify(FoodEvents::isChangedFood,(builder, item) -> {
                    BalancedFoodItems balancedFood = BalancedFoodItems.getBalancedFoodItem(item.toString());
                    builder.set(DataComponents.CONSUMABLE,
                        BalancedFoodHelper.copyConsumableBuilder(Objects.requireNonNull(item.components().get(DataComponents.CONSUMABLE)))
                            .consumeSeconds(balancedFood.getEatSeconds()).build());
                    builder.set(DataComponents.MAX_STACK_SIZE, balancedFood.getStackSize());
                }));

        DefaultItemComponentEvents.MODIFY.register( mod ->
                mod.modify(item -> item.equals(Items.GOLDEN_CARROT), (builder, item) -> {
                    builder.set(DataComponents.CONSUMABLE,
                            BalancedFoodHelper.copyConsumableBuilder(Objects.requireNonNull(item.components().get(DataComponents.CONSUMABLE))).onConsume(
                                    new ApplyStatusEffectsConsumeEffect(
                                    List.of(new MobEffectInstance(MobEffects.SATURATION, 600))))
                                    .build());
                    builder.set(DataComponents.FOOD,
                            new FoodProperties.Builder().nutrition(1).saturationModifier(1).alwaysEdible().build()).build();
                }));

        DefaultItemComponentEvents.MODIFY.register( mod ->
                mod.modify(item -> item.equals(Items.GOLDEN_APPLE), (builder, item) -> builder.set(DataComponents.CONSUMABLE,
                        BalancedFoodHelper.copyWithoutEffects(Objects.requireNonNull(item.components().get(DataComponents.CONSUMABLE))).onConsume(
                                new ApplyStatusEffectsConsumeEffect(List.of(
                                        new MobEffectInstance(MobEffects.REGENERATION, 300, 1),
                                        new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0)))).build())));

        ServerPlayConnectionEvents.JOIN.register((handler, _, _) ->
                ((IFoodManager)handler.player).balance$createFoodData());

        ServerPlayConnectionEvents.DISCONNECT.register((handler, _) ->
                ((IFoodManager)handler.player).balance$saveFoodData());
    }

    private static boolean isChangedFood(Item item) {
        return item.components().has(DataComponents.CONSUMABLE) && BalancedFoodItems.hasBalancedFoodItem(item.toString());
    }

}
