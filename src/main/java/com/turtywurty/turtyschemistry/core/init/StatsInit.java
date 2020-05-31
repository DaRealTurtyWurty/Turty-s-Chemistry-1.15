package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StatsInit {

	public static final DeferredRegister<StatType<?>> STAT_TYPES = new DeferredRegister<>(ForgeRegistries.STAT_TYPES,
			TurtyChemistry.MOD_ID);

	public static final RegistryObject<StatType<ResourceLocation>> MACHINES = STAT_TYPES.register("machines",
			() -> new StatType<>(Registry.CUSTOM_STAT));

	public static final ResourceLocation INTERACT_WITH_FRACTIONAL_DISTILLER = registerCustom(
			"interact_with_fractional_distiller", IStatFormatter.DEFAULT);

	public static final ResourceLocation INTERACT_WITH_AGITATOR = registerCustom("interact_with_agitator",
			IStatFormatter.DEFAULT);
	
	public static final ResourceLocation INTERACT_WITH_AUTOCLAVE = registerCustom("interact_with_autoclave",
			IStatFormatter.DEFAULT);
	
	public static final ResourceLocation INTERACT_WITH_GAS_EXTRACTOR = registerCustom("interact_with_gas_extractor",
			IStatFormatter.DEFAULT);

	private static ResourceLocation registerCustom(String key, IStatFormatter formatter) {
		ResourceLocation resourcelocation = new ResourceLocation(TurtyChemistry.MOD_ID, key);
		Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
		return resourcelocation;
	}
}
