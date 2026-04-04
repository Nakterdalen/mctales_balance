package nakterdalen.mctales.balance.mixin.enchantingmixins;


import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.*;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantingScreenHandlerMixin extends ScreenHandler implements IEnchantingHandler {

    @Shadow
    @Final
    private ScreenHandlerContext context;
    @Shadow
    @Final
    public int[] enchantmentPower;
    @Final
    @Shadow
    public int[] enchantmentLevel;
    @Unique
    public Runnable activateScroll = () -> {};

    @Unique
    public Runnable bookCounter = () -> {};

    protected EnchantingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "onContentChanged", at = @At("TAIL"))
    private void updateActivateScroll(Inventory inventory, CallbackInfo ci) {
        activateScroll.run();
        this.context.run((world, pos) -> {
            int count = 0;
            for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
                if (EnchantingTableBlock.canAccessPowerProvider(world, pos, blockPos)) {
                    count++;
                }
            }
            this.enchantmentLevel[0] = count/3;
            this.sendContentUpdates();
        });
        bookCounter.run();
    }

    @ModifyVariable(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;generateEnchantments(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;ILjava/util/stream/Stream;)Ljava/util/List;"), index = 3, argsOnly = true)
    private int modifyLevel(int value) {
        return 10;
    }

    @Override
    public void balance$setEnchantingListener(Runnable activateScroll) {
        this.activateScroll = activateScroll;
    }
    @Override
    public void balance$getBookCount(Runnable count) {
        this.bookCounter = count;
    }

    @Override
    public int balance$getCount() {
        this.sendContentUpdates();
        return this.enchantmentLevel[0];
    }

}
