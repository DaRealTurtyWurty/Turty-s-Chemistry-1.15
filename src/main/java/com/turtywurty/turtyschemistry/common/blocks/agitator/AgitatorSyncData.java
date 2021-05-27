package com.turtywurty.turtyschemistry.common.blocks.agitator;

import net.minecraft.util.IIntArray;

public class AgitatorSyncData implements IIntArray {

	private final AgitatorTileEntity te;

	public AgitatorSyncData(AgitatorTileEntity te) {
		this.te = te;
	}

	@Override
	public int get(int index) {
		switch (index) {
		case 0:
			return this.te.getRunningTime();
		default:
			return 0;
		}
	}

	@Override
	public void set(int index, int value) {
		switch (index) {
		case 0:
			this.te.setRunningTime(value);
			break;
		default:
			break;
		}
	}

	@Override
	public int size() {
		return 1;
	}
}
