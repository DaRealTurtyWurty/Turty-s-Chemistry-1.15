package com.turtywurty.turtyschemistry.common.energy;

import net.minecraft.tileentity.TileEntity;

public class TurtEnergyStorage {
    protected int energy;
    private StorageProperties properties;

    public TurtEnergyStorage(final StorageProperties propertiesIn) {
        this.properties = propertiesIn;
        this.energy = Math.max(0, Math.min(this.properties.getCapacity(), this.properties.getStartEnergy()));
    }

    public int extractEnergy(final int maxExtract, final boolean simulate) {
        this.properties.getTile().markDirty();
        if (!this.properties.isCanExtract())
            return 0;

        final int energyExtracted = Math.min(this.energy,
                Math.min(this.properties.getMaxExtract(), maxExtract));
        if (!simulate) {
            this.energy -= energyExtracted;
        }
        return energyExtracted;
    }

    public int getEnergyStored() {
        return this.energy;
    }

    public StorageProperties getProperties() {
        return this.properties;
    }

    public int receiveEnergy(final int maxReceive, final boolean simulate) {
        this.properties.getTile().markDirty();
        if (!this.properties.isCanRecieve())
            return 0;

        final int energyReceived = Math.min(this.properties.getCapacity() - this.energy,
                Math.min(this.properties.getMaxReceive(), maxReceive));
        if (!simulate) {
            this.energy += energyReceived;
        }
        return energyReceived;
    }

    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public StorageProperties setProperties(final StorageProperties properties) {
        this.properties = properties;
        return this.properties;
    }

    public static class StorageProperties {
        private TileEntity tile;
        private int capacity, maxReceive, maxExtract, startEnergy;
        private boolean canExtract, canRecieve;

        public StorageProperties(final TileEntity tileIn, final int capacityIn, final int maxExtractIn,
                final int maxReceiveIn, final int startEnergyIn, final boolean canExtractIn,
                final boolean canReceiveIn) {
            this.tile = tileIn;
            this.capacity = capacityIn;
            this.maxExtract = maxExtractIn;
            this.maxReceive = maxReceiveIn;
            this.startEnergy = startEnergyIn;
            this.canExtract = canExtractIn;
            this.canRecieve = canReceiveIn;
        }

        public int getCapacity() {
            return this.capacity;
        }

        public int getMaxExtract() {
            return this.maxExtract;
        }

        public int getMaxReceive() {
            return this.maxReceive;
        }

        public int getStartEnergy() {
            return this.startEnergy;
        }

        public TileEntity getTile() {
            return this.tile;
        }

        public boolean isCanExtract() {
            return this.canExtract;
        }

        public boolean isCanRecieve() {
            return this.canRecieve;
        }

        public StorageProperties setCanExtract(final boolean canExtract) {
            this.canExtract = canExtract;
            return this;
        }

        public StorageProperties setCanRecieve(final boolean canRecieve) {
            this.canRecieve = canRecieve;
            return this;
        }

        public StorageProperties setCapacity(final int capacity) {
            this.capacity = capacity;
            return this;
        }

        public StorageProperties setMaxExtract(final int maxExtract) {
            this.maxExtract = maxExtract;
            return this;
        }

        public StorageProperties setMaxReceive(final int maxReceive) {
            this.maxReceive = maxReceive;
            return this;
        }

        public StorageProperties setStartEnergy(final int startEnergy) {
            this.startEnergy = startEnergy;
            return this;
        }

        public StorageProperties setTile(final TileEntity tile) {
            this.tile = tile;
            return this;
        }
    }
}
