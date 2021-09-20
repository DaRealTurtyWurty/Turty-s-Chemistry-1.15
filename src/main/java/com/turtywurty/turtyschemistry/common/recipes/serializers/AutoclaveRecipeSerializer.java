package com.turtywurty.turtyschemistry.common.recipes.serializers;

import com.google.gson.JsonObject;
import com.turtywurty.turtyschemistry.common.recipes.AutoclaveRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AutoclaveRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<AutoclaveRecipe> {

    @Override
    public AutoclaveRecipe read(final ResourceLocation recipeId, final JsonObject json) {
        final ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
        final Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));

        return new AutoclaveRecipe(recipeId, output, input);
    }

    @Override
    public AutoclaveRecipe read(final ResourceLocation recipeId, final PacketBuffer buffer) {
        final ItemStack output = buffer.readItemStack();
        final Ingredient input = Ingredient.read(buffer);

        return new AutoclaveRecipe(recipeId, output, input);
    }

    @Override
    public void write(final PacketBuffer buffer, final AutoclaveRecipe recipe) {
        final Ingredient input = recipe.getIngredients().get(0);
        input.write(buffer);

        buffer.writeItemStack(recipe.getRecipeOutput(), false);
    }
}