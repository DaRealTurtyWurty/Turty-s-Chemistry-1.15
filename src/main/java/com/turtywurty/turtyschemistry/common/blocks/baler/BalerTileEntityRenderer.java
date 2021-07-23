package com.turtywurty.turtyschemistry.common.blocks.baler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;
import com.turtywurty.turtyschemistry.core.init.BlockInit;

import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

@SuppressWarnings("deprecation")
public class BalerTileEntityRenderer extends TileEntityRenderer<BalerTileEntity> {

	public BalerTileEntityRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(final BalerTileEntity tileEntityIn, final float partialTicks, final MatrixStack matrixStackIn,
			final IRenderTypeBuffer bufferIn, final int combinedLightIn, final int combinedOverlayIn) {

		BlockRendererDispatcher blockRenderer = ClientUtils.MC.getBlockRendererDispatcher();

		renderArm(blockRenderer, matrixStackIn, tileEntityIn, bufferIn, combinedLightIn, combinedOverlayIn);
		renderPress(blockRenderer, matrixStackIn, tileEntityIn, bufferIn, combinedLightIn, combinedOverlayIn,
				partialTicks);
		renderBale(tileEntityIn, matrixStackIn, bufferIn, combinedOverlayIn);
		renderWheat(tileEntityIn, matrixStackIn, bufferIn, combinedOverlayIn);
	}

	private void renderArm(final BlockRendererDispatcher blockRenderer, final MatrixStack stackIn,
			final BalerTileEntity tile, final IRenderTypeBuffer buf, final int combinedLightIn,
			final int combinedOverlayIn) {

		stackIn.push();

		blockRenderer.renderBlock(
				BlockInit.BALER_ARM.get().getDefaultState().with(BaseHorizontalBlock.HORIZONTAL_FACING,
						tile.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING)),
				stackIn, buf, combinedLightIn, combinedOverlayIn);

		stackIn.pop();
	}

	private void renderBale(final BalerTileEntity tile, final MatrixStack stackIn, final IRenderTypeBuffer bufferIn,
			final int combinedLightIn) {

		Direction direction = tile.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING);

		if (tile.completed) {
			stackIn.push();

			stackIn.scale(0.55f, 0.55f, 0.55f);

			stackIn.rotate(Vector3f.YP.rotationDegrees(direction.getOpposite().getHorizontalAngle()));
			stackIn.rotate(Vector3f.XP.rotationDegrees(90.0f));
			stackIn.translate(-0.85f, 0.6f, -0.4f);
			stackIn.translate(0.0f, MathHelper.lerp((float) tile.itemTicksExisted / 200, 0.0f, 1.0f), 0.0f);

			renderItem(new ItemStack(Items.HAY_BLOCK), stackIn, bufferIn, combinedLightIn);

			stackIn.pop();
		}
	}

	private void renderItem(final ItemStack stack, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn,
			final int combinedLightIn) {
		ClientUtils.MC.getItemRenderer().renderItem(stack, TransformType.FIXED, combinedLightIn,
				OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
	}

	private void renderPress(final BlockRendererDispatcher blockRenderer, final MatrixStack stackIn,
			final BalerTileEntity tile, final IRenderTypeBuffer buf, final int combinedLightIn,
			final int combinedOverlayIn, final float partialTicks) {

		stackIn.push();

		if (tile.isCompressing()) {
			float currentTime = tile.getWorld().getGameTime() + partialTicks;

			stackIn.translate(0.0f, Math.sin(Math.PI * currentTime / 16) / 11 - 0.035f, 0.0f);
		}

		blockRenderer.renderBlock(
				BlockInit.BALER_PRESS.get().getDefaultState().with(BaseHorizontalBlock.HORIZONTAL_FACING,
						tile.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING)),
				stackIn, buf, combinedLightIn, combinedOverlayIn);

		stackIn.pop();
	}

	private void renderWheat(final BalerTileEntity tile, final MatrixStack matrixStackIn,
			final IRenderTypeBuffer bufferIn, final int combinedLightIn) {
		Direction direction = tile.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING);

		if (!tile.completed) {
			for (int index = 2; index < tile.getItems().size(); index++) {
				matrixStackIn.push();

				matrixStackIn.scale(0.6F, 0.6F, 0.6F);

				if (direction == Direction.WEST) {
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0f));
					matrixStackIn.translate(-1.6f, 0.0f, -1.65f);
				} else if (direction == Direction.NORTH) {
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0f));
					matrixStackIn.translate(-1.6f, 0.0f, 0.01f);
				} else if (direction == Direction.SOUTH) {
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0f));
					matrixStackIn.translate(0.07f, 0.0f, -1.65f);
				} else if (direction == Direction.EAST) {
					matrixStackIn.translate(0.07f, 0.0f, 0.0f);
				}

				matrixStackIn.translate(0.525f, 0.5f, 0.42f + (index - 2) / 10.0f);

				renderItem(tile.getItems().get(index), matrixStackIn, bufferIn, combinedLightIn);

				matrixStackIn.pop();
			}
		}
	}
}