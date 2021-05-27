package com.turtywurty.turtyschemistry.common.blocks.agitator;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

public class AgitatorScreen extends ContainerScreen<AgitatorContainer> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/agitator.png");

	public AgitatorScreen(AgitatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		getMinecraft().getTextureManager().bindTexture(TEXTURE);
		ClientUtils.blit(this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		// Progress Bar
		ClientUtils.blit(this, this.guiLeft + 79, this.guiTop + 35, 176, 0, this.container.getScaledProgress(), 17);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		this.font.drawString(this.title.getFormattedText(), 7.0f, 5.0f, 0x404040);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 7.0f, 74.0f, 0x404040);

		drawFluids();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		this.renderTooltip(
				new TranslationTextComponent(this.container.getOutputFluid().getTranslationKey()).getFormattedText(),
				mouseX, mouseY, 153, 167, 12, 60);
	}

	public void drawFluids() {
		final FluidStack fluid = this.container.getOutputFluid();
		final int x = 154;
		int y = 13;
		final int w = 13;
		int h = 57;

		RenderSystem.pushMatrix();
		int scaledHeight = (int) (57 * ((float) fluid.getAmount() / 1000));
		RenderType renderType = ClientUtils.getGui(TEXTURE);
		IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		MatrixStack transform = new MatrixStack();
		ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, fluid, x, y + h - scaledHeight, w, scaledHeight);
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, x, y, w, h, 256f, 0, 0, 0, 0);
		buffer.finish(renderType);
		RenderSystem.popMatrix();
	}

	protected void renderTooltip(String tooltip, int mouseX, int mouseY, int xLeft, int xRight, int yTop, int yBottom) {
		if (mouseX > this.guiLeft + xLeft && mouseX < this.guiLeft + xRight && mouseY > this.guiTop + yTop
				&& mouseY < this.guiTop + yBottom) {
			this.renderTooltip(tooltip, mouseX, mouseY);
		}
	}

	@Override
	protected void init() {
		super.init();

		this.addButton(new ChangeModeButton(this, this.guiLeft + 115, this.guiTop + 4, 19, 19));
	}

	public static class ChangeModeButton extends Button {

		private AgitatorScreen screen;

		public ChangeModeButton(AgitatorScreen screen, int x, int y, int width, int height) {
			super(x, y, width, height, "", pressable -> {
				AgitatorTileEntity tile = screen.container.getTile();
				AgitatorType currentType = tile.getAgitatorType();
				AgitatorType[] types = AgitatorType.values();
				int newType = currentType.ordinal() + 1 >= types.length ? 0 : currentType.ordinal() + 1;
				tile.setAgitatorType(AgitatorType.values()[newType]);
			});

			this.screen = screen;
		}

		@Override
		public void renderButton(int mouseY, int mouseX, float partialTicks) {
			int offsetY = 17;
			if (this.isHovered())
				offsetY += this.height;
			ClientUtils.renderButton(TEXTURE, this, partialTicks, this.alpha,
					199 + (this.screen.container.getTile().getAgitatorType().ordinal() * 19), offsetY, this.width,
					this.height, 256, 256);
		}
	}
}
