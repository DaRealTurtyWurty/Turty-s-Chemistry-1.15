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
    private final Ingredient input;
    private final ItemStack output;
    private final int processTime;
    private final String specialEffect;

    public BunsenBurnerRecipe(final ResourceLocation id, final Ingredient input, final ItemStack output,
            final int processTime, final String specialEffect) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.processTime = processTime;
        this.specialEffect = specialEffect;
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
    public int getProcessTime() {
        return this.processTime;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.BUNSEN_SERIALIZER.get();
    }

    @Override
    public String getSpecialEffect() {
        return this.specialEffect;
    }

    @Override
    public boolean matches(final RecipeWrapper inv, final World worldIn) {
        return this.input.test(inv.getStackInSlot(0));
    }
}
