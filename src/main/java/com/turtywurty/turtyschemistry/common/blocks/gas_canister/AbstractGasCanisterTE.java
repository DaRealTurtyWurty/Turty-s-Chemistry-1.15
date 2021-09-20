package com.turtywurty.turtyschemistry.common.blocks.gas_canister;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class AbstractGasCanisterTE extends TileEntity implements ITickableTileEntity {

    private int maxAmount, gasStored = 0;
    private String gasName = "";

    protected AbstractGasCanisterTE(final TileEntityType<?> tileEntityTypeIn, final int maxAmount) {
        super(tileEntityTypeIn);
        this.maxAmount = maxAmount;
    }

    public int getGasStored() {
        return this.gasStored;
    }

    public int getMaxAmount() {
        return this.maxAmount;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        final CompoundNBT nbt = new CompoundNBT();
        write(nbt);

        return new SUpdateTileEntityPacket(getPos(), 1, nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        read(state, tag);
    }

    public void loadFromNbt(final CompoundNBT compound) {
        this.maxAmount = compound.getInt("MaxAmount");
        this.gasStored = compound.getInt("GasStored");
        this.gasName = compound.getString("GasName");
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
        read(this.world.getBlockState(packet.getPos()), packet.getNbtCompound());
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        super.read(state, compound);
        loadFromNbt(compound);
    }

    public CompoundNBT saveToNbt(final CompoundNBT compound) {
        compound.putInt("MaxAmount", this.maxAmount);
        compound.putInt("GasStored", this.gasStored);
        compound.putString("GasName", this.gasName);
        return compound;
    }

    @Override
    public void tick() {

    }

    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        super.write(compound);
        return saveToNbt(compound);
    }
}
