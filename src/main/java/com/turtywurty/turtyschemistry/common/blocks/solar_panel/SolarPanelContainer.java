package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class SolarPanelContainer extends Container {

    public SolarPanelContainer(final ContainerType<?> type, final int id) {
        super(type, id);
    }

    @Override
    public boolean canInteractWith(final PlayerEntity playerIn) {
        return false;
    }

}
