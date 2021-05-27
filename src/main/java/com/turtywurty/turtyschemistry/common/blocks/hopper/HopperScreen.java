package com.turtywurty.turtyschemistry.common.blocks.hopper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class HopperScreen extends ContainerScreen<HopperContainer> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation(
			"minecraft:textures/gui/container/generic_54.png");

	public HopperScreen(HopperContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND);

		ClientUtils.blit(this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		this.font.drawString(this.title.getFormattedText(), 7.0f, 5.0f, 0x404040);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 7, 127, 0x404040);
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
