package com.turtywurty.turtyschemistry.common.energy;

import net.minecraft.tileentity.TileEntity;

public class TurtEnergyStorage {
	protected int energy;
	private StorageProperties properties;

	public TurtEnergyStorage(StorageProperties propertiesIn) {
		this.properties = propertiesIn;
		this.energy = Math.max(0, Math.min(this.properties.getCapacity(), this.properties.getStartEnergy()));
	}

	public int receiveEnergy(int maxReceive, boolean simulate) {
		this.properties.getTile().markDirty();
		if (!this.properties.isCanRecieve())
			return 0;

		int energyReceived = Math.min(this.properties.getCapacity() - this.energy,
				Math.min(this.properties.getMaxReceive(), maxReceive));
		if (!simulate)
			this.energy += energyReceived;
		return energyReceived;
	}

	public int extractEnergy(int maxExtract, boolean simulate) {
		this.properties.getTile().markDirty();
		if (!this.properties.isCanExtract())
			return 0;

		int energyExtracted = Math.min(this.energy, Math.min(this.properties.getMaxExtract(), maxExtract));
		if (!simulate)
			this.energy -= energyExtracted;
		return energyExtracted;
	}

	public int getEnergyStored() {
		return this.energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public StorageProperties getProperties() {
		return this.properties;
	}

	public StorageProperties setProperties(StorageProperties properties) {
		this.properties = properties;
		return this.properties;
	}

	public static class StorageProperties {
		private TileEntity tile;
		private int capacity, maxReceive, maxExtract, startEnergy;
		private boolean canExtract, canRecieve;

		public StorageProperties(TileEntity tileIn, int capacityIn, int maxExtractIn, int maxReceiveIn, int startEnergyIn,
				boolean canExtractIn, boolean canReceiveIn) {
			this.tile = tileIn;
			this.capacity = capacityIn;
			this.maxExtract = maxExtractIn;
			this.maxReceive = maxReceiveIn;
			this.startEnergy = startEnergyIn;
			this.canExtract = canExtractIn;
			this.canRecieve = canReceiveIn;
		}

		public StorageProperties setTile(TileEntity tile) {
			this.tile = tile;
			return this;
		}

		public StorageProperties setCapacity(int capacity) {
			this.capacity = capacity;
			return this;
		}

		public StorageProperties setMaxReceive(int maxReceive) {
			this.maxReceive = maxReceive;
			return this;
		}

		public StorageProperties setMaxExtract(int maxExtract) {
			this.maxExtract = maxExtract;
			return this;
		}

		public StorageProperties setStartEnergy(int startEnergy) {
			this.startEnergy = startEnergy;
			return this;
		}

		public StorageProperties setCanExtract(boolean canExtract) {
			this.canExtract = canExtract;
			return this;
		}

		public StorageProperties setCanRecieve(boolean canRecieve) {
			this.canRecieve = canRecieve;
			return this;
		}

		public TileEntity getTile() {
			return this.tile;
		}

		public int getCapacity() {
			return this.capacity;
		}

		public int getMaxReceive() {
			return this.maxReceive;
		}

		public int getMaxExtract() {
			return this.maxExtract;
		}

		public int getStartEnergy() {
			return this.startEnergy;
		}

		public boolean isCanExtract() {
			return this.canExtract;
		}

		public boolean isCanRecieve() {
			return this.canRecieve;
		}
	}
}
