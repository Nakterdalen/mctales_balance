package nakterdalen.mctales.balance.mixin.client;

import com.google.common.collect.Lists;
import nakterdalen.mctales.balance.enchanting.IEnchantingHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(net.minecraft.client.gui.screens.inventory.EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends AbstractContainerScreen<EnchantmentMenu> {

    @Final
    @Shadow
    protected abstract void renderBook(GuiGraphics guiGraphics, int x, int y);

    @Unique
    private static final ResourceLocation[] LEVEL_TEXTURES = new ResourceLocation[]{ResourceLocation.withDefaultNamespace("container/enchanting_table/level_1"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_2"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_3"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_4"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_5")};
    @Unique
    private static final ResourceLocation[] LEVEL_DISABLED_TEXTURES = new ResourceLocation[]{ResourceLocation.withDefaultNamespace("container/enchanting_table/level_1_disabled"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_2_disabled"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_3_disabled"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_4_disabled"), ResourceLocation.withDefaultNamespace("container/enchanting_table/level_5_disabled")};
    @Final
    @Shadow
    private static ResourceLocation ENCHANTMENT_SLOT_DISABLED_SPRITE;
    @Final
    @Shadow
    private static ResourceLocation ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE;
    @Final
    @Shadow
    private static ResourceLocation ENCHANTMENT_SLOT_SPRITE;
    @Final
    @Shadow
    private static ResourceLocation ENCHANTING_TABLE_LOCATION;

    @Shadow
    protected abstract void init();

    @Unique
    private static final ResourceLocation SCROLLER_TEXTURE = ResourceLocation.withDefaultNamespace("container/loom/scroller");
    @Unique
    private static final ResourceLocation SCROLLER_DISABLED_TEXTURE = ResourceLocation.withDefaultNamespace("container/loom/scroller_disabled");

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

    public EnchantmentScreenMixin(EnchantmentMenu enchantmentMenu, Inventory inventory, Component component) {
        super((EnchantmentMenu ) enchantmentMenu, inventory, component);
    }

    @Unique
    private void checkScroll() {
        this.canScroll = false;
        ItemStack enchantedItem = this.menu.slots.getFirst().getItem();
        if (enchantedItem.isEnchantable() && Objects.requireNonNull(enchantedItem.get(DataComponents.ENCHANTABLE)).value() > 3) {
            this.canScroll = true;
            this.enchantNumber = Objects.requireNonNull(enchantedItem.get(DataComponents.ENCHANTABLE)).value();
        }
    }

    @Unique
    private void checkBooks() {
        this.bookCount = ((IEnchantingHandler)this.menu).balance$getCount();
    }

    @Unique
    private void checkEnchants() {
        this.enchants = ((IEnchantingHandler)this.menu).balance$transferEnchants();
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(EnchantmentMenu enchantmentMenu, Inventory inventory, Component component, CallbackInfo ci) {
        scrollPosition = 0f;
        scrollBarClicked = false;
        canScroll = false;
        bookCount = 0;
        ((IEnchantingHandler) enchantmentMenu).balance$setEnchantingListener(this::checkScroll);
        ((IEnchantingHandler) enchantmentMenu).balance$getBookCount(this::checkBooks);
        ((IEnchantingHandler) enchantmentMenu).balance$getEnchants(this::checkEnchants);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void addToMouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        this.scrollBarClicked = false;
        double xval = mouseButtonEvent.x() - (double) (this.width - this.imageWidth) / 2;
        double yval = mouseButtonEvent.y() - (double) (this.height - this.imageHeight) / 2;
        if (xval > 156 && xval < 168 && yval > 14 + 42*scrollPosition && yval < 29 + 42*scrollPosition) {
            this.scrollBarClicked = true;
        }
    }

    //might be problem
    @ModifyArg(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/EnchantmentMenu;clickMenuButton(Lnet/minecraft/world/entity/player/Player;I)Z"), index = 1)
    private int changeButtonNumberSync(int syncId) {
        return syncId + this.visibleTopRow;
    }

    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        int i = this.enchantNumber - 3;
        if (this.scrollBarClicked && this.canScroll && i > 0) {
            int j = this.topPos + 13;
            int k = j + 56;
            this.scrollPosition = ((float)click.y() - (float)j - 7.5F) / ((float)(k - j) - 15.0F);
            this.scrollPosition = Mth.clamp(this.scrollPosition, 0.0F, 1.0F);
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
                this.scrollPosition = Mth.clamp(this.scrollPosition - f, 0.0F, 1.0F);
                this.visibleTopRow = Math.max((int) (this.scrollPosition * (float) i + 0.5F), 0);
            }
        }
        return true;
    }

    @Inject(method = "renderBg", at = @At("HEAD"), cancellable = true)
    private void newBackground(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        //much copied
        int width = (this.width - this.imageWidth) / 2;
        int height = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ENCHANTING_TABLE_LOCATION, width, height, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        this.renderBook(guiGraphics, width, height);
        EnchantmentNames.getInstance().initSeed((long)((EnchantmentMenu)this.menu).getEnchantmentSeed());
        int lapisCount = this.menu.getGoldCount();

        ItemStack enchantingStack = this.menu.slots.getFirst().getItem();
        int enchantability = enchantingStack.isEmpty() || !enchantingStack.isEnchantable() ? 0 : Objects.requireNonNull(enchantingStack.get(DataComponents.ENCHANTABLE)).value();

        if(enchantability  == 0) {
            this.scrollPosition = 0f;
            this.visibleTopRow = 0;
        }

        int topRow = 0;
        if (this.canScroll) {
            topRow = this.visibleTopRow;
        }

        List<FormattedText> phraseList = new LinkedList<>();
        for (int in = 0; in < enchantability; in++) {
            int phraseWidth = 86 - this.font.width(String.valueOf(in+1)) - 10;
            phraseList.add(EnchantmentNames.getInstance().getRandomName(this.font, phraseWidth));
        }

        int yOffset = 14;

        for(int alt = 0; alt < Math.min(enchantability, 3); alt++) {
            int m = width + 60;
            int n = m + 20;

            int xpLevel = topRow + alt + 1;

            String stringLevel = "" + xpLevel;
            int p = 86 - this.font.width(stringLevel) - 10;

            FormattedText stringVisitable = phraseList.get(xpLevel-1);

            int q = -9937334;
            if ((lapisCount < topRow + alt + 1 || Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).experienceLevel < xpLevel) && !Objects.requireNonNull(Objects.requireNonNull(this.minecraft).player).hasInfiniteMaterials()) {
                //alternative not available
                guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_SPRITE, m, height + yOffset + 19 * alt, 93, 19);
                guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, LEVEL_DISABLED_TEXTURES[Math.min(alt+topRow, 4)], m + 1, height + yOffset + 19 * alt, 16, 16);
                guiGraphics.drawWordWrap(this.font, stringVisitable, n, height + yOffset + 1 + 19 * alt, p, ARGB.opaque((q & 16711422) >> 1), false);
                q = ARGB.opaque(0xFF8C605D);
            } else {
                //alternative available
                int r = this.width - (width + 60);
                int s = this.height - (height + yOffset + 19 * alt);

                if (r >= 0 && s >= 0 && r < 93 && s < 19) {
                    guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE, m, height + yOffset + 19 * alt, 93, 19);
                    q = -128;
                } else {
                    guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_SPRITE, m, height + yOffset + 19 * alt, 93, 19);
                }

                guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, LEVEL_TEXTURES[Math.min(alt+topRow, 4)], m + 1, height + yOffset + 19 * alt, 16, 16);
                guiGraphics.drawWordWrap(this.font, stringVisitable, n, height + 16 + 19 * alt, p, q, false);
                q = -8323296;
            }

            guiGraphics.drawString(this.font, stringLevel, n - 2 - this.font.width(stringLevel), height + yOffset + 2 + 19 * alt + 2, q);
        }
        int k = 0;
        ResourceLocation scrollHandle = this.canScroll ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        guiGraphics.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, scrollHandle, width + 156, height + yOffset + k + (int)(42 * this.scrollPosition), 12, 15);
        ci.cancel();
    }

    @ModifyConstant(method = "mouseClicked", constant = @Constant(doubleValue = 108f))
    private double newBoundsForButton(double constant) {
        return 93.0F;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void replaceRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float f, CallbackInfo ci) {
        if (this.minecraft != null) {
            float g = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
            super.render(guiGraphics, mouseX, mouseY, g);
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }
        for(int j = 0; j < 3; ++j) {
            int windowOffset = (this.height - this.imageHeight) / 2;
            this.toolTipNumber = this.visibleTopRow;
            int relY = mouseY - windowOffset;
            if (relY > 32) {
                this.toolTipNumber++;
            }
            if (relY > 51) {
                this.toolTipNumber++;
            }

            this.checkEnchants();
            if (this.isHovering(61, 15 + 19 * j, 91, 17, mouseX, mouseY)) {
                guiGraphics.setComponentTooltipForNextFrame(this.font, this.alterTooltip(), mouseX, mouseY);
                break;
            }
        }
        ci.cancel();
    }

    @Unique
    private List<Component> alterTooltip() {
        List<Component> text = Lists.newArrayList();
        if (this.minecraft == null || this.minecraft.level == null) return text;
        Optional<Holder.Reference.Reference<Enchantment>> optional = this.minecraft.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(((this.enchants[this.toolTipNumber])));

        if (optional.isPresent()) {
            checkBooks();
            Component enchant = Enchantment.getFullname(optional.get(), 0);
            if (this.toolTipNumber + 1 > this.bookCount) {
                text.addFirst(Component.translatable("container.enchant.clue", "").withStyle(ChatFormatting.WHITE));
            } else if (toolTipNumber == 0){
                text.addFirst(Enchantment.getFullname(optional.get(), 1));
            } else {
                text.addFirst(Component.translatable("container.enchant.clue", enchant).withStyle(ChatFormatting.WHITE));
            }

            if (this.minecraft.player != null && !this.minecraft.player.isCreative()) {
                MutableComponent lapisText;
                MutableComponent levelText;
                int price = this.toolTipNumber + 1;
                if (price == 1) {
                    lapisText = Component.translatable("container.enchant.lapis.one");
                    levelText = Component.translatable("container.enchant.level.one");
                } else {
                    lapisText = Component.translatable("container.enchant.lapis.many", price);
                    levelText = Component.translatable("container.enchant.level.many", price);
                }
                text.add(lapisText.withStyle(this.menu.getGoldCount() >= price ? ChatFormatting.GRAY : ChatFormatting.RED));
                text.add(levelText.withStyle(this.minecraft.player.experienceLevel >= price ? ChatFormatting.GRAY : ChatFormatting.RED));
            }
        }
        return text;
    }
}
