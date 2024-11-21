package nakterdalen.mctales.balance.mixin.enchantingmixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import nakterdalen.mctales.balance.MinecraftTalesBalance;
import nakterdalen.mctales.balance.enchantment.Enchantability;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(Items.class)
public class EnchantabilityMixin {

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 29))
    private static Item bowEnchantability(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("bow")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match bow.");
        }
        return original.call(id, factory, settings.enchantable(Enchantability.BOW_ENCHANTABILITY));
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 101))
    private static Item crossbowEnchantability(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("crossbow")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match crossbow.");
        }
        return original.call(id, factory, settings.enchantable(Enchantability.CROSSBOW_ENCHANTABILITY));
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 72))
    private static Item fishingRodEnchantability(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("fishing_rod")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match fishing rod.");
        }
        return original.call(id, factory, settings.enchantable(Enchantability.FISHING_ROD_ENCHANTABILITY));
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 82))
    private static Item maceEnchantability(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("mace")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match mace.");
        }
        return original.call(id, factory, settings.enchantable(Enchantability.MACE_ENCHANTABILITY));
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 100))
    private static Item tridentEnchantability(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("trident")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match trident.");
        }
        return original.call(id, factory, settings.enchantable(Enchantability.TRIDENT_ENCHANTABILITY));
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 75))
    private static Item shearsEnchantability(String id, Function<Item.Settings, Item> factory, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("shears")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match shears.");
        }
        return original.call(id, factory, settings.enchantable(Enchantability.IRON_ENCHANTABILITY));
    }

    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", ordinal = 11))
    private static Item bookEnchantability(String id, Item.Settings settings, Operation<Item> original) {
        if(!id.equals("book")) {
            MinecraftTalesBalance.LOGGER.error("Mixin ordinal did not match book.");
        }
        return original.call(id, settings.enchantable(Enchantability.BOOK_ENCHANTABILITY));
    }

}
