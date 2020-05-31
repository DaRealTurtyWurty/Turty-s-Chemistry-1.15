package com.turtywurty.turtyschemistry.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.GasExtractorContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GasExtractorScreen extends ContainerScreen<GasExtractorContainer> {

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/gas_extractor.png");

	public GasExtractorScreen(GasExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		this.renderGasTooltip(
				"Helium Gas: " + ((GasExtractorContainer) this.container).tileEntity.getGasStored() + "mb", mouseX,
				mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int startX = this.guiLeft;
		int startY = this.guiTop;

		// Screen#blit draws a part of the current texture (assumed to be 256x256) to
		// the screen (The parameters are (x, y, u, v, width, height))
		this.blit(startX, startY, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int storedAmount = ((GasExtractorContainer) this.container).tileEntity.getGasStored() / 1000;
		boolean full = storedAmount == 3;

		// public void blit(int x0, int y0, int u0, int v0, int width, int height)
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);

		this.blit(154, 66 - (storedAmount * 20), 127, full ? 166 : 229 - (storedAmount * 20), 14,
				17 + (storedAmount * 20));

		this.blit(7, 66 - (storedAmount * 20), 127, full ? 166 : 229 - (storedAmount * 20), 14,
				17 + (storedAmount * 20));

		this.blit(24, 71 - (storedAmount * 19), 0, 222 - (storedAmount * 19), 127, 19 + (storedAmount * 19));
	}

	protected void renderGasTooltip(String tooltip, int mouseX, int mouseY) {
		if (mouseX > this.guiLeft + 23 && mouseX < this.guiLeft + 151 && mouseY > this.guiTop + 14
				&& mouseY < this.guiTop + 71) {
			this.renderTooltip(tooltip, mouseX, mouseY);
		}
	}
}
