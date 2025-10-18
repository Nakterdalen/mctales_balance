package nakterdalen.mctales.balance.mixin.enchantingmixins;

import nakterdalen.mctales.balance.enchanting.Enchantability;
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
public class EnchantabilityMixin {

    /*
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 29))
    private static Item bowEnchantability(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("bow")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match bow.");
        }
        return original.call(id, factory, settings.enchantable(Enchantability.BOW_ENCHANTABILITY));
    }
    */

    @Inject(method = "register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"))
    private static void register1(String id, Item.Settings settings, CallbackInfoReturnable<Item> cir){
        addEnchantability(id, settings);
    }

    @Inject(method = "register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"))
    private static void register2(String id, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir){
        addEnchantability(id, settings);
    }

    @Inject(method = "register(Lnet/minecraft/registry/RegistryKey;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"))
    private static void register3(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir){
        addEnchantability(key.getValue().getNamespace(), settings);
    }

    @Unique
    private static void addEnchantability(String id, Item.Settings settings) {
        if (id.equals("bow")) {
            settings.enchantable(Enchantability.BOW_ENCHANTABILITY);
        }

        if (id.equals("crossbow")) {
            settings.enchantable(Enchantability.CROSSBOW_ENCHANTABILITY);
        }

        if (id.equals("fishing_rod")) {
            settings.enchantable(Enchantability.FISHING_ROD_ENCHANTABILITY);
        }

        if (id.equals("mace")) {
            settings.enchantable(Enchantability.MACE_ENCHANTABILITY);
        }

        if (id.equals("trident")) {
            settings.enchantable(Enchantability.TRIDENT_ENCHANTABILITY);
        }

        if (id.equals("shears")) {
            settings.enchantable(Enchantability.IRON_ENCHANTABILITY);
        }

        if (id.equals("book")) {
            settings.enchantable(Enchantability.BOOK_ENCHANTABILITY);
        }
    }

}
