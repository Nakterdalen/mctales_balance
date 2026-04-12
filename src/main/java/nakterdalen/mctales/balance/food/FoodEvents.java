package nakterdalen.mctales.balance.food;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

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
    }

}
