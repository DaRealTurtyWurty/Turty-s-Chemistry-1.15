package com.turtywurty.turtyschemistry.core.world.features;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

public class HeliumPocket extends Placement<ChanceConfig> {

	public HeliumPocket(Function<Dynamic<?>, ? extends ChanceConfig> p_i51367_1_) {
		super(p_i51367_1_);
	}

	public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn,
			Random random, ChanceConfig configIn, BlockPos pos) {
		if (random.nextInt(configIn.chance) == 0) {
			int i = random.nextInt(16) + pos.getX();
			int j = random.nextInt(16) + pos.getZ();
			int k = random.nextInt(generatorIn.getMaxHeight() - 60);
			return Stream.of(new BlockPos(i, k, j));
		} else {
			return Stream.empty();
		}
	}
}