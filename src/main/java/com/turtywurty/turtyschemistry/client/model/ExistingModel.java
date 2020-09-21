package com.turtywurty.turtyschemistry.client.model;

import java.util.List;
import java.util.Random;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

public class ExistingModel implements IBakedModel {

	private final IBakedModel model;
	private final TextureAtlasSprite texture;

	public ExistingModel(IBakedModel modelIn, TextureAtlasSprite textureIn) {
		this.model = modelIn;
		this.texture = textureIn;
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model.isBuiltInRenderer();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return model.isAmbientOcclusion();
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
		return ClientUtils.getNewQuads(model, state, side, rand, texture);
	}

	@SuppressWarnings("deprecation")
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return model.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return model.getOverrides();
	}

	//isSideLit
	@Override
	public boolean func_230044_c_() {
		return model.func_230044_c_();
	}
}
