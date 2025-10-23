package nakterdalen.mctales.balance.mixin.enchantingmixins;


import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantingScreenHandlerMixin extends ScreenHandler implements IEnchantingHandler {

    @Unique
    public Runnable activateScroll = () -> {};

    protected EnchantingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }


    @Inject(method = "onContentChanged", at = @At("HEAD"))
    private void updateActivateScroll(Inventory inventory, CallbackInfo ci) {
        activateScroll.run();
    }

    @Override
    public void balance$setEnchantingListener(Runnable activateScroll) {
        this.activateScroll = activateScroll;
    }
}
