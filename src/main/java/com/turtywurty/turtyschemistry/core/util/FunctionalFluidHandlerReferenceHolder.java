package com.turtywurty.turtyschemistry.core.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FunctionalFluidHandlerReferenceHolder extends FluidHandlerReferenceHolder {
    private final Supplier<FluidStackHandler> getter;
    private final Consumer<FluidStackHandler> setter;

    public FunctionalFluidHandlerReferenceHolder(final Supplier<FluidStackHandler> getter,
            final Consumer<FluidStackHandler> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public FluidStackHandler get() {
        return this.getter.get();
    }

    @Override
    public void set(final FluidStackHandler newValue) {
        this.setter.accept(newValue);
    }
}
