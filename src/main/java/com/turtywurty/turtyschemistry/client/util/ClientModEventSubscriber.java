package com.turtywurty.turtyschemistry.client.util;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.AgitatorTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.AutoclaveTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.BalerTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.BoilerTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.BriquettingPressTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.BunsenBurnerTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.renderer.tileentity.HopperTileEntityRenderer;
import com.turtywurty.turtyschemistry.client.screen.AgitatorScreen;
import com.turtywurty.turtyschemistry.client.screen.AutoclaveScreen;
import com.turtywurty.turtyschemistry.client.screen.BalerScreen;
import com.turtywurty.turtyschemistry.client.screen.BoilerScreen;
import com.turtywurty.turtyschemistry.client.screen.BriquettingPressScreen;
import com.turtywurty.turtyschemistry.client.screen.ElectrolyzerScreen;
import com.turtywurty.turtyschemistry.client.screen.FractionalDistillerScreen;
import com.turtywurty.turtyschemistry.client.screen.GasExtractorScreen;
import com.turtywurty.turtyschemistry.client.screen.HopperScreen;
import com.turtywurty.turtyschemistry.client.screen.SiloScreen;
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

	@SubscribeEvent
	public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
		setupScreens();
		setupRenderTypes();
		bindTERs();

	}

	public static void setupScreens() {
		ScreenManager.registerFactory(ContainerTypeInit.FRACTIONAL_DISTILLER.get(), FractionalDistillerScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.AUTOCLAVE.get(), AutoclaveScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.GAS_EXTRACTOR.get(), GasExtractorScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.BALER.get(), BalerScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.BRIQUETTING_PRESS.get(), BriquettingPressScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.SILO.get(), SiloScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.HOPPER.get(), HopperScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.ELECTROLYZER.get(), ElectrolyzerScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.AGITATOR.get(), AgitatorScreen::new);
		ScreenManager.registerFactory(ContainerTypeInit.BOILER.get(), BoilerScreen::new);
	}

	public static void setupRenderTypes() {
		RenderTypeLookup.setRenderLayer(BlockInit.AUTOCLAVE.get(),
				layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.AGITATOR.get(),
				layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(BlockInit.GREEN_ALGAE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.GAS_EXTRACTOR.get(), type -> type.equals(RenderType.getCutout()));
		RenderTypeLookup.setRenderLayer(FluidInit.BRINE_STILL.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(FluidInit.BRINE_FLOWING.get(),
				type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(FluidInit.BRINE_BLOCK.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.BRIQUETTING_TURNER.get(),
				type -> type.equals(RenderType.getCutout()));
		RenderTypeLookup.setRenderLayer(BlockInit.ULEXITE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.KERNITE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.COLUMBITE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.TANTALITE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.SPESSARTINE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.TOURMALINE.get(), type -> type.equals(RenderType.getTranslucent()));
		RenderTypeLookup.setRenderLayer(BlockInit.SPODUMENE.get(), type -> type.equals(RenderType.getTranslucent()));
	}

	public static void bindTERs() {
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.AUTOCLAVE.get(), AutoclaveTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.BALER.get(), BalerTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.BRIQUETTING_PRESS.get(),
				BriquettingPressTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.AGITATOR.get(), AgitatorTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.HOPPER.get(), HopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.BOILER.get(), BoilerTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.BUNSEN_BURNER.get(),
				BunsenBurnerTileEntityRenderer::new);
	}
}