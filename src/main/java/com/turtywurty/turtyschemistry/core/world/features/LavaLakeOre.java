package com.turtywurty.turtyschemistry.core.world.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class LavaLakeOre extends OreFeature {

	public LavaLakeOre(Function<Dynamic<?>, ? extends OreFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos,
			OreFeatureConfig config) {
		//TODO: Work out the placement stuff.
		return super.place(worldIn, generator, rand, pos, config);
	}
}
