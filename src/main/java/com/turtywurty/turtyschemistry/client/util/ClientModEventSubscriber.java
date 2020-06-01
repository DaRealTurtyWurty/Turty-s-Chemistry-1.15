package com.turtywurty.turtyschemistry.client.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.AutoclaveTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.screen.AutoclaveScreen;
import com.turtywurty.turtyschemistry.client.screen.FractionalDistillerScreen;
import com.turtywurty.turtyschemistry.client.screen.GasExtractorScreen;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.init.FluidInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(TurtyChemistry.MOD_ID + " Client Mod Event Subscriber");

	@SubscribeEvent
	public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
		// Register ContainerType Screens
		ScreenManager.registerFactory(ContainerTypeInit.FRACTIONAL_DISTILLER.get(), FractionalDistillerScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.AUTOCLAVE.get(), AutoclaveScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.GAS_EXTRACTOR.get(), GasExtractorScreen::new);

		// Set Render Types
		RenderTypeLookup.setRenderLayer(BlockInit.AUTOCLAVE.get(), layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.AGITATOR.get(), layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.CONVEYOR.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.GREEN_ALGAE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.GAS_EXTRACTOR.get(), type -> type.equals(RenderType.getCutout()));
		RenderTypeLookup.setRenderLayer(FluidInit.BRINE_STILL.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(FluidInit.BRINE_FLOWING.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(FluidInit.BRINE_BLOCK.get(), type -> type.equals(RenderType.getTranslucent()));

		// Bind TileEntity to TileEntityRenderer
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.AUTOCLAVE.get(), AutoclaveTileEntityRenderer::new);
		LOGGER.debug("Registered ContainerType Screens");
	}
}