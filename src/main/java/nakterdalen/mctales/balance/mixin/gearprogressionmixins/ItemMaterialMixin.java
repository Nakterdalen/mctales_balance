package nakterdalen.mctales.balance.mixin.gearprogressionmixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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

    @Inject(method = "registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"))
    private static void register1(String id, Item.Properties settings, CallbackInfoReturnable<Item> cir){
        addMaterial(id, settings);
    }

    @Inject(method = "registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"))
    private static void register2(String id, Function<Item.Properties, Item> factory, Item.Properties settings, CallbackInfoReturnable<Item> cir){
        addMaterial(id, settings);
    }

    @Inject(method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"))
    private static void register3(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties settings, CallbackInfoReturnable<Item> cir){
        addMaterial(key.location().getNamespace(), settings);
    }

    @Unique
    private static void addMaterial(String id, Item.Properties settings) {
        if (id.equals("trident")) {
            settings.repairable(Items.PRISMARINE_CRYSTALS);
        }

        if (id.equals("shears")) {
            settings.repairable(Items.IRON_INGOT);
        }
    }

}
