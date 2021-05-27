package com.turtywurty.turtyschemistry.core.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class GlobalWarmingWSD extends WorldSavedData {

	public int stage;

	public GlobalWarmingWSD(String name, int stage) {
		super(name);
		this.stage = stage;
	}

	@Override
	public void read(CompoundNBT nbt) {
		this.stage = nbt.getInt("Stage");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("Stage", this.stage);
		return compound;
	}
}
