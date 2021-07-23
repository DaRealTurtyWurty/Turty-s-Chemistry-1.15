package com.turtywurty.turtyschemistry.common.blocks;

import com.turtywurty.turtyschemistry.core.init.ItemInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class GreenAlgaeBlock extends BushBlock {

	protected static final VoxelShape GREEB_ALGAE_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 1.5D, 15.0D);

	public GreenAlgaeBlock(final AbstractBlock.Properties builder) {
		super(builder);
	}

	@Override
	public VoxelShape getCollisionShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos,
			final ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public ItemStack getItem(final IBlockReader worldIn, final BlockPos pos, final BlockState state) {
		return new ItemStack(ItemInit.GREEN_ALGAE.get());
	}

	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos,
			final ISelectionContext context) {
		return GREEB_ALGAE_SHAPE;
	}

	@Override
	protected boolean isValidGround(final BlockState state, final IBlockReader worldIn, final BlockPos pos) {
		FluidState fluidstate = worldIn.getFluidState(pos);
		return fluidstate.getFluid() == Fluids.WATER;
	}

	@Override
	public boolean isValidPosition(final BlockState state, final IWorldReader worldIn, final BlockPos pos) {
		FluidState fluidstate = worldIn.getFluidState(pos.down());
		return fluidstate.getFluid() == Fluids.WATER;
	}
}
