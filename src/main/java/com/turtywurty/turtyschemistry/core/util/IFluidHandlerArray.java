package com.turtywurty.turtyschemistry.core.util;

public interface IFluidHandlerArray {
    FluidStackHandler get(int index);

    void set(int index, FluidStackHandler value);

    int size();
}
