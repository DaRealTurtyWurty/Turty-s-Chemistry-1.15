package com.turtywurty.turtyschemistry.common.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class BlueprintItem extends Item {

    public BlueprintItem(final Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(final ItemStack stack, final World worldIn, final List<ITextComponent> tooltip,
            final ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getOrCreateTag().contains("Recipe")) {
            tooltip.add(new TranslationTextComponent("recipe." + stack.getOrCreateTag().getString("Recipe")));
        }
    }
}
