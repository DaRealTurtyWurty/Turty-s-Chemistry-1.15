package com.turtywurty.turtyschemistry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class GasCanisterItem extends BlockItem {

    private final boolean large;

    public GasCanisterItem(final Block blockIn, final Properties builder, final boolean isLarge) {
        super(blockIn, builder);
        this.large = isLarge;
    }

    @Override
    public double getDurabilityForDisplay(final ItemStack stack) {
        return 1.0D - (double) stack.getOrCreateChildTag("BlockEntityTag").getInt("GasStored")
                / (double) stack.getOrCreateChildTag("BlockEntityTag").getInt("MaxAmount");
    }

    @Override
    public int getMaxDamage(final ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean isDamaged(final ItemStack stack) {
        return stack.getOrCreateChildTag("BlockEntityTag").getInt("GasStored") > 0;
    }

    public boolean isLarge() {
        return this.large;
    }
}
