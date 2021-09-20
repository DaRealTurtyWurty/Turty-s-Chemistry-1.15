package com.turtywurty.turtyschemistry.common.recipes;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.common.recipes.interfaces.IAutoclaveRecipe;
import com.turtywurty.turtyschemistry.core.init.RecipeSerializerInit;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class AutoclaveRecipe implements IAutoclaveRecipe {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;

    public AutoclaveRecipe(final ResourceLocation id, final ItemStack output, final Ingredient input) {
        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public ItemStack getCraftingResult(final RecipeWrapper inv) {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(Ingredient.EMPTY, this.input);
    }

    @Override
    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.AUTOCLAVE_SERIALIZER.get();
    }

    @Override
    public boolean matches(final RecipeWrapper inv, @Nullable final World worldIn) {
        return this.input.test(inv.getStackInSlot(0));
    }
}