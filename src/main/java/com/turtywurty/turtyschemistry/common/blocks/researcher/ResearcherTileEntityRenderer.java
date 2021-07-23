package com.turtywurty.turtyschemistry.common.blocks.researcher;

import java.util.concurrent.ThreadLocalRandom;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

public class ResearcherTileEntityRenderer extends TileEntityRenderer<ResearcherTileEntity> {

	public ResearcherTileEntityRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(final ResearcherTileEntity tileEntityIn, final float partialTicks,
			final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn,
			final int combinedOverlayIn) {
		matrixStackIn.push();
		Direction facing = tileEntityIn.getBlockState().get(ResearcherBlock.FACING);
		switch (facing) {
		case EAST:
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270f));
			matrixStackIn.translate(0f, 0.0f, -1.0f);
			if (tileEntityIn.getProcessingTime() > 0) {
				if (tileEntityIn.moveLeft) {
					matrixStackIn.translate(tileEntityIn.time += 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time > 0.2f) {
						tileEntityIn.moveLeft = false;
					}
				} else {
					matrixStackIn.translate(tileEntityIn.time -= 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time < -0.2f) {
						tileEntityIn.moveLeft = true;
					}
				}
			} else {
				tileEntityIn.time = 0;
			}
			break;
		case SOUTH:
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180f));
			matrixStackIn.translate(-1f, 0.0f, -1.0f);
			if (tileEntityIn.getProcessingTime() > 0) {
				if (tileEntityIn.moveLeft) {
					matrixStackIn.translate(tileEntityIn.time += 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time > 0.2f) {
						tileEntityIn.moveLeft = false;
					}
				} else {
					matrixStackIn.translate(tileEntityIn.time -= 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time < -0.2f) {
						tileEntityIn.moveLeft = true;
					}
				}
			} else {
				tileEntityIn.time = 0;
			}
			break;
		case WEST:
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90f));
			matrixStackIn.translate(-1f, 0.0f, 0.0f);
			if (tileEntityIn.getProcessingTime() > 0) {
				if (tileEntityIn.moveLeft) {
					matrixStackIn.translate(tileEntityIn.time += 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time > 0.2f) {
						tileEntityIn.moveLeft = false;
					}
				} else {
					matrixStackIn.translate(tileEntityIn.time -= 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time < -0.2f) {
						tileEntityIn.moveLeft = true;
					}
				}
			} else {
				tileEntityIn.time = 0;
			}
			break;
		case NORTH:
			if (tileEntityIn.getProcessingTime() > 0) {
				if (tileEntityIn.moveLeft) {
					matrixStackIn.translate(tileEntityIn.time += 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time > 0.2f) {
						tileEntityIn.moveLeft = false;
					}
				} else {
					matrixStackIn.translate(tileEntityIn.time -= 0.025f, 0.0f, 0.0f);
					if (tileEntityIn.time < -0.2f) {
						tileEntityIn.moveLeft = true;
					}
				}
			} else {
				tileEntityIn.time = 0;
			}
			break;
		default:
			break;
		}
		IBakedModel model = ClientUtils.MC.getModelManager()
				.getModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/researcher_arm"));
		ClientUtils.MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tileEntityIn.getWorld(), model,
				tileEntityIn.getBlockState(), tileEntityIn.getPos(), matrixStackIn,
				bufferIn.getBuffer(RenderType.getSolid()), true, tileEntityIn.getWorld().getRandom(),
				ThreadLocalRandom.current().nextLong(), combinedOverlayIn, EmptyModelData.INSTANCE);
		matrixStackIn.pop();

		matrixStackIn.push();
		matrixStackIn.scale(0.5f, 0.5f, 0.5f);

		switch (facing) {
		case NORTH:
			matrixStackIn.translate(1.05f, 0.75f, 0.35f);
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90f));
			break;
		case EAST:
			matrixStackIn.translate(1.65f, 0.75f, 1.05f);
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90f));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90f));
			break;
		case SOUTH:
			matrixStackIn.translate(1.05f, 0.75f, 1.65f);
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90f));
			break;
		case WEST:
			matrixStackIn.translate(0.35f, 0.75f, 1.05f);
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90f));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90f));
			break;
		default:
			break;
		}
		if (!tileEntityIn.getItemInSlot(1).isEmpty()) {
			renderItem(tileEntityIn.getItemInSlot(1), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		} else if (!tileEntityIn.getItemInSlot(0).isEmpty()) {
			renderItem(tileEntityIn.getItemInSlot(0), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		}
		matrixStackIn.pop();

		if (tileEntityIn.getProcessingTime() > 0) {
			matrixStackIn.push();
			matrixStackIn.translate(0.0f, 0.4f, 0.34f);
			matrixStackIn.scale(1.0f, 0.5f, 1.0f);
			matrixStackIn.translate(-tileEntityIn.time, 0.0f, 0.0f);
			BeaconTileEntityRenderer.renderBeamSegment(matrixStackIn, bufferIn,
					new ResourceLocation("textures/entity/beacon_beam.png"), partialTicks, 1.0f,
					tileEntityIn.getWorld().getGameTime(), 0, 1, new float[] { 1.0f, 0.3f, 0.2f }, 0.025f, 0.0275f);
			matrixStackIn.pop();
		}
	}

	private void renderItem(final ItemStack stack, final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn,
			final int combinedLightIn, final int combinedOverlayIn) {
		Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, combinedLightIn,
				combinedOverlayIn, matrixStackIn, bufferIn);
	}
}
