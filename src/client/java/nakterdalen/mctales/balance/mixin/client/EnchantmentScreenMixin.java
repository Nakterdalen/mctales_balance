package nakterdalen.mctales.balance.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {

    @Shadow
    private void drawBook(DrawContext context, int x, int y, float delta) {}

    @Unique
    private static final Identifier[] LEVEL_TEXTURES = new Identifier[]{Identifier.ofVanilla("container/enchanting_table/level_1"), Identifier.ofVanilla("container/enchanting_table/level_2"), Identifier.ofVanilla("container/enchanting_table/level_3")};
    @Unique
    private static final Identifier[] LEVEL_DISABLED_TEXTURES = new Identifier[]{Identifier.ofVanilla("container/enchanting_table/level_1_disabled"), Identifier.ofVanilla("container/enchanting_table/level_2_disabled"), Identifier.ofVanilla("container/enchanting_table/level_3_disabled")};
    @Unique
    private static final Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE = Identifier.ofVanilla("container/enchanting_table/enchantment_slot_disabled");
    @Unique
    private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("container/enchanting_table/enchantment_slot_highlighted");
    @Unique
    private static final Identifier ENCHANTMENT_SLOT_TEXTURE = Identifier.ofVanilla("container/enchanting_table/enchantment_slot");
    @Unique
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/enchanting_table.png");

    public EnchantmentScreenMixin(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super((EnchantmentScreenHandler) handler, inventory, title);
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void newBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int i = (this.width - this.backgroundWidth) / 2;

        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        this.drawBook(context, i, j, delta);
        EnchantingPhrases.getInstance().setSeed(this.handler.getSeed());
        int lapisCount = this.handler.getLapisCount();

        for(int l = 0; l < 3; ++l) {
            int m = i + 60;
            int n = m + 20;
            int xpLevel = this.handler.enchantmentPower[l];
            if (xpLevel == 0) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, j + 14 + 19 * l, 108, 19);
            } else {
                String string = "" + xpLevel;
                int p = 86 - this.textRenderer.getWidth(string);
                StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(this.textRenderer, p);
                int q = 6839882;
                if ((lapisCount < l + 1 || Objects.requireNonNull(Objects.requireNonNull(this.client).player).experienceLevel < xpLevel) && !Objects.requireNonNull(Objects.requireNonNull(this.client).player).getAbilities().creativeMode) {
                    context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, j + 14 + 19 * l, 108, 19);
                    context.drawGuiTexture(RenderLayer::getGuiTextured, LEVEL_DISABLED_TEXTURES[l], m + 1, j + 15 + 19 * l, 16, 16);
                    context.drawTextWrapped(this.textRenderer, stringVisitable, n, j + 16 + 19 * l, p, (q & 16711422) >> 1);
                    //q = 4226832;
                } else {
                    int r = mouseX - (i + 60);
                    int s = mouseY - (j + 14 + 19 * l);
                    if (r >= 0 && s >= 0 && r < 108 && s < 19) {
                        context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, m, j + 14 + 19 * l, 108, 19);
                        q = 16777088;
                    } else {
                        context.drawGuiTexture(RenderLayer::getGuiTextured, ENCHANTMENT_SLOT_TEXTURE, m, j + 14 + 19 * l, 108, 19);
                    }

                    context.drawGuiTexture(RenderLayer::getGuiTextured, LEVEL_TEXTURES[l], m + 1, j + 15 + 19 * l, 16, 16);
                    context.drawTextWrapped(this.textRenderer, stringVisitable, n, j + 16 + 19 * l, p, q);
                    //q = 8453920;
                }

                //context.drawTextWithShadow(this.textRenderer, string, n + 86 - this.textRenderer.getWidth(string), j + 16 + 19 * l + 7, q);
            }
        }
        ci.cancel();
    }

}
