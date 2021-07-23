package com.turtywurty.turtyschemistry.common.blocks.briquetting_press;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class BriquettingPressScreen extends ContainerScreen<BriquettingPressContainer> {

	public static class RunButton extends Button {

		private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(TurtyChemistry.MOD_ID,
				"textures/gui/buttons/briquetting_press.png");

		public RunButton(final int xPos, final int yPos, final int width, final int height, final IPressable onPress) {
			super(xPos, yPos, width, height, new StringTextComponent(""), onPress);
		}

		@Override
		public void renderWidget(final MatrixStack stack, final int mouseX, final int mouseY,
				final float partialTicks) {
			if (!isMouseOver(mouseX, mouseY)) {
				ClientUtils.renderButton(stack, BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 0, this.width,
						this.height, 64, 64);
			} else {
				ClientUtils.renderButton(stack, BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 17, this.width,
						this.height, 64, 64);
			}
		}
	}

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/briquetting_press.png");

	public BriquettingPressScreen(final BriquettingPressContainer screenContainer, final PlayerInventory inv,
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

		ClientUtils.blit(stack, this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);

		final BriquettingPressTileEntity tileEntity = this.container.tileEntity;
		if (tileEntity.buttonPressed) {
			StringBuilder eta = new StringBuilder("Completed in: ");
			if (Math.floor((double) tileEntity.getMaxRunningTime() / 1200) != 0) {
				eta.append(
						Double.toString(Math.floor((double) tileEntity.getMaxRunningTime() / 1200)).replace(".0", ""))
						.append(" mins and ")
						.append(Double.toString((double) tileEntity.getMaxRunningTime() % 1200 / 20).replace(".0", ""))
						.append(" secs!");
			} else {
				eta.append(Double.toString((double) tileEntity.getMaxRunningTime() % 1200 / 20).replace(".0", ""))
						.append(" secs!");
			}

			if (tileEntity.getMaxRunningTime() != 0) {
				this.font.drawString(stack, eta.toString(), 4.0f, 6.0f, 0x404040);
			}
		}
	}

	@Override
	protected void init() {
		super.init();
		this.addButton(new RunButton(this.guiLeft + 132, this.guiTop + 35, 21, 18, button -> {
			BriquettingPressTileEntity tile = getContainer().tileEntity;
			TurtyChemistry.PACKET_HANDLER.send(PacketDistributor.SERVER.noArg(),
					new BriquettingPressButtonPacket(!tile.buttonPressed));
		}));
	}

	@Override
	public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderItems();
		renderSlotOverlay(stack, mouseX, mouseY);

		renderHoveredTooltip(stack, mouseX, mouseY);
		renderItemTooltips(stack, mouseX, mouseY);
	}

	private void renderItems() {
		this.itemRenderer.renderItemIntoGUI(getContainer().tileEntity.getStackInSlot(1), this.guiLeft + 45,
				this.guiTop + 36);
		this.itemRenderer.renderItemOverlayIntoGUI(this.font, getContainer().tileEntity.getStackInSlot(1),
				this.guiLeft + 45, this.guiTop + 36,
				Integer.toString(getContainer().tileEntity.getStackInSlot(1).getCount()));
		this.itemRenderer.renderItemIntoGUI(getContainer().tileEntity.getStackInSlot(2), this.guiLeft + 63,
				this.guiTop + 36);
		this.itemRenderer.renderItemOverlayIntoGUI(this.font, getContainer().tileEntity.getStackInSlot(2),
				this.guiLeft + 63, this.guiTop + 36,
				Integer.toString(getContainer().tileEntity.getStackInSlot(2).getCount()));
		this.itemRenderer.renderItemIntoGUI(getContainer().tileEntity.getStackInSlot(3), this.guiLeft + 81,
				this.guiTop + 36);
		this.itemRenderer.renderItemOverlayIntoGUI(this.font, getContainer().tileEntity.getStackInSlot(3),
				this.guiLeft + 81, this.guiTop + 36,
				Integer.toString(getContainer().tileEntity.getStackInSlot(3).getCount()));
	}

	private void renderItemTooltips(final MatrixStack stack, final int mouseX, final int mouseY) {
		if (InputMappings.isKeyDown(ClientUtils.MC.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			if (ClientUtils.isMouseInSlot(mouseX, mouseY, this.guiLeft + 45, this.guiTop + 36)
					&& !getContainer().tileEntity.getStackInSlot(1).isEmpty()) {
				this.renderTooltip(stack, getContainer().tileEntity.getStackInSlot(1), mouseX, mouseY);
			} else if (ClientUtils.isMouseInSlot(mouseX, mouseY, this.guiLeft + 63, this.guiTop + 36)
					&& !getContainer().tileEntity.getStackInSlot(2).isEmpty()) {
				this.renderTooltip(stack, getContainer().tileEntity.getStackInSlot(2), mouseX, mouseY);
			} else if (ClientUtils.isMouseInSlot(mouseX, mouseY, this.guiLeft + 81, this.guiTop + 36)
					&& !getContainer().tileEntity.getStackInSlot(3).isEmpty()) {
				this.renderTooltip(stack, getContainer().tileEntity.getStackInSlot(3), mouseX, mouseY);
			}
		}
	}

	private void renderSlotOverlay(final MatrixStack stack, final int mouseX, final int mouseY) {
		RenderSystem.enableBlend();
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 0.5f);
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		ClientUtils.blit(stack, this, this.guiLeft + 44, this.guiTop + 35, 0, 166, 54, 18);
		RenderSystem.disableBlend();
	}
}
