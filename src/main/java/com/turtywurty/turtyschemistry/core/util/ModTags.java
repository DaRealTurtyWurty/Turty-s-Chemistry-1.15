package com.turtywurty.turtyschemistry.core.util;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public final class ModTags {
	
	private ModTags() {}

	public static final Tag<Item> SAWDUST = new Tag<>(new ResourceLocation(TurtyChemistry.MOD_ID, "sawdust"));
}
