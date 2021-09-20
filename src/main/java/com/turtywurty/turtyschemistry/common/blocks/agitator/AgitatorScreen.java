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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

public class AgitatorScreen extends ContainerScreen<AgitatorContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
            "textures/gui/agitator.png");

    public AgitatorScreen(final AgitatorContainer screenContainer, final PlayerInventory inv,
            final ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
    }

    public void drawFluids() {
        final FluidStack fluid = this.container.getOutputFluid();
        final int x = 154;
        final int y = 13;
        final int w = 13;
        final int h = 57;

        RenderSystem.pushMatrix();
        final int scaledHeight = (int) (57 * ((float) fluid.getAmount() / 1000));
        final RenderType renderType = ClientUtils.getGui(TEXTURE);
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer
                .getImpl(Tessellator.getInstance().getBuffer());
        final MatrixStack transform = new MatrixStack();
        ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, fluid, x, y + h - scaledHeight, w,
                scaledHeight);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, x, y, w, h, 256f, 0, 0, 0, 0);
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
                new TranslationTextComponent(this.container.getOutputFluid().getTranslationKey()).getString(),
                mouseX, mouseY, 153, 167, 12, 60);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks,
            final int mouseX, final int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        getMinecraft().getTextureManager().bindTexture(TEXTURE);
        ClientUtils.blit(stack, this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Progress Bar
        ClientUtils.blit(stack, this, this.guiLeft + 79, this.guiTop + 35, 176, 0,
                this.container.getScaledProgress(), 17);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX,
            final int mouseY) {
        super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);

        this.font.drawString(stack, this.title.getString(), 7.0f, 5.0f, 0x404040);
        this.font.drawString(stack, this.playerInventory.getDisplayName().getString(), 7.0f, 74.0f, 0x404040);

        drawFluids();
    }

    @Override
    protected void init() {
        super.init();

        this.addButton(new ChangeModeButton(this, this.guiLeft + 115, this.guiTop + 4, 19, 19));
    }

    protected void renderTooltip(final MatrixStack stack, final String tooltip, final int mouseX,
            final int mouseY, final int xLeft, final int xRight, final int yTop, final int yBottom) {
        if (mouseX > this.guiLeft + xLeft && mouseX < this.guiLeft + xRight && mouseY > this.guiTop + yTop
                && mouseY < this.guiTop + yBottom) {
            super.renderTooltip(stack, new StringTextComponent(tooltip), mouseX, mouseY);
        }
    }

    public static class ChangeModeButton extends Button {

        private final AgitatorScreen screen;

        public ChangeModeButton(final AgitatorScreen screen, final int x, final int y, final int width,
                final int height) {
            super(x, y, width, height, new StringTextComponent(""), pressable -> {
                final AgitatorTileEntity tile = screen.container.getTile();
                final AgitatorType currentType = tile.getAgitatorType();
                final AgitatorType[] types = AgitatorType.values();
                final int newType = currentType.ordinal() + 1 >= types.length ? 0 : currentType.ordinal() + 1;
                tile.setAgitatorType(AgitatorType.values()[newType]);
            });

            this.screen = screen;
        }

        @Override
        public void renderWidget(final MatrixStack stack, final int mouseY, final int mouseX,
                final float partialTicks) {
            int offsetY = 17;
            if (isHovered()) {
                offsetY += this.height;
            }
            ClientUtils.renderButton(stack, TEXTURE, this, partialTicks, this.alpha,
                    199 + this.screen.container.getTile().getAgitatorType().ordinal() * 19, offsetY,
                    this.width, this.height, 256, 256);
        }
    }
}
