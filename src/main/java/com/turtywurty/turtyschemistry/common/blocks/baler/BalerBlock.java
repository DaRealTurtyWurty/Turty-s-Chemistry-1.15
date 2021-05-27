package com.turtywurty.turtyschemistry.common.blocks.baler;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BalerBlock extends BaseHorizontalBlock {

	private static final Optional<VoxelShape> SHAPE = Stream
			.of(Block.makeCuboidShape(0, 0, 0, 16, 1, 16), Block.makeCuboidShape(4, 1, 0, 12, 1.1, 15),
					Block.makeCuboidShape(4, 8, 0, 12, 8.1, 9), Block.makeCuboidShape(0, 1, 0, 1, 7, 15),
					Block.makeCuboidShape(1, 1, 0, 4, 7, 7), Block.makeCuboidShape(12, 1, 0, 15, 7, 7),
					Block.makeCuboidShape(15, 1, 0, 16, 7, 15), Block.makeCuboidShape(0, 8, 4, 1, 14, 15),
					Block.makeCuboidShape(15, 8, 4, 16, 14, 15), Block.makeCuboidShape(0, 7, 9, 1, 8, 15),
					Block.makeCuboidShape(15, 7, 9, 16, 8, 15), Block.makeCuboidShape(0, 14, 3, 16, 15, 15),
					Block.makeCuboidShape(0, 12, 3, 16, 14, 4), Block.makeCuboidShape(0, 7, 0, 16, 8, 9),
					Block.makeCuboidShape(0, 8, 3, 3, 12, 4), Block.makeCuboidShape(13, 8, 3, 16, 12, 4),
					Block.makeCuboidShape(0, 1, 15, 16, 14, 16))
			.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR));
	protected static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

	public BalerBlock(Properties builder) {
		super(builder);
		if (SHAPE.isPresent())
			runCalculation(SHAPES, SHAPE.get());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES.get(state.get(HORIZONTAL_FACING));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.BALER.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			final TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof BalerTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (BalerTileEntity) tile, pos);
				return ActionResultType.SUCCESS;
			}

		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		final TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof BalerTileEntity && !worldIn.isRemote) {
			InventoryHelper.dropItems(worldIn, pos, ((BalerTileEntity) tile).getItems());
		}

		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			worldIn.removeTileEntity(pos);
		}
	}
}
