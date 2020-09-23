package com.turtywurty.turtyschemistry.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.model.ReplacedTextureModel;
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
		IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getSolid());
		matrixStackIn.push();
		FluidStackHandler fluidHandler = tileEntityIn.getFluidHandler();
		FluidStack fluid = fluidHandler.getFluidInTank(5);
		if (!fluid.isEmpty()) {
			TextureAtlasSprite texture = Minecraft.getInstance()
					.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
					.apply(fluid.getFluid().getAttributes().getStillTexture());
			IBakedModel model = ClientUtils.MC.getBlockRendererDispatcher().getBlockModelShapes().getModelManager()
					.getModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/agitator_fluid"));
			
			ClientUtils.MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tileEntityIn.getWorld(),
					new ReplacedTextureModel(model, texture), FluidInit.BRINE_BLOCK.get().getDefaultState(),
					tileEntityIn.getPos(), matrixStackIn, buffer, true, tileEntityIn.getWorld().getRandom(),
					tileEntityIn.getWorld().getSeed(), combinedOverlayIn, EmptyModelData.INSTANCE);
		}
		matrixStackIn.pop();
	}
}
