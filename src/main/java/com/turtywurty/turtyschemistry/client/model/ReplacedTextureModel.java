/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */
package com.turtywurty.turtyschemistry.client.model;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;

public class ReplacedTextureModel implements IBakedModel {

    private final IBakedModel model;

    private final List<BakedQuad> generalQuads;
    private final Map<Direction, List<BakedQuad>> sideQuads = new EnumMap<>(Direction.class);

    public ReplacedTextureModel(final IBakedModel modelIn, final TextureAtlasSprite newTexture) {
        this.model = modelIn;

        this.generalQuads = modelIn.getQuads(null, null, ThreadLocalRandom.current(),
                EmptyModelData.INSTANCE);
        Arrays.stream(Direction.values()).forEach(dir -> this.sideQuads.put(dir,
                modelIn.getQuads(null, dir, ThreadLocalRandom.current(), EmptyModelData.INSTANCE)));

        for (int i = 0; i < this.generalQuads.size(); i++) {
            final BakedQuad quad = this.generalQuads.get(i);

            this.generalQuads.set(i, replaceUV(newTexture, new BakedQuad(quad.getVertexData(),
                    quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.applyDiffuseLighting())));
        }

        for (final Map.Entry<Direction, List<BakedQuad>> quadSide : this.sideQuads.entrySet()) {
            final List<BakedQuad> value = quadSide.getValue();
            for (int i = 0; i < value.size(); i++) {
                final BakedQuad quad = value.get(i);
                value.set(i, replaceUV(newTexture, new BakedQuad(quad.getVertexData(), quad.getTintIndex(),
                        quad.getFace(), quad.getSprite(), quad.applyDiffuseLighting())));
            }
        }
    }

    private static BakedQuad replaceUV(final TextureAtlasSprite newTexture, final BakedQuad oldQuad) {
        if (newTexture.equals(oldQuad.getSprite()))
            return oldQuad;
        final int[] vertexData = oldQuad.getVertexData();
        final int j = 8;
        final int k = vertexData.length / j;

        final TextureAtlasSprite oldTexture = oldQuad.getSprite();

        for (int i = 0; i < k; i++) {
            final float oldU = Float.intBitsToFloat(vertexData[4 + i * 8]);
            final float oldV = Float.intBitsToFloat(vertexData[5 + i * 8]);
            final float newUnscaledU = oldU - oldTexture.getMinU();
            final float newUnscaledV = oldV - oldTexture.getMinV();
            vertexData[4 + i * 8] = Float.floatToIntBits(newTexture.getMinU() + newUnscaledU);
            vertexData[5 + i * 8] = Float.floatToIntBits(newTexture.getMinV() + newUnscaledV);
        }

        return new BakedQuad(vertexData, oldQuad.getTintIndex(), oldQuad.getFace(), newTexture,
                oldQuad.applyDiffuseLighting());
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.model.getOverrides();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.model.getParticleTexture(EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(final BlockState state, final Direction side, final Random rand) {
        return side == null ? this.generalQuads : this.sideQuads.get(side);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.model.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.model.isBuiltInRenderer();
    }

    @Override
    public boolean isGui3d() {
        return this.model.isGui3d();
    }

    @Override
    public boolean isSideLit() {
        return this.model.isSideLit();
    }
}
