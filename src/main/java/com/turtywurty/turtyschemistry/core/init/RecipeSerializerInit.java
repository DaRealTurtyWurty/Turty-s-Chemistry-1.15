package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.recipes.AutoclaveRecipe;
import com.turtywurty.turtyschemistry.common.recipes.BunsenBurnerRecipe;
import com.turtywurty.turtyschemistry.common.recipes.interfaces.IAutoclaveRecipe;
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

    public static final IRecipeType<IAutoclaveRecipe> AUTOCLAVE_TYPE = registerType(IAutoclaveRecipe.TYPE_ID);

    public static final IRecipeType<IBunsenBurnerRecipe> BUNSEN_TYPE = registerType(
            IBunsenBurnerRecipe.TYPE_ID);
    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, TurtyChemistry.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<AutoclaveRecipe>> AUTOCLAVE_SERIALIZER = SERIALIZERS
            .register("autoclave", AutoclaveRecipeSerializer::new);

    public static final RegistryObject<IRecipeSerializer<BunsenBurnerRecipe>> BUNSEN_SERIALIZER = SERIALIZERS
            .register("bunsen", BunsenBurnerRecipeSerializer::new);

    private RecipeSerializerInit() {
    }

    private static <T extends IRecipeType> T registerType(final ResourceLocation recipeTypeId) {
        return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
    }

    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }
}
