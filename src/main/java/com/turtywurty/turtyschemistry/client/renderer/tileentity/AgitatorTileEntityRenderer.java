package com.turtywurty.turtyschemistry.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.model.ReplacedTextureModel;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.core.util.FluidStackHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidStack;

public class AgitatorTileEntityRenderer extends TileEntityRenderer<AgitatorTileEntity> {

	private FluidStack fluid;

	public AgitatorTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(AgitatorTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getTranslucent());
		FluidStackHandler fluidHandler = tileEntityIn.getFluidHandler();

		double index = 0;
		for (FluidStack fluid : fluidHandler.getContents()) {
			if (!fluid.isEmpty()) {
				matrixStackIn.push();
				TextureAtlasSprite texture = Minecraft.getInstance()
						.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
						.apply(fluid.getFluid().getAttributes().getStillTexture());

				IBakedModel model = ClientUtils.MC.getModelManager()
						.getModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/agitator_fluid"));
				int fluidColor = fluid.getFluid().getAttributes().getColor(tileEntityIn.getWorld(), tileEntityIn.getPos());

				matrixStackIn.scale(1.0f, 0.5f, 1.0f);
				matrixStackIn.translate(0.0D, (index++ / 4) + 0.1D, 0.0D);

				ClientUtils.MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tileEntityIn.getWorld(),
						new ReplacedTextureModel(model, texture),
						fluid.getFluid().getFluid().getDefaultState().getBlockState(), tileEntityIn.getPos(),
						matrixStackIn, buffer, true, tileEntityIn.getWorld().getRandom(),
						tileEntityIn.getWorld().getSeed(), combinedOverlayIn, EmptyModelData.INSTANCE);
				matrixStackIn.pop();
			}
		}
	}
}
