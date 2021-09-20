package com.turtywurty.turtyschemistry.common.blocks.boiler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class BoilerTileEntityRenderer extends TileEntityRenderer<BoilerTileEntity> {

    private final BoilerModel boilerModel;

    public BoilerTileEntityRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        this.boilerModel = new BoilerModel();
    }

    @Override
    public void render(final BoilerTileEntity tileEntityIn, final float partialTicks,
            final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn,
            final int combinedOverlayIn) {
        matrixStackIn.push();
        final float rot = tileEntityIn.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING)
                .getHorizontalAngle();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-rot));
        this.boilerModel.getBase().render(matrixStackIn,
                bufferIn.getBuffer(RenderType.getEntityTranslucent(
                        new ResourceLocation(TurtyChemistry.MOD_ID, "textures/blocks/boiler.png"))),
                combinedLightIn, combinedOverlayIn);
        matrixStackIn.pop();
    }
}
