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
			() -> new FractionalDistillerBlock(makeMachineProperties()));

	public static final RegistryObject<Block> PROPANE_TANK = BLOCKS.register("propane", () -> new PropaneTankBlock(
			Block.Properties.create(Material.IRON).hardnessAndResistance(0.6F).sound(SoundType.METAL).notSolid()));

	public static final RegistryObject<Block> BUTANE_TANK = BLOCKS.register("butane", () -> new ButaneTankBlock(
			Block.Properties.create(Material.IRON).hardnessAndResistance(0.6F).sound(SoundType.METAL).notSolid()));

	public static final RegistryObject<Block> AGITATOR = BLOCKS.register("agitator",
			() -> new AgitatorBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<Block> AUTOCLAVE = BLOCKS.register("autoclave",
			() -> new AutoclaveBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<Block> BALER = BLOCKS.register("baler",
			() -> new BalerBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<Block> BLOWER = BLOCKS.register("blower",
			() -> new BlowerBlock(makeMachineProperties()));

	public static final RegistryObject<Block> BOILER = BLOCKS.register("boiler",
			() -> new BoilerBlock(makeMachineProperties()));

	public static final RegistryObject<Block> CONVEYOR = BLOCKS.register("conveyor",
			() -> new PlainConveyorBlock(makeMachineProperties()));

	public static final RegistryObject<Block> BRIQUETTING_PRESS = BLOCKS.register("briquetting_press",
			() -> new BriquettingPressBlock(makeMachineProperties()));

	/*
	 * public static final RegistryObject<Block> CALCINER =
	 * BLOCKS.register("calciner", () -> new
	 * CalcinerBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> CENTRIFUGE =
	 * BLOCKS.register("centrifuge", () -> new
	 * CentrifugeBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> CHILLER =
	 * BLOCKS.register("chiller", () -> new ChillerBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> CLARIFIER =
	 * BLOCKS.register("clarifier", () -> new
	 * ClarifierBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> CLASSIFIER =
	 * BLOCKS.register("classifier", () -> new
	 * ClassifierBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> COMPACTOR =
	 * BLOCKS.register("compactor", () -> new
	 * CompactorBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> COMPRESSOR =
	 * BLOCKS.register("compressor", () -> new
	 * CompressorBlock(makeMachineProperties()));
	 * 
	 * 
	 * public static final RegistryObject<Block> COOLING_TOWER =
	 * BLOCKS.register("cooling_tower", () -> new
	 * CoolingTowerBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> CRYSTALLIZER =
	 * BLOCKS.register("crystallizer", () -> new
	 * CrystallizerBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> CYCLONE =
	 * BLOCKS.register("cyclone", () -> new CycloneBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> DEODORIZER =
	 * BLOCKS.register("deodorizer", () -> new
	 * DeodorizerBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> BASIC_DIGESTER =
	 * BLOCKS.register("basic_digester", () -> new
	 * BasicDigesterBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> EXPELLER =
	 * BLOCKS.register("expeller", () -> new
	 * ExpellerBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> EXTRUDER =
	 * BLOCKS.register("extruder", () -> new
	 * ExtruderBlock(makeMachineProperties()));
	 * 
	 * public static final RegistryObject<Block> FAN = BLOCKS.register("fan", () ->
	 * new FanBlock(makeMachineProperties()));
	 */

	public static final RegistryObject<GreenAlgaeBlock> GREEN_ALGAE = BLOCKS.register("green_algae",
			() -> new GreenAlgaeBlock(Block.Properties.from(Blocks.LILY_PAD)));

	public static final RegistryObject<GasBlock> HELIUM_GAS = BLOCKS.register("helium_gas",
			() -> new GasBlock(Block.Properties.from(Blocks.AIR).tickRandomly()));

	public static final RegistryObject<GasExtractorBlock> GAS_EXTRACTOR = BLOCKS.register("gas_extractor",
			() -> new GasExtractorBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<Block> BRINE_BLOCK = BLOCKS.register("brine_block",
			() -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 4.0f).harvestLevel(1)
					.harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).speedFactor(0.6f).slipperiness(0.5f)));

	protected static Block.Properties makeMachineProperties() {
		return Block.Properties.create(Material.IRON).hardnessAndResistance(1.2f).sound(SoundType.METAL).harvestLevel(2)
				.harvestTool(ToolType.PICKAXE);
	}
}
