package com.turtywurty.turtyschemistry.common.blocks.conveyor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtensibleEnum;

public class ConveyorBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty SLOPE = BooleanProperty.create("slope");
    public static final EnumProperty<CornerDirection> LEFT_RIGHT = EnumProperty.create("left_right",
            CornerDirection.class);

    public ConveyorBlock(final Properties properties) {
        super(properties);
        setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(SLOPE, false)
                .with(LEFT_RIGHT, CornerDirection.NONE));
    }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        // TODO: Create TileEntity
        return super.createTileEntity(state, world);
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
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final BlockState state,
            final LivingEntity placer, final ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onReplaced(final BlockState state, final World worldIn, final BlockPos pos,
            final BlockState newState, final boolean isMoving) {
        if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(final BlockState stateIn, final Direction facing,
            final BlockState facingState, final IWorld worldIn, final BlockPos currentPos,
            final BlockPos facingPos) {
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    protected void fillStateContainer(final Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, SLOPE, LEFT_RIGHT);
    }

    public enum CornerDirection implements IStringSerializable, IExtensibleEnum {

        LEFT("left"), RIGHT("right"), NONE("none");

        private String name;

        CornerDirection(final String nameIn) {
            this.name = nameIn;
        }

        public static CornerDirection create(final String name) {
            throw new IllegalStateException("Enum not extended: " + name);
        }

        @Override
        public String getString() {
            return this.name;
        }
    }
}
