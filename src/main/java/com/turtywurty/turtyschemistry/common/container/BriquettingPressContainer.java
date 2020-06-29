package com.turtywurty.turtyschemistry.common.container;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.turtywurty.turtyschemistry.common.tileentity.BriquettingPressTileEntity;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.init.ItemInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class BriquettingPressContainer extends Container {

	public final BriquettingPressTileEntity tileEntity;
	private final IWorldPosCallable canInteractWithCallable;

	public BriquettingPressContainer(final int windowId, final PlayerInventory playerInventory,
			final BriquettingPressTileEntity tileEntity) {
		super(ContainerTypeInit.BRIQUETTING_PRESS.get(), windowId);
		this.tileEntity = tileEntity;
		this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

		// Briquetting Press Inventory
		this.addSlot(new Slot(tileEntity, 0, 27, 36) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem().equals(ItemInit.SAWDUST.get());
			}
		});

		final int playerInventoryStartX = 8;
		final int playerInventoryStartY = 84;
		final int slotSizePlus2 = 18;

		// Main Inventory
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column,
						playerInventoryStartX + (column * slotSizePlus2),
						playerInventoryStartY + (row * slotSizePlus2)));
			}
		}

		// Hotbar
		final int playerHotbarY = playerInventoryStartY + (slotSizePlus2 * 3) + 4;
		for (int column = 0; column < 9; ++column) {
			this.addSlot(
					new Slot(playerInventory, column, playerInventoryStartX + (column * slotSizePlus2), playerHotbarY));
		}
	}

	public BriquettingPressContainer(final int windowId, final PlayerInventory playerInventory,
			final PacketBuffer data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
	}

	public static BriquettingPressTileEntity getTileEntity(final PlayerInventory playerInventory,
			final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInv cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final TileEntity tile = playerInventory.player.world.getTileEntity(data.readBlockPos());
		if (tile instanceof BriquettingPressTileEntity) {
			return (BriquettingPressTileEntity) tile;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tile);
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
		ItemStack returnStack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			returnStack = slotStack.copy();

			final int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
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
			slot.onTake(player, slotStack);
		}
		return returnStack;
	}

	@Override
	public boolean canInteractWith(@Nonnull final PlayerEntity player) {
		return isWithinUsableDistance(canInteractWithCallable, player, BlockInit.BRIQUETTING_PRESS.get());
	}
}
