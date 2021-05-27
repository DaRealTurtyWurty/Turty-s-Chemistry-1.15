package com.turtywurty.turtyschemistry.common.blocks.silo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class SiloScreen extends ContainerScreen<SiloContainer> {

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/silo.png");

	public SiloScreen(SiloContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	protected void init() {
		super.init();
		this.addButton(new NextPageButton(this.guiLeft + 229, this.guiTop + 164, 16, 11,
				button -> TurtyChemistry.packetHandler.send(PacketDistributor.SERVER.noArg(), new SiloButtonPacket(1)),
				this.container));

		this.addButton(new BackPageButton(this.guiLeft + 13, this.guiTop + 164, 16, 11,
				button -> TurtyChemistry.packetHandler.send(PacketDistributor.SERVER.noArg(), new SiloButtonPacket(0)),
				this.container));
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
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
		this.font.drawString(this.title.getFormattedText(), 12f, 5f, 0x404040);
		this.font.drawString("Page " + this.container.currentPage.get(0) + "/5", 100f, 5f, 0x404040);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 48f, 163f, 0x404040);
	}

	public static class NextPageButton extends Button {

		private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(TurtyChemistry.MOD_ID,
				"textures/gui/buttons/silo.png");

		private SiloContainer container;

		public NextPageButton(int xPos, int yPos, int width, int height, IPressable onPress, SiloContainer containerIn) {
			super(xPos, yPos, width, height, "", onPress);
			this.container = containerIn;
		}

		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks) {
			if (this.container.currentPage.get(0) == 5) {
				this.active = false;
			} else {
				this.active = true;
			}

			if (!this.isMouseOver(mouseX, mouseY)) {
				ClientUtils.renderButton(BUTTON_TEXTURES, this, partialTicks, this.alpha, 16, 0, this.width, this.height, 64,
						64);
			} else {
				ClientUtils.renderButton(BUTTON_TEXTURES, this, partialTicks, this.alpha, 16, 11, this.width, this.height,
						64, 64);
			}
		}
	}

	public static class BackPageButton extends Button {

		private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(TurtyChemistry.MOD_ID,
				"textures/gui/buttons/silo.png");
		private SiloContainer container;

		public BackPageButton(int xPos, int yPos, int width, int height, IPressable onPress, SiloContainer containerIn) {
			super(xPos, yPos, width, height, "", onPress);
			this.container = containerIn;
		}

		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks) {
			if (this.container.currentPage.get(0) == 0) {
				this.active = false;
			} else {
				this.active = true;
			}

			if (!this.isMouseOver(mouseX, mouseY)) {
				ClientUtils.renderButton(BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 0, this.width, this.height, 64,
						64);
			} else {
				ClientUtils.renderButton(BUTTON_TEXTURES, this, partialTicks, this.alpha, 0, 11, this.width, this.height, 64,
						64);
			}
		}
	}
}
