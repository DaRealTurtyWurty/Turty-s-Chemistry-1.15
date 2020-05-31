package com.turtywurty.turtyschemistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.turtywurty.turtyschemistry.common.blocks.GasBlock;
import com.turtywurty.turtyschemistry.core.init.BiomeInit;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.init.FluidInit;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.ParticleInit;
import com.turtywurty.turtyschemistry.core.init.PotionInit;
import com.turtywurty.turtyschemistry.core.init.RecipeSerializerInit;
import com.turtywurty.turtyschemistry.core.init.StatsInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.world.features.HeliumGasPocket;
import com.turtywurty.turtyschemistry.core.world.features.HeliumPocket;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("deprecation")
@Mod("turtychemistry")
@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = Bus.MOD)
public class TurtyChemistry {

	public static TurtyChemistry instance;
	public static final String MOD_ID = "turtychemistry";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final String NETWORK_VERSION = "1";
	public static final String CHAT = "chat." + MOD_ID + ".";
	public static final String CHAT_INFO = CHAT + "info.";

	public static final SimpleChannel packetHandler = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(MOD_ID, "main")).networkProtocolVersion(() -> NETWORK_VERSION)
			.serverAcceptedVersions(NETWORK_VERSION::equals).clientAcceptedVersions(NETWORK_VERSION::equals)
			.simpleChannel();

	public TurtyChemistry() {
		instance = this;

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::generateFeatures);

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

		MinecraftForge.EVENT_BUS.register(this);
	}

	public void generateFeatures(final FMLCommonSetupEvent event) {
		DeferredWorkQueue.runLater(() -> {
			for (Biome biome : ForgeRegistries.BIOMES) {
				if (biome.getCategory().equals(Biome.Category.SWAMP)
						|| biome.getCategory().equals(Biome.Category.RIVER)) {
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
							Feature.RANDOM_PATCH
									.withConfiguration((new BlockClusterFeatureConfig.Builder(
											new SimpleBlockStateProvider(BlockInit.GREEN_ALGAE.get().getDefaultState()),
											new SimpleBlockPlacer())).tries(20).build())
									.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(6))));
				}

				biome.addFeature(Decoration.LOCAL_MODIFICATIONS,
						new HeliumGasPocket(BlockStateFeatureConfig::deserialize)
								.withConfiguration(
										new BlockStateFeatureConfig(BlockInit.HELIUM_GAS.get().getDefaultState()))
								.withPlacement(
										new HeliumPocket(ChanceConfig::deserialize).configure(new ChanceConfig(8))));
			}
		});
	}

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		BlockInit.BLOCKS
				.getEntries().stream().filter(block -> !block.get().equals(BlockInit.GREEN_ALGAE.get())
						&& !(block.get() instanceof GasBlock) && !(block.get() instanceof FlowingFluidBlock))
				.map(RegistryObject::get).forEach(block -> {
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
		public static final ChemistryItemGroup instance = new ChemistryItemGroup(ItemGroup.GROUPS.length,
				"chemistrytab");

		private ChemistryItemGroup(int index, String label) {
			super(index, label);
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemInit.COPPER.get());
		}
	}
}
