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
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {

    @Final
    @Shadow
    protected abstract void drawBook(DrawContext context, int x, int y);

    @Unique
    private static final Identifier[] LEVEL_TEXTURES = new Identifier[]{Identifier.ofVanilla("container/enchanting_table/level_1"), Identifier.ofVanilla("container/enchanting_table/level_2"), Identifier.ofVanilla("container/enchanting_table/level_3"), Identifier.ofVanilla("container/enchanting_table/level_4"), Identifier.ofVanilla("container/enchanting_table/level_5")};
    @Unique
    private static final Identifier[] LEVEL_DISABLED_TEXTURES = new Identifier[]{Identifier.ofVanilla("container/enchanting_table/level_1_disabled"), Identifier.ofVanilla("container/enchanting_table/level_2_disabled"), Identifier.ofVanilla("container/enchanting_table/level_3_disabled"), Identifier.ofVanilla("container/enchanting_table/level_4_disabled"), Identifier.ofVanilla("container/enchanting_table/level_5_disabled")};
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
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/loom/scroller");
    @Unique
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.ofVanilla("container/loom/scroller_disabled");

    @Unique
    private float scrollPosition;
    @Unique
    private boolean scrollBarClicked;
    @Unique
    private boolean canScroll;
    @Unique
    private int enchantNumber;
    @Unique
    private int visibleTopRow;

    public EnchantmentScreenMixin(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super((EnchantmentScreenHandler) handler, inventory, title);
    }

    @Unique
    private void checkScroll() {
        this.canScroll = false;
        ItemStack enchantedItem = this.handler.slots.getFirst().getStack();
        if (enchantedItem.isEnchantable() && Objects.requireNonNull(enchantedItem.get(DataComponentTypes.ENCHANTABLE)).value() > 3) {
            this.canScroll = true;
            this.enchantNumber = Objects.requireNonNull(enchantedItem.get(DataComponentTypes.ENCHANTABLE)).value();
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        scrollPosition = 0f;
        scrollBarClicked = false;
        canScroll = false;
        System.out.println("How many times do we reset");
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            if (this.canScroll) {
                int i = enchantNumber - 3;
                float f = (float) verticalAmount / (float) i;
                this.scrollPosition = MathHelper.clamp(this.scrollPosition - f, 0.0F, 1.0F);
                this.visibleTopRow = Math.max((int) (this.scrollPosition * (float) i + 0.5F), 0);
            }
        }
        return true;
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void newBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        //much copied
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        this.drawBook(context, i, j);
        EnchantingPhrases.getInstance().setSeed(this.handler.getSeed());
        this.drawBook(context, i, j);
        int lapisCount = this.handler.getLapisCount();

        int enchantability = this.handler.slots.getFirst().getStack().isEmpty() ? 0 : Objects.requireNonNull(this.handler.slots.getFirst().getStack().get(DataComponentTypes.ENCHANTABLE)).value();

        int topRow = 0;

        if (this.canScroll) {
            topRow = this.visibleTopRow;
        }

        List<StringVisitable> phraseList = new LinkedList<>();
        for (int in = 0; in < enchantability; in++) {
            int phraseWidth = 86 - this.textRenderer.getWidth(String.valueOf(in+1)) - 10;
            phraseList.add(EnchantingPhrases.getInstance().generatePhrase(this.textRenderer, phraseWidth));
        }

        for(int alt = 0; alt < Math.min(enchantability, 3); alt++) {
            int m = i + 60;
            int n = m + 20;

            int xpLevel = topRow + alt + 1;

            String stringLevel = "" + xpLevel;
            int p = 86 - this.textRenderer.getWidth(stringLevel) - 10;

            StringVisitable stringVisitable = phraseList.get(xpLevel-1);

            int q = -9937334;
            if ((lapisCount < topRow + alt + 1 || Objects.requireNonNull(Objects.requireNonNull(this.client).player).experienceLevel < xpLevel) && !Objects.requireNonNull(Objects.requireNonNull(this.client).player).getAbilities().creativeMode) {
                //alternative not available
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, j + 14 + 19 * alt, 93, 19);
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LEVEL_DISABLED_TEXTURES[Math.min(alt+topRow, 4)], m + 1, j + 15 + 19 * alt, 16, 16);
                context.drawWrappedText(this.textRenderer, stringVisitable, n, j + 16 + 19 * alt, p, ColorHelper.fullAlpha((q & 16711422) >> 1), false);
                q = ColorHelper.fullAlpha(0xFF8C605D);
            } else {
                //alternative available
                int r = mouseX - (i + 60);
                int s = mouseY - (j + 14 + 19 * alt);

                if (r >= 0 && s >= 0 && r < 108 && s < 19) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, m, j + 14 + 19 * alt, 93, 19);
                    q = -128;
                } else {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_TEXTURE, m, j + 14 + 19 * alt, 93, 19);
                }

                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LEVEL_TEXTURES[Math.min(alt+topRow, 4)], m + 1, j + 15 + 19 * alt, 16, 16);
                context.drawWrappedText(this.textRenderer, stringVisitable, n, j + 16 + 19 * alt, p, q, false);
                q = -8323296;
            }

            context.drawTextWithShadow(this.textRenderer, stringLevel, n - 2 - this.textRenderer.getWidth(stringLevel), j + 16 + 19 * alt + 2, q);
        }
        int k = 0;
        Identifier scrollHandle = this.canScroll ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, scrollHandle, i + 156, j + 14 + k + 42 * topRow/(Math.max((enchantability-3), 1)), 12, 15);
        ci.cancel();
    }
}
