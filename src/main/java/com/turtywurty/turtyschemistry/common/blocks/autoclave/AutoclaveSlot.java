package com.turtywurty.turtyschemistry.common.blocks.autoclave;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoclaveSlot extends SlotItemHandler {

    public AutoclaveSlot(final IItemHandler itemHandler, final int index, final int xPosition,
            final int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(final ItemStack stack) {
        return stack.isDamaged();
    }
}
