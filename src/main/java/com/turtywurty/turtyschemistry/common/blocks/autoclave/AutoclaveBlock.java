package com.turtywurty.turtyschemistry.common.blocks.autoclave;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AutoclaveBlock extends HorizontalBlock {

    public static final BooleanProperty PROCESSING = BooleanProperty.create("processing");

    public AutoclaveBlock(final Properties properties) {
        super(properties);
        setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH)
                .with(PROCESSING, false));
    }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return TileEntityTypeInit.AUTOCLAVE.get().create();
    }

    @Override
    public int getComparatorInputOverride(final BlockState blockState, final World worldIn,
            final BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    public int getLightValue(final BlockState state, final IBlockReader world, final BlockPos pos) {
        return state.get(PROCESSING) ? super.getLightValue(state, world, pos) : 0;
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return getDefaultState().with(HORIZONTAL_FACING,
                context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
    }

    @Override
    public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos,
            final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            final TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof AutoclaveTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (AutoclaveTileEntity) tileEntity, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(final BlockState state, final World worldIn, final BlockPos pos,
            final BlockState newState, final boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof AutoclaveTileEntity) {
                final AutoclaveTileEntity tile = (AutoclaveTileEntity) worldIn.getTileEntity(pos);
                for (int index = 0; index < tile.getInventory().getSlots(); index++) {
                    final ItemEntity ie = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                            tile.getItemInSlot(index));
                    worldIn.addEntity(ie);
                }
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            if (state.hasTileEntity()
                    && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
                worldIn.removeTileEntity(pos);
            }
        }
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(HORIZONTAL_FACING, PROCESSING);
    }
}
