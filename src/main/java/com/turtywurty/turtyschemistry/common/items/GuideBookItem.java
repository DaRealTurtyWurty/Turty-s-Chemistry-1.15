package com.turtywurty.turtyschemistry.common.items;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.screen.book.GuideBookScreen;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class GuideBookItem extends Item {

	public GuideBookItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (worldIn.isRemote) {
			ClientUtils.MC.displayGuiScreen(
					new GuideBookScreen(new TranslationTextComponent(TurtyChemistry.MOD_ID + ".guide_book")));
			return ActionResult.resultPass(playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
