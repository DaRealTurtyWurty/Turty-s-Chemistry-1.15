package com.turtywurty.turtyschemistry.common.recipes.interfaces;

import javax.annotation.Nonnull;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface IBunsenBurnerRecipe extends IRecipe<RecipeWrapper> {
	ResourceLocation TYPE_ID = new ResourceLocation(TurtyChemistry.MOD_ID, "bunsen_burner");

	@Override
	default boolean canFit(final int width, final int height) {
		return false;
	}

	Ingredient getInput();

	int getProcessTime();

	String getSpecialEffect();

	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getOrDefault(TYPE_ID);
	}
}
