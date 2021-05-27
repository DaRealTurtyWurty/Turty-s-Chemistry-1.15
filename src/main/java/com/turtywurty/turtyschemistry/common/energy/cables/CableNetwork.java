package com.turtywurty.turtyschemistry.common.energy.cables;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

public class CableNetwork extends WorldSavedData {
	
	protected Map<BlockPos, Integer> positions = new HashMap<BlockPos, Integer>();

	public CableNetwork(int amount, BlockPos... positions) {
		super("cables");
	}

	@Override
	public void read(CompoundNBT nbt) {
		
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		return null;
	}
}
