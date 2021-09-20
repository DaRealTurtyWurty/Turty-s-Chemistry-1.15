package com.turtywurty.turtyschemistry.common.blocks.fractional_distiller;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FractionalDistillerScreen extends ContainerScreen<FractionalDistillerContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
            "textures/gui/fractional_distiller.png");

    public FractionalDistillerScreen(final FractionalDistillerContainer screenContainer,
            final PlayerInventory inv, final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 255;
        this.ySize = 255;
    }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks,
            final int mouseX, final int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        final int startX = this.guiLeft;
        final int startY = this.guiTop;

        ClientUtils.blit(stack, this, startX, startY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX,
            final int mouseY) {
        super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);

        final FractionalDistillerTileEntity tileEntity = this.container.tileEntity;
        if (tileEntity.processTimeLeft > 0) {
            this.font.drawString(stack, tileEntity.processTimeLeft + " / " + tileEntity.maxProcessingTime,
                    8.0f, 1.0f, 0x404040);
        }
    }
}
