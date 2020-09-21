package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IProductionLineTile {

	public final List<Direction> HORIZONTAL_DIRECTIONS = new ArrayList<Direction>(
			Sets.newHashSet(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));

	public boolean hasValidNeighbours(World world, BlockPos pos, Block... exludingBlocks);
}
