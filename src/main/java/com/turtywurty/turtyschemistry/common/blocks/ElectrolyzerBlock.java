package com.turtywurty.turtyschemistry.common.blocks;

import com.turtywurty.turtyschemistry.common.tileentity.ElectrolyzerTileEntity;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class ElectrolyzerBlock extends Block {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public ElectrolyzerBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.ELECTROLYZER.get().create();
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (worldIn.getTileEntity(pos) instanceof ElectrolyzerTileEntity) {
			ElectrolyzerTileEntity tile = (ElectrolyzerTileEntity) worldIn.getTileEntity(pos);
			for (int index = 0; index < tile.getInventory().getSlots(); index++) {
				ItemEntity ie = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
						tile.getItemInSlot(index));
				worldIn.addEntity(ie);
			}

			if (!worldIn.isRemote) {
				if (tile.getStoredOxygen() > 0 || tile.getStoredHydrogen() > 0) {
					((ServerWorld) worldIn).spawnParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D,
							pos.getZ() + 0.5D, 100, 0.5D, 0.5D, 0.5D, 0.5D);
				}

				if (tile.getStoredWater() > 0) {
					((ServerWorld) worldIn).spawnParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5D,
							pos.getY() + 0.5D, pos.getZ() + 0.5D, 100, 0.5D, 0.5D, 0.5D, 0.5D);
				}
			}
		}

		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			worldIn.removeTileEntity(pos);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof ElectrolyzerTileEntity) {
				ElectrolyzerTileEntity electrolyzer = (ElectrolyzerTileEntity) tile;
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) electrolyzer, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}
}
