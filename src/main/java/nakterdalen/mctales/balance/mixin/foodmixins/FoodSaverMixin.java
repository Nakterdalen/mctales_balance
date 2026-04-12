package nakterdalen.mctales.balance.mixin.foodmixins;


import com.mojang.authlib.GameProfile;
import nakterdalen.mctales.balance.food.BalancedFoodManager;
import nakterdalen.mctales.balance.food.IFoodManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueOutput;
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

    public FoodSaverMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void checkFoodManager(CallbackInfo ci) {
        if (!this.hasAttached(BalancedFoodManager.FOOD_ATTACHMENT)) {
            this.setAttached(BalancedFoodManager.FOOD_ATTACHMENT, new BalancedFoodManager());
        }
        manager = this.getAttached(BalancedFoodManager.FOOD_ATTACHMENT);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveFoodManager(ValueOutput output, CallbackInfo ci) {
        this.setAttached(BalancedFoodManager.FOOD_ATTACHMENT, manager);
    }

    //Movement
    @Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V"))
    private void movementHunger(double dx, double dy, double dz, CallbackInfo ci) {
        int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
        addExhaustion(0.01f * distance * 0.01f, BalancedFoodManager.FoodType.GRAIN);
    }
    @Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 5))
    private void movementHungerWalk(double dx, double dy, double dz, CallbackInfo ci) {
        int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
        addExhaustion(-0.005f * distance * 0.01f, BalancedFoodManager.FoodType.GRAIN);
    }
    @Inject(method = "checkMovementStatistics", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V", ordinal = 4))
    private void movementHungerCrouch(double dx, double dy, double dz, CallbackInfo ci) {
        int distance = Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
        addExhaustion(-0.01f * distance * 0.01f, BalancedFoodManager.FoodType.GRAIN);
    }
    @Inject(method = "jumpFromGround", at = @At(value = "TAIL"))
    private void jumpHunger(CallbackInfo ci) {
        if (this.isSprinting()) {
            addExhaustion(0.2F, BalancedFoodManager.FoodType.GRAIN);
        } else {
            addExhaustion(0.05F, BalancedFoodManager.FoodType.GRAIN);
        }
    }

    //tick
    @Redirect(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;tick(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void hungerTick(FoodData instance, ServerPlayer player) {

        if (this.foodData.needsFood()) {
            this.foodData.setFoodLevel(instance.getFoodLevel() + 1);
        }
        manager.foodTick(this);
    }

    //fix sprinting in client.

    //Mining, attacking. Handled through event in different class
    public void balance$mineHunger() {
        this.addExhaustion(0.1F,  BalancedFoodManager.FoodType.VEGETABLE);
    }

    //Can run. Injection in different class
    public boolean balance$canRun() {
        return this.manager.canRun();
    }

    @Unique
    private void addExhaustion(float value, BalancedFoodManager.FoodType type) {
        if (!this.isInvulnerable() && this.level().getDifficulty() != Difficulty.PEACEFUL && !this.level().isClientSide()) {
            this.manager.addExhaustion(value, type);
        }
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
}
