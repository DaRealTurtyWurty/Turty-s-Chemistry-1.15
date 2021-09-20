package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.block.Blocks;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BiomeInit {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
            TurtyChemistry.MOD_ID);

    public static final RegistryObject<Biome> BRINE_FLATS = BIOMES.register("brine_flats",
            () -> new Biome.Builder().category(Category.PLAINS).depth(0.0F).scale(0.025F).downfall(0.02f)
                    .precipitation(RainType.RAIN).temperature(0.5f)
                    .setEffects(new BiomeAmbience.Builder().setWaterColor(0x000000).setWaterFogColor(0x6DA1DE)
                            .setFogColor(0x000000).withSkyColor(0x0000ED).withGrassColor(0x00CC00)
                            .withFoliageColor(0x00CC00).setMusic(BackgroundMusicTracks.WORLD_MUSIC).build())
                    .withGenerationSettings(new BiomeGenerationSettings.Builder()
                            .withSurfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT,
                                    new SurfaceBuilderConfig(BlockInit.BRINE_BLOCK.get().getDefaultState(),
                                            Blocks.STONE.getDefaultState(),
                                            BlockInit.BRINE_BLOCK.get().getDefaultState())))
                            .build())
                    .withMobSpawnSettings(hostileMobs(new MobSpawnInfo.Builder()).build()).build());

    private BiomeInit() {
    }

    private static MobSpawnInfo.Builder hostileMobs(final MobSpawnInfo.Builder builder) {
        DefaultBiomeFeatures.withBatsAndHostiles(builder);
        return builder;
    }

    public static class BiomeMaker {

    }
}
