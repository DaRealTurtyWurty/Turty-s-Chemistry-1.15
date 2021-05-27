package com.turtywurty.turtyschemistry.common.blocks.fractional_distiller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class FractionalDistillerTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public static final int CRUDE_SLOT = 0;
	public static final int REFGAS_SLOT = 1;
	public static final int PETROL_SLOT = 2;
	public static final int NAPHTHA_SLOT = 3;
	public static final int KEROSINE_SLOT = 4;
	public static final int DIESEL_SLOT = 5;
	public static final int LUBOIL_SLOT = 6;
	public static final int FUELOIL_SLOT = 7;
	public static final int BITUMEN_SLOT = 8;
	private int ticks = 0;

	private static final String INVENTORY_TAG = "inventory";
	private static final String PROCESSING_TIME_LEFT_TAG = "processingTimeLeft";
	private static final String MAX_PROCESSING_TIME_TAG = "maxProcessingTime";

	public final ItemStackHandler inventory = new ItemStackHandler(9) {
		@Override
		public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
			switch (slot) {
			case CRUDE_SLOT:
				return isCrudeOil(stack.getItem());
			default:
				return isValidOutput(stack, slot);
			}
		}

		@Override
		protected void onContentsChanged(final int slot) {
			super.onContentsChanged(slot);
			FractionalDistillerTileEntity.this.markDirty();
		}
	};

	// Store the capability lazy optionals as fields to keep the amount of objects
	// we use to a minimum
	private final LazyOptional<ItemStackHandler> inventoryCapabilityExternal = LazyOptional.of(() -> this.inventory);
	// Machines (hoppers, pipes) connected to this furnace's top can only
	// insert/extract items from the input slot
	private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityExternalUp = LazyOptional
			.of(() -> new RangedWrapper(this.inventory, CRUDE_SLOT, CRUDE_SLOT + 1));
	// Machines (hoppers, pipes) connected to this furnace's bottom can only
	// insert/extract items from the output slot
	private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityExternalDown = LazyOptional
			.of(() -> new RangedWrapper(this.inventory, REFGAS_SLOT, BITUMEN_SLOT + 1));

	public short processTimeLeft = -1;
	public short maxProcessingTime = -1;
	public boolean lastProcessing = false;

	public FractionalDistillerTileEntity() {
		super(TileEntityTypeInit.FRACTIONAL_DISTILLER.get());
	}

	public boolean isCrudeOil(Item item) {
		return item != Items.ACACIA_BOAT;
	}

	public boolean isValidOutput(ItemStack item, int slot) {
		switch (slot) {
		case REFGAS_SLOT:
			return item.getItem() == ItemInit.REFINARY_GAS.get();
		case PETROL_SLOT:
			return item.getItem() == ItemInit.PETROL.get();
		case NAPHTHA_SLOT:
			return item.getItem() == ItemInit.NAPHTHA.get();
		case KEROSINE_SLOT:
			return item.getItem() == ItemInit.KEROSINE.get();
		case DIESEL_SLOT:
			return item.getItem() == ItemInit.DIESEL.get();
		case LUBOIL_SLOT:
			return item.getItem() == ItemInit.LUBRICATING_OIL.get();
		case FUELOIL_SLOT:
			return item.getItem() == ItemInit.FUEL_OIL.get();
		case BITUMEN_SLOT:
			return item.getItem() == ItemInit.BITUMEN.get();
		default:
			return false;
		}
	}

	private List<ItemStack> getResults(final Item input) {
		List<ItemStack> results = new ArrayList<>();
		results.add(new ItemStack(ItemInit.REFINARY_GAS.get(), 1));
		results.add(new ItemStack(ItemInit.PETROL.get(), 1));
		results.add(new ItemStack(ItemInit.NAPHTHA.get(), 1));
		results.add(new ItemStack(ItemInit.KEROSINE.get(), 1));
		results.add(new ItemStack(ItemInit.DIESEL.get(), 1));
		results.add(new ItemStack(ItemInit.LUBRICATING_OIL.get(), 1));
		results.add(new ItemStack(ItemInit.FUEL_OIL.get(), 1));
		results.add(new ItemStack(ItemInit.BITUMEN.get(), 1));
		return results;
	}

	@Override
	public void tick() {
		if (world == null || world.isRemote)
			return;
		if (ticks == 0 && !world.isRemote)
			lastProcessing = isProcessing();
		boolean powered = false;
		if (isProcessing()) {
			powered = true;
		}

		final ItemStack input = inventory.getStackInSlot(CRUDE_SLOT);
		final List<ItemStack> results = getResults(input.getItem());

		for (ItemStack result : results) {
			if (result.isEmpty()) {
				processTimeLeft = maxProcessingTime = -1;
				break;
			}
		}

		if (isCrudeOil(input.getItem())) {
			final boolean canInsertResultIntoOutput = true;
			if (canInsertResultIntoOutput) {
				if (!powered)
					return;
				if (processTimeLeft == -1) {
					processTimeLeft = maxProcessingTime = getProcessingTime(input);
				}

				else {
					--processTimeLeft;
					if (processTimeLeft == 0) {
						inventory.insertItem(REFGAS_SLOT, new ItemStack(ItemInit.REFINARY_GAS.get()), false);
						inventory.insertItem(PETROL_SLOT, new ItemStack(ItemInit.PETROL.get()), false);
						inventory.insertItem(NAPHTHA_SLOT, new ItemStack(ItemInit.NAPHTHA.get()), false);
						inventory.insertItem(KEROSINE_SLOT, new ItemStack(ItemInit.KEROSINE.get()), false);
						inventory.insertItem(DIESEL_SLOT, new ItemStack(ItemInit.DIESEL.get()), false);
						inventory.insertItem(LUBOIL_SLOT, new ItemStack(ItemInit.LUBRICATING_OIL.get()), false);
						inventory.insertItem(FUELOIL_SLOT, new ItemStack(ItemInit.FUEL_OIL.get()), false);
						inventory.insertItem(BITUMEN_SLOT, new ItemStack(ItemInit.BITUMEN.get()), false);
						if (input.hasContainerItem()) {
							final ItemStack containerStack = input.getContainerItem();
							input.shrink(1);
							dropStack(containerStack);
							processTimeLeft = -1;
							inventory.setStackInSlot(CRUDE_SLOT, input);
						}

						else
							input.shrink(1);
						processTimeLeft = -1;
						inventory.setStackInSlot(CRUDE_SLOT, input);
					}
				}
			}

			else {
				if (processTimeLeft < maxProcessingTime)
					++processTimeLeft;
			}
		}

		else
			processTimeLeft = maxProcessingTime = -1;

		if (lastProcessing != powered) {
			this.markDirty();
			final BlockState newState = this.getBlockState().with(FractionalDistillerBlock.PROCESSING, powered);
			world.setBlockState(pos, newState, 2);
			lastProcessing = powered;
		}
	}

	private void dropStack(final ItemStack stack) {
		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}

	private short getProcessingTime(final ItemStack input) {
		return (short) 200;
	}

	public boolean isProcessing() {
		return world.isBlockPowered(pos);
	}

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null)
				return inventoryCapabilityExternal.cast();
			switch (side) {
			case DOWN:
				return inventoryCapabilityExternalDown.cast();
			case UP:
				return inventoryCapabilityExternalUp.cast();
			default:
				return super.getCapability(cap, side);
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.inventory.deserializeNBT(compound.getCompound(INVENTORY_TAG));
		this.processTimeLeft = compound.getShort(PROCESSING_TIME_LEFT_TAG);
		this.maxProcessingTime = compound.getShort(MAX_PROCESSING_TIME_TAG);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put(INVENTORY_TAG, this.inventory.serializeNBT());
		compound.putShort(PROCESSING_TIME_LEFT_TAG, this.processTimeLeft);
		compound.putShort(MAX_PROCESSING_TIME_TAG, this.maxProcessingTime);
		return compound;
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void remove() {
		super.remove();
		inventoryCapabilityExternal.invalidate();
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(BlockInit.FRACTIONAL_DISTILLER.get().getTranslationKey());
	}

	@Override
	public Container createMenu(final int windowId, final PlayerInventory inventory, final PlayerEntity player) {
		return new FractionalDistillerContainer(windowId, inventory, this);
	}
}
