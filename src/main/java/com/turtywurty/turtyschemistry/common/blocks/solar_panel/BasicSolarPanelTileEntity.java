package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.TileEntityType;

public class BasicSolarPanelTileEntity extends AbstractSolarPanelTileEntity {

	public BasicSolarPanelTileEntity(TileEntityType<?> tileEntityTypeIn, int maxExtractIn, int maxCapacityIn) {
		super(tileEntityTypeIn, maxExtractIn, maxCapacityIn);
	}

	public BasicSolarPanelTileEntity() {
		this(TileEntityTypeInit.BASIC_SOLAR_PANEL.get(), 500, 2000);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
}
