package nakterdalen.mctales.balance.mixin.foodmixins;

import nakterdalen.mctales.balance.food.BalancedFoodManager;
import nakterdalen.mctales.balance.food.IFoodManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Player.class)
public abstract class FoodPlayerMixin extends Avatar{

    @Final
    @Shadow
    private Abilities abilities;

    protected FoodPlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "canEat", at=@At("HEAD"), cancellable = true)
    private void newCanEat(boolean canAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player)(Object)this;
        if(player instanceof ServerPlayer) {
            cir.setReturnValue(this.abilities.invulnerable || canAlwaysEat || ((IFoodManager)player).balance$canEat());
        } else {
            if (player.hasAttached(BalancedFoodManager.FOOD_ATTACHMENT)) {
                boolean uglyEat = Objects.requireNonNull(player.getAttached(BalancedFoodManager.FOOD_ATTACHMENT)).canEat();
                cir.setReturnValue(this.abilities.invulnerable || canAlwaysEat || uglyEat);
            }
        }
    }

}
