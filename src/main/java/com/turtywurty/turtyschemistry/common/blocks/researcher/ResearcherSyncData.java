package com.turtywurty.turtyschemistry.common.blocks.researcher;

import net.minecraft.util.IIntArray;

public class ResearcherSyncData implements IIntArray {

    private final ResearcherTileEntity te;

    public ResearcherSyncData(final ResearcherTileEntity te) {
        this.te = te;
    }

    @Override
    public int get(final int index) {
        switch (index) {
        case 0:
            return this.te.getProcessingTime();
        case 1:
            return this.te.getMaxProcessingTime();
        default:
            return 0;
        }
    }

    @Override
    public void set(final int index, final int value) {
        switch (index) {
        case 0:
            this.te.setProcessingTime(value);
            break;
        case 1:
            break;
        default:
            break;
        }
    }

    @Override
    public int size() {
        return 2;
    }
}
