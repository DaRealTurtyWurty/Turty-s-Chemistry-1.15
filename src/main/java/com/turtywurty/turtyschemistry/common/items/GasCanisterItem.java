package com.turtywurty.turtyschemistry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class GasCanisterItem extends BlockItem {

	private boolean large;

	public GasCanisterItem(Block blockIn, Properties builder, boolean isLarge) {
		super(blockIn, builder);
		this.large = isLarge;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1.0D - (double) stack.getOrCreateChildTag("BlockEntityTag").getInt("GasStored")
				/ (double) stack.getOrCreateChildTag("BlockEntityTag").getInt("MaxAmount");
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return stack.getOrCreateChildTag("BlockEntityTag").getInt("GasStored") > 0;
	}

	public boolean isLarge() {
		return this.large;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 1;
	}
}
