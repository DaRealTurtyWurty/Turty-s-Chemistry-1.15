package com.turtywurty.turtyschemistry.common.blocks.gas_canister;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class GasCanisterBlock extends Block {

	private static final Optional<VoxelShape> LARGE_SHAPE = Stream
			.of(Block.makeCuboidShape(3, 15, 3, 13, 16, 13), Block.makeCuboidShape(3, 1, 2, 13, 15, 3),
					Block.makeCuboidShape(13, 1, 3, 14, 15, 13), Block.makeCuboidShape(3, 1, 13, 13, 15, 14),
					Block.makeCuboidShape(2, 1, 3, 3, 15, 13), Block.makeCuboidShape(3, 0, 3, 13, 1, 13))
			.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)),

			SMALL_SHAPE = Stream
					.of(Block.makeCuboidShape(5, 12, 5, 11, 13, 11), Block.makeCuboidShape(4, 1, 5, 5, 12, 11),
							Block.makeCuboidShape(5, 1, 11, 11, 12, 12), Block.makeCuboidShape(11, 1, 5, 12, 12, 11),
							Block.makeCuboidShape(5, 1, 4, 11, 12, 5), Block.makeCuboidShape(5, 0, 5, 11, 1, 11))
					.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR));

	private static final String BLOCK_ENTITY_TAG = "BlockEntityTag", MAX_AMOUNT = "MaxAmount", GAS_NAME = "GasName",
			GAS_STORED = "GasStored";

	private boolean large;

	public GasCanisterBlock(Properties properties, boolean isLarge) {
		super(properties);
		this.large = isLarge;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return isLarge() ? TileEntityTypeInit.GAS_CANISTER_L.get().create()
				: TileEntityTypeInit.GAS_CANISTER_S.get().create();
	}

	public boolean isLarge() {
		return this.large;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (worldIn.getTileEntity(pos) instanceof AbstractGasCanisterTE) {
			AbstractGasCanisterTE canister = (AbstractGasCanisterTE) worldIn.getTileEntity(pos);
			if (!worldIn.isRemote && canister.getGasStored() > 0) {
				ItemStack itemstack = new ItemStack(
						isLarge() ? ItemInit.GAS_CANISTER_L.get() : ItemInit.GAS_CANISTER_S.get());

				CompoundNBT compoundnbt = canister.saveToNbt(new CompoundNBT());
				if (!compoundnbt.isEmpty()) {
					itemstack.setTagInfo(BLOCK_ENTITY_TAG, compoundnbt);
				}

				ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX(), (double) pos.getY(),
						(double) pos.getZ(), itemstack);
				itementity.setDefaultPickupDelay();
				worldIn.addEntity(itementity);
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (isLarge() && LARGE_SHAPE.isPresent())
			return LARGE_SHAPE.get();

		else if (SMALL_SHAPE.isPresent())
			return SMALL_SHAPE.get();

		return VoxelShapes.empty();
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		ItemStack itemstack = isLarge() ? new ItemStack(ItemInit.GAS_CANISTER_L.get())
				: new ItemStack(ItemInit.GAS_CANISTER_S.get());
		AbstractGasCanisterTE canisterTE = (AbstractGasCanisterTE) worldIn.getTileEntity(pos);
		CompoundNBT compoundnbt = canisterTE.saveToNbt(new CompoundNBT());
		if (!compoundnbt.isEmpty()) {
			itemstack.setTagInfo(BLOCK_ENTITY_TAG, compoundnbt);
		}
		return itemstack;
	}

	@Override
	public Item asItem() {
		return isLarge() ? ItemInit.GAS_CANISTER_L.get() : ItemInit.GAS_CANISTER_S.get();
	}

	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT nbt = stack.getOrCreateChildTag(BLOCK_ENTITY_TAG);
		if (isLarge()) {
			nbt.putInt(MAX_AMOUNT, 15000);
		} else {
			nbt.putInt(MAX_AMOUNT, 5000);
		}

		if (flagIn.isAdvanced()) {
			if (nbt.contains(GAS_NAME)) {
				tooltip.add(new TranslationTextComponent(nbt.getString(GAS_NAME)));
			}

			if (nbt.getInt(GAS_STORED) > 0) {
				tooltip.add(
						new StringTextComponent("Amount Stored: " + nbt.getInt(GAS_STORED) + "/" + nbt.getInt(MAX_AMOUNT)));
			} else {
				tooltip.add(new TranslationTextComponent("gas_canister.no_gas.tooltip"));
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof AbstractGasCanisterTE) {
			((AbstractGasCanisterTE) tileentity).loadFromNbt(stack.getOrCreateChildTag(BLOCK_ENTITY_TAG));
		}
	}
}
