package com.turtywurty.turtyschemistry.common.blocks.boiler;

import net.minecraft.util.IIntArray;

public class BoilerSyncData implements IIntArray {

    private final BoilerTileEntity te;

    public BoilerSyncData(final BoilerTileEntity te) {
        this.te = te;
    }

    @Override
    public int get(final int index) {
        switch (index) {
        case 0:
            return this.te.getRunningTime();
        case 1:
            return this.te.getMaxRunningTime();
        case 2:
            return this.te.getTemperature();
        default:
            return 0;
        }
    }

    @Override
    public void set(final int index, final int value) {
        switch (index) {
        case 0:
            this.te.setRunningTime(value);
            break;
        case 1:
            this.te.setMaxRunningTime(value);
            break;
        case 2:
            this.te.setTemperature(value);
            break;
        default:
            break;
        }
    }

    @Override
    public int size() {
        return 3;
    }
}