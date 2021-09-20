package com.turtywurty.turtyschemistry.common.blocks.boiler;

import javax.annotation.Nonnull;

public class BoilerRecipe {

    @Nonnull
    private String inputfluid;

    @Nonnull
    private String outputfluid;
    private String outputfluid2;

    @Nonnull
    private String outputgas;
    private String outputgas2;

    public String getInputFluid() {
        return this.inputfluid;
    }

    public String getOutputFluid() {
        return this.outputfluid;
    }

    public String getOutputFluid2() {
        return this.outputfluid2 == null ? "minecraft:empty" : this.outputfluid2;
    }

    public String getOutputGas() {
        return this.outputgas;
    }

    public String getOutputGas2() {
        return this.outputgas2 == null ? "minecraft:empty" : this.outputgas2;
    }
}
