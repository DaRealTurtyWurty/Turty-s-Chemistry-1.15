package com.turtywurty.turtyschemistry.common.blocks.researcher;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.gui.ScrollPanel;

public class ResearcherScreen extends ContainerScreen<ResearcherContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
            "textures/gui/researcher.png");

    private BlueprintScrollPanel scrollPanel;

    public ResearcherScreen(final ResearcherContainer screenContainer, final PlayerInventory inv,
            final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void init() {
        super.init();
        final int xStart = (this.width - this.xSize) / 2;
        final int yStart = (this.height - this.ySize) / 2;
        final ClientWorld world = this.minecraft.world;
        final List<IRecipe<CraftingInventory>> recipes = world != null ? null // ResearcherRecipe.getBlueprints(world.getRecipeManager())
                : ImmutableList.of();
        this.scrollPanel = new BlueprintScrollPanel(this.minecraft, this, recipes, xStart + 4, yStart + 17,
                16, 16);
        this.children.add(this.scrollPanel);
    }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        if (this.scrollPanel != null) {
            this.scrollPanel.render(stack, mouseX, mouseY, 0);
        }
        renderHoveredTooltip(stack, mouseX, mouseY);
    }

    public void renderItemStack(final ItemStack stack, final int x, final int y) {
        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRenderer.renderItemOverlays(this.font, stack, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks,
            final int mouseX, final int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(TEXTURE);
        ClientUtils.blit(stack, this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX,
            final int mouseY) {
        super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
        this.font.drawString(stack, this.title.getString(), 8f, 6f, 0x404040);
        this.font.drawString(stack, this.playerInventory.getDisplayName().getString(), 8f, 74f, 0x404040);
    }

    @Override
    protected void renderHoveredTooltip(final MatrixStack stack, final int mouseX, final int mouseY) {
        if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null
                && this.hoveredSlot.getHasStack()) {
            renderTooltip(stack, this.hoveredSlot.getStack(), mouseX, mouseY);
        } else if (this.scrollPanel != null && !this.scrollPanel.tooltipItem.isEmpty()) {
            renderTooltip(stack, this.scrollPanel.tooltipItem, mouseX, mouseY);
        }

    }

    public static class BlueprintScrollPanel extends ScrollPanel {

        private final List<RecipeButton> buttons = new ArrayList<>();
        public ItemStack tooltipItem = ItemStack.EMPTY;
        public final int totalButtonHeight;

        public BlueprintScrollPanel(final Minecraft client, final ResearcherScreen screen,
                final List<IRecipe<CraftingInventory>> recipes, final int width, final int height,
                final int top, final int left) {
            super(client, width, height, top, left);

            final int buttonWidth = 16;
            int buttonHeight = 0;
            final ClientWorld world = client.world;
            if (world != null) {
                for (final IRecipe<CraftingInventory> recipe : recipes /*
                                                                        * ResearcherRecipe.getAllBlueprints(
                                                                        * world. getRecipeManager())
                                                                        */) {
                    final RecipeButton recipeButton = new RecipeButton(screen, recipe, left,
                            top + buttonHeight, buttonWidth);
                    this.buttons.add(recipeButton);
                    buttonHeight += recipeButton.getHeight();
                }
            }
            this.totalButtonHeight = buttonHeight;
        }

        @Override
        public List<? extends IGuiEventListener> getEventListeners() {
            return this.buttons;
        }

        @Override
        public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected void drawPanel(final MatrixStack stack, final int entryRight, final int relativeY,
                final Tessellator tess, final int mouseX, final int mouseY) {
            this.tooltipItem = ItemStack.EMPTY;
            for (final RecipeButton button : this.buttons) {
                button.scrollButton((int) this.scrollDistance);
                button.render(stack, mouseX, mouseY, 0);
                if (!button.tooltipItem.isEmpty()) {
                    this.tooltipItem = button.tooltipItem;
                }
            }
        }

        @Override
        protected int getContentHeight() {
            return this.totalButtonHeight;
        }
    }
}
