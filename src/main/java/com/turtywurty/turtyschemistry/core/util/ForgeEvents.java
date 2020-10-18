package com.turtywurty.turtyschemistry.core.util;

import java.util.ArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.screen.book.GuideBookData;
import com.turtywurty.turtyschemistry.client.screen.book.GuideBookDataCap;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.blocks.GasifierBlock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent.HighlightBlock;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = Bus.FORGE)
public class ForgeEvents {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void renderBlockPlacement(HighlightBlock event) {
		Item heldItem = ClientUtils.getClientPlayer().getHeldItemMainhand().getItem();
		Block block = heldItem instanceof BlockItem ? ((BlockItem) heldItem).getBlock() : null;

		if (block instanceof GasifierBlock) {
			// IRenderTypeBuffer buffer = event.getBuffers();
			MatrixStack stack = event.getMatrix();
			CustomBuffer buffer = new CustomBuffer();

			stack.push();

			BlockRendererDispatcher renderer = ClientUtils.MC.getBlockRendererDispatcher();
			renderer.renderModel(block.getDefaultState(), event.getTarget().getPos().up(), ClientUtils.getClientWorld(),
					stack, buffer.getBuffer(RenderTypeLookup.getRenderType(block.getDefaultState())),
					EmptyModelData.INSTANCE);

			stack.pop();

			for (CustomBuffer.CustomVertexBuilder builder : buffer.builders) {
				// System.out.println(buffer.builders.size());
				IVertexBuilder builder1 = event.getBuffers().getBuffer(builder.type);
				for (CustomBuffer.Vertex vert : builder.vertices) {
					// System.out.println(builder.vertices.size());
					builder1.addVertex((float) vert.x + 0, (float) vert.y + 0, (float) vert.z + 0, vert.r / 255f,
							vert.g / 255f, vert.b / 255f, 0.5f, vert.u + 0, vert.v + 0, 15728640, 15728640, 0, 0, 0);
				}
			}

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void something(PlayerEvent.ItemPickupEvent event) {
		event.getPlayer().getCapability(GuideBookDataCap.INSTANCE)
				.ifPresent(data -> data.setPlayerUUID(event.getPlayer().getUniqueID()));
	}

	@SubscribeEvent
	public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(new ResourceLocation(TurtyChemistry.MOD_ID, "guide_book_data"), new GuideBookData());
		}
	}

	public static class CustomBuffer implements IRenderTypeBuffer {
		public ArrayList<CustomVertexBuilder> builders = new ArrayList<>();

		@Override
		public IVertexBuilder getBuffer(RenderType renderType) {
			CustomVertexBuilder builder = new CustomVertexBuilder(renderType);
			builders.add(builder);
			return builder;
		}

		public static class CustomVertexBuilder implements IVertexBuilder {
			public ArrayList<Vertex> vertices = new ArrayList<>();
			public Vertex vertex = new Vertex();
			public RenderType type;

			public CustomVertexBuilder(RenderType type) {
				this.type = type;
			}

			@Override
			public IVertexBuilder lightmap(int lightmapUV) {
				vertex.lu = lightmapUV;
				vertex.lv = lightmapUV;
				return this;
			}

			@Override
			public IVertexBuilder overlay(int overlayUV) {
				return this;
			}

			@Override
			public IVertexBuilder pos(double x, double y, double z) {
				vertex.x = x;
				vertex.y = y;
				vertex.z = z;
				return this;
			}

			@Override
			public IVertexBuilder color(int red, int green, int blue, int alpha) {
				vertex.r = red;
				vertex.g = green;
				vertex.b = blue;
				vertex.a = alpha;
				return this;
			}

			@Override
			public IVertexBuilder tex(float u, float v) {
				vertex.u = u;
				vertex.v = v;
				return this;
			}

			@Override
			public IVertexBuilder overlay(int u, int v) {
				vertex.ou = u;
				vertex.ov = v;
				return this;
			}

			@Override
			public IVertexBuilder lightmap(int u, int v) {
				vertex.lu = u;
				vertex.lv = v;
				return this;
			}

			@Override
			public IVertexBuilder normal(float x, float y, float z) {
				vertex.nx = x;
				vertex.ny = y;
				vertex.nz = z;
				return this;
			}

			@Override
			public void endVertex() {
				vertices.add(vertex);
				vertex = new Vertex();
			}

			@Override
			public IVertexBuilder color(float red, float green, float blue, float alpha) {
				vertex.r = (int) (red * 255);
				vertex.g = (int) (green * 255);
				vertex.b = (int) (blue * 255);
				vertex.a = (int) (alpha * 255);
				return null;
			}
		}

		public static class Vertex {
			public double x;
			public double y;
			public double z;
			public int r;
			public int g;
			public int b;
			public int a;
			public float u;
			public float v;
			public float lu;
			public float lv;
			public int ou;
			public int ov;
			public float nx;
			public float ny;
			public float nz;
		}
	}
}
