package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.AutoclaveTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.BalerTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.BriquettingPressTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.FractionalDistillerTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.GasExtractorTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeInit {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(
			ForgeRegistries.TILE_ENTITIES, TurtyChemistry.MOD_ID);

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
	
	public static final RegistryObject<TileEntityType<BalerTileEntity>> BALER = TILE_ENTITY_TYPES
			.register("baler", () -> TileEntityType.Builder
					.create(BalerTileEntity::new, BlockInit.BALER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<BriquettingPressTileEntity>> BRIQUETTING_PRESS = TILE_ENTITY_TYPES
			.register("briquetting_press", () -> TileEntityType.Builder
					.create(BriquettingPressTileEntity::new, BlockInit.BRIQUETTING_PRESS.get()).build(null));
}
