package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.common.energy.TurtEnergyCap;
import com.turtywurty.turtyschemistry.common.energy.TurtEnergyStorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.common.util.LazyOptional;

public abstract class AbstractSolarPanelTileEntity extends TileEntity implements ITickableTileEntity {

	private int maxExtract, capacity;

	protected LazyOptional<TurtEnergyStorage> energyHandler = LazyOptional
			.of(() -> this.createEnergyStorage(this.capacity, 1000, this.maxExtract, 0, true, false));

	protected AbstractSolarPanelTileEntity(TileEntityType<?> tileEntityTypeIn, int maxExtractIn, int maxCapacityIn) {
		super(tileEntityTypeIn);
		this.maxExtract = maxExtractIn;
		this.capacity = maxCapacityIn;
	}

	@Override
	public void tick() {
		if (this.getBlock() == null) {
			this.remove();
			this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
					BlockFlags.BLOCK_UPDATE);
		}

		if (this.world.dimension.hasSkyLight() && !this.world.isRemote()) {
			this.generatePower();
		}
	}

	public AbstractSolarPanelBlock getBlock() {
		return this.getBlockState().getBlock() instanceof AbstractSolarPanelBlock
				? (AbstractSolarPanelBlock) this.getBlockState().getBlock()
				: null;
	}

	public void generatePower() {
		int i = this.world.getLightFor(LightType.SKY, pos) - this.world.getSkylightSubtracted();
		float angle = this.world.getCelestialAngleRadians(1.0F);
		if (i > 0) {
			float f = angle < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
			angle += (f - angle) * 0.2F;
			i = Math.round((float) i * MathHelper.cos(angle));
		}
		if (this.getEnergyStorage().getEnergyStored() != this.getEnergyStorage().getEnergyStored()
				+ (i * this.getBlock().getPanelInfo().get().perTick())) {
			this.getEnergyStorage().setEnergy(
					this.getEnergyStorage().getEnergyStored() + (i * this.getBlock().getPanelInfo().get().perTick()));
			this.markDirty();
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == TurtEnergyCap.ENERGY ? this.energyHandler.cast() : super.getCapability(cap, side);
	}

	public LazyOptional<TurtEnergyStorage> getEnergyHandler() {
		return this.energyHandler;
	}

	public TurtEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int defaultEnergy,
			boolean canExtract, boolean canRecieve) {
		return new TurtEnergyStorage(new TurtEnergyStorage.StorageProperties(this, 1000, 0, 1000, 0, true, false));
	}

	public TurtEnergyStorage getEnergyStorage() {
		return this.energyHandler.orElse(this.createEnergyStorage(1000, 1000, 1000, 0, true, false));
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.getEnergyStorage().setEnergy(compound.getInt("EnergyStored"));
		this.capacity = compound.getInt("EnergyCapacity");
		this.maxExtract = compound.getInt("MaxEnergyExtract");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("EnergyStored", this.getEnergyStorage().getEnergyStored());
		compound.putInt("EnergyCapacity", this.capacity);
		compound.putInt("MaxEnergyExtract", this.maxExtract);
		return compound;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	@Nonnull
	public CompoundNBT getUpdateTag() {
		return this.serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.deserializeNBT(tag);
	}
}
