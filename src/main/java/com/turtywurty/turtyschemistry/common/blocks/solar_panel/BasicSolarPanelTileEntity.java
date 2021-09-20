package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.tileentity.TileEntityType;

public class BasicSolarPanelTileEntity extends AbstractSolarPanelTileEntity {

    public BasicSolarPanelTileEntity() {
        this(TileEntityTypeInit.BASIC_SOLAR_PANEL.get(), 500, 2000);
    }

    public BasicSolarPanelTileEntity(final TileEntityType<?> tileEntityTypeIn, final int maxExtractIn,
            final int maxCapacityIn) {
        super(tileEntityTypeIn, maxExtractIn, maxCapacityIn);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
