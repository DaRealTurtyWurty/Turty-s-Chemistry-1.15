package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.cables.CableTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.AutoclaveTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.BalerTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.BriquettingPressTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.ElectrolyzerTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.FractionalDistillerTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.GasExtractorTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.GasifierTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.HopperTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.SiloTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.gascanister.GasCanisterLargeTE;
import com.turtywurty.turtyschemistry.common.tileentity.gascanister.GasCanisterSmallTE;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeInit {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, TurtyChemistry.MOD_ID);

	public static final RegistryObject<TileEntityType<FractionalDistillerTileEntity>> FRACTIONAL_DISTILLER = TILE_ENTITY_TYPES
			.register("fractional_distiller", () -> TileEntityType.Builder
					.create(FractionalDistillerTileEntity::new, BlockInit.FRACTIONAL_DISTILLER.get()).build(null));

	public static final RegistryObject<TileEntityType<AgitatorTileEntity>> AGITATOR = TILE_ENTITY_TYPES.register(
			"agitator",
			() -> TileEntityType.Builder.create(AgitatorTileEntity::new, BlockInit.AGITATOR.get()).build(null));

	public static final RegistryObject<TileEntityType<AutoclaveTileEntity>> AUTOCLAVE = TILE_ENTITY_TYPES.register(
			"autoclave",
			() -> TileEntityType.Builder.create(AutoclaveTileEntity::new, BlockInit.AUTOCLAVE.get()).build(null));

	public static final RegistryObject<TileEntityType<GasExtractorTileEntity>> GAS_EXTRACTOR = TILE_ENTITY_TYPES
			.register("gas_extractor", () -> TileEntityType.Builder
					.create(GasExtractorTileEntity::new, BlockInit.GAS_EXTRACTOR.get()).build(null));

	public static final RegistryObject<TileEntityType<BalerTileEntity>> BALER = TILE_ENTITY_TYPES.register("baler",
			() -> TileEntityType.Builder.create(BalerTileEntity::new, BlockInit.BALER.get()).build(null));

	public static final RegistryObject<TileEntityType<BriquettingPressTileEntity>> BRIQUETTING_PRESS = TILE_ENTITY_TYPES
			.register("briquetting_press", () -> TileEntityType.Builder
					.create(BriquettingPressTileEntity::new, BlockInit.BRIQUETTING_PRESS.get()).build(null));

	public static final RegistryObject<TileEntityType<GasifierTileEntity>> GASIFIER = TILE_ENTITY_TYPES.register(
			"gasifier",
			() -> TileEntityType.Builder.create(GasifierTileEntity::new, BlockInit.GASIFIER.get()).build(null));

	public static final RegistryObject<TileEntityType<SiloTileEntity>> SILO = TILE_ENTITY_TYPES.register("silo",
			() -> TileEntityType.Builder.create(SiloTileEntity::new, BlockInit.SILO.get()).build(null));

	public static final RegistryObject<TileEntityType<HopperTileEntity>> HOPPER = TILE_ENTITY_TYPES.register("hopper",
			() -> TileEntityType.Builder.create(HopperTileEntity::new, BlockInit.HOPPER.get()).build(null));

	public static final RegistryObject<TileEntityType<CableTileEntity>> CABLE = TILE_ENTITY_TYPES.register("cable",
			() -> TileEntityType.Builder.create(CableTileEntity::new, BlockInit.CABLE.get()).build(null));

	public static final RegistryObject<TileEntityType<ElectrolyzerTileEntity>> ELECTROLYZER = TILE_ENTITY_TYPES
			.register("electrolyzer", () -> TileEntityType.Builder
					.create(ElectrolyzerTileEntity::new, BlockInit.ELECTROLYZER.get()).build(null));

	public static final RegistryObject<TileEntityType<GasCanisterLargeTE>> GAS_CANISTER_L = TILE_ENTITY_TYPES
			.register("gas_canister_large", () -> TileEntityType.Builder
					.create(GasCanisterLargeTE::new, BlockInit.GAS_CANISTER_L.get()).build(null));

	public static final RegistryObject<TileEntityType<GasCanisterSmallTE>> GAS_CANISTER_S = TILE_ENTITY_TYPES
			.register("gas_canister_small", () -> TileEntityType.Builder
					.create(GasCanisterSmallTE::new, BlockInit.GAS_CANISTER_S.get()).build(null));
}
