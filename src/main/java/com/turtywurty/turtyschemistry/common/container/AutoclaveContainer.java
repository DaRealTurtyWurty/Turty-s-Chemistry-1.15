package com.turtywurty.turtyschemistry.common.container;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.turtywurty.turtyschemistry.common.tileentity.AutoclaveTileEntity;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class AutoclaveContainer extends Container {

	public final AutoclaveTileEntity tileEntity;
	private final IWorldPosCallable canInteractWithCallable;

	public AutoclaveContainer(final int windowId, final PlayerInventory playerInventory,
			final AutoclaveTileEntity tileEntity) {
		super(ContainerTypeInit.AUTOCLAVE.get(), windowId);
		this.tileEntity = tileEntity;
		this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

		// Slots
		this.addSlot(new Slot((IInventory) tileEntity, 0, 80, 54) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.isDamaged();
			}
		});

		this.addSlot(new Slot((IInventory) tileEntity, 1, 80, 36) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.isDamaged();
			}
		});

		this.addSlot(new Slot((IInventory) tileEntity, 2, 80, 18) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.isDamaged();
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

	private static AutoclaveTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
		if (tileAtPos instanceof AutoclaveTileEntity)
			return (AutoclaveTileEntity) tileAtPos;
		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public AutoclaveContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
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
		return isWithinUsableDistance(canInteractWithCallable, player, BlockInit.AUTOCLAVE.get());
	}
}
