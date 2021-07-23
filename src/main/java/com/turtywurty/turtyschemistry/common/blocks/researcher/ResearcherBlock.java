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

	public enum Processor implements IStringSerializable {
		BASIC("basic"), INTERMEDIATE("intermediate"), ADVANCED("advanced");

		private String name;

		Processor(final String nameIn) {
			this.name = nameIn;
		}

		@Override
		public String getString() {
			return this.name;
		}
	}

	public static final EnumProperty<Processor> PROCESSOR = EnumProperty.create("processor", Processor.class);

	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

	public ResearcherBlock(final Properties properties, final Processor processorIn) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(PROCESSOR, processorIn));
	}

	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		return TileEntityTypeInit.RESEARCHER.get().create();
	}

	@Override
	protected void fillStateContainer(final Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, PROCESSOR);
	}

	@Override
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean hasTileEntity(final BlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(final BlockState state, final Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos,
			final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
		if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof ResearcherTileEntity) {
			ResearcherTileEntity tile = (ResearcherTileEntity) worldIn.getTileEntity(pos);
			ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
			IContainerProvider provider = ResearcherContainer.getServerContainerProvider(tile, pos);
			INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider, tile.getDisplayName());
			NetworkHooks.openGui(serverPlayer, namedProvider, pos);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public BlockState rotate(final BlockState state, final Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
}
