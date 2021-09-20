package com.turtywurty.turtyschemistry.core.util;

public abstract class FluidHandlerReferenceHolder {
    private FluidStackHandler lastKnownValue;

    public static FluidHandlerReferenceHolder create(final FluidStackHandler[] data, final int index) {
        return new FluidHandlerReferenceHolder() {
            @Override
            public FluidStackHandler get() {
                return data[index];
            }

            @Override
            public void set(final FluidStackHandler value) {
                data[index] = value;
            }
        };
    }

    public static FluidHandlerReferenceHolder create(final IFluidHandlerArray data, final int index) {
        return new FluidHandlerReferenceHolder() {
            @Override
            public FluidStackHandler get() {
                return data.get(index);
            }

            @Override
            public void set(final FluidStackHandler value) {
                data.set(index, value);
            }
        };
    }

    public static FluidHandlerReferenceHolder single() {
        return new FluidHandlerReferenceHolder() {
            private FluidStackHandler value;

            @Override
            public FluidStackHandler get() {
                return this.value;
            }

            @Override
            public void set(final FluidStackHandler value) {
                this.value = value;
            }
        };
    }

    public abstract FluidStackHandler get();

    public boolean isDirty() {
        final FluidStackHandler handler = get();
        final boolean flag = handler != this.lastKnownValue;
        this.lastKnownValue = handler;
        return flag;
    }

    public abstract void set(FluidStackHandler value);
}