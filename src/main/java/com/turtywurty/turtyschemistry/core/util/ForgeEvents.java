package com.turtywurty.turtyschemistry.core.util;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.screen.book.GuideBookData;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = Bus.FORGE)
public final class ForgeEvents {

    private ForgeEvents() {
    }

    @SubscribeEvent
    public static void onAttachPlayerCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(TurtyChemistry.MOD_ID, "guide_book_data"),
                    new GuideBookData());
        }
    }

    public static class CustomBuffer implements IRenderTypeBuffer {
        public List<CustomVertexBuilder> builders = new ArrayList<>();

        @Override
        public IVertexBuilder getBuffer(final RenderType renderType) {
            final CustomVertexBuilder builder = new CustomVertexBuilder(renderType);
            this.builders.add(builder);
            return builder;
        }

        public static class CustomVertexBuilder implements IVertexBuilder {
            public List<Vertex> vertices = new ArrayList<>();
            public Vertex vertex = new Vertex();
            public RenderType type;

            public CustomVertexBuilder(final RenderType type) {
                this.type = type;
            }

            @Override
            public IVertexBuilder color(final float red, final float green, final float blue,
                    final float alpha) {
                this.vertex.r = (int) (red * 255);
                this.vertex.g = (int) (green * 255);
                this.vertex.b = (int) (blue * 255);
                this.vertex.a = (int) (alpha * 255);
                return null;
            }

            @Override
            public IVertexBuilder color(final int red, final int green, final int blue, final int alpha) {
                this.vertex.r = red;
                this.vertex.g = green;
                this.vertex.b = blue;
                this.vertex.a = alpha;
                return this;
            }

            @Override
            public void endVertex() {
                this.vertices.add(this.vertex);
                this.vertex = new Vertex();
            }

            @Override
            public IVertexBuilder lightmap(final int lightmapUV) {
                this.vertex.lu = lightmapUV;
                this.vertex.lv = lightmapUV;
                return this;
            }

            @Override
            public IVertexBuilder lightmap(final int u, final int v) {
                this.vertex.lu = u;
                this.vertex.lv = v;
                return this;
            }

            @Override
            public IVertexBuilder normal(final float x, final float y, final float z) {
                this.vertex.nx = x;
                this.vertex.ny = y;
                this.vertex.nz = z;
                return this;
            }

            @Override
            public IVertexBuilder overlay(final int overlayUV) {
                return this;
            }

            @Override
            public IVertexBuilder overlay(final int u, final int v) {
                this.vertex.ou = u;
                this.vertex.ov = v;
                return this;
            }

            @Override
            public IVertexBuilder pos(final double x, final double y, final double z) {
                this.vertex.x = x;
                this.vertex.y = y;
                this.vertex.z = z;
                return this;
            }

            @Override
            public IVertexBuilder tex(final float u, final float v) {
                this.vertex.u = u;
                this.vertex.v = v;
                return this;
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
