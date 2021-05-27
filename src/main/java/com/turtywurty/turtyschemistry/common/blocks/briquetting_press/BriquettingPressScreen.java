package com.turtywurty.turtyschemistry.common.blocks.briquetting_press;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class BriquettingPressScreen extends ContainerScreen<BriquettingPressContainer> {

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/briquetting_press.png");

	public BriquettingPressScreen(BriquettingPressContainer screenContainer, PlayerInventory inv,
			ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void init() {
		super.init();
		this.addButton(new RunButton(this.guiLeft + 132, this.guiTop + 35, 21, 18, button -> {
			BriquettingPressTileEntity tile = this.getContainer().tileEntity;
			TurtyChemistry.packetHandler.send(PacketDistributor.SERVER.noArg(),
					new BriquettingPressButtonPacket(!tile.buttonPressed));
		}));
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderItems();
		this.renderSlotOverlay(mouseX, mouseY);

		this.renderHoveredToolTip(mouseX, mouseY);
		this.renderItemTooltips(mouseX, mouseY);
	}

	private void renderItems() {
		this.itemRenderer.renderItemIntoGUI(this.getContainer().tileEntity.getStackInSlot(1), this.guiLeft + 45,
				this.guiTop + 36);
		this.itemRenderer.renderItemOverlayIntoGUI(this.font, this.getContainer().tileEntity.getStackInSlot(1),
				this.guiLeft + 45, this.guiTop + 36,
				Integer.toString(this.getContainer().tileEntity.getStackInSlot(1).getCount()));
		this.itemRenderer.renderItemIntoGUI(this.getContainer().tileEntity.getStackInSlot(2), this.guiLeft + 63,
				this.guiTop + 36);
		this.itemRenderer.renderItemOverlayIntoGUI(this.font, this.getContainer().tileEntity.getStackInSlot(2),
				this.guiLeft + 63, this.guiTop + 36,
				Integer.toString(this.getContainer().tileEntity.getStackInSlot(2).getCount()));
		this.itemRenderer.renderItemIntoGUI(this.getContainer().tileEntity.getStackInSlot(3), this.guiLeft + 81,
				this.guiTop + 36);
		this.itemRenderer.renderItemOverlayIntoGUI(this.font, this.getContainer().tileEntity.getStackInSlot(3),
				this.guiLeft + 81, this.guiTop + 36,
				Integer.toString(this.getContainer().tileEntity.getStackInSlot(3).getCount()));
	}

	private void renderItemTooltips(int mouseX, int mouseY) {
		if (InputMappings.isKeyDown(ClientUtils.MC.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			if (ClientUtils.isMouseInSlot(mouseX, mouseY, this.guiLeft + 45, this.guiTop + 36)
					&& !this.getContainer().tileEntity.getStackInSlot(1).isEmpty()) {
				this.renderTooltip(this.getContainer().tileEntity.getStackInSlot(1), mouseX, mouseY);
			} else if (ClientUtils.isMouseInSlot(mouseX, mouseY, this.guiLeft + 63, this.guiTop + 36)
					&& !this.getContainer().tileEntity.getStackInSlot(2).isEmpty()) {
				this.renderTooltip(this.getContainer().tileEntity.getStackInSlot(2), mouseX, mouseY);
			} else if (ClientUtils.isMouseInSlot(mouseX, mouseY, this.guiLeft + 81, this.guiTop + 36)
					&& !this.getContainer().tileEntity.getStackInSlot(3).isEmpty()) {
				this.renderTooltip(this.getContainer().tileEntity.getStackInSlot(3), mouseX, mouseY);
			}
		}
	}

	private void renderSlotOverlay(int mouseX, int mouseY) {
		RenderSystem.enableBlend();
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 0.5f);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		ClientUtils.blit(this, this.guiLeft + 44, this.guiTop + 35, 0, 166, 54, 18);
		RenderSystem.disableBlend();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);

		ClientUtils.blit(this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		final BriquettingPressTileEntity tileEntity = this.container.tileEntity;
		if (tileEntity.buttonPressed) {
			String eta = "Completed in: ";
			if (Math.floor((double) tileEntity.getMaxRunningTime() / 1200) != 0) {
				eta += Double.toString(Math.floor((double) tileEntity.getMaxRunningTime() / 1200)).replace(".0", "")
						+ " mins and "
						+ Double.toString(((double) tileEntity.getMaxRunningTime() % 1200) / 20).replace(".0", "")
						+ " secs!";
			} else {
				eta += Double.toString(((double) tileEntity.getMaxRunningTime() % 1200) / 20).replace(".0", "")
						+ " secs!";
			}

			if (tileEntity.getMaxRunningTime() != 0) {
				this.font.drawString(eta, 4.0f, 6.0f, 0x404040);
			}
		}
	}

	public static class RunButton extends Button {

		private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(TurtyChemistry.MOD_ID,
				"textures/gui/buttons/briquetting_press.png");

		public RunButton(int xPos, int yPos, int width, int height, IPressable onPress) {
			super(xPos, yPos, width, height, "", onPress);
		}

		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks) {
			if (!this.isMouseOver(mouseX, mouseY)) {
				ClientUtils.renderButton(BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 0, this.width, this.height,
						64, 64);
			} else {
				ClientUtils.renderButton(BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 17, this.width,
						this.height, 64, 64);
			}
		}
	}
}
