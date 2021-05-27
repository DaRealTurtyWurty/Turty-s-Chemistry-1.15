package com.turtywurty.turtyschemistry.common.blocks.briquetting_press;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;
import com.turtywurty.turtyschemistry.core.init.BlockInit;

import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BriquettingPressTileEntityRenderer extends TileEntityRenderer<BriquettingPressTileEntity> {

	public BriquettingPressTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(BriquettingPressTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		renderTurner(tileEntityIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}

	@SuppressWarnings("deprecation")
	private static void renderTurner(BriquettingPressTileEntity tile, MatrixStack stackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		BlockRendererDispatcher blockRenderer = ClientUtils.MC.getBlockRendererDispatcher();

		stackIn.push();

		stackIn.translate(-0.125f, 0.5f, -0.05f);

		if (tile.buttonPressed) {
			stackIn.rotate(Vector3f.YP.rotationDegrees(tile.turnerRotation++));
		}

		blockRenderer.renderBlock(
				BlockInit.BRIQUETTING_TURNER.get().getDefaultState().with(BaseHorizontalBlock.HORIZONTAL_FACING,
						tile.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING)),
				stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		stackIn.pop();
	}

	private static void renderSawdust() {

	}
}
