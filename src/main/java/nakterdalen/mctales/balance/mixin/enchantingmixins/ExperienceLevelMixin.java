package nakterdalen.mctales.balance.mixin.enchantingmixins;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class ExperienceLevelMixin {

    @Shadow
    public int experienceLevel;

    @Inject(method = "getNextLevelExperience", at = @At("HEAD"), cancellable = true)
    private void getNewExperienceRequirement(CallbackInfoReturnable<Integer> cir) {
        int returnValue = (this.experienceLevel < 5) ? 18 + this.experienceLevel :
                (this.experienceLevel-this.experienceLevel%5)*18 + (this.experienceLevel-this.experienceLevel%5)*this.experienceLevel%5;
        cir.setReturnValue(returnValue);
    }

}
