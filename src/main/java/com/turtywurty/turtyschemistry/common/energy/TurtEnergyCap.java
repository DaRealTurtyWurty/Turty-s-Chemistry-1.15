package com.turtywurty.turtyschemistry.common.energy;

import com.turtywurty.turtyschemistry.common.energy.TurtEnergyStorage.StorageProperties;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class TurtEnergyCap {

    @CapabilityInject(TurtEnergyStorage.class)
    public static final Capability<TurtEnergyStorage> ENERGY = null;

    private TurtEnergyCap() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(TurtEnergyStorage.class, new IStorage<TurtEnergyStorage>() {

            @Override
            public void readNBT(final Capability<TurtEnergyStorage> capability,
                    final TurtEnergyStorage instance, final Direction side, final INBT nbt) {
                if (!(instance instanceof TurtEnergyStorage))
                    throw new IllegalArgumentException(
                            "Can not deserialize to an instance that isn't the default implementation");
                instance.setEnergy(((IntNBT) nbt).getInt());
            }

            @Override
            public INBT writeNBT(final Capability<TurtEnergyStorage> capability,
                    final TurtEnergyStorage instance, final Direction side) {
                return IntNBT.valueOf(instance.getEnergyStored());
            }

        }, () -> new TurtEnergyStorage(new StorageProperties(null, 0, 0, 0, 0, false, false)));
    }
}
