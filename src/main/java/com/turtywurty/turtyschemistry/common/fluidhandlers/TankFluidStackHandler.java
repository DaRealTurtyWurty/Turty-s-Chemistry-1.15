package com.turtywurty.turtyschemistry.common.fluidhandlers;

import com.turtywurty.turtyschemistry.core.util.FluidStackHandler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

public class TankFluidStackHandler extends FluidStackHandler {

    public TankFluidStackHandler(final int tanks, final int... capacity) {
        super(tanks, capacity);
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        setSize(nbt.contains("Size", Constants.NBT.TAG_INT) && nbt.getInt("Size") > 0 ? nbt.getInt("Size")
                : this.stacks.size());
        final ListNBT tagList = nbt.getList("Fluids", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            final CompoundNBT fluidTags = tagList.getCompound(i);
            final int tank = fluidTags.getInt("Tank");
            if (tank >= 0 && tank < this.stacks.size()) {
                this.stacks.set(tank, FluidStack.loadFluidStackFromNBT(fluidTags));
            }
            setCapacity(nbt.contains("Capacity", Constants.NBT.TAG_INT) && nbt.getInt("Capacity") > 0
                    ? nbt.getInt("Capacity")
                    : this.capacity[tank], tank);
        }
        onLoad();
    }

    @Override
    public CompoundNBT serializeNBT() {
        final ListNBT nbtTagList = new ListNBT();
        for (int i = 0; i < this.stacks.size(); i++) {
            if (!this.stacks.get(i).isEmpty()) {
                final CompoundNBT fluidTag = new CompoundNBT();
                fluidTag.putInt("Tank", i);
                this.stacks.get(i).writeToNBT(fluidTag);
                nbtTagList.add(fluidTag);
            }
        }
        final CompoundNBT nbt = new CompoundNBT();
        nbt.put("Fluids", nbtTagList);
        nbt.putInt("Size", this.stacks.size());
        nbt.putIntArray("Capacity", this.capacity);
        return nbt;
    }
}