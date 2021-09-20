package com.turtywurty.turtyschemistry.common.blocks.agitator;

import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AgitatorSlot extends SlotItemHandler {

    public AgitatorSlot(final IItemHandler itemHandler, final int index, final int xPosition,
            final int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(final ItemStack stack) {
        return stack.getItem() instanceof BucketItem;
    }
}
