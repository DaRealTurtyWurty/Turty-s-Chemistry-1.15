package com.turtywurty.turtyschemistry.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

import java.util.*;

public class ExistingModel implements IBakedModel
{

	private final IBakedModel model;
	private final TextureAtlasSprite texture;
	private List<BakedQuad> generalQuads;
	private Map<Direction, List<BakedQuad>> sideQuads = new HashMap<>();


	public ExistingModel(IBakedModel modelIn, TextureAtlasSprite textureIn)
	{
		this.model = modelIn;
		this.texture = textureIn;

		generalQuads = modelIn.getQuads(null, null, null);
		Arrays.stream(Direction.values()).forEach(dir -> sideQuads.put(dir, modelIn.getQuads(null, dir, null)));

		for (int i = 0; i < generalQuads.size(); i++)
		{
			BakedQuad quad = generalQuads.get(i);
			generalQuads.set(i, new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), textureIn,
					quad.shouldApplyDiffuseLighting()));
		}

		for (Map.Entry<Direction, List<BakedQuad>> quadSide : sideQuads.entrySet())
		{
			List<BakedQuad> value = quadSide.getValue();
			for (int i = 0; i < value.size(); i++)
			{
				BakedQuad quad = value.get(i);
				value.set(i, new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), textureIn,
						quad.shouldApplyDiffuseLighting()));
			}
		}
		int i = 34;
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

	//isSideLit
	@Override
	public boolean func_230044_c_()
	{
		return model.func_230044_c_();
	}
}
