package nakterdalen.mctales.balance.food;

import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

public class FoodEvents {

    public static void registerMineFoodEvent() {
        PlayerBlockBreakEvents.AFTER.register( (level, player, pos, state, blockEntity) -> {
            if (player instanceof ServerPlayer && !player.isSpectator()) {
                ((IFoodManager)player).balance$mineHunger();
            }
        });

        AttackEntityCallback.EVENT.register( (player, level, hand, entity, hitResult) -> {
            if (player instanceof ServerPlayer && !player.isSpectator()) {
                ((IFoodManager)player).balance$mineHunger();
            }
            return InteractionResult.PASS;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register( (oldPlayer, newPlayer, alive) -> {
            BalancedFoodManager newManager = new BalancedFoodManager();
            newPlayer.setAttached(BalancedFoodManager.FOOD_ATTACHMENT, newManager);
            ((IFoodManager)newPlayer).balance$setFoodManager(newManager);
        });
    }

}
