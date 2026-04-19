package nakterdalen.mctales.balance.mixin.client;

import nakterdalen.mctales.balance.food.BalancedFoodManager;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(LocalPlayer.class)
public abstract class ClientFoodMixin{

    @Unique
    BalancedFoodManager manager;

    @Redirect(method = "isSprintingPossible", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEnoughFoodToDoExhaustiveManoeuvres()Z"))
    private boolean canSprint(LocalPlayer instance) {

        if (instance.hasAttached(BalancedFoodManager.FOOD_ATTACHMENT)) {
            this.manager = instance.getAttached(BalancedFoodManager.FOOD_ATTACHMENT);
            return Objects.requireNonNull(this.manager).canRun();
        }

        return false;
    }
}
