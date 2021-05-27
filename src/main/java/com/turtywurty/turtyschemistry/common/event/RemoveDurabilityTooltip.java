package com.turtywurty.turtyschemistry.common.event;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.items.GasCanisterItem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class RemoveDurabilityTooltip {
	
	private RemoveDurabilityTooltip() {}

	@SubscribeEvent
	public static void removeDurability(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.getItem() instanceof GasCanisterItem) {
			event.getToolTip().remove(new TranslationTextComponent("item.durability",
					stack.getMaxDamage() - stack.getDamage(), stack.getMaxDamage()));
		}
	}
}
