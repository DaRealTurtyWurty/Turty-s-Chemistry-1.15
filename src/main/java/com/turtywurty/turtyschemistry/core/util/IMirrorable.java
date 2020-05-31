package com.turtywurty.turtyschemistry.core.util;

import net.minecraft.block.BlockState;

public interface IMirrorable extends BlockstateProvider {

	default boolean getIsMirrored() {
		BlockState state = getState();
		if (state.has(ModBlockProperties.MIRRORED))
			return state.get(ModBlockProperties.MIRRORED);
		else
			return false;
	}

	default void setMirrored(boolean mirrored) {
		BlockState state = getState();
		BlockState newState = state.with(ModBlockProperties.MIRRORED, mirrored);
		setState(newState);
	}
}
