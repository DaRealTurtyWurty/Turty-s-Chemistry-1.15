package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.common.energy.TurtEnergyCap;
import com.turtywurty.turtyschemistry.common.energy.TurtEnergyStorage;

import net.minecraft.block.BlockState;
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
            .of(() -> createEnergyStorage(this.capacity, 1000, this.maxExtract, 0, true, false));

    protected AbstractSolarPanelTileEntity(final TileEntityType<?> tileEntityTypeIn, final int maxExtractIn,
            final int maxCapacityIn) {
        super(tileEntityTypeIn);
        this.maxExtract = maxExtractIn;
        this.capacity = maxCapacityIn;
    }

    public TurtEnergyStorage createEnergyStorage(final int capacity, final int maxReceive,
            final int maxExtract, final int defaultEnergy, final boolean canExtract,
            final boolean canRecieve) {
        return new TurtEnergyStorage(
                new TurtEnergyStorage.StorageProperties(this, 1000, 0, 1000, 0, true, false));
    }

    public void generatePower() {
        int i = this.world.getLightFor(LightType.SKY, this.pos) - this.world.getSkylightSubtracted();
        float angle = this.world.getCelestialAngleRadians(1.0F);
        if (i > 0) {
            final float f = angle < (float) Math.PI ? 0.0F : (float) Math.PI * 2F;
            angle += (f - angle) * 0.2F;
            i = Math.round(i * MathHelper.cos(angle));
        }
        if (getEnergyStorage().getEnergyStored() != getEnergyStorage().getEnergyStored()
                + i * getBlock().getPanelInfo().get().perTick()) {
            getEnergyStorage().setEnergy(
                    getEnergyStorage().getEnergyStored() + i * getBlock().getPanelInfo().get().perTick());
            markDirty();
        }
    }

    public AbstractSolarPanelBlock getBlock() {
        return getBlockState().getBlock() instanceof AbstractSolarPanelBlock
                ? (AbstractSolarPanelBlock) getBlockState().getBlock()
                : null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> cap, final Direction side) {
        return cap == TurtEnergyCap.ENERGY ? this.energyHandler.cast() : super.getCapability(cap, side);
    }

    public LazyOptional<TurtEnergyStorage> getEnergyHandler() {
        return this.energyHandler;
    }

    public TurtEnergyStorage getEnergyStorage() {
        return this.energyHandler.orElse(createEnergyStorage(1000, 1000, 1000, 0, true, false));
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), -1, getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return serializeNBT();
    }

    @Override
    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        this.deserializeNBT(tag);
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        handleUpdateTag(this.world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        super.read(state, compound);
        getEnergyStorage().setEnergy(compound.getInt("EnergyStored"));
        this.capacity = compound.getInt("EnergyCapacity");
        this.maxExtract = compound.getInt("MaxEnergyExtract");
    }

    @Override
    public void tick() {
        if (getBlock() == null) {
            remove();
            getWorld().notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), BlockFlags.BLOCK_UPDATE);
        }

        if (this.world.getDimensionType().hasSkyLight() && !this.world.isRemote()) {
            generatePower();
        }
    }

    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        super.write(compound);
        compound.putInt("EnergyStored", getEnergyStorage().getEnergyStored());
        compound.putInt("EnergyCapacity", this.capacity);
        compound.putInt("MaxEnergyExtract", this.maxExtract);
        return compound;
    }
}
