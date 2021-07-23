package com.turtywurty.turtyschemistry.common.blocks.boiler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.blocks.GasBlock;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BoilerScreen extends ContainerScreen<BoilerContainer> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/boiler.png");

	public BoilerScreen(final BoilerContainer screenContainer, final PlayerInventory inv,
			final ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks, final int mouseX,
			final int mouseY) {
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		getMinecraft().getTextureManager().bindTexture(TEXTURE);

		ClientUtils.blit(stack, this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);

		this.font.drawString(stack, getTitle().getString(), 8.0f, 4.0f, 0x404040);
		this.font.drawString(stack, this.playerInventory.getDisplayName().getString(), 8.0f, 74.0f, 0x404040);

		ClientUtils.drawFluid(TEXTURE, this.container.getInputFluid(), 27, 14, 13, 57);
		ClientUtils.drawFluid(TEXTURE, this.container.getOutputFluid(), 117, 14, 13, 57);
		if (this.container.getOutputGas() instanceof GasBlock) {
			ClientUtils.drawTexture(((GasBlock) this.container.getOutputGas()).getGuiTexture(), 153, 14, 13, 57, 1000,
					1000);
		}
	}

	@Override
	public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(stack, mouseX, mouseY);

		this.renderTooltip(stack, this.container.getInputFluid().getDisplayName().getString(), mouseX, mouseY, 26, 13,
				40, 71);
		this.renderTooltip(stack, this.container.getOutputFluid().getDisplayName().getString(), mouseX, mouseY, 116, 13,
				130, 71);
		this.renderTooltip(stack,
				new TranslationTextComponent(this.container.getOutputGas().getTranslationKey()).getString(), mouseX,
				mouseY, 152, 13, 166, 71);
	}

	protected void renderTooltip(final MatrixStack stack, final String tooltip, final int mouseX, final int mouseY,
			final int xLeft, final int yTop, final int xRight, final int yBottom) {
		if (mouseX > this.guiLeft + xLeft && mouseX < this.guiLeft + xRight && mouseY > this.guiTop + yTop
				&& mouseY < this.guiTop + yBottom) {
			super.renderHoveredTooltip(stack, mouseX, mouseY);
		}
	}
}
