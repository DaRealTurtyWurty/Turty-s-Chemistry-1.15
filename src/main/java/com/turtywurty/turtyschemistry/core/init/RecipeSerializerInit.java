package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.recipes.AutoclaveRecipe;
import com.turtywurty.turtyschemistry.common.recipes.serializers.AutoclaveRecipeSerializer;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerInit {

	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = new DeferredRegister(
			ForgeRegistries.RECIPE_SERIALIZERS, TurtyChemistry.MOD_ID);

	public static final RegistryObject<IRecipeSerializer<AutoclaveRecipe>> AUTOCLAVE_RECIPE_SERIALIZER = SERIALIZERS
			.register("autoclave_recipe", () -> new AutoclaveRecipeSerializer());
}
