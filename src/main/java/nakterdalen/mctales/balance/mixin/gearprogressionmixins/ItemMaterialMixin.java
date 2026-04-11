package nakterdalen.mctales.balance.mixin.gearprogressionmixins;

import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    private static void register1(String name, Item.Properties properties, CallbackInfoReturnable<Item> cir){
        addMaterial(name, properties);
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;spear(Lnet/minecraft/world/item/ToolMaterial;FFFFFFFFF)Lnet/minecraft/world/item/Item$Properties;"))
    private static Item.Properties register1end(Item.Properties instance, ToolMaterial material, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountThreshold, float knockbackTime, float knockbackThreshold, float damageTime, float damageThreshold){

        if (material.equals(ToolMaterial.DIAMOND)) {
            return new Item.Properties().spear(material, attackDuration, 1.2f, delay, dismountTime, dismountThreshold, knockbackTime, knockbackThreshold, damageTime, damageThreshold);
        }
        if (material.equals(ToolMaterial.NETHERITE)) {
            return new Item.Properties().spear(material, attackDuration, 1.075F, delay, dismountTime, dismountThreshold, knockbackTime, knockbackThreshold, damageTime, damageThreshold).fireResistant();
        }
        return instance;
    }

    @Inject(method = "registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"))
    private static void register2(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties, CallbackInfoReturnable<Item> cir){
        addMaterial(name, properties);
    }

    @Inject(method = "registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"))
    private static void register3(ResourceKey<Item> key, Function<Item.Properties, Item> itemFactory, Item.Properties properties, CallbackInfoReturnable<Item> cir){
        addMaterial(key.identifier().getNamespace(), properties);
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
