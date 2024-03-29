package com.turtywurty.turtyschemistry.common.blocks.gasifier;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class GasifierBlock extends Block {

	public static final EnumProperty<GasifierPart> PARTS = EnumProperty.create("part", GasifierPart.class);

	public GasifierBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(PARTS, GasifierPart.FUEL_FEEDER_BOTTOM));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(PARTS);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		/*
		 * return worldIn.getBlockState(pos.up()).canBeReplacedByLogs(worldIn, pos) &&
		 * worldIn.getBlockState(pos.down()).isSolid();
		 */ return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.GASIFIER.get().create();
	}

	public enum GasifierPart implements IStringSerializable {
		FUEL_FEEDER_BOTTOM("fuel_feeder_bottom"), FUEL_FEEDER_TOP("fuel_feeder_top"), GAS_COOLER("gas_cooler"),
		GAS_FILTER_SCRUBBER("gas_filter_scrubber"), GAS_TANK_BOTTOM("gas_tank_bottom"),
		GAS_TANK_MIDDLE("gas_tank_middle"), GAS_TANK_TOP("gas_tank_top"), GASIFIER_BOTTOM("gasifier_bottom"),
		GASIFIER_MIDDLE("gasifier_middle"), GASIFIER_TOP("gasifier_top"), TAR_REFORMER_BOTTOM("tar_reformer_bottom"),
		TAR_REFORMER_MIDDLE("tar_reformer_middle"), TAR_REFORMER_TOP("tar_reformer_top");

		private String name;

		private GasifierPart(String nameIn) {
			this.name = nameIn;
		}

		@Override
		public String getName() {
			return this.name;
		}

	}
}
