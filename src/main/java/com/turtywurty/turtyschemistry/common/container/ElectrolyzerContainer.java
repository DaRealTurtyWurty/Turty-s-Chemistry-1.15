package com.turtywurty.turtyschemistry.common.container;

import java.util.Objects;

import com.turtywurty.turtyschemistry.common.container.slots.ElectrolyzerOutputSlot;
import com.turtywurty.turtyschemistry.common.tileentity.ElectrolyzerTileEntity;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.util.FunctionalIntReferenceHolder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

public class ElectrolyzerContainer extends Container {

	private final IWorldPosCallable callable;
	public final ElectrolyzerTileEntity tileEntity;

	public ElectrolyzerContainer(int id, final PlayerInventory playerInventory, ElectrolyzerTileEntity tileEntityIn) {
		super(ContainerTypeInit.ELECTROLYZER.get(), id);
		this.tileEntity = tileEntityIn;
		this.callable = IWorldPosCallable.of(tileEntityIn.getWorld(), tileEntityIn.getPos());

		int slotSizePlus2 = 18;

		// Electrolyzer Inventory
		this.addSlot(new SlotItemHandler(tileEntityIn.getInventory(), 0, 27, 55) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof BucketItem;
			}
		});
		this.addSlot(new ElectrolyzerOutputSlot(tileEntityIn.getInventory(), 1, 79, 55));
		this.addSlot(new ElectrolyzerOutputSlot(tileEntityIn.getInventory(), 2, 133, 55));

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

		this.trackInt(new FunctionalIntReferenceHolder(() -> tileEntityIn.getStoredHydrogen(),
				tileEntityIn::setStoredHydrogen));

		this.trackInt(
				new FunctionalIntReferenceHolder(() -> tileEntityIn.getStoredOxygen(), tileEntityIn::setStoredOxygen));

		this.trackInt(
				new FunctionalIntReferenceHolder(() -> tileEntityIn.getStoredWater(), tileEntityIn::setStoredWater));

		this.trackInt(
				new FunctionalIntReferenceHolder(() -> tileEntityIn.getRunningTime(), tileEntityIn::setRunningTime));
	}

	public ElectrolyzerContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	public static ElectrolyzerTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "playerInv cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final TileEntity tile = playerInv.player.world.getTileEntity(data.readBlockPos());
		if (tile instanceof ElectrolyzerTileEntity) {
			return (ElectrolyzerTileEntity) tile;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tile);
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

	@OnlyIn(Dist.CLIENT)
	public int getScaledProgress() {
		return this.tileEntity.getRunningTime() != 0 && this.tileEntity.getMaxRunningTime() != 0
				? this.tileEntity.getRunningTime() * 24 / this.tileEntity.getMaxRunningTime()
				: 0;
	}
}
