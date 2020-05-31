package com.turtywurty.turtyschemistry.core.util;

import net.minecraft.block.BlockState;

public interface BlockstateProvider {

	BlockState getState();

	void setState(BlockState newState);
}
