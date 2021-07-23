package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.turtywurty.turtyschemistry.core.util.SimpleJsonDataManager;

import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings({ "unchecked", "deprecation" })
public class ElectrolyzerRecipeReader<T extends ElectrolyzerRecipe> extends SimpleJsonDataManager<T> {

	private static final Codec<ElectrolyzerRecipe> CODEC = RecordCodecBuilder
			.create(instance -> instance
					.group(FluidStack.CODEC.fieldOf("Input").forGetter(recipe -> recipe.input),
							FluidStack.CODEC.fieldOf("Output1").forGetter(recipe -> recipe.output1),
							FluidStack.CODEC.fieldOf("Output2").forGetter(recipe -> recipe.output2),
							Codec.INT.fieldOf("RatioInput").forGetter(recipe -> recipe.ratio0),
							Codec.INT.fieldOf("RatioOutput1").forGetter(recipe -> recipe.ratio1),
							Codec.INT.fieldOf("RatioOutput2").forGetter(recipe -> recipe.ratio2),
							Registry.ITEM.fieldOf("Anode").forGetter(recipe -> recipe.anode),
							Registry.ITEM.fieldOf("Cathode").forGetter(recipe -> recipe.cathode))
					.apply(instance,
							(input, output1, output2, ratioInput, ratioOutput1, ratioOutput2, anode, cathode) -> {
								ElectrolyzerRecipe recipe = new ElectrolyzerRecipe();
								recipe.input = input;
								recipe.output1 = output1;
								recipe.output2 = output2;
								recipe.ratio0 = ratioInput;
								recipe.ratio1 = ratioOutput1;
								recipe.ratio2 = ratioOutput2;
								recipe.anode = anode;
								recipe.cathode = cathode;
								return recipe;
							}));

	public ElectrolyzerRecipeReader() {
		super("electrolyzer", (Class<T>) ElectrolyzerRecipe.class);
	}

	@Override
	protected T getJsonAsData(final JsonElement json) {
		final AtomicReference<ElectrolyzerRecipe> recipe = new AtomicReference<>();
		CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(this::onError).ifPresent(recipe::set);
		return (T) recipe.get();
	}

	private void onError(final String errorMessage) {
		throw new JsonParseException(errorMessage);
	}
}
