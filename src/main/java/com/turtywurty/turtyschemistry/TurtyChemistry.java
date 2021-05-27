package com.turtywurty.turtyschemistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.turtywurty.turtyschemistry.client.screen.book.GuideBookData;
import com.turtywurty.turtyschemistry.client.screen.book.GuideBookDataCap;
import com.turtywurty.turtyschemistry.client.screen.book.IGuideBookData;
import com.turtywurty.turtyschemistry.common.blocks.CrystalBlock;
import com.turtywurty.turtyschemistry.common.blocks.GasBlock;
import com.turtywurty.turtyschemistry.common.blocks.agitator.AgitatorData;
import com.turtywurty.turtyschemistry.common.blocks.agitator.AgitatorFluidPacket;
import com.turtywurty.turtyschemistry.common.blocks.agitator.AgitatorTypePacket;
import com.turtywurty.turtyschemistry.common.blocks.baler.BalerPart;
import com.turtywurty.turtyschemistry.common.blocks.boiler.BoilerFluidPacket;
import com.turtywurty.turtyschemistry.common.blocks.boiler.BoilerRecipe;
import com.turtywurty.turtyschemistry.common.blocks.briquetting_press.BriquettingPressButtonPacket;
import com.turtywurty.turtyschemistry.common.blocks.briquetting_press.BriquettingPressPart;
import com.turtywurty.turtyschemistry.common.blocks.gas_canister.GasCanisterBlock;
import com.turtywurty.turtyschemistry.common.blocks.researcher.ResearcherRecipeButtonPacket;
import com.turtywurty.turtyschemistry.common.blocks.silo.SiloButtonPacket;
import com.turtywurty.turtyschemistry.common.energy.TurtEnergyCap;
import com.turtywurty.turtyschemistry.core.init.BiomeInit;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.init.EntityTypeInit;
import com.turtywurty.turtyschemistry.core.init.FluidInit;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.ParticleInit;
import com.turtywurty.turtyschemistry.core.init.PotionInit;
import com.turtywurty.turtyschemistry.core.init.RecipeSerializerInit;
import com.turtywurty.turtyschemistry.core.init.StatsInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.util.SimpleJsonDataManager;
import com.turtywurty.turtyschemistry.core.world.features.FeatureGeneration;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("deprecation")
@Mod(TurtyChemistry.MOD_ID)
@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = Bus.MOD)
public class TurtyChemistry {

	public static TurtyChemistry instance;
	public static final String MOD_ID = "turtychemistry";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final String NETWORK_VERSION = "1";
	public static final String CHAT = "chat." + MOD_ID + ".";
	public static final String CHAT_INFO = CHAT + "info.";

	public static final SimpleJsonDataManager<AgitatorData> AGITATOR_DATA = new SimpleJsonDataManager<>(
			"agitator", AgitatorData.class);

	public static final SimpleJsonDataManager<BoilerRecipe> BOILER_RECIPE = new SimpleJsonDataManager<>("boiler",
			BoilerRecipe.class);

	public static void onClientInit() {
		ModelLoader.addSpecialModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/agitator_fluid"));
		ModelLoader.addSpecialModel(new ResourceLocation(TurtyChemistry.MOD_ID, "block/researcher_arm"));

		IResourceManager manager = Minecraft.getInstance().getResourceManager();
		if (manager instanceof IReloadableResourceManager) {
			IReloadableResourceManager reloader = (IReloadableResourceManager) manager;
			reloader.addReloadListener(AGITATOR_DATA);
			reloader.addReloadListener(BOILER_RECIPE);
		}
	}

	public static final SimpleChannel packetHandler = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MOD_ID, "main")).networkProtocolVersion(() -> NETWORK_VERSION)
			.serverAcceptedVersions(NETWORK_VERSION::equals).clientAcceptedVersions(NETWORK_VERSION::equals).simpleChannel();

	public TurtyChemistry() {
		instance = this;

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::commonSetup);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> TurtyChemistry::onClientInit);

		ParticleInit.PARTICLE_TYPES.register(modEventBus);
		PotionInit.EFFECTS.register(modEventBus);
		StatsInit.STAT_TYPES.register(modEventBus);
		ItemInit.ITEMS.register(modEventBus);
		RecipeSerializerInit.SERIALIZERS.register(modEventBus);
		TileEntityTypeInit.TILE_ENTITY_TYPES.register(modEventBus);
		ContainerTypeInit.CONTAINER_TYPES.register(modEventBus);
		FluidInit.FLUIDS.register(modEventBus);
		BlockInit.BLOCKS.register(modEventBus);
		BiomeInit.BIOMES.register(modEventBus);
		EntityTypeInit.ENTITY_TYPES.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void registerPackets() {
		int index = 0;
		packetHandler.registerMessage(index++, BriquettingPressButtonPacket.class, BriquettingPressButtonPacket::encode,
				BriquettingPressButtonPacket::decode, BriquettingPressButtonPacket::onRecieved);
		packetHandler.registerMessage(index++, SiloButtonPacket.class, SiloButtonPacket::encode, SiloButtonPacket::decode,
				SiloButtonPacket::onRecieved);
		packetHandler.registerMessage(index++, AgitatorFluidPacket.class, AgitatorFluidPacket::encode,
				AgitatorFluidPacket::decode, AgitatorFluidPacket::onRecieved);
		packetHandler.registerMessage(index++, AgitatorTypePacket.class, AgitatorTypePacket::encode,
				AgitatorTypePacket::decode, AgitatorTypePacket::onRecieved);
		packetHandler.registerMessage(index++, BoilerFluidPacket.class, BoilerFluidPacket::encode, BoilerFluidPacket::decode,
				BoilerFluidPacket::onRecieved);
		packetHandler.registerMessage(index++, ResearcherRecipeButtonPacket.class, ResearcherRecipeButtonPacket::write,
				ResearcherRecipeButtonPacket::read, ResearcherRecipeButtonPacket::handle);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		registerPackets();
		DeferredWorkQueue.runLater(FeatureGeneration::addAllFeatures);
		CapabilityManager.INSTANCE.register(IGuideBookData.class, new GuideBookDataCap.Storage(), GuideBookData::new);
		TurtEnergyCap.register();
	}

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)
				.filter(block -> !block.equals(BlockInit.GREEN_ALGAE.get()) && !(block instanceof GasBlock)
						&& !(block instanceof FlowingFluidBlock) && !(block instanceof BalerPart)
						&& !(block instanceof BriquettingPressPart) && !(block instanceof GasCanisterBlock)
						&& !(block instanceof CrystalBlock))
				.forEach(block -> {
					final Item.Properties properties = new Item.Properties().group(ChemistryItemGroup.instance);
					final BlockItem blockItem = new BlockItem(block, properties);
					blockItem.setRegistryName(block.getRegistryName());
					registry.register(blockItem);
				});

		LOGGER.debug("Registered BlockItems");
	}

	@SubscribeEvent
	public static void initBiomes(final RegistryEvent.Register<Biome> event) {
		BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(BiomeInit.BRINE_FLATS.get(), 2));
		BiomeManager.addSpawnBiome(BiomeInit.BRINE_FLATS.get());
		BiomeDictionary.addTypes(BiomeInit.BRINE_FLATS.get(), Type.BEACH, Type.WASTELAND, Type.WET, Type.WATER);
	}

	public static class ChemistryItemGroup extends ItemGroup {
		public static final ChemistryItemGroup instance = new ChemistryItemGroup(ItemGroup.GROUPS.length, "chemistrytab");

		private ChemistryItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemInit.COPPER.get());
		}
	}
}
