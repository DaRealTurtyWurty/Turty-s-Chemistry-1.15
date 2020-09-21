package com.turtywurty.turtyschemistry.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.model.ExistingModel;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.core.init.FluidInit;

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

	public AgitatorTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(AgitatorTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		FluidStackHandler fluidHandler = tileEntityIn.getFluidHandler();
		FluidStack fluid = new FluidStack(FluidInit.BRINE_STILL.get(), 1000);
		if (!fluid.isEmpty()) {
			TextureAtlasSprite texture = Minecraft.getInstance()
					.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
					.apply(fluid.getFluid().getAttributes().getStillTexture());
			IBakedModel model = ClientUtils.MC.getBlockRendererDispatcher().getBlockModelShapes().getModelManager()
					.getModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/agitator_fluid"));
			
			matrixStackIn.translate(0.0f, 2f, 0.0f);

			IBakedModel newModel = new ExistingModel(model, texture);
			ClientUtils.MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tileEntityIn.getWorld(),
					newModel, FluidInit.BRINE_BLOCK.get().getDefaultState(), tileEntityIn.getPos(), matrixStackIn,
					bufferIn.getBuffer(RenderType.getTranslucent()), true, tileEntityIn.getWorld().getRandom(), 2l,
					combinedOverlayIn, EmptyModelData.INSTANCE);
		}
	}
}
