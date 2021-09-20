package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import com.turtywurty.turtyschemistry.core.init.BlockInit;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElectrolyzerOutputSlot extends SlotItemHandler {

    public ElectrolyzerOutputSlot(final IItemHandler itemHandler, final int index, final int xPosition,
            final int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(final ItemStack stack) {
        return stack.getItem().equals(BlockInit.GAS_CANISTER_S.get().asItem())
                || stack.getItem().equals(BlockInit.GAS_CANISTER_L.get().asItem());
    }
}
