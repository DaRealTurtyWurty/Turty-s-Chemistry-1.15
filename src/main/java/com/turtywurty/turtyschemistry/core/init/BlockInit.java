package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.AgitatorBlock;
import com.turtywurty.turtyschemistry.common.blocks.AutoclaveBlock;
import com.turtywurty.turtyschemistry.common.blocks.BalerBlock;
import com.turtywurty.turtyschemistry.common.blocks.BlowerBlock;
import com.turtywurty.turtyschemistry.common.blocks.BoilerBlock;
import com.turtywurty.turtyschemistry.common.blocks.BriquettingPressBlock;
import com.turtywurty.turtyschemistry.common.blocks.ButaneTankBlock;
import com.turtywurty.turtyschemistry.common.blocks.FractionalDistillerBlock;
import com.turtywurty.turtyschemistry.common.blocks.GasBlock;
import com.turtywurty.turtyschemistry.common.blocks.GasExtractorBlock;
import com.turtywurty.turtyschemistry.common.blocks.GreenAlgaeBlock;
import com.turtywurty.turtyschemistry.common.blocks.PlainConveyorBlock;
import com.turtywurty.turtyschemistry.common.blocks.PropaneTankBlock;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS,
			TurtyChemistry.MOD_ID);

	public static final RegistryObject<Block> FRACTIONAL_DISTILLER = BLOCKS.register("fractional_distiller",
			() -> new FractionalDistillerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));

	public static final RegistryObject<Block> PROPANE_TANK = BLOCKS.register("propane", () -> new PropaneTankBlock(
			Block.Properties.create(Material.IRON).hardnessAndResistance(0.6F).sound(SoundType.METAL).notSolid()));

	public static final RegistryObject<Block> BUTANE_TANK = BLOCKS.register("butane", () -> new ButaneTankBlock(
			Block.Properties.create(Material.IRON).hardnessAndResistance(0.6F).sound(SoundType.METAL).notSolid()));

	public static final RegistryObject<Block> AGITATOR = BLOCKS.register("agitator",
			() -> new AgitatorBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES.notSolid()));

	public static final RegistryObject<Block> AUTOCLAVE = BLOCKS.register("autoclave",
			() -> new AutoclaveBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES.notSolid()));

	public static final RegistryObject<Block> BALER = BLOCKS.register("baler",
			() -> new BalerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES.notSolid()));

	public static final RegistryObject<Block> BLOWER = BLOCKS.register("blower",
			() -> new BlowerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));

	public static final RegistryObject<Block> BOILER = BLOCKS.register("boiler",
			() -> new BoilerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));

	public static final RegistryObject<Block> CONVEYOR = BLOCKS.register("conveyor",
			() -> new PlainConveyorBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));

	public static final RegistryObject<Block> BRIQUETTING_PRESS = BLOCKS.register("briquetting_press",
			() -> new BriquettingPressBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));

	/*
	 * public static final RegistryObject<Block> CALCINER =
	 * BLOCKS.register("calciner", () -> new
	 * CalcinerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> CENTRIFUGE =
	 * BLOCKS.register("centrifuge", () -> new
	 * CentrifugeBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> CHILLER =
	 * BLOCKS.register("chiller", () -> new
	 * ChillerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> CLARIFIER =
	 * BLOCKS.register("clarifier", () -> new
	 * ClarifierBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> CLASSIFIER =
	 * BLOCKS.register("classifier", () -> new
	 * ClassifierBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> COMPACTOR =
	 * BLOCKS.register("compactor", () -> new
	 * CompactorBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> COMPRESSOR =
	 * BLOCKS.register("compressor", () -> new
	 * CompressorBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * 
	 * public static final RegistryObject<Block> COOLING_TOWER =
	 * BLOCKS.register("cooling_tower", () -> new
	 * CoolingTowerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> CRYSTALLIZER =
	 * BLOCKS.register("crystallizer", () -> new
	 * CrystallizerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> CYCLONE =
	 * BLOCKS.register("cyclone", () -> new
	 * CycloneBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> DEODORIZER =
	 * BLOCKS.register("deodorizer", () -> new
	 * DeodorizerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> BASIC_DIGESTER =
	 * BLOCKS.register("basic_digester", () -> new
	 * BasicDigesterBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> EXPELLER =
	 * BLOCKS.register("expeller", () -> new
	 * ExpellerBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> EXTRUDER =
	 * BLOCKS.register("extruder", () -> new
	 * ExtruderBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 * 
	 * public static final RegistryObject<Block> FAN = BLOCKS.register("fan", () ->
	 * new FanBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES));
	 */

	public static final RegistryObject<GreenAlgaeBlock> GREEN_ALGAE = BLOCKS.register("green_algae",
			() -> new GreenAlgaeBlock(Block.Properties.from(Blocks.LILY_PAD)));

	public static final RegistryObject<GasBlock> HELIUM_GAS = BLOCKS.register("helium_gas",
			() -> new GasBlock(Block.Properties.from(Blocks.AIR).tickRandomly()));

	public static final RegistryObject<GasExtractorBlock> GAS_EXTRACTOR = BLOCKS.register("gas_extractor",
			() -> new GasExtractorBlock(BlockPropertyInit.BASIC_MACHINE_PROPERTIES.notSolid()));

	public static final RegistryObject<Block> BRINE_BLOCK = BLOCKS.register("brine_block",
			() -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 4.0f).harvestLevel(1)
					.harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).speedFactor(0.6f).slipperiness(0.5f)));
}
