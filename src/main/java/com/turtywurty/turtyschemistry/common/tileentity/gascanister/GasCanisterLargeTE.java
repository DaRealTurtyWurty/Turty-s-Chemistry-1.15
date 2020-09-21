package com.turtywurty.turtyschemistry.common.tileentity.gascanister;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.TileEntityType;

public class GasCanisterLargeTE extends AbstractGasCanisterTE {

	public GasCanisterLargeTE(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 40000);
	}

	public GasCanisterLargeTE() {
		this(TileEntityTypeInit.GAS_CANISTER_L.get());
	}
}
