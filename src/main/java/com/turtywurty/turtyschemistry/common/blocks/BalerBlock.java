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

public class BalerBlock extends BaseHorizontalBlock {

	protected static final Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();

	public BalerBlock(Properties builder) {
		super(builder);
		runCalculation(Stream.of(Block.makeCuboidShape(0, 0, 0, 16, 1, 16), Block.makeCuboidShape(4, 1, 0, 12, 1.1, 15),
				Block.makeCuboidShape(4, 8, 0, 12, 8.1, 9), Block.makeCuboidShape(0, 1, 0, 1, 7, 15),
				Block.makeCuboidShape(1, 1, 0, 4, 7, 7), Block.makeCuboidShape(12, 1, 0, 15, 7, 7),
				Block.makeCuboidShape(15, 1, 0, 16, 7, 15), Block.makeCuboidShape(0, 8, 4, 1, 14, 15),
				Block.makeCuboidShape(15, 8, 4, 16, 14, 15), Block.makeCuboidShape(0, 7, 9, 1, 8, 15),
				Block.makeCuboidShape(15, 7, 9, 16, 8, 15), Block.makeCuboidShape(0, 14, 3, 16, 15, 15),
				Block.makeCuboidShape(7, 11, 10, 9, 14, 12), Block.makeCuboidShape(1, 10, 9, 15, 11, 15),
				Block.makeCuboidShape(0, 12, 3, 16, 14, 4), Block.makeCuboidShape(0, 7, 0, 16, 8, 9),
				Block.makeCuboidShape(0, 8, 3, 3, 12, 4), Block.makeCuboidShape(13, 8, 3, 16, 12, 4),
				Block.makeCuboidShape(0, 1, 15, 16, 14, 16)).reduce((v1, v2) -> {
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
