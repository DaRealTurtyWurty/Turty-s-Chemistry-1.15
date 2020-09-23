/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */
package com.turtywurty.turtyschemistry.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

import java.util.*;

public class ReplacedTextureModel implements IBakedModel
{

	private final IBakedModel model;
	private final TextureAtlasSprite texture;
	private List<BakedQuad> generalQuads;
	private Map<Direction, List<BakedQuad>> sideQuads = new HashMap<>();


	public ReplacedTextureModel(IBakedModel modelIn, TextureAtlasSprite newTexture)
	{
		this.model = modelIn;
		this.texture = newTexture;

		generalQuads = modelIn.getQuads(null, null, null);
		Arrays.stream(Direction.values()).forEach(dir -> sideQuads.put(dir, modelIn.getQuads(null, dir, null)));

		for (int i = 0; i < generalQuads.size(); i++)
		{
			BakedQuad quad = generalQuads.get(i);

			generalQuads.set(i, replaceUV(newTexture, new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.func_187508_a(),
					quad.shouldApplyDiffuseLighting())));
		}

		for (Map.Entry<Direction, List<BakedQuad>> quadSide : sideQuads.entrySet())
		{
			List<BakedQuad> value = quadSide.getValue();
			for (int i = 0; i < value.size(); i++)
			{
				BakedQuad quad = value.get(i);
				value.set(i, replaceUV(newTexture, new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.func_187508_a(),
						quad.shouldApplyDiffuseLighting())));
			}
		}
	}

	@Override
	public boolean isGui3d()
	{
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return model.isBuiltInRenderer();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return model.isAmbientOcclusion();
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand)
	{
		return side == null ? this.generalQuads : this.sideQuads.get(side);
	}

	@SuppressWarnings("deprecation")
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return model.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return model.getOverrides();
	}

	@Override
	public boolean func_230044_c_()
	{
		return model.func_230044_c_();
	}


	private static BakedQuad replaceUV(TextureAtlasSprite newTexture, BakedQuad oldQuad)
	{
		if (newTexture.equals(oldQuad.func_187508_a()))
		{
			return oldQuad;
		}
		int[] vertexData = oldQuad.getVertexData();
		int j = 8;
		int k = vertexData.length / j;

		TextureAtlasSprite oldTexture = oldQuad.func_187508_a();

		for (int i = 0; i < k; i++)
		{
			float oldU = Float.intBitsToFloat(vertexData[4 + i * 8]);
			float oldV = Float.intBitsToFloat(vertexData[5 + i * 8]);
			float newUnscaledU = oldU - oldTexture.getMinU();
			float newUnscaledV = oldV - oldTexture.getMinV();
			vertexData[4 + i * 8] = Float.floatToIntBits(newTexture.getMinU() + newUnscaledU);
			vertexData[5 + i * 8] = Float.floatToIntBits(newTexture.getMinV() + newUnscaledV);
		}

		return new BakedQuad(vertexData, oldQuad.getTintIndex(), oldQuad.getFace(), newTexture);
	}
}
