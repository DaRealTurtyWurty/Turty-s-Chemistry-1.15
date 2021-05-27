package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.recipes.AutoclaveRecipe;
import com.turtywurty.turtyschemistry.common.recipes.BunsenBurnerRecipe;
import com.turtywurty.turtyschemistry.common.recipes.interfaces.IBunsenBurnerRecipe;
import com.turtywurty.turtyschemistry.common.recipes.serializers.AutoclaveRecipeSerializer;
import com.turtywurty.turtyschemistry.common.recipes.serializers.BunsenBurnerRecipeSerializer;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class RecipeSerializerInit {
	
	private RecipeSerializerInit() {}

	public static final IRecipeSerializer<BunsenBurnerRecipe> B_RECIPE_SERIALIZER = new BunsenBurnerRecipeSerializer();

	public static final IRecipeType<IBunsenBurnerRecipe> BUNSEN_TYPE = registerType(IBunsenBurnerRecipe.RECIPE_TYPE_ID);

	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister
			.create(ForgeRegistries.RECIPE_SERIALIZERS, TurtyChemistry.MOD_ID);

	public static final RegistryObject<IRecipeSerializer<AutoclaveRecipe>> AUTOCLAVE_RECIPE_SERIALIZER = SERIALIZERS
			.register("autoclave_recipe", AutoclaveRecipeSerializer::new);

	public static final RegistryObject<IRecipeSerializer<BunsenBurnerRecipe>> BUNSEN_BURNER_RECIPE = SERIALIZERS
			.register("bunsen_burner", () -> B_RECIPE_SERIALIZER);

	private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}

	private static <T extends IRecipeType> T registerType(ResourceLocation recipeTypeId) {
		return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
}
