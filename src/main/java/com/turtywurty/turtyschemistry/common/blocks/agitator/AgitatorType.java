package com.turtywurty.turtyschemistry.common.blocks.agitator;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.util.IStringSerializable;

public enum AgitatorType implements IStringSerializable {
	LIQUID_ONLY("liquid"), LIQUID_SOLID("solid"), LIQUID_GAS("gas");

	private String name;

	private AgitatorType(String nameIn) {
		this.name = nameIn;
	}

	private static final Map<String, AgitatorType> NAME_LOOKUP = Arrays.stream(values())
			.collect(Collectors.toMap(AgitatorType::getName, type -> type));

	@Override
	public String getName() {
		return this.name;
	}

	@Nullable
	public static AgitatorType byName(String name) {
		return NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
	}
}
