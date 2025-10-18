package nakterdalen.mctales.balance.mixin.gearprogressionmixins;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Items.class)
public class ItemMaterialMixin {

    /*
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 100))
    private static Item tridentMaterial(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("trident")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match trident.");
        }
        return original.call(id, factory, settings.repairable(Items.PRISMARINE_CRYSTALS));
    }
     */

    @Inject(method = "register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"))
    private static void register1(String id, Item.Settings settings, CallbackInfoReturnable<Item> cir){
        addMaterial(id, settings);
    }

    @Inject(method = "register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"))
    private static void register2(String id, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir){
        addMaterial(id, settings);
    }

    @Inject(method = "register(Lnet/minecraft/registry/RegistryKey;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"))
    private static void register3(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir){
        addMaterial(key.getValue().getNamespace(), settings);
    }

    @Unique
    private static void addMaterial(String id, Item.Settings settings) {
        if (id.equals("trident")) {
            settings.repairable(Items.PRISMARINE_CRYSTALS);
        }

        if (id.equals("shears")) {
            settings.repairable(Items.IRON_INGOT);
        }
    }

}
