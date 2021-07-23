package com.turtywurty.turtyschemistry.common.blocks.autoclave;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class AutoclaveTileEntityRenderer extends TileEntityRenderer<AutoclaveTileEntity> {

	public AutoclaveTileEntityRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(final AutoclaveTileEntity tileEntityIn, final float partialTicks,
			final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn,
			final int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING);
		for (int index = 0; index < tileEntityIn.size; ++index) {
			ItemStack itemstack = tileEntityIn.getItemInSlot(index);
			if (itemstack != ItemStack.EMPTY) {
				matrixStackIn.push();
				switch (index) {
				case 0:
					matrixStackIn.translate(0.5D, 0.26D, 0.5D);
					break;
				case 1:
					matrixStackIn.translate(0.5D, 0.45D, 0.5D);
					break;
				case 2:
					matrixStackIn.translate(0.5D, 0.637D, 0.5D);
					break;
				default:
					matrixStackIn.translate(0.5D, 0.45D, 0.5D);
					break;
				}
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(direction.getOpposite().getHorizontalAngle()));
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0f));
				// matrixStackIn.rotate(Vector3f.YP.rotationDegrees((degrees++ / 3)));
				matrixStackIn.scale(0.35F, 0.35F, 0.35F);
				renderItem(itemstack, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
				matrixStackIn.pop();
			}
		}
	}

	private void renderItem(final ItemStack stack, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn,
			final int combinedLightIn, final int combinedOverlayIn) {
		Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, combinedLightIn,
				combinedOverlayIn, matrixStackIn, bufferIn);
	}
}
