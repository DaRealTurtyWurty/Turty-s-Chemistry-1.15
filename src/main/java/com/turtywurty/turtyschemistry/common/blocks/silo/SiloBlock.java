package com.turtywurty.turtyschemistry.common.blocks.silo;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

public class SiloBlock extends Block {

	public static final EnumProperty<SiloPart> PARTS = EnumProperty.<SiloPart>create("part", SiloPart.class);
	private static final DirectionProperty ROTATION = DirectionProperty.create("rotation", Direction.Plane.HORIZONTAL);
	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 7);

	protected static final Map<Direction, VoxelShape> BOTTOM_SHAPES = new EnumMap<>(Direction.class),
			MIDDLE_SHAPES = new EnumMap<>(Direction.class), TOP_SHAPES = new EnumMap<>(Direction.class);

	public SiloBlock(Block.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(ROTATION, Direction.NORTH)
				.with(PARTS, SiloPart.FRONT_BOTTOM_LEFT).with(LEVEL, 0));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(PARTS, ROTATION, LEVEL);
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		checkValidity(worldIn, pos);
	}

	// TODO: Fix
	@SuppressWarnings("deprecation")
	private void checkValidity(World worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos);
		if (state.get(PARTS).equals(SiloPart.FRONT_BOTTOM_LEFT)) {
			int index = 0;

			for (int x = 0; x < 2; x++) {
				for (int z = 0; z < 2; z++) {
					for (int y = 0; y < 3; y++) {
						BlockPos offsetPos = pos.add(x, y, z);
						if (!offsetPos.equals(pos) && !worldIn.getBlockState(offsetPos).isAir()) {
							worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
							return;
						}
					}
				}
			}

			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 2; z++) {
					for (int x = 0; x < 2; x++) {
						worldIn.setBlockState(
								pos.add(x, y, z), state.with(PARTS, SiloPart.values()[index])
										.with(ROTATION, state.get(ROTATION)).with(LEVEL, state.get(LEVEL)),
								Constants.BlockFlags.BLOCK_UPDATE);
						index++;
					}
				}
			}
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (worldIn != null && !worldIn.isRemote) {
			if (state.get(PARTS).equals(SiloPart.FRONT_BOTTOM_LEFT)) {
				TileEntity tile = worldIn.getTileEntity(pos);
				if (tile instanceof SiloTileEntity) {
					ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
					IContainerProvider provider = SiloContainer.getServerContainerProvider((SiloTileEntity) tile, pos, 0);
					INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider,
							((SiloTileEntity) tile).getDisplayName());
					NetworkHooks.openGui(serverPlayer, namedProvider, pos);
					return ActionResultType.SUCCESS;
				}
			} else {
				for (int width = 0; width < 2; width++) {
					for (int depth = 0; depth < 2; depth++) {
						for (int height = 0; height < 3; height++) {
							if (worldIn.getTileEntity(pos.add(-width, -height, -depth)) instanceof SiloTileEntity) {
								SiloTileEntity tile = (SiloTileEntity) worldIn
										.getTileEntity(pos.add(-width, -height, -depth));
								ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
								IContainerProvider provider = SiloContainer.getServerContainerProvider(tile, pos, 0);
								INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider,
										tile.getDisplayName());
								NetworkHooks.openGui(serverPlayer, namedProvider, pos.add(-width, -height, -depth));
								return ActionResultType.SUCCESS;
							}
						}
					}
				}
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		if (state.get(PARTS).equals(SiloPart.FRONT_BOTTOM_LEFT)) {
			return TileEntityTypeInit.SILO.get().create();
		}
		return null;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(ROTATION, context.getPlacementHorizontalFacing().getOpposite())
				.with(PARTS, SiloPart.FRONT_BOTTOM_LEFT).with(LEVEL, Integer.valueOf(0));
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (worldIn.getTileEntity(pos) instanceof SiloTileEntity && state.getBlock() != newState.getBlock()) {
			SiloTileEntity tile = (SiloTileEntity) worldIn.getTileEntity(pos);

			for (int index = 0; index < tile.getInventory().getSlots(); index++) {
				if (!tile.getInventory().getStackInSlot(index).isEmpty()) {
					ItemEntity itemEntity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ());
					itemEntity.setItem(tile.getInventory().getStackInSlot(index));
					worldIn.addEntity(itemEntity);
				}
			}

			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			for (int width = 0; width < 2; width++) {
				for (int depth = 0; depth < 2; depth++) {
					for (int height = 0; height < 3; height++) {
						BlockPos offsetPos = pos.add(width, height, depth);
						worldIn.setBlockState(offsetPos, Blocks.AIR.getDefaultState());
					}
				}
			}
		}

		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			worldIn.removeTileEntity(pos);
		}
	}

	@Override
	public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
		super.onPlayerDestroy(worldIn, pos, state);
		if (!state.get(PARTS).equals(SiloPart.FRONT_BOTTOM_LEFT)) {
			worldIn.setBlockState(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
		return blockState.get(PARTS).equals(SiloPart.FRONT_BOTTOM_LEFT) ? super.getBlockHardness(blockState, worldIn, pos)
				: -1f;
	}
}
