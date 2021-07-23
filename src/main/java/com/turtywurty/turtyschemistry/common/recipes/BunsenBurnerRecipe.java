package com.turtywurty.turtyschemistry.common.recipes;

import com.turtywurty.turtyschemistry.common.recipes.interfaces.IBunsenBurnerRecipe;
import com.turtywurty.turtyschemistry.core.init.RecipeSerializerInit;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class BunsenBurnerRecipe implements IBunsenBurnerRecipe {

	private final ResourceLocation id;
	private Ingredient input;
	private final ItemStack output;
	private final int processTime;
	private final String specialEffect;

	public BunsenBurnerRecipe(ResourceLocation id, Ingredient input, ItemStack output, int processTime,
			String specialEffect) {
		this.id = id;
		this.output = output;
		this.input = input;
		this.processTime = processTime;
		this.specialEffect = specialEffect;
	}

	@Override
	public boolean matches(RecipeWrapper inv, World worldIn) {
		return this.input.test(inv.getStackInSlot(0));
	}

	@Override
	public ItemStack getCraftingResult(RecipeWrapper inv) {
		return this.output;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return RecipeSerializerInit.BUNSEN_SERIALIZER.get();
	}

	@Override
	public Ingredient getInput() {
		return this.input;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.from(Ingredient.EMPTY, this.input);
	}

	@Override
	public int getProcessTime() {
		return this.processTime;
	}

	@Override
	public String getSpecialEffect() {
		return this.specialEffect;
	}
}
