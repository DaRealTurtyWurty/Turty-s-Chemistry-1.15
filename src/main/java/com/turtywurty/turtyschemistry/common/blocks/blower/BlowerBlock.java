package com.turtywurty.turtyschemistry.common.blocks.blower;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlowerBlock extends BaseHorizontalBlock {

	private static final Optional<VoxelShape> SHAPE = Stream
			.of(VoxelShapes.combineAndSimplify(Block.makeCuboidShape(12, 1, 6, 16, 4, 9),
					Block.makeCuboidShape(4, 0, 5, 12, 5, 11), IBooleanFunction.OR))
			.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR));
	protected static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

	public BlowerBlock(Properties builder) {
		super(builder);
		if (SHAPE.isPresent())
			runCalculation(SHAPES, SHAPE.get());
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES.get(state.get(HORIZONTAL_FACING));
	}
}
