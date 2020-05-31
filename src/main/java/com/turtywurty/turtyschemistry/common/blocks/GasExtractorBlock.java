package com.turtywurty.turtyschemistry.common.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.common.tileentity.GasExtractorTileEntity;
import com.turtywurty.turtyschemistry.core.init.StatsInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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

public class GasExtractorBlock extends BaseHorizontalBlock {

	protected static final Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();

	public GasExtractorBlock(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
		runCalculation(Stream
				.of(Block.makeCuboidShape(13.5, 0, 1, 14.5, 1, 15), Block.makeCuboidShape(0, 0, 0, 1, 2, 16),
						Block.makeCuboidShape(1, 2, 1, 15, 3, 15), Block.makeCuboidShape(1, 15, 1, 15, 16, 15),
						Block.makeCuboidShape(2, 3, 2, 14, 6, 14), Block.makeCuboidShape(2, 12, 2, 14, 15, 14),
						Block.makeCuboidShape(3, 6, 3, 13, 12, 13), Block.makeCuboidShape(1, 0, 15, 15, 2, 16),
						Block.makeCuboidShape(15, 0, 0, 16, 2, 16), Block.makeCuboidShape(1, 0, 0, 15, 2, 1),
						Block.makeCuboidShape(1.5, 0, 1, 2.5, 1, 15), Block.makeCuboidShape(3.5, 0, 1, 4.5, 1, 15),
						Block.makeCuboidShape(5.5, 0, 1, 6.5, 1, 15), Block.makeCuboidShape(7.4, 0, 1, 8.4, 1, 15),
						Block.makeCuboidShape(9.5, 0, 1, 10.5, 1, 15), Block.makeCuboidShape(11.5, 0, 1, 12.5, 1, 15))
				.reduce((v1, v2) -> {
					return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
				}).get());
	}

	protected static void calculateShapes(Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[] { shape, VoxelShapes.empty() };

		int times = (to.getHorizontalIndex() - Direction.NORTH.getHorizontalIndex() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1],
					VoxelShapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		SHAPES.put(to, buffer[0]);
	}

	protected void runCalculation(VoxelShape shape) {
		for (Direction direction : Direction.values()) {
			calculateShapes(direction, shape);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES.get(state.get(HORIZONTAL_FACING));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.GAS_EXTRACTOR.get().create();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos,
			final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof GasExtractorTileEntity) {
			if (!worldIn.isRemote) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (GasExtractorTileEntity) tileentity, pos);
				player.addStat(StatsInit.INTERACT_WITH_FRACTIONAL_DISTILLER);
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.SUCCESS;
	}
}
