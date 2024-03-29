package com.turtywurty.turtyschemistry.common.blocks.briquetting_press;

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

public class BriquettingPressBlock extends BaseHorizontalBlock {

	private static final Optional<VoxelShape> SHAPE = Stream
			.of(Block.makeCuboidShape(0, 0, 2, 1, 1, 3), Block.makeCuboidShape(0, 0, 13, 1, 1, 14),
					Block.makeCuboidShape(0, 1, 2, 13, 2, 3), Block.makeCuboidShape(0, 1, 13, 13, 2, 14),
					Block.makeCuboidShape(1, 1, 7, 12, 2, 9), Block.makeCuboidShape(0, 1, 3, 1, 2, 13),
					Block.makeCuboidShape(12, 1, 3, 13, 2, 13), Block.makeCuboidShape(12, 0, 2, 13, 1, 3),
					Block.makeCuboidShape(12, 0, 13, 13, 1, 14), Block.makeCuboidShape(13, 8, 9, 14, 9, 10),
					Block.makeCuboidShape(2, 1, 3, 5, 2, 7), Block.makeCuboidShape(2, 2, 4, 5, 6, 8),
					Block.makeCuboidShape(1, 6, 4, 6, 10, 8), Block.makeCuboidShape(1, 10, 5, 6, 15, 8),
					Block.makeCuboidShape(0.9000000000000004, 15, 4.9, 6.1000000000000005, 15.1, 8.1),
					Block.makeCuboidShape(6.9, 7.000000000000002, 7, 7, 10.200000000000003, 10.2),
					Block.makeCuboidShape(0, 8, 8, 7, 9, 9), Block.makeCuboidShape(7, 2, 8, 13, 7, 13),
					Block.makeCuboidShape(7, 7, 4, 8, 13, 14), Block.makeCuboidShape(12, 7, 4, 13, 13, 14),
					Block.makeCuboidShape(8, 7, 4, 12, 13, 5), Block.makeCuboidShape(8, 7, 13, 12, 13, 14),
					Block.makeCuboidShape(8, 6, 4, 12, 7, 8), Block.makeCuboidShape(8, 2, 7, 12, 6, 8),
					Block.makeCuboidShape(13, 9, 8, 14, 10, 9), Block.makeCuboidShape(13, 8, 7, 14, 9, 8),
					Block.makeCuboidShape(14, 8, 8, 16, 9, 9), Block.makeCuboidShape(13, 7, 8, 14, 8, 9),
					Block.makeCuboidShape(2, 12, 4.8, 2.5, 12.75, 5), Block.makeCuboidShape(3, 12, 4.8, 3.5, 12.75, 5),
					Block.makeCuboidShape(4, 11, 4.9, 4.75, 12, 5),
					Block.makeCuboidShape(4.225, 11.15, 4.75, 4.5249999999999995, 11.450000000000001, 4.85),
					Block.makeCuboidShape(4.225, 11.55, 4.75, 4.5249999999999995, 11.850000000000001, 4.85),
					Block.makeCuboidShape(5, 12, 4.8, 5.5, 12.3, 5), Block.makeCuboidShape(5, 12.6, 4.8, 5.5, 12.9, 5),
					Block.makeCuboidShape(5, 13.2, 4.8, 5.5, 13.5, 5),
					Block.makeCuboidShape(3.7, 13.2, 4.8, 4.6000000000000005, 14.1, 5))
			.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR));
	
	protected static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

	public BriquettingPressBlock(Properties builder) {
		super(builder);
		if(SHAPE.isPresent())
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
		return TileEntityTypeInit.BRIQUETTING_PRESS.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof BriquettingPressTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (BriquettingPressTileEntity) tile, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		final TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof BriquettingPressTileEntity && !worldIn.isRemote) {
			InventoryHelper.dropItems(worldIn, pos, ((BriquettingPressTileEntity) tile).getItems());
		}

		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			worldIn.removeTileEntity(pos);
		}
	}
}
