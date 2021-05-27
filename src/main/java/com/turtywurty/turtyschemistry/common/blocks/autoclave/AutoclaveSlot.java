package com.turtywurty.turtyschemistry.common.blocks.autoclave;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoclaveSlot extends SlotItemHandler {

	public AutoclaveSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.isDamaged();
	}
}
