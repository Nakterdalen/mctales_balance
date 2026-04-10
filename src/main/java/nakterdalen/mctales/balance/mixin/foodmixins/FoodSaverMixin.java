package nakterdalen.mctales.balance.mixin.foodmixins;


import nakterdalen.mctales.balance.food.BalancedFoodManager;
import nakterdalen.mctales.balance.food.IFoodSaver;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class FoodSaverMixin implements IFoodSaver {

    private NbtCompound persistentData;

    @Override
    public NbtCompound balance$getPersistentData() {

        if(this.persistentData == null) {
            this.persistentData = new NbtCompound();
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
