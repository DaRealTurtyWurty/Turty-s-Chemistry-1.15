package com.turtywurty.turtyschemistry.common.recipes.serializers;

import com.google.gson.JsonObject;
import com.turtywurty.turtyschemistry.common.recipes.BunsenBurnerRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BunsenBurnerRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
		implements IRecipeSerializer<BunsenBurnerRecipe> {

	@Override
	public BunsenBurnerRecipe read(ResourceLocation recipeId, JsonObject json) {
		Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
		ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
		int processTime = JSONUtils.getInt(json, "process_time");
		String specialEffect = JSONUtils.getString(json, "special_effect");

		return new BunsenBurnerRecipe(recipeId, input, output, processTime, specialEffect);
	}

	@Override
	public BunsenBurnerRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
		Ingredient input = Ingredient.read(buffer);
		ItemStack output = buffer.readItemStack();
		int processTime = buffer.readInt();
		String specialEffect = buffer.readString();

		return new BunsenBurnerRecipe(recipeId, input, output, processTime, specialEffect);
	}

	@Override
	public void write(PacketBuffer buffer, BunsenBurnerRecipe recipe) {
		Ingredient input = recipe.getIngredients().get(0);
		input.write(buffer);
		buffer.writeItemStack(recipe.getRecipeOutput(), false);
		buffer.writeInt(recipe.getProcessTime());
		buffer.writeString(recipe.getSpecialEffect());
	}
}
