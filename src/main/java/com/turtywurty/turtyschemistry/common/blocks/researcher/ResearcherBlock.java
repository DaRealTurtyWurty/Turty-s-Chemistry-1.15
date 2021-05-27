package com.turtywurty.turtyschemistry.common.blocks.researcher;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ResearcherBlock extends Block {

	public static final EnumProperty<Processor> PROCESSOR = EnumProperty.create("processor", Processor.class);
	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

	public ResearcherBlock(Properties properties, Processor processorIn) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(PROCESSOR, processorIn));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.RESEARCHER.get().create();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, PROCESSOR);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof ResearcherTileEntity) {
			ResearcherTileEntity tile = (ResearcherTileEntity) worldIn.getTileEntity(pos);
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			IContainerProvider provider = ResearcherContainer.getServerContainerProvider(tile, pos);
			INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider, tile.getDisplayName());
			NetworkHooks.openGui(serverPlayer, namedProvider, pos);
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.SUCCESS;
	}

	public enum Processor implements IStringSerializable {
		BASIC("basic"), INTERMEDIATE("intermediate"), ADVANCED("advanced");

		private String name;

		private Processor(String nameIn) {
			this.name = nameIn;
		}

		@Override
		public String getName() {
			return this.name;
		}
	}
}
