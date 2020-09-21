package com.turtywurty.turtyschemistry.common.cables;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class CableTileEntity extends TileEntity implements ITickableTileEntity {

	private CableNetwork network;
	private int energyStored = 0;

	public CableTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public CableTileEntity() {
		this(TileEntityTypeInit.CABLE.get());
		this.network = new CableNetwork(this.energyStored, this.getPos());
	}

	@Override
	public void tick() {

	}

	public CableNetwork getCableNetwork() {
		return this.network;
	}
}
