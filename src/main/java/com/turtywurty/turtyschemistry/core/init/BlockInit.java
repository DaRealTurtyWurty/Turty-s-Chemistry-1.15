package com.turtywurty.turtyschemistry.core.init;

import java.util.stream.Stream;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.ButaneTankBlock;
import com.turtywurty.turtyschemistry.common.blocks.CrystalBlock;
import com.turtywurty.turtyschemistry.common.blocks.GasBlock;
import com.turtywurty.turtyschemistry.common.blocks.GreenAlgaeBlock;
import com.turtywurty.turtyschemistry.common.blocks.PropaneTankBlock;
import com.turtywurty.turtyschemistry.common.blocks.agitator.AgitatorBlock;
import com.turtywurty.turtyschemistry.common.blocks.autoclave.AutoclaveBlock;
import com.turtywurty.turtyschemistry.common.blocks.baler.BalerBlock;
import com.turtywurty.turtyschemistry.common.blocks.baler.BalerPart;
import com.turtywurty.turtyschemistry.common.blocks.blower.BlowerBlock;
import com.turtywurty.turtyschemistry.common.blocks.boiler.BoilerBlock;
import com.turtywurty.turtyschemistry.common.blocks.briquetting_press.BriquettingPressBlock;
import com.turtywurty.turtyschemistry.common.blocks.briquetting_press.BriquettingPressPart;
import com.turtywurty.turtyschemistry.common.blocks.bunsen_burner.BunsenBurnerBlock;
import com.turtywurty.turtyschemistry.common.blocks.electrolyzer.ElectrolyzerBlock;
import com.turtywurty.turtyschemistry.common.blocks.fractional_distiller.FractionalDistillerBlock;
import com.turtywurty.turtyschemistry.common.blocks.gas_canister.GasCanisterBlock;
import com.turtywurty.turtyschemistry.common.blocks.gas_extractor.GasExtractorBlock;
import com.turtywurty.turtyschemistry.common.blocks.gasifier.GasifierBlock;
import com.turtywurty.turtyschemistry.common.blocks.hopper.HopperBlock;
import com.turtywurty.turtyschemistry.common.blocks.researcher.ResearcherBlock;
import com.turtywurty.turtyschemistry.common.blocks.researcher.ResearcherBlock.Processor;
import com.turtywurty.turtyschemistry.common.blocks.silo.SiloBlock;
import com.turtywurty.turtyschemistry.common.blocks.solar_panel.AbstractSolarPanelBlock;
import com.turtywurty.turtyschemistry.common.blocks.solar_panel.BasicSolarPanelBlock;
import com.turtywurty.turtyschemistry.common.energy.cables.CableBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BlockInit {

	static final VoxelShape BALER_ARM_SHAPE = Block.makeCuboidShape(3, 11, 10, 13, 12, 14);

	static final VoxelShape BALER_PRESS_SHAPE = Block.makeCuboidShape(7, 12, 11, 9, 14, 13);
	static final VoxelShape BRIQUETTING_TURNER_SHAPE = Stream
			.of(Block.makeCuboidShape(9.9, 9.5, 6.75, 10.1, 9.7, 8.75),
					Block.makeCuboidShape(9.75, 10, 8.75, 10.25, 11, 9.25),
					Block.makeCuboidShape(9.25, 8, 8.25, 10.75, 9, 9.75),
					Block.makeCuboidShape(9.5, 9, 8.5, 10.5, 10, 9.5), Block.makeCuboidShape(9, 7, 8, 11, 8, 10))
			.reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			TurtyChemistry.MOD_ID);

	public static final RegistryObject<FractionalDistillerBlock> FRACTIONAL_DISTILLER = BLOCKS
			.register("fractional_distiller", () -> new FractionalDistillerBlock(makeMachineProperties()));

	public static final RegistryObject<PropaneTankBlock> PROPANE_TANK = BLOCKS.register("propane",
			() -> new PropaneTankBlock(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(0.6F)
					.sound(SoundType.METAL).notSolid()));

	public static final RegistryObject<ButaneTankBlock> BUTANE_TANK = BLOCKS.register("butane",
			() -> new ButaneTankBlock(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(0.6F)
					.sound(SoundType.METAL).notSolid()));

	public static final RegistryObject<AgitatorBlock> AGITATOR = BLOCKS.register("agitator",
			() -> new AgitatorBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<AutoclaveBlock> AUTOCLAVE = BLOCKS.register("autoclave",
			() -> new AutoclaveBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<BalerBlock> BALER = BLOCKS.register("baler",
			() -> new BalerBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<BalerPart> BALER_ARM = BLOCKS.register("baler_arm",
			() -> new BalerPart(makeMachineProperties().notSolid(), BALER_ARM_SHAPE));

	public static final RegistryObject<BalerPart> BALER_PRESS = BLOCKS.register("baler_press",
			() -> new BalerPart(makeMachineProperties().notSolid(), BALER_PRESS_SHAPE));

	public static final RegistryObject<BlowerBlock> BLOWER = BLOCKS.register("blower",
			() -> new BlowerBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<BoilerBlock> BOILER = BLOCKS.register("boiler",
			() -> new BoilerBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<BriquettingPressBlock> BRIQUETTING_PRESS = BLOCKS.register("briquetting_press",
			() -> new BriquettingPressBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<BriquettingPressPart> BRIQUETTING_TURNER = BLOCKS.register("briquetting_turner",
			() -> new BriquettingPressPart(makeMachineProperties().notSolid(), BRIQUETTING_TURNER_SHAPE));

	public static final RegistryObject<GreenAlgaeBlock> GREEN_ALGAE = BLOCKS.register("green_algae",
			() -> new GreenAlgaeBlock(AbstractBlock.Properties.from(Blocks.LILY_PAD)));

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

	public static final RegistryObject<GasBlock> HELIUM_GAS = BLOCKS.register("helium_gas",
			() -> new GasBlock(AbstractBlock.Properties.from(Blocks.AIR).tickRandomly(),
					new ResourceLocation("turtychemistry:textures/gui/hydrogen.png")));

	public static final RegistryObject<GasExtractorBlock> GAS_EXTRACTOR = BLOCKS.register("gas_extractor",
			() -> new GasExtractorBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<Block> BRINE_BLOCK = BLOCKS.register("brine_block",
			() -> new Block(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(2.0f, 4.0f)
					.harvestLevel(1).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).speedFactor(0.6f)
					.slipperiness(0.5f)));

	public static final RegistryObject<Block> PAINITE = BLOCKS.register("painite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.IRON_ORE)));

	public static final RegistryObject<Block> COLEMANITE = BLOCKS.register("colemanite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.IRON_ORE)));

	public static final RegistryObject<Block> BORACITE = BLOCKS.register("boracite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.IRON_ORE)));

	public static final RegistryObject<Block> BORAX = BLOCKS.register("borax",
			() -> new Block(AbstractBlock.Properties.from(Blocks.IRON_ORE).hardnessAndResistance(0.5f, 15.0f)));

	public static final RegistryObject<Block> ULEXITE = BLOCKS.register("ulexite",
			() -> new Block(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA)
					.harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3.0f).setLightLevel(state -> 5)
					.sound(SoundType.GLASS).notSolid()));

	public static final RegistryObject<Block> PEGMATITE = BLOCKS.register("pegmatite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> ALMANDINE = BLOCKS.register("almandine",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> COLUMBITE = BLOCKS.register("columbite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> SPESSARTINE = BLOCKS.register("spessartine",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> TANTALITE = BLOCKS.register("tantalite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> CASSITERITE = BLOCKS.register("cassiterite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> HIDDENITE = BLOCKS.register("hiddenite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> KUNZITE = BLOCKS.register("kunzite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> SPODUMENE = BLOCKS.register("spodumene",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid()));

	public static final RegistryObject<Block> TRIPHANE = BLOCKS.register("triphane",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> LEPIDOLITE = BLOCKS.register("lepidolite",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE)));

	public static final RegistryObject<Block> TOURMALINE = BLOCKS.register("tourmaline",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid()));

	public static final RegistryObject<Block> KERNITE = BLOCKS.register("kernite",
			() -> new Block(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE)
					.harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0f).setLightLevel(state -> 1)
					.sound(SoundType.GLASS).notSolid()));

	public static final RegistryObject<GasifierBlock> GASIFIER = BLOCKS.register("gasifier",
			() -> new GasifierBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<SiloBlock> SILO = BLOCKS.register("silo",
			() -> new SiloBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<HopperBlock> HOPPER = BLOCKS.register("hopper",
			() -> new HopperBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<CableBlock> CABLE = BLOCKS.register("cable",
			() -> new CableBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<ElectrolyzerBlock> ELECTROLYZER = BLOCKS.register("electrolyzer",
			() -> new ElectrolyzerBlock(makeMachineProperties().notSolid().variableOpacity()));

	public static final RegistryObject<GasCanisterBlock> GAS_CANISTER_S = BLOCKS.register("gas_canister_small",
			() -> new GasCanisterBlock(makeMachineProperties().notSolid(), false));

	public static final RegistryObject<GasCanisterBlock> GAS_CANISTER_L = BLOCKS.register("gas_canister_large",
			() -> new GasCanisterBlock(makeMachineProperties().notSolid(), true));

	public static final RegistryObject<Block> BORON_BLOCK = BLOCKS.register("boron_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.COBBLESTONE)));

	public static final RegistryObject<Block> CADMIUM_BLOCK = BLOCKS.register("cadmium_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.COBBLESTONE).hardnessAndResistance(1.0f, 4.5f)));

	public static final RegistryObject<Block> GADOLINIUM_BLOCK = BLOCKS.register("gadolinium_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.IRON_BLOCK)));

	public static final RegistryObject<Block> HAFNIUM_BLOCK = BLOCKS.register("hafnium_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIAMOND_BLOCK)));

	public static final RegistryObject<Block> INDIUM_BLOCK = BLOCKS.register("indium_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.COBBLESTONE).hardnessAndResistance(0.5f, 2.5f)));

	public static final RegistryObject<Block> LEAD_BLOCK = BLOCKS.register("lead_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.COBBLESTONE).hardnessAndResistance(5f, 7f)));

	public static final RegistryObject<Block> OSMIUM_BLOCK = BLOCKS.register("osmium_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.IRON_BLOCK).hardnessAndResistance(10f, 2f)));

	public static final RegistryObject<Block> PALLADIUM_BLOCK = BLOCKS.register("palladium_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.COBBLESTONE).hardnessAndResistance(5f, 7f)));

	public static final RegistryObject<Block> RUTHENIUM_BLOCK = BLOCKS.register("ruthenium_block",
			() -> new Block(AbstractBlock.Properties.from(Blocks.DIAMOND_BLOCK)));

	public static final RegistryObject<BunsenBurnerBlock> BUNSEN_BURNER = BLOCKS.register("bunsen_burner",
			() -> new BunsenBurnerBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<ResearcherBlock> RESEARCHER = BLOCKS.register("researcher",
			() -> new ResearcherBlock(makeMachineProperties().notSolid(), Processor.BASIC));

	public static final RegistryObject<AbstractSolarPanelBlock> BASIC_SOLAR_PANEL = BLOCKS.register("basic_solar_panel",
			() -> new BasicSolarPanelBlock(makeMachineProperties().notSolid()));

	public static final RegistryObject<Block> RED_BERYL = BLOCKS.register("red_beryl",
			() -> new CrystalBlock(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid().variableOpacity()));

	public static final RegistryObject<Block> PURE_BERYL = BLOCKS.register("pure_beryl",
			() -> new CrystalBlock(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid().variableOpacity()));

	public static final RegistryObject<Block> GREEN_BERYL = BLOCKS.register("green_beryl",
			() -> new CrystalBlock(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid().variableOpacity()));

	public static final RegistryObject<Block> BLUE_BERYL = BLOCKS.register("blue_beryl",
			() -> new CrystalBlock(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid().variableOpacity()));

	public static final RegistryObject<Block> YELLOW_BERYL = BLOCKS.register("yellow_beryl",
			() -> new CrystalBlock(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid().variableOpacity()));

	public static final RegistryObject<Block> BLACK_BERYL = BLOCKS.register("black_beryl",
			() -> new CrystalBlock(AbstractBlock.Properties.from(Blocks.DIORITE).notSolid().variableOpacity()));

	protected static AbstractBlock.Properties makeMachineProperties() {
		return AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(1.2f).sound(SoundType.METAL)
				.harvestLevel(2).harvestTool(ToolType.PICKAXE);
	}

	private BlockInit() {
	}
}
