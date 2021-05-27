package com.turtywurty.turtyschemistry.common.blocks;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class PropaneTankBlock extends BaseHorizontalBlock {

	private static final Optional<VoxelShape> SHAPE = Stream
			.of(Block.makeCuboidShape(7.5, 8.5, 8.5, 8.5, 9.5, 9.5), Block.makeCuboidShape(5, 1, 5, 11, 8, 11),
					Block.makeCuboidShape(4.9, 0, 4.9, 11.1, 1, 11.1), Block.makeCuboidShape(5.1, 8, 5.1, 10.9, 9, 10.9),
					Block.makeCuboidShape(6, 11, 6.5, 10, 12, 7.5), Block.makeCuboidShape(6, 11, 8.51, 10, 12, 9.51),
					Block.makeCuboidShape(6, 9, 6.5, 7, 11, 7.5), Block.makeCuboidShape(6, 9, 8.51, 7, 11, 9.51),
					Block.makeCuboidShape(9, 9, 6.5, 10, 11, 7.5), Block.makeCuboidShape(9, 9, 8.51, 10, 11, 9.51),
					Block.makeCuboidShape(7, 9, 6.5, 9, 10, 7.5), Block.makeCuboidShape(9, 9, 7.5, 10, 10, 8.51),
					Block.makeCuboidShape(6, 9, 7.5, 7, 10, 8.51), Block.makeCuboidShape(7, 9, 8.51, 9, 10, 9.51),
					Block.makeCuboidShape(7.5, 9.5, 5.5, 8.5, 10.5, 9.5))
			.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR));
	protected static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

	public PropaneTankBlock(Properties builder) {
		super(builder);
		if (SHAPE.isPresent())
			runCalculation(SHAPES, SHAPE.get());
	}

	protected static void calculateShapes(Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[] { shape, VoxelShapes.empty() };

		int times = (to.getHorizontalIndex() - Direction.NORTH.getHorizontalIndex() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1],
					VoxelShapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		SHAPES.put(to, buffer[0]);
	}

	protected void runCalculation(VoxelShape shape) {
		for (Direction direction : Direction.values()) {
			calculateShapes(direction, shape);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES.get(state.get(HORIZONTAL_FACING));
	}
}
