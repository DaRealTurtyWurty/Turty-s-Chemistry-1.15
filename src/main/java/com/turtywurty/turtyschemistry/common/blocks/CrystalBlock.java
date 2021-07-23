package com.turtywurty.turtyschemistry.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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

	public CrystalBlock(final Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(LAVALOGGED, false));
	}

	@Override
	public boolean canContainFluid(final IBlockReader worldIn, final BlockPos pos, final BlockState state,
			final Fluid fluidIn) {
		return !state.get(LAVALOGGED) && fluidIn == Fluids.LAVA;
	}

	@Override
	protected void fillStateContainer(final Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(LAVALOGGED);
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(final BlockState state) {
		return state.get(LAVALOGGED) ? Fluids.LAVA.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos,
			final ISelectionContext context) {
		return VoxelShapes.create(0.25D, 0, 0.25D, 0.75D, 0.4375D, 0.75D);
	}

	@Override
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		return getDefaultState().with(LAVALOGGED, fluidstate.getFluid() == Fluids.LAVA);
	}

	@Override
	public boolean isValidPosition(final BlockState state, final IWorldReader worldIn, final BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isSolid();
	}

	@Override
	public void neighborChanged(final BlockState state, final World worldIn, final BlockPos pos, final Block blockIn,
			final BlockPos fromPos, final boolean isMoving) {
		if (!worldIn.isRemote && state.get(LAVALOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickRate(worldIn));
		}
	}

	@Override
	public Fluid pickupFluid(final IWorld worldIn, final BlockPos pos, final BlockState state) {
		if (state.get(LAVALOGGED)) {
			worldIn.setBlockState(pos, state.with(LAVALOGGED, false), 3);
			return Fluids.LAVA;
		}
		return Fluids.EMPTY;
	}

	@Override
	public boolean receiveFluid(final IWorld worldIn, final BlockPos pos, final BlockState state,
			final FluidState fluidStateIn) {
		if (state.get(LAVALOGGED) || fluidStateIn.getFluid() != Fluids.LAVA)
			return false;
		if (!worldIn.isRemote()) {
			worldIn.setBlockState(pos, state.with(LAVALOGGED, true), 3);
			worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(),
					fluidStateIn.getFluid().getTickRate(worldIn));
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(final BlockState stateIn, final Direction facing,
			final BlockState facingState, final IWorld worldIn, final BlockPos currentPos, final BlockPos facingPos) {
		if (stateIn.get(LAVALOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.LAVA, Fluids.LAVA.getTickRate(worldIn));
		}

		if (!isValidPosition(stateIn, worldIn, currentPos)) {
			worldIn.setBlockState(currentPos, stateIn, Constants.BlockFlags.BLOCK_UPDATE);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}
