package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IProductionLineTile {

    Set<Direction> HORIZONTAL_DIRECTIONS = Sets.newHashSet(Direction.NORTH, Direction.EAST, Direction.SOUTH,
            Direction.WEST);

    boolean hasValidNeighbours(World world, BlockPos pos, Block... exludingBlocks);
}
