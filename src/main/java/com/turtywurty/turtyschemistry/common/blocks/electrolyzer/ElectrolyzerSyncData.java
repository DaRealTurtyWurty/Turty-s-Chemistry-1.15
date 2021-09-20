package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import net.minecraft.util.IIntArray;

public class ElectrolyzerSyncData implements IIntArray {

    private final ElectrolyzerTileEntity te;

    public ElectrolyzerSyncData(final ElectrolyzerTileEntity te) {
        this.te = te;
    }

    @Override
    public int get(final int index) {
        switch (index) {
        case 0:
            return this.te.runningTime;
        case 1:
            return this.te.maxRunningTime;
        case 2:
            return this.te.maxInput;
        case 3:
            return this.te.maxOutput1;
        case 4:
            return this.te.maxOutput2;
        default:
            return 0;
        }
    }

    @Override
    public void set(final int index, final int value) {
        switch (index) {
        case 0:
            this.te.runningTime = value;
            break;
        case 1:
            this.te.maxRunningTime = value;
            break;
        case 2:
            this.te.maxInput = value;
            break;
        case 3:
            this.te.maxOutput1 = value;
            break;
        case 4:
            this.te.maxOutput2 = value;
            break;
        default:
            break;
        }
    }

    @Override
    public int size() {
        return 5;
    }
}
