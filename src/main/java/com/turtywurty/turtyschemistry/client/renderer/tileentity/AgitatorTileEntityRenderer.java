package com.turtywurty.turtyschemistry.client.renderer.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;

import net.minecraft.client.renderer.FluidBlockRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;

public class AgitatorTileEntityRenderer extends TileEntityRenderer<AgitatorTileEntity> {

	public AgitatorTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(AgitatorTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.push();

		List<Fluid> fluids = new ArrayList<Fluid>();
		tileEntityIn.getHandler().getContents().forEach(stack -> {
			fluids.add(stack.getFluid());
		});

		FluidBlockRenderer fluidRenderer = new FluidBlockRenderer();
		int fluidIndex = 0;
		for (Fluid fluid : fluids) {
			fluidRenderer.render(tileEntityIn.getWorld(), tileEntityIn.getPos().add(0.0D, fluidIndex++, 0.0D),
					bufferIn.getBuffer(RenderType.getTranslucent()), fluid.getDefaultState());
		}

		matrixStackIn.pop();
	}
}
