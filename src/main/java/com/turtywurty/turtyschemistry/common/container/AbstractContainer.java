package com.turtywurty.turtyschemistry.common.container;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.items.IItemHandler;

public abstract class AbstractContainer<T extends TileEntity> extends Container {

	protected static final int SLOT_SIZE_PLUS_2 = 18;
	private final IWorldPosCallable callable;
	private final Block[] validBlocks;
	protected T tile;
	public final IIntArray data;

	protected AbstractContainer(final int id, final PlayerInventory playerInv, final IItemHandler slots,
			final BlockPos pos, final IIntArray data,
			final RegistryObject<ContainerType<? extends AbstractContainer<T>>> container, final T tileEntity,
			final Block... validBlocks) {
		super(container.get(), id);
		this.callable = IWorldPosCallable.of(playerInv.player.getEntityWorld(), pos);
		this.data = data;
		this.validBlocks = validBlocks;
		this.tile = tileEntity;

		this.addMainInv(slots);
		this.addPlayerMain(playerInv);
		this.addPlayerHotbar(playerInv);

		trackIntArray(data);
	}

	public abstract void addMainInv(IItemHandler slots);

	public abstract void addPlayerHotbar(PlayerInventory inv);

	public abstract void addPlayerMain(PlayerInventory inv);

	@Override
	public boolean canInteractWith(final PlayerEntity playerIn) {
		boolean valid = false;
		for (Block block : this.validBlocks) {
			if (isWithinUsableDistance(this.callable, playerIn, block)) {
				valid = true;
				break;
			}
		}
		return valid;
	}

	public abstract int getScaledProgress();

	public T getTile() {
		return this.tile;
	}

	@Override
	public ItemStack transferStackInSlot(final PlayerEntity playerIn, final int index) {
		ItemStack returnStack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			returnStack = slotStack.copy();

			final int containerSlots = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(slotStack, 0, containerSlots, false))
				return ItemStack.EMPTY;
			if (slotStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (slotStack.getCount() == returnStack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, slotStack);
		}
		return returnStack;
	}
}
