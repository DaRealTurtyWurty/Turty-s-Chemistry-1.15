package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.util.text.TranslationTextComponent;

import com.turtywurty.turtyschemistry.common.blocks.solar_panel.AbstractSolarPanelBlock.PanelInfo;
import net.minecraft.block.AbstractBlock.Properties;

public class BasicSolarPanelBlock extends AbstractSolarPanelBlock {

	public BasicSolarPanelBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Supplier<PanelInfo<? extends AbstractSolarPanelTileEntity>> getPanelInfo() {
		return () -> new PanelInfo<>((IContainerProvider) null,
				new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + "."), 20,
				TileEntityTypeInit.BASIC_SOLAR_PANEL.get());
	}
}
