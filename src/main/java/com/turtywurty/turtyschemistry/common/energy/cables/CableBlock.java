package com.turtywurty.turtyschemistry.common.energy.cables;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CableBlock extends SixWayBlock {

	public CableBlock(Block.Properties properties) {
		super(0.3125F, properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.CABLE.get().create();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
}
