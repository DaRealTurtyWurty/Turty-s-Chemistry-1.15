package com.turtywurty.turtyschemistry.core.util;

import javax.annotation.Nullable;

public class AgitatorData {

	private String inputfluid1;
	private String inputfluid2;
	@Nullable
	private String inputfluid3;
	@Nullable
	private String inputfluid4;
	@Nullable
	private String inputfluid5;

	private String outputfluid;
	@Nullable
	private String outputgas1;
	@Nullable
	private String outputgas2;

	public String getInputfluid1() {
		return inputfluid1 == null ? "minecraft:empty" : inputfluid1;
	}

	public String getInputfluid2() {
		return inputfluid2 == null ? "minecraft:empty" : inputfluid2;
	}

	public String getInputfluid3() {
		return inputfluid3 == null ? "minecraft:empty" : inputfluid3;
	}

	public String getInputfluid4() {
		return inputfluid4 == null ? "minecraft:empty" : inputfluid4;
	}

	public String getInputfluid5() {
		return inputfluid5 == null ? "minecraft:empty" : inputfluid5;
	}

	public String getOutputfluid() {
		return outputfluid == null ? "minecraft:empty" : outputfluid;
	}

	public String getOutputgas1() {
		return outputgas1 == null ? "minecraft:empty" : outputgas1;
	}

	public String getOutputgas2() {
		return outputgas2 == null ? "minecraft:empty" : outputgas2;
	}
}
