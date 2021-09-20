package com.turtywurty.turtyschemistry.core.util;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ContainerListener implements IContainerListener {

    @Override
    public void sendAllContents(final Container containerToSend, final NonNullList<ItemStack> itemsList) {

    }

    @Override
    public void sendSlotContents(final Container containerToSend, final int slotInd, final ItemStack stack) {

    }

    public void sendWindowProperty(final Container containerIn, final int varToUpdate,
            final FluidStackHandler newValue) {

    }

    @Override
    public void sendWindowProperty(final Container containerIn, final int varToUpdate, final int newValue) {

    }
}
