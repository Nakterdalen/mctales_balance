package nakterdalen.mctales.balance.mixin.client;

import nakterdalen.mctales.balance.MinecraftTalesBalance;
import nakterdalen.mctales.balance.food.BalancedFoodManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Gui.class)
public abstract class FoodBarMixin {

    @Unique
    private static final Identifier EMPTY_BAR = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_empty");
    @Unique
    private static final Identifier EMPTY_BAR_HUNGER = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_empty_hunger");
    @Unique
    private static final Identifier GRAIN_BAR = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_grain");
    @Unique
    private static final Identifier GRAIN_BAR_HUNGER = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_grain_hunger");
    @Unique
    private static final Identifier MEAT_BAR = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_meat");
    @Unique
    private static final Identifier MEAT_BAR_HUNGER = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_meat_hunger");
    @Unique
    private static final Identifier VEGETABLE_BAR = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_vegetable");
    @Unique
    private static final Identifier VEGETABLE_BAR_HUNGER = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_vegetable_hunger");

    @Unique
    private static final Identifier MEAT_HALF = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_meat_half");
    @Unique
    private static final Identifier MEAT_HALF_HUNGER = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_meat_half_hunger");
    @Unique
    private static final Identifier GRAIN_HALF = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_grain_half");
    @Unique
    private static final Identifier GRAIN_HALF_HUNGER = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_grain_half_hunger");
    @Unique
    private static final Identifier VEGETABLE_HALF = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_vegetable_half");
    @Unique
    private static final Identifier VEGETABLE_HALF_HUNGER = Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food/food_vegetable_half_hunger");



    @Unique
    private static final Identifier[] BAR_LIST = {EMPTY_BAR, MEAT_BAR, GRAIN_BAR, VEGETABLE_BAR};
    @Unique
    private static final Identifier[] BAR_LIST_HUNGER = {EMPTY_BAR_HUNGER, MEAT_BAR_HUNGER, GRAIN_BAR_HUNGER, VEGETABLE_BAR_HUNGER};
    @Unique
    private static final Identifier[] HALF_LIST = {MEAT_HALF, GRAIN_HALF, VEGETABLE_HALF};
    @Unique
    private static final Identifier[] HALF_LIST_HUNGER = {MEAT_HALF_HUNGER, GRAIN_HALF_HUNGER, VEGETABLE_HALF_HUNGER};

    @Shadow
    private int tickCount;

    @Mutable
    @Final
    @Shadow
    private final RandomSource random;

    protected FoodBarMixin(RandomSource random) {
        this.random = random;
    }

    @Inject(method = "extractFood", at = @At("HEAD"), cancellable = true)
    private void renderFoodBar(GuiGraphicsExtractor graphics, Player player, int yLineBase, int xRight, CallbackInfo ci) {

        BalancedFoodManager manager;
        if (player.hasAttached(BalancedFoodManager.FOOD_ATTACHMENT) && (manager = player.getAttached(BalancedFoodManager.FOOD_ATTACHMENT)) != null) {
            List<BalancedFoodManager.FoodType> filteredList = manager.getHungerBar().stream().filter(foodType -> !foodType.equals(BalancedFoodManager.FoodType.NONE)).toList();
            for (int i = 0; i < 10; i++) {
                BalancedFoodManager.FoodType currentType = manager.getHungerBar().get(i);
                Identifier currentId;
                if (manager.getExhaustion() > 10 && i == 10 - filteredList.size()) {
                    currentId = player.hasEffect(MobEffects.HUNGER) ? HALF_LIST_HUNGER[currentType.ordinal() - 1] :
                            HALF_LIST[currentType.ordinal() - 1];
                } else {
                    currentId = player.hasEffect(MobEffects.HUNGER) ? BAR_LIST_HUNGER[currentType.ordinal()] :
                            BAR_LIST[currentType.ordinal()];
                }
                int x_pos = xRight - (9 - i) * 8 - 9;
                int y_pos = yLineBase;
                if (!currentType.equals(manager.getLastFoodType()) && this.tickCount % (manager.getHungerBar().stream().filter(type -> !type.equals(BalancedFoodManager.FoodType.NONE)).count() * 9 + 1) == 0) {
                    y_pos = yLineBase + (this.random.nextInt(3) - 1);
                }
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, currentId, x_pos, y_pos, 9, 9);
            }
        }
        ci.cancel();
    }
}
