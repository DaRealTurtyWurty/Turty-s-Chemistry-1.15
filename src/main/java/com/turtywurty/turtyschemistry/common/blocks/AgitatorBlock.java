package com.turtywurty.turtyschemistry.common.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.common.container.AgitatorContainer;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
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

public class AgitatorBlock extends BaseHorizontalBlock {

	protected static final Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();

	public AgitatorBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
		runCalculation(Stream.of(Block.makeCuboidShape(7.5, 10, 8.5, 8.5, 11, 13),
				Block.makeCuboidShape(4, 0, 0, 12, 1, 1), Block.makeCuboidShape(4, 0, 15, 12, 1, 16),
				Block.makeCuboidShape(0, 0, 4, 16, 1, 12), Block.makeCuboidShape(3, 0, 14, 13, 1, 15),
				Block.makeCuboidShape(1, 0, 3, 15, 1, 4), Block.makeCuboidShape(1, 0, 12, 15, 1, 13),
				Block.makeCuboidShape(3, 0, 1, 13, 1, 2), Block.makeCuboidShape(2, 0, 13, 14, 1, 14),
				Block.makeCuboidShape(2, 0, 2, 14, 1, 3), Block.makeCuboidShape(4, 1, 0, 12, 4, 1),
				Block.makeCuboidShape(0, 1, 4, 1, 4, 12), Block.makeCuboidShape(4, 1, 15, 12, 4, 16),
				Block.makeCuboidShape(15, 1, 4, 16, 4, 12), Block.makeCuboidShape(4, 4, 0, 12, 10, 1),
				Block.makeCuboidShape(0, 4, 4, 1, 10, 12), Block.makeCuboidShape(4, 4, 15, 12, 10, 16),
				Block.makeCuboidShape(15, 4, 4, 16, 10, 12), Block.makeCuboidShape(4, 10, 0, 12, 13, 1),
				Block.makeCuboidShape(0, 10, 4, 1, 13, 12), Block.makeCuboidShape(4, 10, 15, 12, 13, 16),
				Block.makeCuboidShape(15, 10, 4, 16, 13, 12), Block.makeCuboidShape(1, 13, 4, 2, 15, 12),
				Block.makeCuboidShape(4, 13, 1, 12, 15, 2), Block.makeCuboidShape(14, 13, 4, 15, 15, 12),
				Block.makeCuboidShape(4, 13, 14, 12, 15, 15), Block.makeCuboidShape(13, 1, 2, 14, 13, 3),
				Block.makeCuboidShape(12, 1, 1, 13, 13, 2), Block.makeCuboidShape(14, 1, 3, 15, 13, 4),
				Block.makeCuboidShape(2, 1, 2, 3, 13, 3), Block.makeCuboidShape(3, 1, 1, 4, 13, 2),
				Block.makeCuboidShape(1, 1, 3, 2, 13, 4), Block.makeCuboidShape(1, 1, 12, 2, 4, 13),
				Block.makeCuboidShape(12, 1, 14, 13, 4, 15), Block.makeCuboidShape(1, 10, 12, 2, 13, 13),
				Block.makeCuboidShape(12, 10, 14, 13, 13, 15), Block.makeCuboidShape(1, 4, 12, 2, 10, 13),
				Block.makeCuboidShape(12, 4, 14, 13, 10, 15), Block.makeCuboidShape(2, 13, 12, 3, 15, 13),
				Block.makeCuboidShape(12, 13, 13, 13, 15, 14), Block.makeCuboidShape(12, 13, 2, 13, 15, 3),
				Block.makeCuboidShape(2, 13, 3, 3, 15, 4), Block.makeCuboidShape(2, 1, 13, 3, 4, 14),
				Block.makeCuboidShape(13, 1, 13, 14, 4, 14), Block.makeCuboidShape(2, 10, 13, 3, 13, 14),
				Block.makeCuboidShape(13, 10, 13, 14, 13, 14), Block.makeCuboidShape(2, 4, 13, 3, 10, 14),
				Block.makeCuboidShape(13, 4, 13, 14, 10, 14), Block.makeCuboidShape(3, 13, 13, 4, 15, 14),
				Block.makeCuboidShape(13, 13, 12, 14, 15, 13), Block.makeCuboidShape(13, 13, 3, 14, 15, 4),
				Block.makeCuboidShape(3, 13, 2, 4, 15, 3), Block.makeCuboidShape(3, 1, 14, 4, 4, 15),
				Block.makeCuboidShape(14, 1, 12, 15, 4, 13), Block.makeCuboidShape(3, 10, 14, 4, 13, 15),
				Block.makeCuboidShape(14, 10, 12, 15, 13, 13), Block.makeCuboidShape(3, 4, 14, 4, 10, 15),
				Block.makeCuboidShape(14, 4, 12, 15, 10, 13), Block.makeCuboidShape(2, 15, 4, 14, 16, 12),
				Block.makeCuboidShape(3, 15, 3, 13, 16, 4), Block.makeCuboidShape(3, 15, 12, 13, 16, 13),
				Block.makeCuboidShape(4, 15, 13, 12, 16, 14), Block.makeCuboidShape(4, 15, 2, 12, 16, 3),
				Block.makeCuboidShape(7.5, 2, 7.5, 8.5, 15, 8.5), Block.makeCuboidShape(7.5, 2, 8.5, 8.5, 3, 13),
				Block.makeCuboidShape(7.5, 2, 3, 8.5, 3, 7.5), Block.makeCuboidShape(3, 2, 7.5, 7.5, 3, 8.5),
				Block.makeCuboidShape(8.5, 2, 7.5, 13, 3, 8.5), Block.makeCuboidShape(8.5, 6, 7.5, 13, 7, 8.5),
				Block.makeCuboidShape(7.5, 6, 3, 8.5, 7, 7.5), Block.makeCuboidShape(3, 6, 7.5, 7.5, 7, 8.5),
				Block.makeCuboidShape(7.5, 6, 8.5, 8.5, 7, 13), Block.makeCuboidShape(8.5, 10, 7.5, 13, 11, 8.5),
				Block.makeCuboidShape(7.5, 10, 3, 8.5, 11, 7.5), Block.makeCuboidShape(3, 10, 7.5, 7.5, 11, 8.5))
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
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.AGITATOR.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos,
			final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
		if (worldIn.getTileEntity(pos) instanceof AgitatorTileEntity && !worldIn.isRemote) {
			AgitatorTileEntity tile = (AgitatorTileEntity) worldIn.getTileEntity(pos);
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			IContainerProvider provider = AgitatorContainer.getServerContainerProvider((AgitatorTileEntity) tile, pos,
					tile.getAgitatorType());
			INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider,
					((AgitatorTileEntity) tile).getDisplayName());
			NetworkHooks.openGui(serverPlayer, namedProvider, pos);
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.SUCCESS;
	}
}
