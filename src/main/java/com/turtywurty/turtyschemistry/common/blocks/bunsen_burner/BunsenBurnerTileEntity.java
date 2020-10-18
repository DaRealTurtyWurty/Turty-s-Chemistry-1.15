package com.turtywurty.turtyschemistry.common.blocks.bunsen_burner;

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

import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants.BlockFlags;

public class BunsenBurnerTileEntity extends InventoryTile {

	private int completionTime;
	private int maxTime;

	public BunsenBurnerTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 1);
	}

	public BunsenBurnerTileEntity() {
		this(TileEntityTypeInit.BUNSEN_BURNER.get());
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isRemote) {
			if (this.getRecipe(this.getItemInSlot(0)) != null) {
				this.maxTime = this.getRecipe(this.getItemInSlot(0)).getProcessTime();

				if (this.isBurning()) {
					if (this.completionTime >= this.maxTime) {
						System.out.println("done");
						ItemStack output = this.getRecipe(this.getItemInSlot(0)).getRecipeOutput().copy();
						this.getInventory().setStackInSlot(0, output);
						this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
								BlockFlags.BLOCK_UPDATE);
						this.completionTime = 0;
						this.maxTime = 0;
					} else {
						this.completionTime++;
					}
					this.markDirty();
				} else {
					if (this.canStartBurning()) {
						this.completionTime++;
						this.markDirty();
					}
				}
			}
		}
	}

	public int getCompletionTime() {
		return this.completionTime;
	}

	public int getMaxTime() {
		return this.maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public void setCompletionTime(int completionTime) {
		this.completionTime = completionTime;
	}

	public boolean isBurning() {
		return !this.getBlockState().get(BunsenBurnerBlock.HAS_GAS) && this.completionTime != 0
				&& this.completionTime <= this.maxTime;
	}

	public boolean canStartBurning() {
		return !this.getBlockState().get(BunsenBurnerBlock.HAS_GAS) && this.completionTime < this.maxTime;
	}

	@Nullable
	public BunsenBurnerRecipe getRecipe(ItemStack stack) {
		if (stack == null) {
			return null;
		}

		Set<IRecipe<?>> recipes = findRecipesByType(RecipeSerializerInit.BUNSEN_TYPE, this.world);
		for (IRecipe<?> iRecipe : recipes) {
			BunsenBurnerRecipe recipe = (BunsenBurnerRecipe) iRecipe;
			if (recipe.getInput().test(stack)) {
				return recipe;
			}
		}

		return null;
	}

	public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
		return world != null ? world.getRecipeManager().getRecipes().stream()
				.filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
	}

	@OnlyIn(Dist.CLIENT)
	public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn) {
		ClientWorld world = ClientUtils.getClientWorld();
		return world != null ? world.getRecipeManager().getRecipes().stream()
				.filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
	}

	public static Set<ItemStack> getAllRecipeInputs(IRecipeType<?> typeIn, World worldIn) {
		Set<ItemStack> inputs = new HashSet<ItemStack>();
		Set<IRecipe<?>> recipes = findRecipesByType(typeIn, worldIn);
		for (IRecipe<?> recipe : recipes) {
			NonNullList<Ingredient> ingredients = recipe.getIngredients();
			ingredients.forEach(ingredient -> {
				for (ItemStack stack : ingredient.getMatchingStacks()) {
					inputs.add(stack);
				}
			});
		}
		return inputs;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.completionTime = compound.getInt("CompletionTime");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("CompletionTime", this.completionTime);
		return super.write(compound);
	}
}
