package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

public class ElectrolyzerScreen extends ContainerScreen<ElectrolyzerContainer> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/electrolyzer.png");

	public ElectrolyzerScreen(ElectrolyzerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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

		ClientUtils.blit(this, this.guiLeft + 49, this.guiTop + 32, 176, 0, this.container.getScaledProgress(), 17);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.font.drawString(this.title.getFormattedText(), 7.0f, 3.0f, 0x404040);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 7.0f, 74.0f, 0x404040);

		this.drawFluids();
	}

	public void drawFluids() {
		int waterX = 9;
		int oxygenX = 154;
		int hydrogenX = 100;
		int y = 13;
		int w = 13;
		int h = 57;

		RenderType renderType = ClientUtils.getGui(TEXTURE);

		RenderSystem.pushMatrix();
		int waterHeight = (int) (57 * (this.container.data.get(2) / (float) this.container.data.get(3)));
		int oxygenHeight = (int) (57 * (this.container.data.get(4) / (float) this.container.data.get(5)));
		int hydrogenHeight = (int) (57 * (this.container.data.get(6) / (float) this.container.data.get(7)));

		IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		MatrixStack transform = new MatrixStack();

		ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, new FluidStack(Fluids.WATER, 1000), waterX,
				y + h - waterHeight, w, waterHeight);
		ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform,
				new ResourceLocation(TurtyChemistry.MOD_ID, "textures/gui/oxygen.png"), 0xE1EDF8, oxygenX,
				y + h - oxygenHeight, w, oxygenHeight);
		ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform,
				new ResourceLocation("turtychemistry:textures/gui/hydrogen.png"), 0xEFEFEF, hydrogenX,
				y + h - hydrogenHeight, w, hydrogenHeight);

		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, waterX, y, w, h, 256f, 0, 0, 0, 0);
		ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, oxygenX, y, w, h, 256f, 0, 0, 0, 0);
		ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, hydrogenX, y, w, h, 256f, 0, 0, 0, 0);

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
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		this.renderTooltip("Water: " + this.container.data.get(2) + "/" + this.container.data.get(3), mouseX, mouseY, 8, 22,
				12, 70);

		this.renderTooltip("Oxygen: " + this.container.data.get(4) + "/" + this.container.data.get(5), mouseX, mouseY, 153,
				167, 12, 70);

		this.renderTooltip("Hydrogen: " + this.container.data.get(6) + "/" + this.container.data.get(7), mouseX, mouseY, 99,
				113, 12, 70);
	}
}
