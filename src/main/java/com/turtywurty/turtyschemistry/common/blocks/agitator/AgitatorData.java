package com.turtywurty.turtyschemistry.common.blocks.agitator;

import javax.annotation.Nullable;

public class AgitatorData {
	
	private static final String EMPTY = "minecraft:empty";

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
		return inputfluid1 == null ? EMPTY : inputfluid1;
	}

	public String getInputfluid2() {
		return inputfluid2 == null ? EMPTY : inputfluid2;
	}

	public String getInputfluid3() {
		return inputfluid3 == null ? EMPTY : inputfluid3;
	}

	public String getInputfluid4() {
		return inputfluid4 == null ? EMPTY : inputfluid4;
	}

	public String getInputfluid5() {
		return inputfluid5 == null ? EMPTY : inputfluid5;
	}

	public String getOutputfluid() {
		return outputfluid == null ? EMPTY : outputfluid;
	}

	public String getOutputgas1() {
		return outputgas1 == null ? EMPTY : outputgas1;
	}

	public String getOutputgas2() {
		return outputgas2 == null ? EMPTY : outputgas2;
	}
}
