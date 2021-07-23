package com.turtywurty.turtyschemistry.common.blocks.gas_extractor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GasExtractorScreen extends ContainerScreen<GasExtractorContainer> {

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/gas_extractor.png");

	public GasExtractorScreen(final GasExtractorContainer screenContainer, final PlayerInventory inv,
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
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int startX = this.guiLeft;
		int startY = this.guiTop;

		ClientUtils.blit(stack, this, startX, startY, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
		int storedAmount = this.container.tileEntity.getGasStored() / 1000;
		boolean full = storedAmount == 3;

		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);

		ClientUtils.blit(stack, this, 154, 66 - storedAmount * 20, 127, full ? 166 : 229 - storedAmount * 20, 14,
				17 + storedAmount * 20);

		ClientUtils.blit(stack, this, 7, 66 - storedAmount * 20, 127, full ? 166 : 229 - storedAmount * 20, 14,
				17 + storedAmount * 20);

		ClientUtils.blit(stack, this, 24, 71 - storedAmount * 19, 0, 222 - storedAmount * 19, 127,
				19 + storedAmount * 19);
	}

	@Override
	public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(stack, mouseX, mouseY);
		renderGasTooltip(stack, "Helium Gas: " + this.container.tileEntity.getGasStored() + "mb", mouseX, mouseY);
	}

	protected void renderGasTooltip(final MatrixStack stack, final String tooltip, final int mouseX, final int mouseY) {
		if (mouseX > this.guiLeft + 23 && mouseX < this.guiLeft + 151 && mouseY > this.guiTop + 14
				&& mouseY < this.guiTop + 71) {
			renderTooltip(stack, new StringTextComponent(tooltip), mouseX, mouseY);
		}
	}
}
