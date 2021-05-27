package com.turtywurty.turtyschemistry.common.blocks.researcher;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
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

	public ResearcherScreen(ResearcherContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		getMinecraft().getTextureManager().bindTexture(TEXTURE);
		ClientUtils.blit(this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.font.drawString(this.title.getFormattedText(), 8f, 6f, 0x404040);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8f, 74f, 0x404040);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		if (this.scrollPanel != null) {
			this.scrollPanel.render(mouseX, mouseY, 0);
		}
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void init() {
		super.init();
		int xStart = (this.width - this.xSize) / 2;
		int yStart = (this.height - this.ySize) / 2;
		ClientWorld world = this.minecraft.world;
		List<IRecipe<CraftingInventory>> recipes = world != null
				? null // ResearcherRecipe.getBlueprints(world.getRecipeManager())
				: ImmutableList.of();
		this.scrollPanel = new BlueprintScrollPanel(this.minecraft, this, recipes, xStart + 4, yStart + 17, 16, 16);
		this.children.add(this.scrollPanel);
	}

	public void renderItemStack(ItemStack stack, int x, int y) {
		this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRenderer.renderItemOverlays(this.font, stack, x, y);
	}

	@Override
	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null
				&& this.hoveredSlot.getHasStack()) {
			this.renderTooltip(this.hoveredSlot.getStack(), mouseX, mouseY);
		} else if (this.scrollPanel != null && !this.scrollPanel.tooltipItem.isEmpty()) {
			this.renderTooltip(this.scrollPanel.tooltipItem, mouseX, mouseY);
		}

	}

	public static class BlueprintScrollPanel extends ScrollPanel {

		private List<RecipeButton> buttons = new ArrayList<>();
		public ItemStack tooltipItem = ItemStack.EMPTY;
		public final int totalButtonHeight;

		public BlueprintScrollPanel(Minecraft client, ResearcherScreen screen, List<IRecipe<CraftingInventory>> recipes,
				int width, int height, int top, int left) {
			super(client, width, height, top, left);

			int buttonWidth = 16;
			int buttonHeight = 0;
			ClientWorld world = client.world;
			if (world != null) {
				for (IRecipe<CraftingInventory> recipe : recipes /*ResearcherRecipe.getAllBlueprints(world.getRecipeManager()) */) {
					RecipeButton recipeButton = new RecipeButton(screen, recipe, left, top + buttonHeight, buttonWidth);
					this.buttons.add(recipeButton);
					buttonHeight += recipeButton.getHeight();
				}
			}
			this.totalButtonHeight = buttonHeight;
		}

		@Override
		public List<? extends IGuiEventListener> children() {
			return this.buttons;
		}

		@Override
		protected int getContentHeight() {
			return this.totalButtonHeight;
		}

		@Override
		protected void drawPanel(int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
			this.tooltipItem = ItemStack.EMPTY;
			for (RecipeButton button : this.buttons) {
				button.scrollButton((int) this.scrollDistance);
				button.render(mouseX, mouseY, 0);
				if (!button.tooltipItem.isEmpty()) {
					this.tooltipItem = button.tooltipItem;
				}
			}
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			return super.mouseClicked(mouseX, mouseY, button);
		}
	}
}
