package com.turtywurty.turtyschemistry.common.blocks.researcher;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class RecipeButton extends ExtendedButton {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/blueprint_section.png");
	private final int baseY;
	private final ResearcherScreen screen;
	private final IRecipe<CraftingInventory> recipe;
	public ItemStack tooltipItem = ItemStack.EMPTY;

	public RecipeButton(ResearcherScreen screen, IRecipe<CraftingInventory> recipe, int x, int y, int width) {
		super(x, y, width, getHeightForRecipe(recipe), "", button -> onButtonClicked(screen.getContainer(), recipe));
		this.baseY = y;
		this.screen = screen;
		this.recipe = recipe;
	}

	public static void onButtonClicked(ResearcherContainer container, IRecipe<CraftingInventory> recipe) {
		TurtyChemistry.packetHandler.sendToServer(new ResearcherRecipeButtonPacket(recipe.getId()));
		// Assemble recipe
	}

	public static int getHeightForRecipe(IRecipe<?> recipe) {
		int rows = 1 + (recipe.getIngredients().size() - 1) / 3;
		return (rows * 18) + 4; // 2 padding on top, 2 on bottom
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return false;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void renderButton(int mouseX, int mouseY, float partial) {
		this.tooltipItem = ItemStack.EMPTY;
		if (this.visible) {
			super.renderButton(mouseX, mouseY, partial);
			NonNullList<Ingredient> ingredients = this.recipe.getIngredients();
			int ingredientCount = ingredients.size();
			// render ingredients
			for (int ingredientIndex = 0; ingredientIndex < ingredientCount; ingredientIndex++) {
				ItemStack stack = getIngredientVariant(ingredients.get(ingredientIndex).getMatchingStacks());

				int itemRow = ingredientIndex / 3;
				int itemColumn = ingredientIndex % 3;
				int itemOffsetX = 2 + itemColumn * 18;
				int itemOffsetY = 2 + itemRow * 18;
				int itemX = this.x + itemOffsetX;
				int itemY = this.y + itemOffsetY;
				int itemEndX = itemX + 18;
				int itemEndY = itemY + 18;
				this.screen.renderItemStack(stack, itemX, itemY);
				if (mouseX >= itemX && mouseX < itemEndX && mouseY >= itemY && mouseY < itemEndY) {
					this.tooltipItem = stack;
				}
			}
			if (ingredientCount > 0) {
				int extraIngredientRows = (ingredientCount - 1) / 3; // 0 if 3 ingredients, 1 if 4-6 ingredients, 2 if
																		// 7-9 ingredients, etc
				int arrowX = this.x + 2 + (18 * 3) + 4;
				int arrowY = this.y + 2 + 4 + 9 * extraIngredientRows;
				int arrowWidth = 10;
				int arrowHeight = 9;
				int arrowU = 15;
				int arrowV = 171;
				this.screen.minecraft.textureManager.bindTexture(TEXTURE);
				AbstractGui.blit(arrowX, arrowY, arrowU, arrowV, arrowWidth, arrowHeight, 512, 256);

				// render the output item
				ItemStack outputStack = this.recipe.getRecipeOutput();
				if (!outputStack.isEmpty()) {
					int itemX = this.x + 2 + (18 * 4);
					int itemY = this.y + 2 + 9 * extraIngredientRows;
					int itemEndX = itemX + 18;
					int itemEndY = itemY + 18;
					this.screen.renderItemStack(outputStack, itemX, itemY);
					if (mouseX >= itemX && mouseX < itemEndX && mouseY >= itemY && mouseY < itemEndY) {
						this.tooltipItem = outputStack;
					}
				}

			}
		}
	}

	public static ItemStack getIngredientVariant(ItemStack[] variants) {
		int variantCount = variants.length;
		if (variantCount > 0) {
			// if this ingredient has multiple stacks, cycle through them
			int variantIndex = (int) ((Util.milliTime() / 1000L) / variantCount);
			return variants[MathHelper.clamp(variantIndex, 0, variantCount - 1)];
		} else {
			return ItemStack.EMPTY;
		}
	}

	public void scrollButton(int currentScrollAmount) {
		this.y = this.baseY - currentScrollAmount;
	}

	public void onClickButton() {

	}
}
