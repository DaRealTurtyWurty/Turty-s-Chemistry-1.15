package com.turtywurty.turtyschemistry.common.blocks.agitator;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.util.IStringSerializable;

public enum AgitatorType implements IStringSerializable {
	LIQUID_ONLY("liquid"), LIQUID_SOLID("solid"), LIQUID_GAS("gas");

	private static final Map<String, AgitatorType> NAME_LOOKUP = Arrays.stream(values())
			.collect(Collectors.toMap(AgitatorType::getString, type -> type));

	@Nullable
	public static AgitatorType byName(final String name) {
		return NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
	}

	private String name;

	AgitatorType(final String nameIn) {
		this.name = nameIn;
	}

	@Override
	public String getString() {
		return this.name;
	}
}
