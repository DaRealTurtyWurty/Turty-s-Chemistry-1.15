package com.turtywurty.turtyschemistry.common.blocks.baler;

import java.util.Objects;

import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.util.FunctionalIntReferenceHolder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class BalerContainer extends Container {

	private final IWorldPosCallable callable;
	public final BalerTileEntity tileEntity;

	public BalerContainer(int id, final PlayerInventory playerInventory, BalerTileEntity tileEntityIn) {
		super(ContainerTypeInit.BALER.get(), id);
		this.tileEntity = tileEntityIn;
		this.callable = IWorldPosCallable.of(tileEntityIn.getWorld(), tileEntityIn.getPos());

		int slotSizePlus2 = 18;

		// Wheat Inventory
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 3; column++) {
				this.addSlot(new Slot(tileEntityIn, (row + column * 3) + 2, 63 + (column * slotSizePlus2),
						17 + (row * slotSizePlus2)) {
					@Override
					public boolean isItemValid(ItemStack stack) {
						return stack.getItem().equals(Items.WHEAT);
					}
				});
			}
		}

		// String Inventory
		this.addSlot(new Slot(tileEntityIn, 0, 27, 35) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.STRING);
			}
		});
		
		this.addSlot(new Slot(tileEntityIn, 1, 134, 35) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(Items.STRING);
			}
		});

		// Main Inventory
		int startX = 8;
		int startY = 84;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * slotSizePlus2),
						startY + (row * slotSizePlus2)));
			}
		}

		// Hotbar
		int hotbarY = 142;
		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
		}

		this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntityIn.compressingTime,
				v -> tileEntity.compressingTime = (short) v));

		this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntityIn.maxCompressingTime,
				v -> tileEntity.maxCompressingTime = (short) v));
	}

	public BalerContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	public static BalerTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "playerInv cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final TileEntity tile = playerInv.player.world.getTileEntity(data.readBlockPos());
		if (tile instanceof BalerTileEntity) {
			return (BalerTileEntity) tile;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tile);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.BALER.get());
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack returnStack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			returnStack = slotStack.copy();

			final int containerSlots = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(slotStack, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
			if (slotStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (slotStack.getCount() == returnStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, slotStack);
		}
		return returnStack;
	}
}
