package com.turtywurty.turtyschemistry.common.blocks.silo;

import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SiloContainer extends Container {

	private final IWorldPosCallable callable;
	public final IIntArray currentPage;
	private int page = 0;
	private static SiloTileEntity tile;

	// Server
	public SiloContainer(int id, final PlayerInventory playerInventory, BlockPos pos, IItemHandler slots,
			IIntArray data, int page) {
		super(ContainerTypeInit.SILO.get(), id);
		this.callable = IWorldPosCallable.of(playerInventory.player.getEntityWorld(), pos);
		this.currentPage = data;

		int slotSizePlus2 = 18;

		// Player Inventory
		int startX = 49;
		int startY = 174;

		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * slotSizePlus2),
						startY + (row * slotSizePlus2)));
			}
		}

		// Hotbar
		int hotbarY = 232;
		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
		}

		this.trackIntArray(data);

		// Silo Inventory
		int siloStartX = 13;
		int siloStartY = 18;

		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 13; column++) {
				SiloSlotItemHandler slot = new SiloSlotItemHandler(slots, (page * 104) + (column + (row * 13)),
						siloStartX + (column * slotSizePlus2), siloStartY + (row * slotSizePlus2));
				this.addSlot(slot);
			}
		}
	}

	public static IContainerProvider getServerContainerProvider(SiloTileEntity te, BlockPos activationPos, int page) {
		tile = te;
		return (id, playerInventory, serverPlayer) -> new SiloContainer(id, playerInventory, activationPos,
				te.getInventory(), new SiloSyncData(te), page);
	}

	public static SiloContainer getClientContainer(int id, PlayerInventory playerInventory) {
		return new SiloContainer(id, playerInventory, BlockPos.ZERO, new ItemStackHandler(624), new IntArray(1), 0);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.SILO.get());
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

	public SiloTileEntity getTile() {
		return tile;
	}
}
