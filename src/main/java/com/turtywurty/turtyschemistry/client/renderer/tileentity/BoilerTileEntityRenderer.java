package com.turtywurty.turtyschemistry.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.model.BoilerModel;
import com.turtywurty.turtyschemistry.common.blocks.BoilerBlock;
import com.turtywurty.turtyschemistry.common.tileentity.BoilerTileEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class BoilerTileEntityRenderer extends TileEntityRenderer<BoilerTileEntity> {

	private BoilerModel boilerModel;

	public BoilerTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);

		this.boilerModel = new BoilerModel();
	}

	@Override
	public void render(BoilerTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.push();
		float rot = tileEntityIn.getBlockState().get(BoilerBlock.HORIZONTAL_FACING).getHorizontalAngle();
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-rot));
		this.boilerModel.getBase().render(matrixStackIn,
				bufferIn.getBuffer(RenderType.getEntityTranslucent(
						new ResourceLocation(TurtyChemistry.MOD_ID, "textures/blocks/boiler.png"))),
				combinedLightIn, combinedOverlayIn);
		matrixStackIn.pop();
	}
}
