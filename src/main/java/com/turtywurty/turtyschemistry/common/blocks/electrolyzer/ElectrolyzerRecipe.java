package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import javax.annotation.Nonnull;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fluids.FluidStack;

public class ElectrolyzerRecipe {

    @Nonnull
    protected FluidStack input = FluidStack.EMPTY, output1 = FluidStack.EMPTY, output2 = FluidStack.EMPTY;
    @Nonnull
    protected Item anode = Items.AIR, cathode = Items.AIR;
    protected int ratio0 = 1, ratio1 = 1, ratio2 = 1;
}
