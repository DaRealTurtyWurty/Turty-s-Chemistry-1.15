package com.turtywurty.turtyschemistry.common.container;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.turtywurty.turtyschemistry.common.tileentity.FractionalDistillerTileEntity;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.util.FunctionalIntReferenceHolder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;

public class FractionalDistillerContainer extends Container {

	public final FractionalDistillerTileEntity tileEntity;
	private final IWorldPosCallable canInteractWithCallable;

	public FractionalDistillerContainer(final int windowId, final PlayerInventory playerInventory,
			final FractionalDistillerTileEntity tileEntity) {
		super(ContainerTypeInit.FRACTIONAL_DISTILLER.get(), windowId);
		this.tileEntity = tileEntity;
		this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

		this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntity.processTimeLeft,
				v -> tileEntity.processTimeLeft = (short) v));
		this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntity.maxProcessingTime,
				v -> tileEntity.maxProcessingTime = (short) v));

		// Slots
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.CRUDE_SLOT, 49, 73));
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.BITUMEN_SLOT, 195, 150));
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.FUELOIL_SLOT, 174, 138));
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.LUBOIL_SLOT, 174, 116));
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.KEROSINE_SLOT, 174, 88));
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.NAPHTHA_SLOT, 174, 60));
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.PETROL_SLOT, 174, 32));
		this.addSlot(new SlotItemHandler(tileEntity.inventory, FractionalDistillerTileEntity.REFGAS_SLOT, 195, 4));

		final int playerInventoryStartX = 49;
		final int playerInventoryStartY = 174;
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
		final int playerHotbarY = playerInventoryStartY + slotSizePlus2 * 3 + 4;
		for (int column = 0; column < 9; ++column) {
			this.addSlot(
					new Slot(playerInventory, column, playerInventoryStartX + (column * slotSizePlus2), playerHotbarY));
		}
	}

	private static FractionalDistillerTileEntity getTileEntity(final PlayerInventory playerInventory,
			final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
		if (tileAtPos instanceof FractionalDistillerTileEntity)
			return (FractionalDistillerTileEntity) tileAtPos;
		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public FractionalDistillerContainer(final int windowId, final PlayerInventory playerInventory,
			final PacketBuffer data) {
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
		return isWithinUsableDistance(canInteractWithCallable, player, BlockInit.FRACTIONAL_DISTILLER.get());
	}
}
