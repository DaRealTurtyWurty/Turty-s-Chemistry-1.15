package com.turtywurty.turtyschemistry.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.common.blocks.AutoclaveBlock;
import com.turtywurty.turtyschemistry.common.tileentity.AutoclaveTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class AutoclaveTileEntityRenderer extends TileEntityRenderer<AutoclaveTileEntity> {

	private float degrees;

	public AutoclaveTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		degrees = 0.0f;
	}

	@Override
	public void render(AutoclaveTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(AutoclaveBlock.HORIZONTAL_FACING);
		for (int index = 0; index < tileEntityIn.size; ++index) {
			ItemStack itemstack = tileEntityIn.getItemInSlot(index);
			if (itemstack != ItemStack.EMPTY) {
				matrixStackIn.push();
				if (index == 0) {
					matrixStackIn.translate(0.5D, 0.26D, 0.5D);
				} else if (index == 1) {
					matrixStackIn.translate(0.5D, 0.45D, 0.5D);
				} else if (index == 2) {
					matrixStackIn.translate(0.5D, 0.637D, 0.5D);
				} else {
					matrixStackIn.translate(0.5D, 0.45D, 0.5D);
				}
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(direction.getOpposite().getHorizontalAngle()));
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0f));
				// matrixStackIn.rotate(Vector3f.YP.rotationDegrees((degrees++ / 3)));
				matrixStackIn.scale(0.35F, 0.35F, 0.35F);
				renderItem(itemstack, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
				matrixStackIn.pop();
			}
		}
	}

	private void renderItem(ItemStack stack, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, combinedLightIn,
				combinedOverlayIn, matrixStackIn, bufferIn);
	}
}
