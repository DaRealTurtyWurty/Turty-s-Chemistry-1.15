package com.turtywurty.turtyschemistry.mixins;

import org.spongepowered.asm.mixin.Mixin;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.ITurtyChemistryGas;

import net.minecraft.block.AirBlock;
import net.minecraft.util.ResourceLocation;

@Mixin(AirBlock.class)
public abstract class AirBlockToGas extends AirBlock implements ITurtyChemistryGas {

	protected AirBlockToGas(Properties properties) {
		super(properties);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(TurtyChemistry.MOD_ID);
	}
}
