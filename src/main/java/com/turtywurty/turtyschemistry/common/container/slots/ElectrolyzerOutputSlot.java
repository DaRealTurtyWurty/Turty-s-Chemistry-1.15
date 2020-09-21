package com.turtywurty.turtyschemistry.common.container.slots;

import com.turtywurty.turtyschemistry.core.init.BlockInit;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElectrolyzerOutputSlot extends SlotItemHandler {

	public ElectrolyzerOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem().equals(BlockInit.GAS_CANISTER_S.get().asItem())
				|| stack.getItem().equals(BlockInit.GAS_CANISTER_L.get().asItem());
	}
}
