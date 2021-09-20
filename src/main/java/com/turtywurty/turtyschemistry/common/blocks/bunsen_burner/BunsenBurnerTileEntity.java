package com.turtywurty.turtyschemistry.common.blocks.bunsen_burner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.recipes.BunsenBurnerRecipe;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.RecipeSerializerInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;

public class BunsenBurnerTileEntity extends InventoryTile {

    private int completionTime;

    private int maxTime;

    public BunsenBurnerTileEntity() {
        this(TileEntityTypeInit.BUNSEN_BURNER.get());
    }

    public BunsenBurnerTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 1);
    }

    public static Set<IRecipe<?>> findRecipesByType(final IRecipeType<?> typeIn) {
        final ClientWorld world = ClientUtils.getClientWorld();
        return world != null
                ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn)
                        .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    public static Set<IRecipe<?>> findRecipesByType(final IRecipeType<?> typeIn, final World world) {
        return world != null
                ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn)
                        .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    public static Set<ItemStack> getAllRecipeInputs(final IRecipeType<?> typeIn, final World worldIn) {
        final Set<ItemStack> inputs = new HashSet<>();
        final Set<IRecipe<?>> recipes = findRecipesByType(typeIn, worldIn);
        for (final IRecipe<?> recipe : recipes) {
            final NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(ingredient -> inputs.addAll(Arrays.asList(ingredient.getMatchingStacks())));
        }
        return inputs;
    }

    public boolean canStartBurning() {
        return !getBlockState().get(BunsenBurnerBlock.HAS_GAS) && this.completionTime < this.maxTime;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public int getMaxTime() {
        return this.maxTime;
    }

    @Nullable
    public BunsenBurnerRecipe getRecipe(final ItemStack stack) {
        if (stack == null)
            return null;

        final Set<IRecipe<?>> recipes = findRecipesByType(RecipeSerializerInit.BUNSEN_TYPE, this.world);
        for (final IRecipe<?> iRecipe : recipes) {
            final BunsenBurnerRecipe recipe = (BunsenBurnerRecipe) iRecipe;
            if (recipe.getInput().test(stack))
                return recipe;
        }

        return null;
    }

    public boolean isBurning() {
        return !getBlockState().get(BunsenBurnerBlock.HAS_GAS) && this.completionTime != 0
                && this.completionTime <= this.maxTime;
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        super.read(state, compound);
        this.completionTime = compound.getInt("CompletionTime");
    }

    public void setCompletionTime(final int completionTime) {
        this.completionTime = completionTime;
    }

    public void setMaxTime(final int maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote && getRecipe(getItemInSlot(0)) != null) {
            this.maxTime = getRecipe(getItemInSlot(0)).getProcessTime();
            if (isBurning()) {
                if (this.completionTime >= this.maxTime) {
                    final ItemStack output = getRecipe(getItemInSlot(0)).getRecipeOutput().copy();
                    getInventory().setStackInSlot(0, output);
                    this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
                            BlockFlags.BLOCK_UPDATE);
                    this.completionTime = 0;
                    this.maxTime = 0;
                } else {
                    this.completionTime++;
                }
                markDirty();
            } else if (canStartBurning()) {
                this.completionTime++;
                markDirty();
            }
        }
    }

    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        compound.putInt("CompletionTime", this.completionTime);
        return super.write(compound);
    }
}
