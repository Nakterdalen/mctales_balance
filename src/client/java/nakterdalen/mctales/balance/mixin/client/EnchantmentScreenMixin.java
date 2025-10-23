package nakterdalen.mctales.balance.mixin.client;

import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {

    @Final
    @Shadow
    protected abstract void drawBook(DrawContext context, int x, int y);

    @Final
    @Shadow
    private static Identifier[] LEVEL_TEXTURES;
    @Final
    @Shadow
    private static Identifier[] LEVEL_DISABLED_TEXTURES;
    @Final
    @Shadow
    private static Identifier ENCHANTMENT_SLOT_DISABLED_TEXTURE;
    @Final
    @Shadow
    private static Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE;
    @Final
    @Shadow
    private static Identifier ENCHANTMENT_SLOT_TEXTURE;
    @Final
    @Shadow
    private static Identifier TEXTURE;

    @Unique
    private float scrollPosition;
    @Unique
    private boolean scrollBarClicked;
    @Unique
    private boolean canScroll;

    public EnchantmentScreenMixin(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super((EnchantmentScreenHandler) handler, inventory, title);
    }

    @Unique
    private void checkScroll() {

        ItemStack enchantedItem = this.handler.slots.getFirst().getStack();

        if (enchantedItem.isEnchantable() && Objects.requireNonNull(enchantedItem.get(DataComponentTypes.ENCHANTABLE)).value() > 3) {
            System.out.println("We made it");
            this.canScroll = true;
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        scrollPosition = 0f;
        scrollBarClicked = false;
        canScroll = false;

        ((IEnchantingHandler) handler).balance$setEnchantingListener(this::checkScroll);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void addToMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        this.scrollBarClicked = false;
        /*
        if ()
        if (click.x() >= (double)i && click.x() < (double)(i + 12) && click.y() >= (double)j && click.y() < (double)(j + 56)) {
            this.scrollbarClicked = true;
            System.out.println("we are clicking.");
        }

         */
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void newBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        //copied
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        this.drawBook(context, i, j);
        EnchantingPhrases.getInstance().setSeed(this.handler.getSeed());
        this.drawBook(context, i, j);
        int lapisCount = this.handler.getLapisCount();

        int enchantability = this.handler.slots.getFirst().getStack().isEmpty() ? 0 : Objects.requireNonNull(this.handler.slots.getFirst().getStack().get(DataComponentTypes.ENCHANTABLE)).value();

        for(int alt = 0; alt < enchantability; alt++) {
            int m = i + 60;
            int n = m + 20;

            int xpLevel = alt + 1;

            String string = "" + xpLevel;
            int p = 86 - this.textRenderer.getWidth(string);
            StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(this.textRenderer, p);

            int q = -9937334;
            if ((lapisCount < alt + 1 || Objects.requireNonNull(Objects.requireNonNull(this.client).player).experienceLevel < xpLevel) && !Objects.requireNonNull(Objects.requireNonNull(this.client).player).getAbilities().creativeMode) {
                //alternative not available
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, j + 14 + 19 * alt, 108, 19);
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LEVEL_DISABLED_TEXTURES[Math.min(alt, 2)], m + 1, j + 15 + 19 * alt, 16, 16);
                context.drawWrappedText(this.textRenderer, stringVisitable, n, j + 16 + 19 * alt, p, ColorHelper.fullAlpha((q & 16711422) >> 1), false);
                q = -12550384;
            } else {
                //alternative available
                int r = mouseX - (i + 60);
                int s = mouseY - (j + 14 + 19 * alt);

                if (r >= 0 && s >= 0 && r < 108 && s < 19) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, m, j + 14 + 19 * alt, 108, 19);
                    q = 16777088;
                } else {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_TEXTURE, m, j + 14 + 19 * alt, 108, 19);
                }

                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LEVEL_TEXTURES[Math.min(alt, 2)], m + 1, j + 15 + 19 * alt, 16, 16);
                context.drawWrappedText(this.textRenderer, stringVisitable, n, j + 16 + 19 * alt, p, q, false);
                q = 8453920;
            }

            context.drawTextWithShadow(this.textRenderer, string, n + 86 - this.textRenderer.getWidth(string), j + 16 + 19 * alt + 7, q);

            // just for show, remove later
            if (this.canScroll) {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_TEXTURE, m+40, j + 14 + 19 * alt, 108, 19);
            }
        }
        ci.cancel();
    }
}
