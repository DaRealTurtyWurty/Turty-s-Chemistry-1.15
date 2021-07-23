package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import java.util.Random;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
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

import net.minecraft.block.AbstractBlock.Properties;

public class ElectrolyzerBlock extends Block {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public ElectrolyzerBlock(final Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		return TileEntityTypeInit.ELECTROLYZER.get().create();
	}

	@Override
	protected void fillStateContainer(final Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasTileEntity(final BlockState state) {
		return true;
	}

	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos,
			final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
		if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof ElectrolyzerTileEntity) {
			ElectrolyzerTileEntity tile = (ElectrolyzerTileEntity) worldIn.getTileEntity(pos);
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			IContainerProvider provider = ElectrolyzerContainer.getServerContainerProvider(tile, pos);
			INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider, tile.getDisplayName());
			NetworkHooks.openGui(serverPlayer, namedProvider, pos);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onReplaced(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState,
			final boolean isMoving) {
		if (worldIn.getTileEntity(pos) instanceof ElectrolyzerTileEntity) {
			ElectrolyzerTileEntity tile = (ElectrolyzerTileEntity) worldIn.getTileEntity(pos);
			for (int index = 0; index < tile.getInventory().getSlots(); index++) {
				ItemEntity ie = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
						tile.getItemInSlot(index));
				worldIn.addEntity(ie);
			}

			if (!worldIn.isRemote && !tile.fluidInventory.isEmpty()) {
				((ServerWorld) worldIn).spawnParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D,
						pos.getZ() + 0.5D, 100, 0.5D, 0.5D, 0.5D, 0.5D);
			}
		}

		if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
			worldIn.removeTileEntity(pos);
		}
	}

	@Override
	public void randomTick(final BlockState state, final ServerWorld worldIn, final BlockPos pos, final Random random) {
		if (worldIn.getTileEntity(pos) instanceof ElectrolyzerTileEntity
				&& ((ElectrolyzerTileEntity) worldIn.getTileEntity(pos)).converting) {
			worldIn.spawnParticle(ParticleTypes.POOF, pos.getX(), pos.getY(), pos.getZ(), 20,
					random.nextDouble() - 0.5D, 0.0D, random.nextDouble() - 0.5D, 0.01D);
		}
	}
}