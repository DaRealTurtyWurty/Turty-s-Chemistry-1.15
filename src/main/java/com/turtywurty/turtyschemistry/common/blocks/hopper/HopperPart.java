package com.turtywurty.turtyschemistry.common.blocks.hopper;

import net.minecraft.util.IStringSerializable;

public enum HopperPart implements IStringSerializable {
	FRONT_BOTTOM_LEFT("front_bottom_left"), FRONT_BOTTOM_RIGHT("front_bottom_right"),
	BACK_BOTTOM_LEFT("back_bottom_left"), BACK_BOTTOM_RIGHT("back_bottom_right"), FRONT_TOP_LEFT("front_top_left"),
	FRONT_TOP_RIGHT("front_top_right"), BACK_TOP_LEFT("back_top_left"), BACK_TOP_RIGHT("back_top_right");

	private String name;

	HopperPart(final String nameIn) {
		this.name = nameIn;
	}

	@Override
	public String getString() {
		return this.name;
	}
}