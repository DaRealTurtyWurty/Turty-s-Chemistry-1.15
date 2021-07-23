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
	private static final String INVENTORY_TAG = "inventory";

	private static final String PROCESSING_TIME_LEFT_TAG = "processingTimeLeft";
	private static final String MAX_PROCESSING_TIME_TAG = "maxProcessingTime";
	private final int ticks = 0;

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

	@Override
	public Container createMenu(final int windowId, final PlayerInventory inventory, final PlayerEntity player) {
		return new FractionalDistillerContainer(windowId, inventory, this);
	}

	private void dropStack(final ItemStack stack) {
		InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), stack);
	}

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null)
				return this.inventoryCapabilityExternal.cast();
			switch (side) {
			case DOWN:
				return this.inventoryCapabilityExternalDown.cast();
			case UP:
				return this.inventoryCapabilityExternalUp.cast();
			default:
				return super.getCapability(cap, side);
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(BlockInit.FRACTIONAL_DISTILLER.get().getTranslationKey());
	}

	private short getProcessingTime(final ItemStack input) {
		return (short) 200;
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
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	public boolean isCrudeOil(final Item item) {
		return item != Items.ACACIA_BOAT;
	}

	public boolean isProcessing() {
		return this.world.isBlockPowered(this.pos);
	}

	public boolean isValidOutput(final ItemStack item, final int slot) {
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

	@Override
	public void read(final BlockState state, final CompoundNBT compound) {
		super.read(state, compound);
		this.inventory.deserializeNBT(compound.getCompound(INVENTORY_TAG));
		this.processTimeLeft = compound.getShort(PROCESSING_TIME_LEFT_TAG);
		this.maxProcessingTime = compound.getShort(MAX_PROCESSING_TIME_TAG);
	}

	@Override
	public void remove() {
		super.remove();
		this.inventoryCapabilityExternal.invalidate();
	}

	@Override
	public void tick() {
		if (this.world == null || this.world.isRemote)
			return;
		if (this.ticks == 0 && !this.world.isRemote) {
			this.lastProcessing = isProcessing();
		}
		boolean powered = false;
		if (isProcessing()) {
			powered = true;
		}

		final ItemStack input = this.inventory.getStackInSlot(CRUDE_SLOT);
		final List<ItemStack> results = getResults(input.getItem());

		for (ItemStack result : results) {
			if (result.isEmpty()) {
				this.processTimeLeft = this.maxProcessingTime = -1;
				break;
			}
		}

		if (isCrudeOil(input.getItem())) {
			final boolean canInsertResultIntoOutput = true;
			if (canInsertResultIntoOutput) {
				if (!powered)
					return;
				if (this.processTimeLeft == -1) {
					this.processTimeLeft = this.maxProcessingTime = getProcessingTime(input);
				}

				else {
					--this.processTimeLeft;
					if (this.processTimeLeft == 0) {
						this.inventory.insertItem(REFGAS_SLOT, new ItemStack(ItemInit.REFINARY_GAS.get()), false);
						this.inventory.insertItem(PETROL_SLOT, new ItemStack(ItemInit.PETROL.get()), false);
						this.inventory.insertItem(NAPHTHA_SLOT, new ItemStack(ItemInit.NAPHTHA.get()), false);
						this.inventory.insertItem(KEROSINE_SLOT, new ItemStack(ItemInit.KEROSINE.get()), false);
						this.inventory.insertItem(DIESEL_SLOT, new ItemStack(ItemInit.DIESEL.get()), false);
						this.inventory.insertItem(LUBOIL_SLOT, new ItemStack(ItemInit.LUBRICATING_OIL.get()), false);
						this.inventory.insertItem(FUELOIL_SLOT, new ItemStack(ItemInit.FUEL_OIL.get()), false);
						this.inventory.insertItem(BITUMEN_SLOT, new ItemStack(ItemInit.BITUMEN.get()), false);
						if (input.hasContainerItem()) {
							final ItemStack containerStack = input.getContainerItem();
							input.shrink(1);
							dropStack(containerStack);
							this.processTimeLeft = -1;
							this.inventory.setStackInSlot(CRUDE_SLOT, input);
						} else {
							input.shrink(1);
						}
						this.processTimeLeft = -1;
						this.inventory.setStackInSlot(CRUDE_SLOT, input);
					}
				}
			} else if (this.processTimeLeft < this.maxProcessingTime) {
				++this.processTimeLeft;
			}
		} else {
			this.processTimeLeft = this.maxProcessingTime = -1;
		}

		if (this.lastProcessing != powered) {
			markDirty();
			final BlockState newState = getBlockState().with(FractionalDistillerBlock.PROCESSING, powered);
			this.world.setBlockState(this.pos, newState, 2);
			this.lastProcessing = powered;
		}
	}

	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		super.write(compound);
		compound.put(INVENTORY_TAG, this.inventory.serializeNBT());
		compound.putShort(PROCESSING_TIME_LEFT_TAG, this.processTimeLeft);
		compound.putShort(MAX_PROCESSING_TIME_TAG, this.maxProcessingTime);
		return compound;
	}
}
