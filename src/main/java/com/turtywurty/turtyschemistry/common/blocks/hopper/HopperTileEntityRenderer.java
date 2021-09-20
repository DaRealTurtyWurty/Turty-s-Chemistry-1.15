package com.turtywurty.turtyschemistry.common.blocks.hopper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

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
    private final List<BlockPos> offsets = new ArrayList<>(54);
    private final Random rand = new Random();

    public HopperTileEntityRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        for (int index = 0; index < 54; index++) {
            this.offsets.add(index,
                    new BlockPos(this.rand.nextDouble(), this.rand.nextDouble(), this.rand.nextDouble()));
        }
    }

    @Override
    public void render(final HopperTileEntity tileEntityIn, final float partialTicks,
            final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn,
            final int combinedOverlayIn) {
        for (final AxisAlignedBB boundingBox : Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 32.0D, 32.0D, 32.0D)
                .toBoundingBoxList()) {
            WorldRenderer.drawBoundingBox(matrixStackIn, bufferIn.getBuffer(RenderType.getLines()),
                    boundingBox, 1.0f, 0.0f, 0.5f, 1.0f);
        }

        for (int slot = 0; slot < tileEntityIn.getInventory().getSlots(); slot++) {
            if (slot % 2 == 0) {
                matrixStackIn.translate(this.offsets.get(slot).getX(), this.offsets.get(slot).getY(),
                        this.offsets.get(slot).getZ());
                renderItem(tileEntityIn.getItemInSlot(slot), matrixStackIn, bufferIn, combinedLightIn);
            }
        }
    }

    private void renderItem(final ItemStack stack, final MatrixStack matrixStackIn,
            final IRenderTypeBuffer bufferIn, final int combinedLightIn) {
        ClientUtils.MC.getItemRenderer().renderItem(stack, TransformType.FIXED, combinedLightIn,
                OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
    }
}
