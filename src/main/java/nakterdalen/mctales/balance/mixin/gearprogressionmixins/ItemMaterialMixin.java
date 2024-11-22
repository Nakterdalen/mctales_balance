package nakterdalen.mctales.balance.mixin.gearprogressionmixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(Items.class)
public class ItemMaterialMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 100))
    private static Item tridentMaterial(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("trident")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match trident.");
        }
        return original.call(id, factory, settings.repairable(Items.PRISMARINE_CRYSTALS));
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 75))
    private static Item shearsMaterial(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("shears")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match shears.");
        }
        return original.call(id, factory, settings.repairable(Items.IRON_INGOT));
    }

}
