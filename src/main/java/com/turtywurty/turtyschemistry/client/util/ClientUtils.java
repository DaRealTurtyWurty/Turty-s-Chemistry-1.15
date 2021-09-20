package com.turtywurty.turtyschemistry.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.agitator.AgitatorData;
import com.turtywurty.turtyschemistry.common.blocks.boiler.BoilerRecipe;
import com.turtywurty.turtyschemistry.common.blocks.electrolyzer.ElectrolyzerRecipe;
import com.turtywurty.turtyschemistry.common.blocks.electrolyzer.ElectrolyzerRecipeReader;
import com.turtywurty.turtyschemistry.core.util.SimpleJsonDataManager;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState.AlphaState;
import net.minecraft.client.renderer.RenderState.TextureState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("deprecation")
public final class ClientUtils {

    public static final SimpleJsonDataManager<AgitatorData> AGITATOR_DATA = new SimpleJsonDataManager<>(
            "agitator", AgitatorData.class);

    public static final SimpleJsonDataManager<BoilerRecipe> BOILER_DATA = new SimpleJsonDataManager<>(
            "boiler", BoilerRecipe.class);

    public static final SimpleJsonDataManager<ElectrolyzerRecipe> ELECTROLYZER_DATA = new ElectrolyzerRecipeReader<>();

    public static final Minecraft MC = Minecraft.getInstance();

    private ClientUtils() {
    }

    public static void blit(final MatrixStack stack, final AbstractGui object, final int xPos, final int yPos,
            final float textureX, final float textureY, final int drawWidth, final int drawHeight,
            final int textureWidth, final int textureHeight) {
        AbstractGui.blit(stack, xPos, yPos, textureX, textureY, drawWidth, drawHeight, textureWidth,
                textureHeight);
    }

    public static void blit(final MatrixStack stack, final AbstractGui object, final int xPos, final int yPos,
            final int textureX, final int textureY, final int width, final int height) {
        object.blit(stack, xPos, yPos, textureX, textureY, width, height);
    }

    public static boolean crossesChunkBoundary(final Vector3d start, final Vector3d end,
            final BlockPos offset) {
        if (crossesChunkBorderSingleDim(start.x, end.x, offset.getX())
                || crossesChunkBorderSingleDim(start.y, end.y, offset.getY()))
            return true;
        return crossesChunkBorderSingleDim(start.z, end.z, offset.getZ());
    }

    public static Quaternion degreeToQuaterion(double x, double y, double z) {
        x = Math.toRadians(x);
        y = Math.toRadians(y);
        z = Math.toRadians(z);
        final Quaternion qYaw = new Quaternion(0, (float) Math.sin(y / 2), 0, (float) Math.cos(y / 2));
        final Quaternion qPitch = new Quaternion((float) Math.sin(x / 2), 0, 0, (float) Math.cos(x / 2));
        final Quaternion qRoll = new Quaternion(0, 0, (float) Math.sin(z / 2), (float) Math.cos(z / 2));

        final Quaternion quat = qYaw;
        quat.multiply(qRoll);
        quat.multiply(qPitch);
        return quat;
    }

    public static void drawFluid(final ResourceLocation texture, final FluidStack fluid, final int x,
            final int y, final int w, final int h) {
        RenderSystem.pushMatrix();
        final int scaledHeight = (int) (h * ((float) fluid.getAmount() / 1000));
        final RenderType renderType = ClientUtils.getGui(texture);
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer
                .getImpl(Tessellator.getInstance().getBuffer());
        final MatrixStack transform = new MatrixStack();
        ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, fluid, x, y + h - scaledHeight, w,
                scaledHeight);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, x, y, w, h, 256f, 0, 0, 0, 0);
        buffer.finish(renderType);
        RenderSystem.popMatrix();
    }

    public static void drawRepeatedFluidSprite(final IVertexBuilder builder, final MatrixStack transform,
            final FluidStack fluid, final float x, final float y, final float w, final float h) {
        final TextureAtlasSprite sprite = getSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));
        final int col = fluid.getFluid().getAttributes().getColor(fluid);
        final int iW = sprite.getWidth();
        final int iH = sprite.getHeight();
        if (iW > 0 && iH > 0) {
            drawRepeatedSprite(builder, transform, x, y, w, h, iW, iH, sprite.getMinU(), sprite.getMaxU(),
                    sprite.getMinV(), sprite.getMaxV(), (col >> 16 & 255) / 255.0f, (col >> 8 & 255) / 255.0f,
                    (col & 255) / 255.0f, 1);
        }
    }

    public static void drawRepeatedFluidSprite(final IVertexBuilder builder, final MatrixStack transform,
            final ResourceLocation location, final int color, final float x, final float y, final float w,
            final float h) {
        final int col = color;
        final int iW = 16;
        final int iH = 512;
        if (iW > 0 && iH > 0) {
            drawRepeatedSprite(builder, transform, x, y, w, h, iW, iH, 0, iW, 0, iH,
                    (col >> 16 & 255) / 255.0f, (col >> 8 & 255) / 255.0f, (col & 255) / 255.0f, 1);
        }
    }

    public static void drawRepeatedFluidSpriteGui(final IRenderTypeBuffer buffer, final MatrixStack transform,
            final FluidStack fluid, final float x, final float y, final float w, final float h) {
        final RenderType renderType = getGui(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        final IVertexBuilder builder = buffer.getBuffer(renderType);
        drawRepeatedFluidSprite(builder, transform, fluid, x, y, w, h);
    }

    public static void drawRepeatedFluidSpriteGui(final IRenderTypeBuffer buffer, final MatrixStack transform,
            final ResourceLocation location, final int color, final float x, final float y, final float w,
            final float h) {
        final RenderType renderType = getGui(location);
        final IVertexBuilder builder = buffer.getBuffer(renderType);
        drawRepeatedFluidSprite(builder, transform, location, color, x, y, w, h);
    }

    public static void drawRepeatedSprite(final IVertexBuilder builder, final MatrixStack transform,
            final float x, final float y, final float w, final float h, final int iconWidth,
            final int iconHeight, final float uMin, final float uMax, final float vMin, final float vMax,
            final float r, final float g, final float b, final float alpha) {
        final int iterMaxW = (int) (w / iconWidth);
        final int iterMaxH = (int) (h / iconHeight);
        final float leftoverW = w % iconWidth;
        final float leftoverH = h % iconHeight;
        final float leftoverWf = leftoverW / iconWidth;
        final float leftoverHf = leftoverH / iconHeight;
        final float iconUDif = uMax - uMin;
        final float iconVDif = vMax - vMin;
        for (int ww = 0; ww < iterMaxW; ww++) {
            for (int hh = 0; hh < iterMaxH; hh++) {
                drawTexturedRect(builder, transform, x + ww * iconWidth, y + hh * iconHeight, iconWidth,
                        iconHeight, r, g, b, alpha, uMin, uMax, vMin, vMax);
            }
            drawTexturedRect(builder, transform, x + ww * iconWidth, y + iterMaxH * iconHeight, iconWidth,
                    leftoverH, r, g, b, alpha, uMin, uMax, vMin, vMin + iconVDif * leftoverHf);
        }
        if (leftoverW > 0) {
            for (int hh = 0; hh < iterMaxH; hh++) {
                drawTexturedRect(builder, transform, x + iterMaxW * iconWidth, y + hh * iconHeight, leftoverW,
                        iconHeight, r, g, b, alpha, uMin, uMin + iconUDif * leftoverWf, vMin, vMax);
            }
            drawTexturedRect(builder, transform, x + iterMaxW * iconWidth, y + iterMaxH * iconHeight,
                    leftoverW, leftoverH, r, g, b, alpha, uMin, uMin + iconUDif * leftoverWf, vMin,
                    vMin + iconVDif * leftoverHf);
        }
    }

    public static void drawTexture(final ResourceLocation texture, final int x, final int y, final int w,
            final int h, final float storedAmount, final float maxStoredAmount) {
        final RenderType renderType = ClientUtils.getGui(texture);

        RenderSystem.pushMatrix();
        final int scaledHeight = (int) (h * (storedAmount / maxStoredAmount));
        final IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer
                .getImpl(Tessellator.getInstance().getBuffer());
        final MatrixStack transform = new MatrixStack();
        ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, texture, 0xEFEFEF, x, y + h - scaledHeight,
                w, scaledHeight);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, x, y, w, h, 256f, 0, 0, 0, 0);
        buffer.finish(renderType);
        RenderSystem.popMatrix();
    }

    public static void drawTexturedRect(final IVertexBuilder builder, final MatrixStack transform,
            final float x, final float y, final float w, final float h, final float r, final float g,
            final float b, final float alpha, final float u0, final float u1, final float v0,
            final float v1) {
        final Matrix4f mat = transform.getLast().getMatrix();
        builder.pos(mat, x, y + h, 0).color(r, g, b, alpha).tex(u0, v1).overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(0xf000f0).normal(1, 1, 1).endVertex();
        builder.pos(mat, x + w, y + h, 0).color(r, g, b, alpha).tex(u1, v1).overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880).normal(1, 1, 1).endVertex();
        builder.pos(mat, x + w, y, 0).color(r, g, b, alpha).tex(u1, v0).overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880).normal(1, 1, 1).endVertex();
        builder.pos(mat, x, y, 0).color(r, g, b, alpha).tex(u0, v0).overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880).normal(1, 1, 1).endVertex();
    }

    public static void drawTexturedRect(final IVertexBuilder builder, final MatrixStack transform,
            final int x, final int y, final int w, final int h, final float picSize, final int u0,
            final int u1, final int v0, final int v1) {
        drawTexturedRect(builder, transform, x, y, w, h, 1, 1, 1, 1, u0 / picSize, u1 / picSize, v0 / picSize,
                v1 / picSize);
    }

    public static ClientPlayerEntity getClientPlayer() {
        return MC.player;
    }

    public static ClientWorld getClientWorld() {
        return MC.world;
    }

    public static RenderType getGui(final ResourceLocation texture) {
        return RenderType.makeType("gui_" + texture, DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS,
                256, RenderType.State.getBuilder().texture(new TextureState(texture, false, false))
                        .alpha(new AlphaState(0.5F)).build(false));
    }

    public static List<BakedQuad> getNewQuads(final IBakedModel existingModel, final BlockState state,
            final Direction side, final Random rand, final TextureAtlasSprite newTexture) {
        final List<BakedQuad> newQuads = new ArrayList<>();
        for (final BakedQuad quad : existingModel.getQuads(state, side, rand, EmptyModelData.INSTANCE)) {
            newQuads.add(new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), newTexture,
                    quad.applyDiffuseLighting()));
        }

        for (final BakedQuad quad : existingModel.getQuads(state, null, rand, EmptyModelData.INSTANCE)) {
            newQuads.add(new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), newTexture,
                    quad.applyDiffuseLighting()));
        }
        return newQuads;
    }

    public static TextureAtlasSprite getSprite(final ResourceLocation rl) {
        return MC.getModelManager().getAtlasTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getSprite(rl);
    }

    public static int intFromRgb(final float[] rgb) {
        int ret = (int) (255 * rgb[0]);
        ret = (ret << 8) + (int) (255 * rgb[1]);
        return (ret << 8) + (int) (255 * rgb[2]);
    }

    public static boolean isMouseInArea(final int mouseX, final int mouseY, final int startX,
            final int startY, final int sizeX, final int sizeY) {
        return mouseX >= startX && mouseX <= startX + sizeX && mouseY >= startY && mouseY <= startY + sizeY;
    }

    public static boolean isMouseInSlot(final int mouseX, final int mouseY, final int slotX,
            final int slotY) {
        return mouseX >= slotX && mouseX <= slotX + 15 && mouseY >= slotY && mouseY <= slotY + 15;
    }

    public static void onClientInit() {
        ModelLoader.addSpecialModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/agitator_fluid"));
        ModelLoader.addSpecialModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/researcher_arm"));

        final IResourceManager manager = Minecraft.getInstance().getResourceManager();
        if (manager instanceof IReloadableResourceManager) {
            final IReloadableResourceManager reloader = (IReloadableResourceManager) manager;
            reloader.addReloadListener(AGITATOR_DATA);
            reloader.addReloadListener(BOILER_DATA);
            reloader.addReloadListener(ELECTROLYZER_DATA);
            reloader.close();
        }
    }

    public static void renderButton(final MatrixStack stack, final ResourceLocation textureIn,
            final Widget widgetIn, final float partialTicks, final float alphaIn, final float xPos,
            final float yPos, final int widthIn, final int heightIn, final int textureWidth,
            final int textureHeight) {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().getTexture(textureIn);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alphaIn);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        blit(stack, widgetIn, widgetIn.x, widgetIn.y, xPos, yPos, widthIn, heightIn, textureWidth,
                textureHeight);
    }

    private static boolean crossesChunkBorderSingleDim(final double a, final double b, final int offset) {
        return (int) Math.floor(a + offset) >> 4 != (int) Math.floor(b + offset) >> 4;
    }
}
