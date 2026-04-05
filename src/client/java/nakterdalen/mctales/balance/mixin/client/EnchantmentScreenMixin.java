package nakterdalen.mctales.balance.mixin.client;

import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Shadow
    protected abstract void init();

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

    @Unique
    private int bookCount;

    @Unique
    private int toolTipNumber;

    @Unique
    private int[] enchants;

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

    @Unique
    private void checkBooks() {
        this.bookCount = ((IEnchantingHandler)this.handler).balance$getCount();
    }

    @Unique
    private void checkEnchants() {
        this.enchants = ((IEnchantingHandler)this.handler).balance$transferEnchants();
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        scrollPosition = 0f;
        scrollBarClicked = false;
        canScroll = false;
        bookCount = 0;
        ((IEnchantingHandler) handler).balance$setEnchantingListener(this::checkScroll);
        ((IEnchantingHandler) handler).balance$getBookCount(this::checkBooks);
        ((IEnchantingHandler) handler).balance$getEnchants(this::checkEnchants);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void addToMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        this.scrollBarClicked = false;
        double xval = click.x() - (double) (this.width - this.backgroundWidth) / 2;
        double yval = click.y() - (double) (this.height - this.backgroundHeight) / 2;
        if (xval > 156 && xval < 168 && yval > 14 + 42*scrollPosition && yval < 29 + 42*scrollPosition) {
            this.scrollBarClicked = true;
        }
    }

    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        int i = this.enchantNumber - 3;
        if (this.scrollBarClicked && this.canScroll && i > 0) {
            int j = this.y + 13;
            int k = j + 56;
            this.scrollPosition = ((float)click.y() - (float)j - 7.5F) / ((float)(k - j) - 15.0F);
            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0F, 1.0F);
            this.visibleTopRow = Math.max((int)((double)(this.scrollPosition * (float)i) + (double)0.5F), 0);
            return true;
        } else {
            return super.mouseDragged(click, offsetX, offsetY);
        }
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
        int width = (this.width - this.backgroundWidth) / 2;
        int height = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, width, height, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        this.drawBook(context, width, height);
        EnchantingPhrases.getInstance().setSeed(this.handler.getSeed());
        this.drawBook(context, width, height);
        int lapisCount = this.handler.getLapisCount();

        ItemStack enchantingStack = this.handler.slots.getFirst().getStack();
        int enchantability = enchantingStack.isEmpty() || !enchantingStack.isEnchantable() ? 0 : Objects.requireNonNull(enchantingStack.get(DataComponentTypes.ENCHANTABLE)).value();

        if(enchantability  == 0) {
            this.scrollPosition = 0f;
            this.visibleTopRow = 0;
        }

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
            int m = width + 60;
            int n = m + 20;

            int xpLevel = topRow + alt + 1;

            String stringLevel = "" + xpLevel;
            int p = 86 - this.textRenderer.getWidth(stringLevel) - 10;

            StringVisitable stringVisitable = phraseList.get(xpLevel-1);

            int q = -9937334;
            if ((lapisCount < topRow + alt + 1 || Objects.requireNonNull(Objects.requireNonNull(this.client).player).experienceLevel < xpLevel) && !Objects.requireNonNull(Objects.requireNonNull(this.client).player).getAbilities().creativeMode) {
                //alternative not available
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_TEXTURE, m, height + 14 + 19 * alt, 93, 19);
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LEVEL_DISABLED_TEXTURES[Math.min(alt+topRow, 4)], m + 1, height + 15 + 19 * alt, 16, 16);
                context.drawWrappedText(this.textRenderer, stringVisitable, n, height + 16 + 19 * alt, p, ColorHelper.fullAlpha((q & 16711422) >> 1), false);
                q = ColorHelper.fullAlpha(0xFF8C605D);
            } else {
                //alternative available
                int r = mouseX - (width + 60);
                int s = mouseY - (height + 13 + 19 * alt);

                if (r >= 0 && s >= 0 && r < 93 && s < 19) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_TEXTURE, m, height + 14 + 19 * alt, 93, 19);
                    q = -128;
                } else {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_TEXTURE, m, height + 14 + 19 * alt, 93, 19);
                }

                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, LEVEL_TEXTURES[Math.min(alt+topRow, 4)], m + 1, height + 15 + 19 * alt, 16, 16);
                context.drawWrappedText(this.textRenderer, stringVisitable, n, height + 16 + 19 * alt, p, q, false);
                q = -8323296;
            }

            context.drawTextWithShadow(this.textRenderer, stringLevel, n - 2 - this.textRenderer.getWidth(stringLevel), height + 16 + 19 * alt + 2, q);
        }
        int k = 0;
        Identifier scrollHandle = this.canScroll ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, scrollHandle, width + 156, height + 14 + k + (int)(42 * this.scrollPosition), 12, 15);
        ci.cancel();
    }

    @ModifyConstant(method = "mouseClicked", constant = @Constant(doubleValue = 108f))
    private double newBoundsForButton(double constant) {
        return 93.0F;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getEntry(I)Ljava/util/Optional;"))
    private void hintModifier(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci){
        int j = (this.height - this.backgroundHeight) / 2;
        this.toolTipNumber = this.visibleTopRow;
        int relY = mouseY + j;
        if (relY > 107) {
            this.toolTipNumber++;
        }
        if (relY > 124) {
            this.toolTipNumber++;
        }
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;II)V"), index = 1)
    private List<Text> alterTooltip(List<Text> text) {
        assert this.client != null && this.client.world != null;
        checkEnchants();

        Optional<RegistryEntry.Reference<Enchantment>> optional = this.client.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(this.enchants[this.toolTipNumber]);

        if (optional.isPresent()) {
            text.removeFirst();
            checkBooks();
            System.out.println("We check the books: " + this.bookCount);
            Text enchant = Enchantment.getName(optional.get(), 0);
            //System.out.println("We have the following enchants: " + this.enchants[this.toolTipNumber] + " " + this.toolTipNumber);
            if (this.toolTipNumber+1 > this.bookCount) {
                System.out.println("Tooltip : Bookcount " + this.toolTipNumber + " : " + this.bookCount);
                text.addFirst(Text.translatable("container.enchant.clue", "").formatted(Formatting.WHITE));
            } else if (toolTipNumber == 0){
                text.addFirst(enchant);
            } else {
                text.addFirst(Text.translatable("container.enchant.clue", enchant).formatted(Formatting.WHITE));
            }
        } else {
            text.clear();
        }
        return text;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 4)
    private int preventLevelRequirementText(int value){
        System.out.println("What do we have initially? " + value);
        return 1;
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/EnchantmentScreen;isPointWithinBounds(IIIIDD)Z"))
    private void changeBounds(Args args) {
        args.set(0, 61);
        args.set(2, 91);
    }
}
