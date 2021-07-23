package com.turtywurty.turtyschemistry.core.world.features;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class LavaLakeOre extends OreFeature {

	public LavaLakeOre() {
		super(OreFeatureConfig.CODEC);
	}

	@Override
	public boolean generate(final ISeedReader worldIn, final ChunkGenerator generator, final Random rand,
			final BlockPos pos, final OreFeatureConfig config) {
		// TODO: Work out the placement stuff.
		return super.generate(worldIn, generator, rand, pos, config);
	}
}
