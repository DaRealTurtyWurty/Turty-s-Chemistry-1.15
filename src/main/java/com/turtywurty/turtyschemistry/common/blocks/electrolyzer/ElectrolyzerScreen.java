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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ElectrolyzerScreen extends ContainerScreen<ElectrolyzerContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
            "textures/gui/electrolyzer.png");

    public ElectrolyzerScreen(final ElectrolyzerContainer screenContainer, final PlayerInventory inv,
            final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
    }

    @SuppressWarnings("deprecation")
    public void drawFluids() {
        final int inputX = 9;
        final int output1X = 154;
        final int output2X = 100;
        final int y = 13;
        final int w = 13;
        final int h = 57;

        final RenderType renderType = ClientUtils.getGui(TEXTURE);

        RenderSystem.pushMatrix();
        final int inputHeight = (int) (57
                * (this.container.inputFluid.getAmount() / (float) this.container.data.get(2)));
        final int output1Height = (int) (57
                * (this.container.outputFluid1.getAmount() / (float) this.container.data.get(3)));
        final int output2Height = (int) (57
                * (this.container.outputFluid2.getAmount() / (float) this.container.data.get(4)));

        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer
                .getImpl(Tessellator.getInstance().getBuffer());
        final MatrixStack transform = new MatrixStack();

        ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, this.container.inputFluid, inputX,
                y + h - inputHeight, w, inputHeight);
        ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, this.container.outputFluid1, output1X,
                y + h - output1Height, w, output1Height);
        ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, this.container.outputFluid2, output2X,
                y + h - output2Height, w, output2Height);

        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, inputX, y, w, h, 256f, 0, 0, 0,
                0);
        ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, output1X, y, w, h, 256f, 0, 0,
                0, 0);
        ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, output2X, y, w, h, 256f, 0, 0,
                0, 0);

        buffer.finish(renderType);
        RenderSystem.popMatrix();
    }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(stack, mouseX, mouseY);

        this.renderTooltip(stack,
                this.container.inputFluid.getDisplayName().getString() + ": "
                        + this.container.inputFluid.getAmount() + "/" + this.container.data.get(2),
                mouseX, mouseY, 8, 22, 12, 70);

        this.renderTooltip(stack,
                this.container.outputFluid1.getDisplayName().getString() + ": "
                        + this.container.outputFluid1.getAmount() + "/" + this.container.data.get(3),
                mouseX, mouseY, 153, 167, 12, 70);

        this.renderTooltip(stack,
                this.container.outputFluid2.getDisplayName().getString() + ": "
                        + this.container.outputFluid2.getAmount() + "/" + this.container.data.get(4),
                mouseX, mouseY, 99, 113, 12, 70);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks,
            final int mouseX, final int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(TEXTURE);
        ClientUtils.blit(stack, this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        ClientUtils.blit(stack, this, this.guiLeft + 49, this.guiTop + 32, 176, 0,
                this.container.getScaledProgress(), 17);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX,
            final int mouseY) {
        this.font.drawString(stack, this.title.getString(), 7.0f, 3.0f, 0x404040);
        this.font.drawString(stack, this.playerInventory.getDisplayName().getString(), 7.0f, 74.0f, 0x404040);

        drawFluids();
    }

    protected void renderTooltip(final MatrixStack stack, final String tooltip, final int mouseX,
            final int mouseY, final int xLeft, final int xRight, final int yTop, final int yBottom) {
        if (mouseX > this.guiLeft + xLeft && mouseX < this.guiLeft + xRight && mouseY > this.guiTop + yTop
                && mouseY < this.guiTop + yBottom) {
            super.renderTooltip(stack, new StringTextComponent(tooltip), mouseX, mouseY);
        }
    }
}
