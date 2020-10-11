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
import com.turtywurty.turtyschemistry.common.network.MessageNoSpamChatComponents;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {

	public static final Minecraft MC = Minecraft.getInstance();
	private static final int DELETION_ID = 3718126;
	private static int lastAdded;

	public static ClientPlayerEntity getClientPlayer() {
		return MC.player;
	}

	public static ClientWorld getClientWorld() {
		return MC.world;
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendClientNoSpamMessages(ITextComponent[] messages) {
		NewChatGui chat = MC.ingameGUI.getChatGUI();
		for (int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++)
			chat.deleteChatLine(i);
		for (int i = 0; i < messages.length; i++)
			chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
		lastAdded = DELETION_ID + messages.length - 1;
	}

	public static void sendServerNoSpamMessages(PlayerEntity player, ITextComponent... messages) {
		if (messages.length > 0 && player instanceof ServerPlayerEntity)
			TurtyChemistry.packetHandler.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
					new MessageNoSpamChatComponents(messages));
	}

	public static void renderButton(ResourceLocation textureIn, Widget widgetIn, float partialTicks, float alphaIn,
			float xPos, float yPos, int widthIn, int heightIn, int textureWidth, int textureHeight) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(textureIn);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, alphaIn);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		blit(widgetIn, widgetIn.x, widgetIn.y, xPos, yPos, widthIn, heightIn, textureWidth, textureHeight);
	}

	protected static void renderBg(Minecraft mc, int mouseX, int mouseY) {

	}

	public static void blit(AbstractGui object, int xPos, int yPos, int textureX, int textureY, int width, int height) {
		object.blit(xPos, yPos, textureX, textureY, width, height);
	}

	@SuppressWarnings("static-access")
	public static void blit(AbstractGui object, int xPos, int yPos, float textureX, float textureY, int drawWidth,
			int drawHeight, int textureWidth, int textureHeight) {
		object.blit(xPos, yPos, textureX, textureY, drawWidth, drawHeight, textureWidth, textureHeight);
	}

	public static boolean isMouseInSlot(int mouseX, int mouseY, int slotX, int slotY) {
		if ((mouseX >= slotX && mouseX <= slotX + 15) && (mouseY >= slotY && mouseY <= slotY + 15)) {
			return true;
		}

		return false;
	}

	public static boolean isMouseInArea(int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY) {
		if ((mouseX >= startX && mouseX <= startX + sizeX) && (mouseY >= startY && mouseY <= startY + sizeY)) {
			return true;
		}

		return false;
	}

	public static void drawTexturedRect(IVertexBuilder builder, MatrixStack transform, float x, float y, float w,
			float h, float r, float g, float b, float alpha, float u0, float u1, float v0, float v1) {
		Matrix4f mat = transform.getLast().getMatrix();
		builder.pos(mat, x, y + h, 0).color(r, g, b, alpha).tex(u0, v1).overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(0xf000f0).normal(1, 1, 1).endVertex();
		builder.pos(mat, x + w, y + h, 0).color(r, g, b, alpha).tex(u1, v1).overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880).normal(1, 1, 1).endVertex();
		builder.pos(mat, x + w, y, 0).color(r, g, b, alpha).tex(u1, v0).overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880).normal(1, 1, 1).endVertex();
		builder.pos(mat, x, y, 0).color(r, g, b, alpha).tex(u0, v0).overlay(OverlayTexture.NO_OVERLAY)
				.lightmap(15728880).normal(1, 1, 1).endVertex();
	}

	public static void drawTexturedRect(IVertexBuilder builder, MatrixStack transform, int x, int y, int w, int h,
			float picSize, int u0, int u1, int v0, int v1) {
		drawTexturedRect(builder, transform, x, y, w, h, 1, 1, 1, 1, u0 / picSize, u1 / picSize, v0 / picSize,
				v1 / picSize);
	}

	public static boolean crossesChunkBoundary(Vec3d start, Vec3d end, BlockPos offset) {
		if (crossesChunkBorderSingleDim(start.x, end.x, offset.getX()))
			return true;
		if (crossesChunkBorderSingleDim(start.y, end.y, offset.getY()))
			return true;
		return crossesChunkBorderSingleDim(start.z, end.z, offset.getZ());
	}

	private static boolean crossesChunkBorderSingleDim(double a, double b, int offset) {
		return ((int) Math.floor(a + offset)) >> 4 != ((int) Math.floor(b + offset)) >> 4;
	}

	public static Quaternion degreeToQuaterion(double x, double y, double z) {
		x = Math.toRadians(x);
		y = Math.toRadians(y);
		z = Math.toRadians(z);
		Quaternion qYaw = new Quaternion(0, (float) Math.sin(y / 2), 0, (float) Math.cos(y / 2));
		Quaternion qPitch = new Quaternion((float) Math.sin(x / 2), 0, 0, (float) Math.cos(x / 2));
		Quaternion qRoll = new Quaternion(0, 0, (float) Math.sin(z / 2), (float) Math.cos(z / 2));

		Quaternion quat = qYaw;
		quat.multiply(qRoll);
		quat.multiply(qPitch);
		return quat;
	}

	public static int intFromRgb(float[] rgb) {
		int ret = (int) (255 * rgb[0]);
		ret = (ret << 8) + (int) (255 * rgb[1]);
		ret = (ret << 8) + (int) (255 * rgb[2]);
		return ret;
	}

	public static void drawRepeatedFluidSpriteGui(IRenderTypeBuffer buffer, MatrixStack transform,
			ResourceLocation location, int color, float x, float y, float w, float h) {
		RenderType renderType = getGui(location);
		IVertexBuilder builder = buffer.getBuffer(renderType);
		drawRepeatedFluidSprite(builder, transform, location, color, x, y, w, h);
	}

	public static void drawRepeatedFluidSprite(IVertexBuilder builder, MatrixStack transform, ResourceLocation location,
			int color, float x, float y, float w, float h) {
		int col = color;
		int iW = 16;
		int iH = 512;
		if (iW > 0 && iH > 0)
			drawRepeatedSprite(builder, transform, x, y, w, h, iW, iH, 0, iW, 0, iH, (col >> 16 & 255) / 255.0f,
					(col >> 8 & 255) / 255.0f, (col & 255) / 255.0f, 1);
	}

	public static void drawRepeatedFluidSpriteGui(IRenderTypeBuffer buffer, MatrixStack transform, FluidStack fluid,
			float x, float y, float w, float h) {
		RenderType renderType = getGui(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
		IVertexBuilder builder = buffer.getBuffer(renderType);
		drawRepeatedFluidSprite(builder, transform, fluid, x, y, w, h);
	}

	public static void drawRepeatedFluidSprite(IVertexBuilder builder, MatrixStack transform, FluidStack fluid, float x,
			float y, float w, float h) {
		TextureAtlasSprite sprite = getSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));
		int col = fluid.getFluid().getAttributes().getColor(fluid);
		int iW = sprite.getWidth();
		int iH = sprite.getHeight();
		if (iW > 0 && iH > 0)
			drawRepeatedSprite(builder, transform, x, y, w, h, iW, iH, sprite.getMinU(), sprite.getMaxU(),
					sprite.getMinV(), sprite.getMaxV(), (col >> 16 & 255) / 255.0f, (col >> 8 & 255) / 255.0f,
					(col & 255) / 255.0f, 1);
	}

	public static void drawRepeatedSprite(IVertexBuilder builder, MatrixStack transform, float x, float y, float w,
			float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax, float r, float g,
			float b, float alpha) {
		int iterMaxW = (int) (w / iconWidth);
		int iterMaxH = (int) (h / iconHeight);
		float leftoverW = w % iconWidth;
		float leftoverH = h % iconHeight;
		float leftoverWf = leftoverW / (float) iconWidth;
		float leftoverHf = leftoverH / (float) iconHeight;
		float iconUDif = uMax - uMin;
		float iconVDif = vMax - vMin;
		for (int ww = 0; ww < iterMaxW; ww++) {
			for (int hh = 0; hh < iterMaxH; hh++)
				drawTexturedRect(builder, transform, x + ww * iconWidth, y + hh * iconHeight, iconWidth, iconHeight, r,
						g, b, alpha, uMin, uMax, vMin, vMax);
			drawTexturedRect(builder, transform, x + ww * iconWidth, y + iterMaxH * iconHeight, iconWidth, leftoverH, r,
					g, b, alpha, uMin, uMax, vMin, (vMin + iconVDif * leftoverHf));
		}
		if (leftoverW > 0) {
			for (int hh = 0; hh < iterMaxH; hh++)
				drawTexturedRect(builder, transform, x + iterMaxW * iconWidth, y + hh * iconHeight, leftoverW,
						iconHeight, r, g, b, alpha, uMin, (uMin + iconUDif * leftoverWf), vMin, vMax);
			drawTexturedRect(builder, transform, x + iterMaxW * iconWidth, y + iterMaxH * iconHeight, leftoverW,
					leftoverH, r, g, b, alpha, uMin, (uMin + iconUDif * leftoverWf), vMin,
					(vMin + iconVDif * leftoverHf));
		}
	}

	public static TextureAtlasSprite getSprite(ResourceLocation rl) {
		return MC.getModelManager().getAtlasTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getSprite(rl);
	}

	public static RenderType getGui(ResourceLocation texture) {
		return RenderType.makeType("gui_" + texture, DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
				RenderType.State.getBuilder().texture(new TextureState(texture, false, false))
						.alpha(new AlphaState(0.5F)).build(false));
	}

	public static List<BakedQuad> getNewQuads(IBakedModel existingModel, BlockState state, Direction side, Random rand,
			TextureAtlasSprite newTexture) {
		List<BakedQuad> newQuads = new ArrayList<BakedQuad>();
		for (BakedQuad quad : existingModel.getQuads(state, side, rand, EmptyModelData.INSTANCE)) {
			newQuads.add(new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), newTexture,
					quad.shouldApplyDiffuseLighting()));
		}

		for (BakedQuad quad : existingModel.getQuads(state, null, rand, EmptyModelData.INSTANCE)) {
			newQuads.add(new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), newTexture,
					quad.shouldApplyDiffuseLighting()));
		}
		return newQuads;
	}

	public static void drawFluid(final ResourceLocation TEXTURE, final FluidStack fluid, final int x, final int y,
			final int w, final int h) {
		RenderSystem.pushMatrix();
		int scaledHeight = (int) (h * ((float) fluid.getAmount() / 1000));
		RenderType renderType = ClientUtils.getGui(TEXTURE);
		IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		MatrixStack transform = new MatrixStack();
		ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, fluid, x, y + h - scaledHeight, w, scaledHeight);
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, x, y, w, h, 256f, 0, 0, 0, 0);
		buffer.finish(renderType);
		RenderSystem.popMatrix();
	}

	public static void drawTexture(final ResourceLocation TEXTURE, final int x, final int y, final int w, final int h,
			final float storedAmount, final float maxStoredAmount) {
		RenderType renderType = ClientUtils.getGui(TEXTURE);

		RenderSystem.pushMatrix();
		int scaledHeight = (int) (h * (storedAmount / maxStoredAmount));
		IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		MatrixStack transform = new MatrixStack();
		ClientUtils.drawRepeatedFluidSpriteGui(buffer, transform, TEXTURE, 0xEFEFEF, x, y + h - scaledHeight, w,
				scaledHeight);
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		ClientUtils.drawTexturedRect(buffer.getBuffer(renderType), transform, x, y, w, h, 256f, 0, 0, 0, 0);
		buffer.finish(renderType);
		RenderSystem.popMatrix();
	}
}
