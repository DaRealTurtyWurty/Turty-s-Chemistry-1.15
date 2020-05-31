package com.turtywurty.turtyschemistry.common.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BriquettingPressBlock extends BaseHorizontalBlock {

	protected static final Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();

	public BriquettingPressBlock(Properties builder) {
		super(builder);
		runCalculation(Stream.of(Block.makeCuboidShape(0, 0, 2, 1, 1, 3), Block.makeCuboidShape(0, 0, 13, 1, 1, 14),
				Block.makeCuboidShape(0, 1, 2, 13, 2, 3), Block.makeCuboidShape(0, 1, 13, 13, 2, 14),
				Block.makeCuboidShape(1, 1, 7, 12, 2, 9), Block.makeCuboidShape(0, 1, 3, 1, 2, 13),
				Block.makeCuboidShape(12, 1, 3, 13, 2, 13), Block.makeCuboidShape(12, 0, 2, 13, 1, 3),
				Block.makeCuboidShape(12, 0, 13, 13, 1, 14), Block.makeCuboidShape(13, 8, 9, 14, 9, 10),
				Block.makeCuboidShape(2, 1, 3, 5, 2, 7), Block.makeCuboidShape(2, 2, 4, 5, 6, 8),
				Block.makeCuboidShape(1, 6, 4, 6, 10, 8), Block.makeCuboidShape(1, 10, 5, 6, 15, 8),
				Block.makeCuboidShape(0.9000000000000004, 15, 4.9, 6.1000000000000005, 15.1, 8.1),
				Block.makeCuboidShape(6.9, 7.000000000000002, 7, 7, 10.200000000000003, 10.2),
				Block.makeCuboidShape(0, 8, 8, 7, 9, 9), Block.makeCuboidShape(7, 2, 8, 13, 7, 13),
				Block.makeCuboidShape(7, 7, 4, 8, 13, 14), Block.makeCuboidShape(12, 7, 4, 13, 13, 14),
				Block.makeCuboidShape(8, 7, 4, 12, 13, 5), Block.makeCuboidShape(8, 7, 13, 12, 13, 14),
				Block.makeCuboidShape(8, 6, 4, 12, 7, 8), Block.makeCuboidShape(8, 2, 7, 12, 6, 8),
				Block.makeCuboidShape(13, 9, 8, 14, 10, 9), Block.makeCuboidShape(13, 8, 7, 14, 9, 8),
				Block.makeCuboidShape(14, 8, 8, 16, 9, 9), Block.makeCuboidShape(13, 7, 8, 14, 8, 9),
				Block.makeCuboidShape(2, 12, 4.8, 2.5, 12.75, 5), Block.makeCuboidShape(3, 12, 4.8, 3.5, 12.75, 5),
				Block.makeCuboidShape(4, 11, 4.9, 4.75, 12, 5),
				Block.makeCuboidShape(4.225, 11.15, 4.75, 4.5249999999999995, 11.450000000000001, 4.85),
				Block.makeCuboidShape(4.225, 11.55, 4.75, 4.5249999999999995, 11.850000000000001, 4.85),
				Block.makeCuboidShape(5, 12, 4.8, 5.5, 12.3, 5), Block.makeCuboidShape(5, 12.6, 4.8, 5.5, 12.9, 5),
				Block.makeCuboidShape(5, 13.2, 4.8, 5.5, 13.5, 5),
				Block.makeCuboidShape(3.7, 13.2, 4.8, 4.6000000000000005, 14.1, 5)).reduce((v1, v2) -> {
					return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
				}).get());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
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

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return super.createTileEntity(state, world);
	}
}
