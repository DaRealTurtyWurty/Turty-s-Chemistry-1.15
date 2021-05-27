package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElectrolyzerContainer extends Container {

	private final IWorldPosCallable callable;
	public static ElectrolyzerTileEntity tileEntity;
	public final IIntArray data;

	public ElectrolyzerContainer(int id, final PlayerInventory playerInventory, IItemHandler slots, BlockPos pos,
			IIntArray data) {
		super(ContainerTypeInit.ELECTROLYZER.get(), id);
		this.callable = IWorldPosCallable.of(playerInventory.player.getEntityWorld(), pos);
		this.data = data;

		int slotSizePlus2 = 18;

		// Electrolyzer Inventory
		this.addSlot(new SlotItemHandler(slots, 0, 27, 55) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof BucketItem;
			}
		});
		this.addSlot(new ElectrolyzerOutputSlot(slots, 1, 79, 55));
		this.addSlot(new ElectrolyzerOutputSlot(slots, 2, 133, 55));

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

		this.trackIntArray(data);
	}

	public static IContainerProvider getServerContainerProvider(ElectrolyzerTileEntity te, BlockPos activationPos) {
		tileEntity = te;
		return (id, playerInv, serverPlayer) -> new ElectrolyzerContainer(id, playerInv, te.getInventory(), activationPos,
				new ElectrolyzerSyncData(te));
	}

	public static ElectrolyzerContainer getClientContainer(int id, PlayerInventory playerInv) {
		return new ElectrolyzerContainer(id, playerInv, new ItemStackHandler(3), BlockPos.ZERO, new IntArray(8));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.ELECTROLYZER.get());
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

	public int getScaledProgress() {
		return this.data.get(0) != 0 && this.data.get(1) != 0 ? this.data.get(0) * 24 / this.data.get(1) : 0;
	}
}
