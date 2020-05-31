package com.turtywurty.turtyschemistry.common.blocks;

import com.turtywurty.turtyschemistry.core.util.ConveyorState;
import com.turtywurty.turtyschemistry.core.util.ConveyorState.ConveyorShape;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public abstract class AbstractConveyorBlock extends Block {

	protected static final VoxelShape FLAT_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	protected static final VoxelShape ASCENDING_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

	public static boolean isConveyor(World worldIn, BlockPos pos) {
		return isConveyor(worldIn.getBlockState(pos));
	}

	public static boolean isConveyor(BlockState state) {
		return state.getBlock() instanceof AbstractConveyorBlock;
	}

	protected AbstractConveyorBlock(Block.Properties properties) {
		super(properties);
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		ConveyorShape conveyorshape = state.getBlock() == this ? getConveyorDirection(state, worldIn, pos) : null;
		return conveyorshape != null && conveyorshape.isAscending() ? ASCENDING_AABB : FLAT_AABB;
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return hasSolidSideOnTop(worldIn, pos.down());
	}

	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (oldState.getBlock() != state.getBlock()) {
			state = this.getUpdatedState(worldIn, pos, state, true);
			state.neighborChanged(worldIn, pos, this, pos, isMoving);

		}
	}

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (!worldIn.isRemote) {
			ConveyorShape conveyorshape = getConveyorDirection(state, worldIn, pos);
			boolean flag = false;
			BlockPos blockpos = pos.down();
			if (!hasSolidSideOnTop(worldIn, blockpos)) {
				flag = true;
			}

			BlockPos blockpos1 = pos.east();
			if (conveyorshape == ConveyorShape.ASCENDING_EAST && !hasSolidSideOnTop(worldIn, blockpos1)) {
				flag = true;
			} else {
				BlockPos blockpos2 = pos.west();
				if (conveyorshape == ConveyorShape.ASCENDING_WEST && !hasSolidSideOnTop(worldIn, blockpos2)) {
					flag = true;
				} else {
					BlockPos blockpos3 = pos.north();
					if (conveyorshape == ConveyorShape.ASCENDING_NORTH && !hasSolidSideOnTop(worldIn, blockpos3)) {
						flag = true;
					} else {
						BlockPos blockpos4 = pos.south();
						if (conveyorshape == ConveyorShape.ASCENDING_SOUTH && !hasSolidSideOnTop(worldIn, blockpos4)) {
							flag = true;
						}
					}
				}
			}

			if (flag && !worldIn.isAirBlock(pos)) {
				if (!isMoving) {
					spawnDrops(state, worldIn, pos);
				}

				worldIn.removeBlock(pos, isMoving);
			} else {
				this.updateState(state, worldIn, pos, blockIn);
			}

		}
	}

	protected void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
	}

	protected BlockState getUpdatedState(World worldIn, BlockPos pos, BlockState state, boolean placing) {
		if (worldIn.isRemote) {
			return state;
		} else {
			ConveyorShape conveyorshape = state.get(this.getShapeProperty());
			return (new ConveyorState(worldIn, pos, state))
					.getState(worldIn.isBlockPowered(pos), placing, conveyorshape).getNewState();
		}
	}

	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.NORMAL;
	}

	@SuppressWarnings("deprecation")
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!isMoving) {
			super.onReplaced(state, worldIn, pos, newState, isMoving);
			if (getConveyorDirection(state, worldIn, pos).isAscending()) {
				worldIn.notifyNeighborsOfStateChange(pos.up(), this);
			}
			worldIn.notifyNeighborsOfStateChange(pos, this);
			worldIn.notifyNeighborsOfStateChange(pos.down(), this);
		}
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockstate = super.getDefaultState();
		Direction direction = context.getPlacementHorizontalFacing();
		boolean flag = direction == Direction.EAST || direction == Direction.WEST;
		return blockstate.with(this.getShapeProperty(), flag ? ConveyorShape.EAST_WEST : ConveyorShape.NORTH_SOUTH);
	}

	// Forge: Use getConveyorDirection(IBlockAccess, BlockPos, IBlockState,
	// EntityMinecart) for enhanced ability
	public abstract IProperty<ConveyorShape> getShapeProperty();

	public ConveyorShape getConveyorDirection(BlockState state, IBlockReader world, BlockPos pos) {
		return state.get(getShapeProperty());
	}

	public float getConveyorMaxSpeed(BlockState state, World world, BlockPos pos) {
		return 0.4f;
	}
}