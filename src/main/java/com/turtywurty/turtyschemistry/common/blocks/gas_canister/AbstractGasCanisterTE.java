package com.turtywurty.turtyschemistry.common.blocks.gas_canister;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class AbstractGasCanisterTE extends TileEntity implements ITickableTileEntity {

	private int maxAmount, gasStored = 0;
	private String gasName = "";

	protected AbstractGasCanisterTE(TileEntityType<?> tileEntityTypeIn, int maxAmount) {
		super(tileEntityTypeIn);
		this.maxAmount = maxAmount;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		loadFromNbt(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		return saveToNbt(compound);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);

		return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(packet.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	public void loadFromNbt(CompoundNBT compound) {
		this.maxAmount = compound.getInt("MaxAmount");
		this.gasStored = compound.getInt("GasStored");
		this.gasName = compound.getString("GasName");
	}

	public CompoundNBT saveToNbt(CompoundNBT compound) {
		compound.putInt("MaxAmount", this.maxAmount);
		compound.putInt("GasStored", this.gasStored);
		compound.putString("GasName", this.gasName);
		return compound;
	}
	
	public int getGasStored() {
		return this.gasStored;
	}
	
	public int getMaxAmount() {
		return this.maxAmount;
	}
}
