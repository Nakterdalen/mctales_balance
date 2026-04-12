package nakterdalen.mctales.balance.mixin.foodmixins;

import nakterdalen.mctales.balance.food.BalancedFoodHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class FoodItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void onConsumeItem(ItemStack itemStack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        BalancedFoodHelper.consumeFood(itemStack, entity);
    }

}
