package com.turtywurty.turtyschemistry.common.blocks.silo;

import net.minecraft.util.IIntArray;

public class SiloSyncData implements IIntArray {

    private final SiloTileEntity te;

    public SiloSyncData(final SiloTileEntity te) {
        this.te = te;
    }

    @Override
    public int get(final int index) {
        switch (index) {
        case 0:
            return this.te.getCurrentPage();
        default:
            return 0;
        }
    }

    @Override
    public void set(final int index, final int value) {
        switch (index) {
        case 0:
            this.te.setCurrentPage(value);
            break;
        default:
            break;
        }
    }

    @Override
    public int size() {
        return 1;
    }
}