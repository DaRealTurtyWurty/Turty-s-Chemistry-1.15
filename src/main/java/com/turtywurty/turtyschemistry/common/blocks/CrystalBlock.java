package com.turtywurty.turtyschemistry.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class CrystalBlock extends Block implements IBucketPickupHandler, ILiquidContainer {

	public static final BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");

	public CrystalBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(LAVALOGGED, Boolean.valueOf(false)));
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isSolid();
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return !state.get(LAVALOGGED) && fluidIn == Fluids.LAVA;
	}

	@Override
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
		if (!state.get(LAVALOGGED) && fluidStateIn.getFluid() == Fluids.LAVA) {
			if (!worldIn.isRemote()) {
				worldIn.setBlockState(pos, state.with(LAVALOGGED, Boolean.valueOf(true)), 3);
				worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(),
						fluidStateIn.getFluid().getTickRate(worldIn));
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
		if (state.get(LAVALOGGED)) {
			worldIn.setBlockState(pos, state.with(LAVALOGGED, Boolean.valueOf(false)), 3);
			return Fluids.LAVA;
		} else {
			return Fluids.EMPTY;
		}
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(LAVALOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.create(0.25D, 0, 0.25D, 0.75D, 0.4375D, 0.75D);
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		if (!worldIn.isRemote && state.get(LAVALOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickRate(worldIn));
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(LAVALOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.LAVA));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(LAVALOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.LAVA, Fluids.LAVA.getTickRate(worldIn));
		}

		if (!isValidPosition(stateIn, worldIn, currentPos)) {
			worldIn.setBlockState(currentPos, stateIn, Constants.BlockFlags.BLOCK_UPDATE);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IFluidState getFluidState(BlockState state) {
		return state.get(LAVALOGGED) ? Fluids.LAVA.getStillFluidState(false) : super.getFluidState(state);
	}
}
