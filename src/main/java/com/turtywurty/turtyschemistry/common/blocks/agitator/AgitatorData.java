package com.turtywurty.turtyschemistry.common.blocks.agitator;

import javax.annotation.Nullable;

public class AgitatorData {

    private static final String EMPTY = "minecraft:empty";

    private String inputfluid1;
    private String inputfluid2;
    @Nullable
    private String inputfluid3;
    @Nullable
    private String inputfluid4;
    @Nullable
    private String inputfluid5;

    private String outputfluid;
    @Nullable
    private String outputgas1;
    @Nullable
    private String outputgas2;

    public String getInputfluid1() {
        return this.inputfluid1 == null ? EMPTY : this.inputfluid1;
    }

    public String getInputfluid2() {
        return this.inputfluid2 == null ? EMPTY : this.inputfluid2;
    }

    public String getInputfluid3() {
        return this.inputfluid3 == null ? EMPTY : this.inputfluid3;
    }

    public String getInputfluid4() {
        return this.inputfluid4 == null ? EMPTY : this.inputfluid4;
    }

    public String getInputfluid5() {
        return this.inputfluid5 == null ? EMPTY : this.inputfluid5;
    }

    public String getOutputfluid() {
        return this.outputfluid == null ? EMPTY : this.outputfluid;
    }

    public String getOutputgas1() {
        return this.outputgas1 == null ? EMPTY : this.outputgas1;
    }

    public String getOutputgas2() {
        return this.outputgas2 == null ? EMPTY : this.outputgas2;
    }
}
