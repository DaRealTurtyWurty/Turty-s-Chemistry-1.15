package com.turtywurty.turtyschemistry.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.AutoclaveContainer;
import com.turtywurty.turtyschemistry.common.tileentity.AutoclaveTileEntity;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AutoclaveScreen extends ContainerScreen<AutoclaveContainer> {

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/autoclave.png");

	public AutoclaveScreen(AutoclaveContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 175;
		this.ySize = 165;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final AutoclaveTileEntity tileEntity = this.container.tileEntity;
		if (tileEntity.claveTime > 0) {
			this.font.drawString(tileEntity.claveTime + " / " + tileEntity.claveTimeTotal, 8.0F, this.ySize, 0x404040);
		}
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
}
