package com.turtywurty.turtyschemistry.common.blocks.silo;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SiloSlotItemHandler extends SlotItemHandler {

    private final IItemHandler itemHandler;
    private final int index;
    private boolean enabled;

    public SiloSlotItemHandler(final IItemHandler itemHandler, final int index, final int xPosition,
            final int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
        this.enabled = true;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public IItemHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
