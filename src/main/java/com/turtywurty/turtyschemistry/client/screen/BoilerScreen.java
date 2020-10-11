package com.turtywurty.turtyschemistry.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.blocks.GasBlock;
import com.turtywurty.turtyschemistry.common.container.BoilerContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BoilerScreen extends ContainerScreen<BoilerContainer> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/boiler.png");

	public BoilerScreen(BoilerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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

		this.font.drawString(this.getTitle().getFormattedText(), 8.0f, 4.0f, 0x404040);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0f, 74.0f, 0x404040);

		ClientUtils.drawFluid(TEXTURE, this.container.getInputFluid(), 27, 14, 13, 57);
		ClientUtils.drawFluid(TEXTURE, this.container.getOutputFluid(), 117, 14, 13, 57);
		if (this.container.getOutputGas() instanceof GasBlock) {
			ClientUtils.drawTexture(((GasBlock) this.container.getOutputGas()).getGuiTexture(), 153, 14, 13, 57, 1000,
					1000);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		this.renderTooltip(this.container.getInputFluid().getDisplayName().getFormattedText(), mouseX, mouseY, 26, 13,
				40, 71);
		this.renderTooltip(this.container.getOutputFluid().getDisplayName().getFormattedText(), mouseX, mouseY, 116, 13,
				130, 71);
		this.renderTooltip(
				new TranslationTextComponent(this.container.getOutputGas().getTranslationKey()).getFormattedText(),
				mouseX, mouseY, 152, 13, 166, 71);
	}

	protected void renderTooltip(String tooltip, int mouseX, int mouseY, int xLeft, int yTop, int xRight, int yBottom) {
		if (mouseX > this.guiLeft + xLeft && mouseX < this.guiLeft + xRight && mouseY > this.guiTop + yTop
				&& mouseY < this.guiTop + yBottom) {
			this.renderTooltip(tooltip, mouseX, mouseY);
		}
	}
}
