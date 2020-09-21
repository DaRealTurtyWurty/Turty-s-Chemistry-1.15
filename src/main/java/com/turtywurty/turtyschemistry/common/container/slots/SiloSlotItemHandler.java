package com.turtywurty.turtyschemistry.common.container.slots;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SiloSlotItemHandler extends SlotItemHandler {

	private final IItemHandler itemHandler;
	private final int index;
	private boolean enabled;

	public SiloSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.itemHandler = itemHandler;
		this.index = index;
		this.enabled = true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
