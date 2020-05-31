package com.turtywurty.turtyschemistry.common.blocks;

import com.turtywurty.turtyschemistry.core.util.ConveyorState.ConveyorShape;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class PlainConveyorBlock extends AbstractConveyorBlock {

	public static final EnumProperty<ConveyorShape> CONVEYOR_SHAPE_STRAIGHT = EnumProperty.create("shape",
			ConveyorShape.class, (shape) -> {
				return shape != ConveyorShape.NORTH_EAST && shape != ConveyorShape.NORTH_WEST
						&& shape != ConveyorShape.SOUTH_EAST && shape != ConveyorShape.SOUTH_WEST;
			});

	public static final EnumProperty<ConveyorShape> CONVEYOR_SHAPE = EnumProperty.create("shape", ConveyorShape.class);

	public static final EnumProperty<ConveyorShape> SHAPE = CONVEYOR_SHAPE;

	public PlainConveyorBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(SHAPE, ConveyorShape.NORTH_SOUTH));
	}

	@Override
	public IProperty<ConveyorShape> getShapeProperty() {
		return SHAPE;
	}

	@SuppressWarnings("incomplete-switch")
	public BlockState rotate(BlockState state, Rotation rot) {
		switch (rot) {
		case CLOCKWISE_180:
			switch ((ConveyorShape) state.get(SHAPE)) {
			case ASCENDING_EAST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_WEST);
			case ASCENDING_WEST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_EAST);
			case ASCENDING_NORTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_SOUTH);
			case ASCENDING_SOUTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_NORTH);
			case SOUTH_EAST:
				return state.with(SHAPE, ConveyorShape.NORTH_WEST);
			case SOUTH_WEST:
				return state.with(SHAPE, ConveyorShape.NORTH_EAST);
			case NORTH_WEST:
				return state.with(SHAPE, ConveyorShape.SOUTH_EAST);
			case NORTH_EAST:
				return state.with(SHAPE, ConveyorShape.SOUTH_WEST);
			}
		case COUNTERCLOCKWISE_90:
			switch ((ConveyorShape) state.get(SHAPE)) {
			case ASCENDING_EAST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_NORTH);
			case ASCENDING_WEST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_SOUTH);
			case ASCENDING_NORTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_WEST);
			case ASCENDING_SOUTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_EAST);
			case SOUTH_EAST:
				return state.with(SHAPE, ConveyorShape.NORTH_EAST);
			case SOUTH_WEST:
				return state.with(SHAPE, ConveyorShape.SOUTH_EAST);
			case NORTH_WEST:
				return state.with(SHAPE, ConveyorShape.SOUTH_WEST);
			case NORTH_EAST:
				return state.with(SHAPE, ConveyorShape.NORTH_WEST);
			case NORTH_SOUTH:
				return state.with(SHAPE, ConveyorShape.EAST_WEST);
			case EAST_WEST:
				return state.with(SHAPE, ConveyorShape.NORTH_SOUTH);
			}
		case CLOCKWISE_90:
			switch ((ConveyorShape) state.get(SHAPE)) {
			case ASCENDING_EAST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_SOUTH);
			case ASCENDING_WEST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_NORTH);
			case ASCENDING_NORTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_EAST);
			case ASCENDING_SOUTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_WEST);
			case SOUTH_EAST:
				return state.with(SHAPE, ConveyorShape.SOUTH_WEST);
			case SOUTH_WEST:
				return state.with(SHAPE, ConveyorShape.NORTH_WEST);
			case NORTH_WEST:
				return state.with(SHAPE, ConveyorShape.NORTH_EAST);
			case NORTH_EAST:
				return state.with(SHAPE, ConveyorShape.SOUTH_EAST);
			case NORTH_SOUTH:
				return state.with(SHAPE, ConveyorShape.EAST_WEST);
			case EAST_WEST:
				return state.with(SHAPE, ConveyorShape.NORTH_SOUTH);
			}
		default:
			return state;
		}
	}

	@SuppressWarnings({ "deprecation", "incomplete-switch" })
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		ConveyorShape railshape = state.get(SHAPE);
		switch (mirrorIn) {
		case LEFT_RIGHT:
			switch (railshape) {
			case ASCENDING_NORTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_SOUTH);
			case ASCENDING_SOUTH:
				return state.with(SHAPE, ConveyorShape.ASCENDING_NORTH);
			case SOUTH_EAST:
				return state.with(SHAPE, ConveyorShape.NORTH_EAST);
			case SOUTH_WEST:
				return state.with(SHAPE, ConveyorShape.NORTH_WEST);
			case NORTH_WEST:
				return state.with(SHAPE, ConveyorShape.SOUTH_WEST);
			case NORTH_EAST:
				return state.with(SHAPE, ConveyorShape.SOUTH_EAST);
			default:
				return super.mirror(state, mirrorIn);
			}
		case FRONT_BACK:
			switch (railshape) {
			case ASCENDING_EAST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_WEST);
			case ASCENDING_WEST:
				return state.with(SHAPE, ConveyorShape.ASCENDING_EAST);
			case ASCENDING_NORTH:
			case ASCENDING_SOUTH:
			default:
				break;
			case SOUTH_EAST:
				return state.with(SHAPE, ConveyorShape.SOUTH_WEST);
			case SOUTH_WEST:
				return state.with(SHAPE, ConveyorShape.SOUTH_EAST);
			case NORTH_WEST:
				return state.with(SHAPE, ConveyorShape.NORTH_EAST);
			case NORTH_EAST:
				return state.with(SHAPE, ConveyorShape.NORTH_WEST);
			}
		}

		return super.mirror(state, mirrorIn);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SHAPE);
	}
}
