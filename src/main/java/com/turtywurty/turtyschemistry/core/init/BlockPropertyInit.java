package com.turtywurty.turtyschemistry.core.init;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockPropertyInit {
	
	public static final Block.Properties BASIC_MACHINE_PROPERTIES = Block.Properties.create(Material.IRON)
			.hardnessAndResistance(1.2f).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE);
}
