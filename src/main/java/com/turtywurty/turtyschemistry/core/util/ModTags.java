package com.turtywurty.turtyschemistry.core.util;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.tags.ITag.ItemEntry;
import net.minecraft.util.ResourceLocation;

public final class ModTags {

    public static final ItemEntry SAWDUST = new ItemEntry(
            new ResourceLocation(TurtyChemistry.MOD_ID, "sawdust"));

    private ModTags() {
    }
}
