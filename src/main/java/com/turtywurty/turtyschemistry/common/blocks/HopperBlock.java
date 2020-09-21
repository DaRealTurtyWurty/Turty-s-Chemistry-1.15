package com.turtywurty.turtyschemistry.common.blocks;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.common.tileentity.HopperTileEntity;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.util.HopperPart;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

public class HopperBlock extends Block {

	public static final EnumProperty<HopperPart> PARTS = EnumProperty.<HopperPart>create("part", HopperPart.class);

	private static final VoxelShape TOP = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(0, 0, 0, 16, 16, 2),
			Block.makeCuboidShape(0, 0, 2, 2, 16, 16), IBooleanFunction.OR);
	private static final VoxelShape BOTTOM = Stream
			.of(Block.makeCuboidShape(4, 5, 4, 16, 6, 6), Block.makeCuboidShape(14, 0, 14, 15, 1, 16),
					Block.makeCuboidShape(12, 1, 14, 14, 2, 16), Block.makeCuboidShape(10, 2, 12, 12, 3, 16),
					Block.makeCuboidShape(8, 3, 10, 10, 4, 16), Block.makeCuboidShape(6, 4, 8, 8, 5, 16),
					Block.makeCuboidShape(2, 6, 4, 4, 7, 16), Block.makeCuboidShape(0, 7, 2, 2, 16, 16),
					Block.makeCuboidShape(4, 5, 6, 6, 6, 16), Block.makeCuboidShape(15, 0, 14, 16, 1, 15),
					Block.makeCuboidShape(12, 1, 12, 16, 2, 14), Block.makeCuboidShape(10, 2, 10, 16, 3, 12),
					Block.makeCuboidShape(8, 3, 8, 16, 4, 10), Block.makeCuboidShape(6, 4, 6, 16, 5, 8),
					Block.makeCuboidShape(2, 6, 2, 16, 7, 4), Block.makeCuboidShape(0, 7, 0, 16, 16, 2))
			.reduce((v1, v2) -> {
				return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
			}).get();

	public HopperBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(PARTS);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(PARTS)) {
		case BACK_BOTTOM_LEFT:
			return calculateShape(Direction.WEST, BOTTOM);
		case BACK_BOTTOM_RIGHT:
			return calculateShape(Direction.SOUTH, BOTTOM);
		case BACK_TOP_LEFT:
			return calculateShape(Direction.WEST, TOP);
		case BACK_TOP_RIGHT:
			return calculateShape(Direction.SOUTH, TOP);
		case FRONT_BOTTOM_LEFT:
			return calculateShape(Direction.NORTH, BOTTOM);
		case FRONT_BOTTOM_RIGHT:
			return calculateShape(Direction.EAST, BOTTOM);
		case FRONT_TOP_LEFT:
			return calculateShape(Direction.NORTH, TOP);
		case FRONT_TOP_RIGHT:
			return calculateShape(Direction.EAST, TOP);
		default:
			return VoxelShapes.fullCube();
		}
	}

	protected static VoxelShape calculateShape(Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[] { shape, VoxelShapes.empty() };

		int times = (to.getHorizontalIndex() - Direction.NORTH.getHorizontalIndex() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1],
					VoxelShapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		return buffer[0];
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		checkValidity(worldIn, pos);
	}

	// TODO: Fix
	@SuppressWarnings("deprecation")
	private void checkValidity(World worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos);
		if (state.get(PARTS).equals(HopperPart.FRONT_BOTTOM_LEFT)) {
			int index = 0;

			for (int x = 0; x < 2; x++) {
				for (int z = 0; z < 2; z++) {
					for (int y = 0; y < 2; y++) {
						BlockPos offsetPos = pos.add(x, y, z);
						if (!offsetPos.equals(pos)) {
							if (!worldIn.getBlockState(offsetPos).isAir()) {
								worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(),
										Constants.BlockFlags.BLOCK_UPDATE);
								return;
							}
						}
					}
				}
			}

			for (int y = 0; y < 2; y++) {
				for (int z = 0; z < 2; z++) {
					for (int x = 0; x < 2; x++) {
						worldIn.setBlockState(pos.add(x, y, z), state.with(PARTS, HopperPart.values()[index]),
								Constants.BlockFlags.BLOCK_UPDATE);
						index++;
					}
				}
			}
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(PARTS).equals(HopperPart.FRONT_BOTTOM_LEFT) ? true : false;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (worldIn != null && !worldIn.isRemote) {
			if (state.get(PARTS).equals(HopperPart.FRONT_BOTTOM_LEFT)) {
				TileEntity tile = worldIn.getTileEntity(pos);
				if (tile instanceof HopperTileEntity) {
					NetworkHooks.openGui((ServerPlayerEntity) player,
							((INamedContainerProvider) ((HopperTileEntity) tile)), pos);
					return ActionResultType.SUCCESS;
				}
			} else {
				for (int width = 0; width < 2; width++) {
					for (int depth = 0; depth < 2; depth++) {
						for (int height = 0; height < 2; height++) {
							if (worldIn.getTileEntity(pos.add(-width, -height, -depth)) instanceof HopperTileEntity) {
								HopperTileEntity tile = (HopperTileEntity) worldIn
										.getTileEntity(pos.add(-width, -height, -depth));
								NetworkHooks.openGui((ServerPlayerEntity) player, tile,
										pos.add(-width, -height, -depth));
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
		return state.get(PARTS).equals(HopperPart.FRONT_BOTTOM_LEFT) ? TileEntityTypeInit.HOPPER.get().create() : null;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(PARTS, HopperPart.FRONT_BOTTOM_LEFT);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.get(PARTS).equals(HopperPart.FRONT_BOTTOM_LEFT) && state.hasTileEntity()
				&& newState.getBlock() != state.getBlock()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof HopperTileEntity) {
				HopperTileEntity hopper = (HopperTileEntity) tile;
				for (int index = 0; index < hopper.getInventory().getSlots(); index++) {
					if (!hopper.getItemInSlot(index).isEmpty()) {
						ItemEntity ie = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ());
						ie.setItem(hopper.getItemInSlot(index));
						worldIn.addEntity(ie);
					}
				}

				for (int x = 0; x < 2; x++) {
					for (int y = 0; y < 2; y++) {
						for (int z = 0; z < 2; z++) {
							if (worldIn.getBlockState(pos.add(x, y, z)).getBlock() instanceof HopperBlock) {
								worldIn.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
							}
						}
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
		if (!state.get(PARTS).equals(HopperPart.FRONT_BOTTOM_LEFT)) {
			worldIn.setBlockState(pos, state, Constants.BlockFlags.BLOCK_UPDATE);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
		return blockState.get(PARTS).equals(HopperPart.FRONT_BOTTOM_LEFT)
				? super.getBlockHardness(blockState, worldIn, pos)
				: -1f;
	}
}
