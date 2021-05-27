package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

public class SolarPanelScreen<T extends SolarPanelContainer> extends ContainerScreen<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/solar_panel.png");
	private final AbstractSolarPanelTileEntity tileEntity;

	public SolarPanelScreen(T container, PlayerInventory playerInv, AbstractSolarPanelTileEntity tileIn) {
		super(container, playerInv, tileIn.getBlock().getPanelInfo().get().getDisplayName());
		this.tileEntity = tileIn;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.guiLeft = (ClientUtils.MC.currentScreen.width / 2) - 88;
		this.guiTop = (ClientUtils.MC.currentScreen.height / 2) - 83;
	}

	public AbstractSolarPanelTileEntity getTileEntity() {
		return this.tileEntity;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		ClientUtils.MC.getTextureManager().bindTexture(TEXTURE);
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		ClientUtils.blit(this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		RenderSystem.pushMatrix();
		RenderType renderType = ClientUtils.getGui(TEXTURE);
		IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		MatrixStack transform = new MatrixStack();
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, this.guiLeft + 13, this.guiTop + 9, 24, 69,
				256, 176, 199, 0, 69);
		buffer.finish(renderType);
		RenderSystem.popMatrix();
	}
}
