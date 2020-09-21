package com.turtywurty.turtyschemistry.core.util;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ContainerListener implements IContainerListener {

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		
	}

	public void sendWindowProperty(Container containerIn, int varToUpdate, FluidStackHandler newValue) {
		
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
		
	}
}
