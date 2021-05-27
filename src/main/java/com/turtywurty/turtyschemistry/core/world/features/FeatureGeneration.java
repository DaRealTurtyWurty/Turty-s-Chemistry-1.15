package com.turtywurty.turtyschemistry.core.world.features;

import com.turtywurty.turtyschemistry.core.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public final class FeatureGeneration {
	
	private FeatureGeneration() {}

	private static final FillerBlockType STONE = FillerBlockType.NATURAL_STONE;
	private static final FillerBlockType RIVER_LAKE = FillerBlockType.create("river", "river",
			state -> Tags.Blocks.DIRT.contains(state.getBlock()) || state.getBlock().equals(Blocks.CLAY)
					|| Tags.Blocks.SAND.contains(state.getBlock()) || Tags.Blocks.GRAVEL.contains(state.getBlock()));
	private static final FillerBlockType PEGMATITE = FillerBlockType.create("pegmatite", "pegmatite",
			state -> state.getBlock().equals(BlockInit.PEGMATITE.get()));

	public static void addDesertOres() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome.getTempCategory().equals(TempCategory.WARM)
					&& !BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.NETHER)) {
				generateOre(biome, STONE, BlockInit.ULEXITE.get().getDefaultState(), 7, 3, 10, 40);
				generateOre(biome, STONE, BlockInit.KERNITE.get().getDefaultState(), 10, 2, 15, 45);
				generateOre(biome, STONE, BlockInit.BORAX.get().getDefaultState(), 15, 1, 45, 70);
				generateOre(biome, PEGMATITE, BlockInit.CASSITERITE.get().getDefaultState(), 4, 1, 38, 46);
				generateOre(biome, PEGMATITE, BlockInit.HIDDENITE.get().getDefaultState(), 9, 1, 30, 40);
				generateOre(biome, PEGMATITE, BlockInit.TRIPHANE.get().getDefaultState(), 9, 1, 30, 40);
				generateOre(biome, PEGMATITE, BlockInit.SPODUMENE.get().getDefaultState(), 7, 1, 25, 35);
				generateOre(biome, PEGMATITE, BlockInit.HIDDENITE.get().getDefaultState(), 9, 1, 30, 40);
				generateOre(biome, PEGMATITE, BlockInit.ALMANDINE.get().getDefaultState(), 3, 2, 30, 32);
				generateOre(biome, PEGMATITE, BlockInit.COLUMBITE.get().getDefaultState(), 2, 1, 2, 5);
				generateOre(biome, PEGMATITE, BlockInit.TANTALITE.get().getDefaultState(), 2, 1, 2, 5);
				generateOre(biome, PEGMATITE, BlockInit.KERNITE.get().getDefaultState(), 5, 3, 40, 55);
				generateOre(biome, PEGMATITE, BlockInit.SPESSARTINE.get().getDefaultState(), 9, 2, 55, 70);
			}
		}
	}

	public static void addRiverOres() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome.getCategory().equals(Category.RIVER)) {
				generateOre(biome, RIVER_LAKE, BlockInit.COLEMANITE.get().getDefaultState(), 6, 1, 50, 64);
				generateOre(biome, RIVER_LAKE, BlockInit.BORACITE.get().getDefaultState(), 2, 1, 50, 64);
			}
		}
	}

	public static void addJungleOres() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if ((biome.getCategory().equals(Biome.Category.JUNGLE)
					|| BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.JUNGLE)
					|| BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.LUSH))
					&& biome.getDefaultTemperature() > 0.5f) {
				generateOre(biome, PEGMATITE, BlockInit.LEPIDOLITE.get().getDefaultState(), 11, 2, 20, 40);
			}
		}
	}

	public static void addOceanicOres() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.OCEAN)
					&& !BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.NETHER)) {
				generateOre(biome, RIVER_LAKE, BlockInit.BORACITE.get().getDefaultState(), 5, 3, 20, 64);
			}
		}
	}

	public static void addNetherOres() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.NETHER)) {
				generateOre(biome, FillerBlockType.NETHERRACK, BlockInit.BORACITE.get().getDefaultState(), 3, 3, 2, 40);
			}
		}
	}

	public static void addPlainsOre() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.PLAINS)
					|| biome.getCategory().equals(Biome.Category.PLAINS)) {
				generateOre(biome, PEGMATITE, BlockInit.TOURMALINE.get().getDefaultState(), 2, 2, 10, 15);
				generateOre(biome, PEGMATITE, BlockInit.ALMANDINE.get().getDefaultState(), 3, 2, 30, 32);
				generateOre(biome, PEGMATITE, BlockInit.COLUMBITE.get().getDefaultState(), 2, 1, 2, 5);
				generateOre(biome, PEGMATITE, BlockInit.TANTALITE.get().getDefaultState(), 2, 1, 2, 5);
			}
		}
	}

	private static void generateOre(Biome biome, FillerBlockType blockFiller, BlockState oreBlock, int veinSize,
			int count, int minY, int maxY) {
		biome.addFeature(Decoration.UNDERGROUND_ORES,
				Feature.ORE.withConfiguration(new OreFeatureConfig(blockFiller, oreBlock, veinSize))
						.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(count, minY, 0, maxY))));
	}

	public static void addAlgae() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			if (biome.getCategory().equals(Biome.Category.SWAMP) || biome.getCategory().equals(Biome.Category.RIVER)) {
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
						Feature.RANDOM_PATCH
								.withConfiguration((new BlockClusterFeatureConfig.Builder(
										new SimpleBlockStateProvider(BlockInit.GREEN_ALGAE.get().getDefaultState()),
										new SimpleBlockPlacer())).tries(20).build())
								.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(6))));
			}
		}
	}

	public static void addGasPockets() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			biome.addFeature(Decoration.LOCAL_MODIFICATIONS,
					new HeliumGasPocket(BlockStateFeatureConfig::deserialize)
							.withConfiguration(
									new BlockStateFeatureConfig(BlockInit.HELIUM_GAS.get().getDefaultState()))
							.withPlacement(new HeliumPocket(ChanceConfig::deserialize).configure(new ChanceConfig(3))));
		}
	}

	public static void addPegmatiteDisks() {
		for (Biome biome : ForgeRegistries.BIOMES) {
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
					Feature.ORE
							.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
									BlockInit.PEGMATITE.get().getDefaultState(), 33))
							.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 80))));
		}
	}

	public static void addAllFeatures() {
		addPegmatiteDisks();
		addGasPockets();
		addDesertOres();
		addPlainsOre();
		addOceanicOres();
		addRiverOres();
		addJungleOres();
		addNetherOres();
		addAlgae();
	}
}
