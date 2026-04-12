package nakterdalen.mctales.balance.food;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;

import java.util.Arrays;
import java.util.Collections;
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

        DefaultItemComponentEvents.MODIFY.register( mod -> {
            mod.modify(FoodEvents::isChangedFood,(builder, provider, item) -> {
                BalancedFoodItems balancedFood = BalancedFoodItems.getBalancedFoodItem(item.toString());
                builder.set(DataComponents.CONSUMABLE,
                        BalancedFoodHelper.copyFoodBuilder(Objects.requireNonNull(item.components().get(DataComponents.CONSUMABLE)))
                                .consumeSeconds(balancedFood.getEatSeconds()).build());
                builder.set(DataComponents.MAX_STACK_SIZE, balancedFood.getStackSize());
            });
        });
    }

    private static boolean isChangedFood(Item item) {
        return item.components().has(DataComponents.CONSUMABLE) && BalancedFoodItems.hasBalancedFoodItem(item.toString());
    }

}
