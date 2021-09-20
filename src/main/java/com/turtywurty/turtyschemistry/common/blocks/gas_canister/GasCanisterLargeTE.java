package com.turtywurty.turtyschemistry.common.blocks.gas_canister;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.TileEntityType;

public class GasCanisterLargeTE extends AbstractGasCanisterTE {

    public GasCanisterLargeTE() {
        this(TileEntityTypeInit.GAS_CANISTER_L.get());
    }

    public GasCanisterLargeTE(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 40000);
    }
}
