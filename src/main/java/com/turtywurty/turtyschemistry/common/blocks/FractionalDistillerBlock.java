package com.turtywurty.turtyschemistry.common.blocks;

import com.turtywurty.turtyschemistry.common.tileentity.FractionalDistillerTileEntity;
import com.turtywurty.turtyschemistry.core.init.StatsInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class FractionalDistillerBlock extends HorizontalBlock {

	public static final BooleanProperty PROCESSING = BooleanProperty.create("processing");

	public FractionalDistillerBlock(Properties builder) {
		super(builder);
		this.setDefaultState(
				this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(PROCESSING, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		return TileEntityTypeInit.FRACTIONAL_DISTILLER.get().create();
	}

	@SuppressWarnings("deprecation")
	public int getLightValue(BlockState state) {
		return state.get(PROCESSING) ? super.getLightValue(state) : 0;
	}

	@Override
	public void onReplaced(BlockState oldState, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (oldState.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof FractionalDistillerTileEntity) {
				final ItemStackHandler inventory = ((FractionalDistillerTileEntity) tileEntity).inventory;
				for (int slot = 0; slot < inventory.getSlots(); ++slot) {
					ItemEntity ie = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(),
							inventory.getStackInSlot(slot));
					worldIn.addEntity(ie);
				}
			}
		}
		
		if (oldState.hasTileEntity() && (oldState.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			worldIn.removeTileEntity(pos);
		}
	}

	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos,
			final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			final TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof FractionalDistillerTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (FractionalDistillerTileEntity) tileEntity, pos);
				player.addStat(StatsInit.INTERACT_WITH_FRACTIONAL_DISTILLER);
			}
		}
		return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof FractionalDistillerTileEntity) {
			return ItemHandlerHelper.calcRedstoneFromInventory(((FractionalDistillerTileEntity) tileEntity).inventory);
		}
		return super.getComparatorInputOverride(blockState, worldIn, pos);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(HORIZONTAL_FACING, PROCESSING);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
