package com.turtywurty.turtyschemistry.common.energy.cables;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class CableTileEntity extends TileEntity implements ITickableTileEntity {

    private CableNetwork network;
    private final int energyStored = 0;

    public CableTileEntity() {
        this(TileEntityTypeInit.CABLE.get());
        this.network = new CableNetwork(this.energyStored, getPos());
    }

    public CableTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public CableNetwork getCableNetwork() {
        return this.network;
    }

    @Override
    public void tick() {

    }
}
