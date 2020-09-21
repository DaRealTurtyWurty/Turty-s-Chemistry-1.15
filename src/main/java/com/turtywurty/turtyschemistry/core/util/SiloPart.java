package com.turtywurty.turtyschemistry.core.util;

import net.minecraft.util.IStringSerializable;

public enum SiloPart implements IStringSerializable {
	FRONT_BOTTOM_LEFT("front_bottom_left"), FRONT_BOTTOM_RIGHT("front_bottom_right"),
	BACK_BOTTOM_LEFT("back_bottom_left"), BACK_BOTTOM_RIGHT("back_bottom_right"),
	FRONT_MIDDLE_LEFT("front_middle_left"), FRONT_MIDDLE_RIGHT("front_middle_right"),
	BACK_MIDDLE_LEFT("back_middle_left"), BACK_MIDDLE_RIGHT("back_middle_right"), 
	FRONT_TOP_LEFT("front_top_left"), FRONT_TOP_RIGHT("front_top_right"), 
	BACK_TOP_LEFT("back_top_left"), BACK_TOP_RIGHT("back_top_right");

	private String name;

	private SiloPart(String nameIn) {
		this.name = nameIn;
	}

	@Override
	public String getName() {
		return this.name;
	}
}