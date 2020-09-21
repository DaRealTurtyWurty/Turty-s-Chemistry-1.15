package com.turtywurty.turtyschemistry.common.blocks;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class GasifierBlock extends BaseHorizontalBlock {

	public static final BooleanProperty RUNNING = BooleanProperty.create("running");
	public static final EnumProperty<GasifierBlock.Half> HALF = EnumProperty.<GasifierBlock.Half>create("half",
			GasifierBlock.Half.class);

	public GasifierBlock(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(RUNNING, Boolean.valueOf(false))
				.with(HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(RUNNING, HALF);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote) {
			if (state.get(HALF).equals(Half.BOTTOM)) {
				worldIn.setBlockState(pos.up(),
						state.with(HALF, Half.TOP).with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)), 3);
				worldIn.notifyNeighbors(pos, Blocks.AIR);
				state.updateNeighbors(worldIn, pos, 3);
			} else if (state.get(HALF).equals(Half.TOP)) {
				worldIn.setBlockState(pos.up(),
						state.with(HALF, Half.BOTTOM).with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING)), 3);
				worldIn.notifyNeighbors(pos, Blocks.AIR);
				state.updateNeighbors(worldIn, pos, 3);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.up()).isAir()
				&& worldIn.getBlockState(pos.down()).isSolidSide(worldIn, pos.down(), Direction.UP);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			worldIn.removeTileEntity(pos);
		}

		if (worldIn.getBlockState(pos.up()).getBlock() instanceof GasifierBlock) {
			if (worldIn.getBlockState(pos.up()).get(GasifierBlock.HALF).equals(Half.TOP)) {
				worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 3);
			}
		} else if (worldIn.getBlockState(pos.down()).getBlock() instanceof GasifierBlock) {
			if (worldIn.getBlockState(pos.down()).get(GasifierBlock.HALF).equals(Half.BOTTOM)) {
				worldIn.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 3);
			}
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(HALF).equals(Half.BOTTOM) ? true : false;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return state.get(HALF).equals(Half.BOTTOM) ? TileEntityTypeInit.GASIFIER.get().create() : null;
	}

	public enum Half implements IStringSerializable {
		BOTTOM("bottom"), TOP("top");

		private final String name;

		private Half(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}
}
