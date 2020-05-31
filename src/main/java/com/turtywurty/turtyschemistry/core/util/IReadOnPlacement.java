package com.turtywurty.turtyschemistry.core.util;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IReadOnPlacement {

	void readOnPlacement(@Nullable LivingEntity placer, ItemStack stack);
}
