package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import net.minecraft.util.IIntArray;

public class ElectrolyzerSyncData implements IIntArray {

	private final ElectrolyzerTileEntity te;

	public ElectrolyzerSyncData(ElectrolyzerTileEntity te) {
		this.te = te;
	}

	@Override
	public int get(int index) {
		switch (index) {
		case 0:
			return this.te.getRunningTime();
		case 1:
			return this.te.getMaxRunningTime();
		case 2:
			return this.te.getStoredWater();
		case 3:
			return this.te.getMaxWater();
		case 4:
			return this.te.getStoredOxygen();
		case 5:
			return this.te.getMaxOxygen();
		case 6:
			return this.te.getStoredHydrogen();
		case 7:
			return this.te.getMaxHydrogen();
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
		case 1:
			this.te.setMaxRunningTime(value);
			break;
		case 2:
			this.te.setStoredWater(value);
			break;
		case 3:
			this.te.setMaxWater(value);
			break;
		case 4:
			this.te.setStoredOxygen(value);
			break;
		case 5:
			this.te.setMaxOxygen(value);
			break;
		case 6:
			this.te.setStoredHydrogen(value);
			break;
		case 7:
			this.te.setMaxHydrogen(value);
			break;
		default:
			break;
		}
	}

	@Override
	public int size() {
		return 5;
	}
}
