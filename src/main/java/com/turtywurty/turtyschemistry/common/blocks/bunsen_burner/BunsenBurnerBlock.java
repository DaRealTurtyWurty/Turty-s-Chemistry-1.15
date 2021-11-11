package com.turtywurty.turtyschemistry.common.blocks.bunsen_burner;

import java.awt.Color;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.common.recipes.BunsenBurnerRecipe;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;

public class BunsenBurnerBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty STRENGTH = IntegerProperty.create("strength", 0, 8);
    public static final BooleanProperty HAS_GAS = BooleanProperty.create("has_gas");
    public static final BooleanProperty FRAME = BooleanProperty.create("frame");
    public static final BooleanProperty MESH = BooleanProperty.create("mesh");
    public static final BooleanProperty CRUCIBLE = BooleanProperty.create("crucible");

    private static final Optional<VoxelShape> SHAPE = Stream
            .of(Block.makeCuboidShape(7.5, 3, 7, 8.5, 4, 7), Block.makeCuboidShape(5, 0, 5, 11, 1, 11),
                    Block.makeCuboidShape(6, 1, 6, 10, 2, 10), Block.makeCuboidShape(7, 4, 7, 9, 9, 9),
                    Block.makeCuboidShape(7, 2, 7, 9, 3, 9), Block.makeCuboidShape(7, 3, 8, 9, 4, 9),
                    Block.makeCuboidShape(7, 3, 7, 7.5, 4, 8), Block.makeCuboidShape(8.5, 3, 7, 9, 4, 8),
                    Block.makeCuboidShape(7.625, 2.2, 9, 8.375, 2.95, 9.75),
                    Block.makeCuboidShape(7, 9, 7, 7.25, 9.75, 9),
                    Block.makeCuboidShape(7.25, 9, 8.75, 8.75, 9.75, 9),
                    Block.makeCuboidShape(7.25, 9, 7, 8.75, 9.75, 7.25),
                    Block.makeCuboidShape(7.75, 9, 7.75, 8.25, 9.25, 8.25),
                    Block.makeCuboidShape(8.75, 9, 7, 9, 9.75, 9))
            .reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR));

    public BunsenBurnerBlock(final Properties properties) {
        super(properties);
        setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(STRENGTH, 0)
                .with(HAS_GAS, false).with(FRAME, false).with(MESH, false).with(CRUCIBLE, false));
    }

    public void addFireParticles(final World world, final BlockPos pos, final int amount, final Color rgba,
            final double strength) {
        for (int particle = 0; particle < amount; particle++) {
            /*
             * world.addParticle( new FireParticle.FireColourData((float) rgba.getRed() /
             * 255, (float) rgba.getGreen() / 255, (float) rgba.getBlue() / 255, (float)
             * rgba.getAlpha() / 255), pos.getX() + 0.5D, pos.getY() + 0.7D, pos.getZ() +
             * 0.5D, 0.0D, strength, 0.0D);
             */
        }
    }

    @Override
    public void animateTick(final BlockState stateIn, final World worldIn, final BlockPos pos,
            final Random rand) {
        if (!stateIn.get(HAS_GAS)) {
            switch (stateIn.get(STRENGTH)) {
            case 1:
                // yellow
                addFireParticles(worldIn, pos, 20, new Color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(),
                        Color.ORANGE.getBlue(), 250), 0.075D);
                break;
            case 2:
                // yellow
                addFireParticles(worldIn, pos, 15, new Color(Color.YELLOW.getRed(),
                        Color.YELLOW.getGreen() - 15, Color.YELLOW.getBlue(), 80), 0.05);
                break;
            case 3:
                // yellow
                addFireParticles(worldIn, pos, 10,
                        new Color(Color.YELLOW.getRed(), Color.YELLOW.getGreen(), Color.YELLOW.getBlue(), 30),
                        0.003D);
                break;
            case 4:
                // yellow
                addFireParticles(worldIn, pos, 6, new Color(130, 130, 50, 10), 0.003D);
                break;
            case 5:
                // blue
                addFireParticles(worldIn, pos, 10, new Color(70, 70, 140, 50), 0.003D);
                break;
            case 6:
                // blue
                addFireParticles(worldIn, pos, 12, new Color(50, 50, 150, 70), 0.005D);
                break;
            case 7:
                // blue
                addFireParticles(worldIn, pos, 12, new Color(Color.MAGENTA.getRed() - 200,
                        Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue() - 50, 80), 0.007D);
                break;
            case 8:
                // blue
                addFireParticles(worldIn, pos, 12,
                        new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue() - 80, 100),
                        0.02D);
                break;
            default:
                // none
                addFireParticles(worldIn, pos, 0, Color.WHITE, 0.0D);
                break;
            }
        }
    }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return TileEntityTypeInit.BUNSEN_BURNER.get().create();
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos,
            final ISelectionContext context) {
        if (SHAPE.isPresent())
            return SHAPE.get();
        return null;
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(FACING,
                context.getPlacementHorizontalFacing().getOpposite());
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
    	if(player.getHeldItemMainhand().getItem() instanceof FlintAndSteelItem && !state.get(HAS_GAS)) {
        	worldIn.setBlockState(pos, state.with(HAS_GAS, true), BlockFlags.BLOCK_UPDATE);
        	return ActionResultType.SUCCESS;
        }
    	
        if (player.isCrouching() && player.getHeldItemMainhand().isEmpty()) {
            worldIn.setBlockState(pos,
                    state.with(STRENGTH, state.get(STRENGTH) + 1 >= 9 ? 0 : state.get(STRENGTH) + 1),
                    BlockFlags.BLOCK_UPDATE);
        } else if (!state.get(FRAME)
                && player.getHeldItemMainhand().getItem().equals(ItemInit.BUNSEN_FRAME.get())) {
            worldIn.setBlockState(pos, state.with(FRAME, true), BlockFlags.BLOCK_UPDATE);
            if (!player.abilities.isCreativeMode) {
                player.getHeldItemMainhand().shrink(1);
            }
        } else if (!state.get(MESH) && state.get(FRAME)
                && player.getHeldItemMainhand().getItem().equals(ItemInit.WIRE_GAUZE.get())) {
            worldIn.setBlockState(pos, state.with(MESH, true), BlockFlags.BLOCK_UPDATE);
            if (!player.abilities.isCreativeMode) {
                player.getHeldItemMainhand().shrink(1);
            }
        } else if (!state.get(CRUCIBLE) && state.get(FRAME) && state.get(MESH)
                && player.getHeldItemMainhand().getItem().equals(ItemInit.CRUCIBLE.get())) {
            worldIn.setBlockState(pos, state.with(CRUCIBLE, true), BlockFlags.BLOCK_UPDATE);
            if (!player.abilities.isCreativeMode) {
                player.getHeldItemMainhand().shrink(1);
            }
        } else if (state.get(CRUCIBLE) && state.get(FRAME) && state.get(MESH)
                && !player.getHeldItemMainhand().isEmpty()) {
            final TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof BunsenBurnerTileEntity) {
                final ItemStack heldStack = player.getHeldItemMainhand().copy();
                heldStack.setCount(1);
                final BunsenBurnerTileEntity bunsenBurner = (BunsenBurnerTileEntity) tile;
                final BunsenBurnerRecipe recipe = bunsenBurner.getRecipe(heldStack);
                if (recipe != null && bunsenBurner.getItemInSlot(0).isEmpty()) {
                    bunsenBurner.getInventory().setStackInSlot(0, heldStack);
                    if (!player.abilities.isCreativeMode) {
                        player.getHeldItemMainhand().shrink(1);
                    }
                }
            }
        } else if (player.getHeldItemMainhand().isEmpty()) {
            final TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof BunsenBurnerTileEntity) {
                final BunsenBurnerTileEntity bunsenBurner = (BunsenBurnerTileEntity) tile;
                if (!bunsenBurner.getItemInSlot(0).isEmpty()) {
                    player.addItemStackToInventory(bunsenBurner.getItemInSlot(0));
                    bunsenBurner.getItemInSlot(0).shrink(1);
                } else if (state.get(CRUCIBLE)) {
                    if (!player.abilities.isCreativeMode) {
                        player.addItemStackToInventory(new ItemStack(ItemInit.CRUCIBLE.get()));
                    }
                    worldIn.setBlockState(pos, state.with(CRUCIBLE, false), BlockFlags.BLOCK_UPDATE);
                } else if (state.get(MESH)) {
                    if (!player.abilities.isCreativeMode) {
                        player.addItemStackToInventory(new ItemStack(ItemInit.WIRE_GAUZE.get()));
                    }
                    worldIn.setBlockState(pos, state.with(MESH, false), BlockFlags.BLOCK_UPDATE);
                } else if (state.get(FRAME)) {
                    if (!player.abilities.isCreativeMode) {
                        player.addItemStackToInventory(new ItemStack(ItemInit.BUNSEN_FRAME.get()));
                    }
                    worldIn.setBlockState(pos, state.with(FRAME, false), BlockFlags.BLOCK_UPDATE);
                }
            }
        }
        
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(final BlockState state, final World worldIn, final BlockPos pos,
            final BlockState newState, final boolean isMoving) {
        final TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof BunsenBurnerTileEntity && state.getBlock() != newState.getBlock()) {
            final BunsenBurnerTileEntity bunsenBurner = (BunsenBurnerTileEntity) tile;
            for (int index = 0; index < bunsenBurner.getInventory().getSlots(); index++) {
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                        bunsenBurner.getItemInSlot(0)));
            }

            if (state.get(FRAME)) {
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(ItemInit.BUNSEN_FRAME.get())));
            }

            if (state.get(MESH)) {
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(ItemInit.WIRE_GAUZE.get())));
            }

            if (state.get(CRUCIBLE)) {
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(ItemInit.CRUCIBLE.get())));
            }
        }

        if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public BlockState rotate(final BlockState state, final IWorld world, final BlockPos pos,
            final Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(final Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, STRENGTH, HAS_GAS, FRAME, MESH, CRUCIBLE);
    }
}
