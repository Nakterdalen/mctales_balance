package nakterdalen.mctales.balance.mixin.foodmixins;

import com.mojang.authlib.GameProfile;
import nakterdalen.mctales.balance.food.BalancedFoodManager;
import nakterdalen.mctales.balance.food.IFoodManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayer.class)
public abstract class FoodSaverMixin extends Player implements IFoodManager {

    @Unique
    BalancedFoodManager manager;

    @Unique
    private boolean dataChanged = true;

    public FoodSaverMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }


    //Movement
    @Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V"))
    private void movementHunger(double dx, double dy, double dz, CallbackInfo ci) {
        int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
        manager.addExhaustion(0.01f * distance * 0.01f, BalancedFoodManager.FoodType.GRAIN, this, 0.01f);
        this.dataChanged = true;
    }
    @Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 5))
    private void movementHungerWalk(double dx, double dy, double dz, CallbackInfo ci) {
        int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
        manager.addExhaustion(-0.005f * distance * 0.01f, BalancedFoodManager.FoodType.GRAIN, this, 0f);
        this.dataChanged = true;
    }
    @Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 4))
    private void movementHungerCrouch(double dx, double dy, double dz, CallbackInfo ci) {
        int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
        manager.addExhaustion(-0.01f * distance * 0.01f, BalancedFoodManager.FoodType.GRAIN, this, 0f);
        this.dataChanged = true;
    }
    @Inject(method = "jumpFromGround", at = @At(value = "TAIL"))
    private void jumpHunger(CallbackInfo ci) {
        float jumpCost = this.isSprinting() ? 0.2f : 0.05f;
        manager.addExhaustion(jumpCost, BalancedFoodManager.FoodType.GRAIN, this);
        this.dataChanged = true;
    }

    //tick
    @Redirect(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;tick(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void hungerTick(FoodData instance, ServerPlayer player) {

        // Sync food data
        if (this.tickCount % 5 == 0 && dataChanged) {
            this.setAttached(BalancedFoodManager.FOOD_ATTACHMENT, new BalancedFoodManager(manager));
            dataChanged = false;
        }

        if (this.foodData.needsFood()) {
            this.foodData.setFoodLevel(instance.getFoodLevel() + 1);
        }

        manager.foodTick(this);
    }

    //Mining, attacking. Handled through event in different class
    public void balance$mineHunger() {
        manager.addExhaustion(0.1F,  BalancedFoodManager.FoodType.VEGETABLE, this);
        this.dataChanged = true;
    }

    public void balance$markDirtyFood() {
        this.dataChanged =  true;
    }

    public boolean balance$canEat() {
        return this.manager.canEat();
    }

    public void balance$setFoodManager(BalancedFoodManager manager) {
        this.manager = manager;
    }

    @Override
    public BalancedFoodManager balance$getFoodManager() {
        return this.manager;
    }

    public void balance$regenHunger() {
        manager.addExhaustion(1.0f, BalancedFoodManager.FoodType.MEAT, this);
    }

    public void balance$createFoodData() {
        if (!this.hasAttached(BalancedFoodManager.FOOD_ATTACHMENT)) {
            this.setAttached(BalancedFoodManager.FOOD_ATTACHMENT, new BalancedFoodManager());
        }
        manager = this.getAttached(BalancedFoodManager.FOOD_ATTACHMENT);
    }

    public void balance$saveFoodData() {
        this.setAttached(BalancedFoodManager.FOOD_ATTACHMENT, new BalancedFoodManager(manager));
    }
}
