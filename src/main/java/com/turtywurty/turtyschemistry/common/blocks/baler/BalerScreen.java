package com.turtywurty.turtyschemistry.common.blocks.baler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BalerScreen extends ContainerScreen<BalerContainer> {

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/baler.png");

	public BalerScreen(BalerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);

		this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final BalerTileEntity tileEntity = this.container.tileEntity;
		if (tileEntity.compressingTime > 0) {
			this.font.drawString(tileEntity.compressingTime + " / " + tileEntity.maxCompressingTime, 6.0f, 6.0f,
					0x404040);
		}
	}
}
