package nakterdalen.mctales.balance.mixin.foodmixins;


import nakterdalen.mctales.balance.food.BalancedFoodManager;
import nakterdalen.mctales.balance.food.IFoodSaver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class FoodSaverMixin implements IFoodSaver {

    private CompoundTag persistentData;

    @Override
    public CompoundTag balance$getPersistentData() {

        if(this.persistentData == null) {
            this.persistentData = new CompoundTag();
        }
        return persistentData;
    }

    /*
    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void writeFoodData(WriteView view, CallbackInfo ci) {
        view.put(BalancedFoodManager.FOOD_DATA, );
    }

     */



}
