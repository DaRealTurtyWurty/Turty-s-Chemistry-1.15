package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.AgitatorContainer;
import com.turtywurty.turtyschemistry.common.container.AutoclaveContainer;
import com.turtywurty.turtyschemistry.common.container.BalerContainer;
import com.turtywurty.turtyschemistry.common.container.BriquettingPressContainer;
import com.turtywurty.turtyschemistry.common.container.FractionalDistillerContainer;
import com.turtywurty.turtyschemistry.common.container.GasExtractorContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeInit {

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(
			ForgeRegistries.CONTAINERS, TurtyChemistry.MOD_ID);

	public static final RegistryObject<ContainerType<FractionalDistillerContainer>> FRACTIONAL_DISTILLER = CONTAINER_TYPES
			.register("fractional_distiller", () -> IForgeContainerType.create(FractionalDistillerContainer::new));

	public static final RegistryObject<ContainerType<AutoclaveContainer>> AUTOCLAVE = CONTAINER_TYPES
			.register("autoclave", () -> IForgeContainerType.create(AutoclaveContainer::new));

	public static final RegistryObject<ContainerType<GasExtractorContainer>> GAS_EXTRACTOR = CONTAINER_TYPES
			.register("gas_extractor", () -> IForgeContainerType.create(GasExtractorContainer::new));

	public static final RegistryObject<ContainerType<AgitatorContainer>> AGITATOR = CONTAINER_TYPES.register("agitator",
			() -> IForgeContainerType.create(AgitatorContainer::new));

	public static final RegistryObject<ContainerType<BalerContainer>> BALER = CONTAINER_TYPES.register("baler",
			() -> IForgeContainerType.create(BalerContainer::new));
	
	public static final RegistryObject<ContainerType<BriquettingPressContainer>> BRIQUETTING_PRESS = CONTAINER_TYPES.register("briquetting_press",
			() -> IForgeContainerType.create(BriquettingPressContainer::new));
}
