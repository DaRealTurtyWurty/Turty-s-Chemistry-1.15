package com.turtywurty.turtyschemistry.common.blocks.silo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class SiloScreen extends ContainerScreen<SiloContainer> {

	public static class BackPageButton extends Button {

		private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(TurtyChemistry.MOD_ID,
				"textures/gui/buttons/silo.png");
		private final SiloContainer container;

		public BackPageButton(final int xPos, final int yPos, final int width, final int height,
				final IPressable onPress, final SiloContainer containerIn) {
			super(xPos, yPos, width, height, new StringTextComponent(""), onPress);
			this.container = containerIn;
		}

		@Override
		public void renderWidget(final MatrixStack stack, final int mouseX, final int mouseY,
				final float partialTicks) {
			if (this.container.currentPage.get(0) == 0) {
				this.active = false;
			} else {
				this.active = true;
			}

			if (!isMouseOver(mouseX, mouseY)) {
				ClientUtils.renderButton(stack, BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 0, this.width,
						this.height, 64, 64);
			} else {
				ClientUtils.renderButton(stack, BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 11, this.width,
						this.height, 64, 64);
			}
		}
	}

	public static class NextPageButton extends Button {

		private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(TurtyChemistry.MOD_ID,
				"textures/gui/buttons/silo.png");

		private final SiloContainer container;

		public NextPageButton(final int xPos, final int yPos, final int width, final int height,
				final IPressable onPress, final SiloContainer containerIn) {
			super(xPos, yPos, width, height, new StringTextComponent(""), onPress);
			this.container = containerIn;
		}

		@Override
		public void renderWidget(final MatrixStack stack, final int mouseX, final int mouseY,
				final float partialTicks) {
			if (this.container.currentPage.get(0) == 5) {
				this.active = false;
			} else {
				this.active = true;
			}

			if (!isMouseOver(mouseX, mouseY)) {
				ClientUtils.renderButton(stack, BUTTON_TEXTURES, this, partialTicks, this.alpha, 16, 0, this.width,
						this.height, 64, 64);
			} else {
				ClientUtils.renderButton(stack, BUTTON_TEXTURES, this, partialTicks, this.alpha, 16, 11, this.width,
						this.height, 64, 64);
			}
		}
	}

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/silo.png");

	public SiloScreen(final SiloContainer screenContainer, final PlayerInventory inv, final ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 256;
		this.ySize = 256;
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
		this.font.drawString(stack, this.title.getString(), 12f, 5f, 0x404040);
		this.font.drawString(stack, "Page " + this.container.currentPage.get(0) + "/5", 100f, 5f, 0x404040);
		this.font.drawString(stack, this.playerInventory.getDisplayName().getString(), 48f, 163f, 0x404040);
	}

	@Override
	protected void init() {
		super.init();
		this.addButton(new NextPageButton(this.guiLeft + 229, this.guiTop + 164, 16, 11,
				button -> TurtyChemistry.PACKET_HANDLER.send(PacketDistributor.SERVER.noArg(), new SiloButtonPacket(1)),
				this.container));

		this.addButton(new BackPageButton(this.guiLeft + 13, this.guiTop + 164, 16, 11,
				button -> TurtyChemistry.PACKET_HANDLER.send(PacketDistributor.SERVER.noArg(), new SiloButtonPacket(0)),
				this.container));
	}

	@Override
	public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		renderHoveredTooltip(stack, mouseX, mouseY);
	}
}
