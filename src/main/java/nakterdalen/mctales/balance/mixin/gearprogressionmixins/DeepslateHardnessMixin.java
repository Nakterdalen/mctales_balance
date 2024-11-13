package nakterdalen.mctales.balance.mixin.gearprogressionmixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import java.util.function.Function;

@Mixin(Blocks.class)
public class DeepslateHardnessMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/Block;", ordinal = 804))
    private static Block newHardnessDeepslate(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, Operation<Block> original) {
        if (!Objects.equals(id, "deepslate")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match Deepslate");
        }
        return original.call(id, factory, settings.hardness(2.4f));
    }
}
