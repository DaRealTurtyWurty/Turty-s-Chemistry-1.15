package com.turtywurty.turtyschemistry.common.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.common.container.BoilerContainer;
import com.turtywurty.turtyschemistry.common.tileentity.BoilerTileEntity;
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

public class BoilerBlock extends BaseHorizontalBlock {

	protected static final Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();

	public BoilerBlock(Properties builder) {
		super(builder);
		runCalculation(Stream
				.of(Block.makeCuboidShape(14, 11, 5, 15, 12, 7), Block.makeCuboidShape(1, 0, 2, 15, 1, 4),
						Block.makeCuboidShape(1, 0, 13, 15, 1, 15), Block.makeCuboidShape(2, 1, 3, 3, 3, 5),
						Block.makeCuboidShape(13, 1, 3, 14, 3, 5), Block.makeCuboidShape(2, 1, 12, 3, 3, 14),
						Block.makeCuboidShape(13, 1, 12, 14, 3, 14), Block.makeCuboidShape(0, 3, 7, 1, 12, 10),
						Block.makeCuboidShape(0, 6, 10, 1, 9, 13), Block.makeCuboidShape(0, 4, 10, 1, 6, 12),
						Block.makeCuboidShape(0, 9, 10, 1, 11, 12), Block.makeCuboidShape(0, 4, 5, 1, 6, 7),
						Block.makeCuboidShape(0, 9, 5, 1, 11, 7), Block.makeCuboidShape(0, 6, 4, 1, 9, 7),
						Block.makeCuboidShape(15, 6, 4, 16, 9, 7), Block.makeCuboidShape(15, 9, 5, 16, 11, 7),
						Block.makeCuboidShape(15, 3, 7, 16, 12, 10), Block.makeCuboidShape(15, 9, 10, 16, 11, 12),
						Block.makeCuboidShape(15, 6, 10, 16, 9, 13), Block.makeCuboidShape(15, 4, 10, 16, 6, 12),
						Block.makeCuboidShape(15, 4, 5, 16, 6, 7), Block.makeCuboidShape(1, 6, 13, 2, 9, 14),
						Block.makeCuboidShape(2, 6, 14, 14, 9, 15), Block.makeCuboidShape(14, 6, 13, 15, 9, 14),
						Block.makeCuboidShape(14, 9, 12, 15, 11, 13), Block.makeCuboidShape(2, 9, 13, 14, 11, 14),
						Block.makeCuboidShape(1, 9, 12, 2, 11, 13), Block.makeCuboidShape(1, 11, 10, 2, 12, 12),
						Block.makeCuboidShape(2, 11, 12, 14, 12, 13), Block.makeCuboidShape(14, 11, 10, 15, 12, 12),
						Block.makeCuboidShape(14, 12, 7, 15, 13, 10), Block.makeCuboidShape(2, 12, 10, 14, 13, 12),
						Block.makeCuboidShape(1, 12, 7, 2, 13, 10), Block.makeCuboidShape(2, 13, 7, 14, 14, 10),
						Block.makeCuboidShape(14, 4, 12, 15, 6, 13), Block.makeCuboidShape(2, 4, 13, 14, 6, 14),
						Block.makeCuboidShape(1, 4, 12, 2, 6, 13), Block.makeCuboidShape(1, 3, 10, 2, 4, 12),
						Block.makeCuboidShape(2, 3, 12, 14, 4, 13), Block.makeCuboidShape(14, 3, 10, 15, 4, 12),
						Block.makeCuboidShape(14, 2, 7, 15, 3, 10), Block.makeCuboidShape(2, 2, 10, 14, 3, 12),
						Block.makeCuboidShape(2, 1, 7, 14, 2, 10), Block.makeCuboidShape(1, 2, 7, 2, 3, 10),
						Block.makeCuboidShape(2, 2, 5, 14, 3, 7), Block.makeCuboidShape(1, 3, 5, 2, 4, 7),
						Block.makeCuboidShape(1, 4, 4, 2, 6, 5), Block.makeCuboidShape(1, 6, 3, 2, 9, 4),
						Block.makeCuboidShape(1, 9, 4, 2, 11, 5), Block.makeCuboidShape(1, 11, 5, 2, 12, 7),
						Block.makeCuboidShape(2, 12, 5, 14, 13, 7), Block.makeCuboidShape(2, 11, 4, 14, 12, 5),
						Block.makeCuboidShape(2, 9, 3, 14, 11, 4), Block.makeCuboidShape(2, 6, 2, 14, 9, 3),
						Block.makeCuboidShape(2, 4, 3, 14, 6, 4), Block.makeCuboidShape(2, 3, 4, 14, 4, 5),
						Block.makeCuboidShape(14, 3, 5, 15, 4, 7), Block.makeCuboidShape(14, 4, 4, 15, 6, 5),
						Block.makeCuboidShape(14, 6, 3, 15, 9, 4), Block.makeCuboidShape(14, 9, 4, 15, 11, 5))
				.reduce((v1, v2) -> {
					return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);
				}).get());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
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
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.BOILER.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof BoilerTileEntity) {
			BoilerTileEntity tile = (BoilerTileEntity) worldIn.getTileEntity(pos);
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			IContainerProvider provider = BoilerContainer.getServerContainerProvider(tile, pos);
			INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider, tile.getDisplayName());
			NetworkHooks.openGui(serverPlayer, namedProvider, pos);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}
