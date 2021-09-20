package com.turtywurty.turtyschemistry.common.energy.cables;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

public class CableNetwork extends WorldSavedData {

    protected Map<BlockPos, Integer> positions = new HashMap<>();

    public CableNetwork(final int amount, final BlockPos... positions) {
        super("cables");
    }

    @Override
    public void read(final CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        return null;
    }
}
