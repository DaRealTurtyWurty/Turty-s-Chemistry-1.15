package com.turtywurty.turtyschemistry.common.blocks.agitator;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.model.ReplacedTextureModel;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidStack;

public class AgitatorTileEntityRenderer extends TileEntityRenderer<AgitatorTileEntity> {

    public AgitatorTileEntityRenderer(final TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @SuppressWarnings("unused")
    @Override
    public void render(final AgitatorTileEntity tileEntityIn, final float partialTicks,
            final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int combinedLightIn,
            final int combinedOverlayIn) {
        final IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getTranslucent());
        final FluidStackHandler fluidHandler = tileEntityIn.getFluidHandler();

        double index = 0;
        for (final FluidStack fluid : fluidHandler.getContents()) {
            if (!fluid.isEmpty()) {
                matrixStackIn.push();
                final TextureAtlasSprite texture = Minecraft.getInstance()
                        .getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
                        .apply(fluid.getFluid().getAttributes().getStillTexture());

                final IBakedModel model = ClientUtils.MC.getModelManager()
                        .getModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/agitator_fluid"));
                final int fluidColor = fluid.getFluid().getAttributes().getColor(tileEntityIn.getWorld(),
                        tileEntityIn.getPos());
                // TODO: Color the fluid

                matrixStackIn.scale(1.0f, 0.5f, 1.0f);
                matrixStackIn.translate(0.0D, index / 4 + 0.1D, 0.0D);
                index++;

                ClientUtils.MC.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                        tileEntityIn.getWorld(), new ReplacedTextureModel(model, texture),
                        fluid.getFluid().getFluid().getDefaultState().getBlockState(), tileEntityIn.getPos(),
                        matrixStackIn, buffer, true, tileEntityIn.getWorld().getRandom(),
                        ((ServerWorld) tileEntityIn.getWorld()).getSeed(), combinedOverlayIn,
                        EmptyModelData.INSTANCE);
                matrixStackIn.pop();
            }
        }
    }
}
