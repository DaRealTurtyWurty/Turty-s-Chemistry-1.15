package com.turtywurty.turtyschemistry.common.blocks.researcher;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class RecipeButton extends ExtendedButton {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
            "textures/gui/blueprint_section.png");

    private final int baseY;

    private final ResearcherScreen screen;

    private final IRecipe<CraftingInventory> recipe;

    public ItemStack tooltipItem = ItemStack.EMPTY;

    public RecipeButton(final ResearcherScreen screen, final IRecipe<CraftingInventory> recipe, final int x,
            final int y, final int width) {
        super(x, y, width, getHeightForRecipe(recipe), new StringTextComponent(""),
                button -> onButtonClicked(screen.getContainer(), recipe));
        this.baseY = y;
        this.screen = screen;
        this.recipe = recipe;
    }

    public static int getHeightForRecipe(final IRecipe<?> recipe) {
        final int rows = 1 + (recipe.getIngredients().size() - 1) / 3;
        return rows * 18 + 4; // 2 padding on top, 2 on bottom
    }

    public static ItemStack getIngredientVariant(final ItemStack[] variants) {
        final int variantCount = variants.length;
        if (variantCount > 0) {
            // if this ingredient has multiple stacks, cycle through them
            final int variantIndex = (int) (Util.milliTime() / 1000L / variantCount);
            return variants[MathHelper.clamp(variantIndex, 0, variantCount - 1)];
        }
        return ItemStack.EMPTY;
    }

    public static void onButtonClicked(final ResearcherContainer container,
            final IRecipe<CraftingInventory> recipe) {
        TurtyChemistry.PACKET_HANDLER.sendToServer(new ResearcherRecipeButtonPacket(recipe.getId()));
        // Assemble recipe
    }

    @Override
    public boolean mouseDragged(final double mouseX, final double mouseY, final int button,
            final double deltaX, final double deltaY) {
        return false;
    }

    public void onClickButton() {

    }

    @Override
    public void renderWidget(final MatrixStack matrix, final int mouseX, final int mouseY,
            final float partial) {
        this.tooltipItem = ItemStack.EMPTY;
        if (this.visible) {
            super.renderWidget(matrix, mouseX, mouseY, partial);
            final NonNullList<Ingredient> ingredients = this.recipe.getIngredients();
            final int ingredientCount = ingredients.size();
            // render ingredients
            for (int ingredientIndex = 0; ingredientIndex < ingredientCount; ingredientIndex++) {
                final ItemStack stack = getIngredientVariant(
                        ingredients.get(ingredientIndex).getMatchingStacks());

                final int itemRow = ingredientIndex / 3;
                final int itemColumn = ingredientIndex % 3;
                final int itemOffsetX = 2 + itemColumn * 18;
                final int itemOffsetY = 2 + itemRow * 18;
                final int itemX = this.x + itemOffsetX;
                final int itemY = this.y + itemOffsetY;
                final int itemEndX = itemX + 18;
                final int itemEndY = itemY + 18;
                this.screen.renderItemStack(stack, itemX, itemY);
                if (mouseX >= itemX && mouseX < itemEndX && mouseY >= itemY && mouseY < itemEndY) {
                    this.tooltipItem = stack;
                }
            }
            if (ingredientCount > 0) {
                final int extraIngredientRows = (ingredientCount - 1) / 3; // 0 if 3 ingredients, 1 if 4-6
                                                                           // ingredients, 2 if
                                                                           // 7-9 ingredients, etc
                final int arrowX = this.x + 2 + 18 * 3 + 4;
                final int arrowY = this.y + 2 + 4 + 9 * extraIngredientRows;
                final int arrowWidth = 10;
                final int arrowHeight = 9;
                final int arrowU = 15;
                final int arrowV = 171;
                ClientUtils.MC.textureManager.bindTexture(TEXTURE);
                ClientUtils.blit(matrix, this, arrowX, arrowY, arrowU, arrowV, arrowWidth, arrowHeight, 512,
                        256);

                // render the output item
                final ItemStack outputStack = this.recipe.getRecipeOutput();
                if (!outputStack.isEmpty()) {
                    final int itemX = this.x + 2 + 18 * 4;
                    final int itemY = this.y + 2 + 9 * extraIngredientRows;
                    final int itemEndX = itemX + 18;
                    final int itemEndY = itemY + 18;
                    this.screen.renderItemStack(outputStack, itemX, itemY);
                    if (mouseX >= itemX && mouseX < itemEndX && mouseY >= itemY && mouseY < itemEndY) {
                        this.tooltipItem = outputStack;
                    }
                }

            }
        }
    }

    public void scrollButton(final int currentScrollAmount) {
        this.y = this.baseY - currentScrollAmount;
    }
}
