package com.turtywurty.turtyschemistry.common.tileentity.gascanister;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.TileEntityType;

public class GasCanisterSmallTE extends AbstractGasCanisterTE {

	public GasCanisterSmallTE(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 10000);
	}

	public GasCanisterSmallTE() {
		this(TileEntityTypeInit.GAS_CANISTER_S.get());
	}
}
