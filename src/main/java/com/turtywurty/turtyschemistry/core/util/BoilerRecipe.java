package com.turtywurty.turtyschemistry.core.util;

import javax.annotation.Nonnull;

public class BoilerRecipe {

	@Nonnull
	private String inputfluid;

	@Nonnull
	private String outputfluid;
	private String outputfluid2;

	@Nonnull
	private String outputgas;
	private String outputgas2;

	public String getInputFluid() {
		return inputfluid;
	}

	public String getOutputFluid() {
		return outputfluid;
	}

	public String getOutputGas() {
		return outputgas;
	}

	public String getOutputFluid2() {
		return outputfluid2 == null ? "minecraft:empty" : outputfluid2;
	}

	public String getOutputGas2() {
		return outputgas2 == null ? "minecraft:empty" : outputgas2;
	}
}
