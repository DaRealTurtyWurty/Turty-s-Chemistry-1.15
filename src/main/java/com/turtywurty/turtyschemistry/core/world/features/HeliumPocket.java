package com.turtywurty.turtyschemistry.core.world.features;

import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

public class HeliumPocket extends Placement<ChanceConfig> {

    public HeliumPocket() {
        super(ChanceConfig.CODEC);
    }

    @Override
    public Stream<BlockPos> getPositions(final WorldDecoratingHelper worldIn, final Random random,
            final ChanceConfig configIn, final BlockPos pos) {
        if (random.nextInt(configIn.chance) == 0) {
            final int i = random.nextInt(16) + pos.getX();
            final int j = random.nextInt(16) + pos.getZ();
            final int k = random.nextInt(worldIn.func_242891_a() - 60);
            return Stream.of(new BlockPos(i, k, j));
        }
        return Stream.empty();
    }
}