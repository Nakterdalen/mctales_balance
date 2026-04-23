package nakterdalen.mctales.balance.mixin.foodmixins;

import net.minecraft.world.effect.InstantenousMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InstantenousMobEffect.class)
public abstract class InstantSaturationMixin {

    //Very tacky
    @Inject(method = "isInstantenous", at = @At("HEAD"), cancellable = true)
    private void changeForSaturation(CallbackInfoReturnable<Boolean> cir) {
        if (this.toString().contains("SaturationMobEffect")) {
            cir.setReturnValue(false);
        }
    }
}
