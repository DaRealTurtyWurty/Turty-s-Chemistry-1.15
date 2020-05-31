package com.turtywurty.turtyschemistry.core.util;

import net.minecraft.block.BlockState;

public interface IGeneralMultiblock extends BlockstateProvider {
	
	default boolean isDummy() {
		BlockState state = getState();
		if (state.has(ModBlockProperties.MULTIBLOCKSLAVE))
			return state.get(ModBlockProperties.MULTIBLOCKSLAVE);
		else
			return true;
	}
}