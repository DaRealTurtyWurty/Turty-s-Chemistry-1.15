package com.turtywurty.turtyschemistry.common.blocks.bunsen_burner;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.recipes.BunsenBurnerRecipe;
import com.turtywurty.turtyschemistry.core.init.ItemInit;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class BunsenBurnerTileEntityRenderer extends TileEntityRenderer<BunsenBurnerTileEntity> {

	private int lightDecrease = 0;

	public BunsenBurnerTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(BunsenBurnerTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (tileEntityIn.getBlockState().get(BunsenBurnerBlock.FRAME)) {
			matrixStackIn.translate(0.5D, 0.5D, 0.575D);
			ClientUtils.MC.getItemRenderer().renderItem(new ItemStack(ItemInit.BUNSEN_FRAME.get()), TransformType.FIXED,
					combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
			if (tileEntityIn.getBlockState().get(BunsenBurnerBlock.MESH)) {
				ClientUtils.MC.getItemRenderer().renderItem(new ItemStack(ItemInit.WIRE_GAUZE.get()),
						TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
				if (tileEntityIn.getBlockState().get(BunsenBurnerBlock.CRUCIBLE)) {
					matrixStackIn.translate(0.0D, 0.282D, 0.0D);
					ClientUtils.MC.getItemRenderer().renderItem(new ItemStack(ItemInit.CRUCIBLE.get()),
							TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
					if (!tileEntityIn.getItemInSlot(0).isEmpty()) {
						BunsenBurnerRecipe recipe = tileEntityIn.getRecipe(tileEntityIn.getItemInSlot(0));
						if (recipe != null) {
							if (recipe.getSpecialEffect().equalsIgnoreCase("darken")) {
								// Darken
							}
						}
						matrixStackIn.scale(0.15F, 0.15F, 0.15F);
						matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90f));
						matrixStackIn.translate(0.0D, -0.5D, 0.0D);
						ClientUtils.MC.getItemRenderer().renderItem(tileEntityIn.getItemInSlot(0), TransformType.FIXED,
								combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
					}
				}
			}
		}
	}
}
