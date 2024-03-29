package com.turtywurty.turtyschemistry.common.blocks.briquetting_press;

import java.util.EnumMap;
import java.util.Map;

import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BriquettingPressPart extends BaseHorizontalBlock {

	protected static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

	private VoxelShape shape;

	public BriquettingPressPart(Properties builder, VoxelShape shapeIn) {
		super(builder);
		this.shape = shapeIn;
		runCalculation(SHAPES, this.shape);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES.get(state.get(HORIZONTAL_FACING));
	}
}
