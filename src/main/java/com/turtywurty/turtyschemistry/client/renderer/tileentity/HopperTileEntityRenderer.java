package com.turtywurty.turtyschemistry.client.renderer.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.tileentity.HopperTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HopperTileEntityRenderer extends TileEntityRenderer<HopperTileEntity> {
	private List<BlockPos> offsets = new ArrayList<BlockPos>(54);
	private Random rand = new Random();

	public HopperTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);

		for (int index = 0; index < 54; index++) {
			offsets.add(index, new BlockPos(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
		}
	}

	@Override
	public void render(HopperTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		for (AxisAlignedBB boundingBox : Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 32.0D, 32.0D, 32.0D)
				.toBoundingBoxList()) {
			WorldRenderer.drawBoundingBox(matrixStackIn, bufferIn.getBuffer(RenderType.getLines()), boundingBox, 1.0f,
					0.0f, 0.5f, 1.0f);
		}

		for (int slot = 0; slot < tileEntityIn.getInventory().getSlots(); slot++) {
			if (slot % 2 == 0) {
				matrixStackIn.translate(offsets.get(slot).getX(), offsets.get(slot).getY(), offsets.get(slot).getZ());
				this.renderItem(tileEntityIn.getItemInSlot(slot), matrixStackIn, bufferIn, combinedLightIn);
			}
		}
	}

	private void renderItem(ItemStack stack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn) {
		ClientUtils.MC.getItemRenderer().renderItem(stack, TransformType.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY,
				matrixStackIn, bufferIn);
	}
}
