package nakterdalen.mctales.balance.mixin.enchantingmixins;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class ExperienceLevelMixin {

    @Shadow
    public int experienceLevel;

    @Inject(method = "getXpNeededForNextLevel", at = @At("HEAD"), cancellable = true)
    private void getNewExperienceRequirement(CallbackInfoReturnable<Integer> cir) {
        int returnValue = (this.experienceLevel < 5) ? 18 + this.experienceLevel :
                (this.experienceLevel-this.experienceLevel%5)*18 + (this.experienceLevel-this.experienceLevel%5)*this.experienceLevel%5;
        cir.setReturnValue(returnValue);
    }

}
