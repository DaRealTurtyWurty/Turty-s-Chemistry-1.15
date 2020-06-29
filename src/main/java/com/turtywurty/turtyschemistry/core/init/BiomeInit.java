package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.core.world.biomes.BrineFlatsBiome;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeInit {

	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
			TurtyChemistry.MOD_ID);

	public static final RegistryObject<BrineFlatsBiome> BRINE_FLATS = BIOMES
			.register("brine_flats",
					() -> new BrineFlatsBiome(new Biome.Builder().category(Category.PLAINS).depth(0.0F).scale(0.025F)
							.downfall(0.02f).parent((String) null).precipitation(RainType.RAIN).temperature(0.5f)
							.waterColor(0x6DBBFF).waterFogColor(0x6DA1DE)
							.surfaceBuilder(new ConfiguredSurfaceBuilder(SurfaceBuilder.DEFAULT,
									new SurfaceBuilderConfig(BlockInit.BRINE_BLOCK.get().getDefaultState(),
											Blocks.STONE.getDefaultState(),
											BlockInit.BRINE_BLOCK.get().getDefaultState())))));
}
