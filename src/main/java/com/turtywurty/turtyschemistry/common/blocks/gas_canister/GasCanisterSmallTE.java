package com.turtywurty.turtyschemistry.common.blocks.gas_canister;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.TileEntityType;

public class GasCanisterSmallTE extends AbstractGasCanisterTE {

    public GasCanisterSmallTE() {
        this(TileEntityTypeInit.GAS_CANISTER_S.get());
    }

    public GasCanisterSmallTE(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 10000);
    }
}
