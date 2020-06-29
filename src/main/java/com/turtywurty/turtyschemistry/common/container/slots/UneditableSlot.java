package com.turtywurty.turtyschemistry.common.container.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class UneditableSlot extends Slot {

	public UneditableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		return false;
	}
}
