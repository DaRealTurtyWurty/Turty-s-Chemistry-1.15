package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.agitator.AgitatorContainer;
import com.turtywurty.turtyschemistry.common.blocks.autoclave.AutoclaveContainer;
import com.turtywurty.turtyschemistry.common.blocks.baler.BalerContainer;
import com.turtywurty.turtyschemistry.common.blocks.boiler.BoilerContainer;
import com.turtywurty.turtyschemistry.common.blocks.briquetting_press.BriquettingPressContainer;
import com.turtywurty.turtyschemistry.common.blocks.electrolyzer.ElectrolyzerContainer;
import com.turtywurty.turtyschemistry.common.blocks.fractional_distiller.FractionalDistillerContainer;
import com.turtywurty.turtyschemistry.common.blocks.gas_extractor.GasExtractorContainer;
import com.turtywurty.turtyschemistry.common.blocks.hopper.HopperContainer;
import com.turtywurty.turtyschemistry.common.blocks.researcher.ResearcherContainer;
import com.turtywurty.turtyschemistry.common.blocks.silo.SiloContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ContainerTypeInit {
	
	private ContainerTypeInit() {}

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, TurtyChemistry.MOD_ID);

	public static final RegistryObject<ContainerType<FractionalDistillerContainer>> FRACTIONAL_DISTILLER = CONTAINER_TYPES
			.register("fractional_distiller", () -> IForgeContainerType.create(FractionalDistillerContainer::new));

	public static final RegistryObject<ContainerType<AutoclaveContainer>> AUTOCLAVE = CONTAINER_TYPES.register("autoclave",
			() -> IForgeContainerType.create(AutoclaveContainer::new));

	public static final RegistryObject<ContainerType<GasExtractorContainer>> GAS_EXTRACTOR = CONTAINER_TYPES
			.register("gas_extractor", () -> IForgeContainerType.create(GasExtractorContainer::new));

	public static final RegistryObject<ContainerType<AgitatorContainer>> AGITATOR = CONTAINER_TYPES.register("agitator",
			() -> new ContainerType<>(AgitatorContainer::getClientContainer));

	public static final RegistryObject<ContainerType<BalerContainer>> BALER = CONTAINER_TYPES.register("baler",
			() -> IForgeContainerType.create(BalerContainer::new));

	public static final RegistryObject<ContainerType<BriquettingPressContainer>> BRIQUETTING_PRESS = CONTAINER_TYPES
			.register("briquetting_press", () -> IForgeContainerType.create(BriquettingPressContainer::new));

	public static final RegistryObject<ContainerType<SiloContainer>> SILO = CONTAINER_TYPES.register("silo",
			() -> new ContainerType<>(SiloContainer::getClientContainer));

	public static final RegistryObject<ContainerType<HopperContainer>> HOPPER = CONTAINER_TYPES.register("hopper",
			() -> IForgeContainerType.create(HopperContainer::new));

	public static final RegistryObject<ContainerType<ElectrolyzerContainer>> ELECTROLYZER = CONTAINER_TYPES
			.register("electrolyzer", () -> new ContainerType<>(ElectrolyzerContainer::getClientContainer));

	public static final RegistryObject<ContainerType<BoilerContainer>> BOILER = CONTAINER_TYPES.register("boiler",
			() -> new ContainerType<>(BoilerContainer::getClientContainer));

	public static final RegistryObject<ContainerType<ResearcherContainer>> RESEARCHER = CONTAINER_TYPES
			.register("researcher", () -> new ContainerType<>(ResearcherContainer::getClientContainer));
}
