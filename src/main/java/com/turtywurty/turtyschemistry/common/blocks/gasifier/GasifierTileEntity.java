package com.turtywurty.turtyschemistry.common.blocks.gasifier;

import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.TileEntityType;

public class GasifierTileEntity extends InventoryTile {

	public GasifierTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 2);
	}

	public GasifierTileEntity() {
		this(TileEntityTypeInit.GASIFIER.get());
	}
}
