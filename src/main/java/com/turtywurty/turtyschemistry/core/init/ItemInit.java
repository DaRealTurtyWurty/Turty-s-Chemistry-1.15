package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.TurtyChemistry.ChemistryItemGroup;
import com.turtywurty.turtyschemistry.common.items.BlueprintItem;
import com.turtywurty.turtyschemistry.common.items.CrystalBlockItem;
import com.turtywurty.turtyschemistry.common.items.GasCanisterItem;
import com.turtywurty.turtyschemistry.common.items.GreenAlgaeItem;
import com.turtywurty.turtyschemistry.common.items.GuideBookItem;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemInit {
	
	private ItemInit() {}

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TurtyChemistry.MOD_ID);

	private static Item.Properties makeItemProperties() {
		return new Item.Properties().group(ChemistryItemGroup.instance);
	}

	public static final RegistryObject<Item> HYDROGEN = ITEMS.register("hydrogen", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> HELIUM = ITEMS.register("helium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LITHIUM = ITEMS.register("lithium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BERYLLIUM = ITEMS.register("beryllium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BORON = ITEMS.register("boron", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CARBON = ITEMS.register("carbon", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NITROGEN = ITEMS.register("nitrogen", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> OXYGEN = ITEMS.register("oxygen", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> FLUORINE = ITEMS.register("fluorine", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NEON = ITEMS.register("neon", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SODIUM = ITEMS.register("sodium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MAGNESIUM = ITEMS.register("magnesium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ALUMINIUM = ITEMS.register("aluminium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SILICON = ITEMS.register("silicon", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PHOSPHORUS = ITEMS.register("phosphorus", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CHLORINE = ITEMS.register("chlorine", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ARGON = ITEMS.register("argon", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> POTASSIUM = ITEMS.register("potassium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CALCIUM = ITEMS.register("calcium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SCANDIUM = ITEMS.register("scandium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TITANIUM = ITEMS.register("titanium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> VANADIUM = ITEMS.register("vanadium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CHROMIUM = ITEMS.register("chromium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MANGANESE = ITEMS.register("manganese", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> IRON = ITEMS.register("iron", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> COBALT = ITEMS.register("cobalt", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NICKEL = ITEMS.register("nickel", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> COPPER = ITEMS.register("copper", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ZINC = ITEMS.register("zinc", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> GALLIUM = ITEMS.register("gallium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> GERMANIUM = ITEMS.register("germanium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ARSENIC = ITEMS.register("arsenic", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SELENIUM = ITEMS.register("selenium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BROMINE = ITEMS.register("bromine", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> KRYPTON = ITEMS.register("krypton", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RUBIDIUM = ITEMS.register("rubidium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> STRONTIUM = ITEMS.register("strontium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> YTTRIUM = ITEMS.register("yttrium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ZIRCONIUM = ITEMS.register("zirconium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NIOBIUM = ITEMS.register("niobium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MOLYDBENUM = ITEMS.register("molydbenum", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TECHNETIUM = ITEMS.register("technetium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RUTHENIUM = ITEMS.register("ruthenium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RHODIUM = ITEMS.register("rhodium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PALLADIUM = ITEMS.register("palladium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SILVER = ITEMS.register("silver", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CADMIUM = ITEMS.register("cadmium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> INDIUM = ITEMS.register("indium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TIN = ITEMS.register("tin", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ANTIMONY = ITEMS.register("antimony", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TELLURIUM = ITEMS.register("tellurium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> IODINE = ITEMS.register("iodine", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> XENON = ITEMS.register("xenon", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CESIUM = ITEMS.register("cesium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BARIUM = ITEMS.register("barium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LANTHANUM = ITEMS.register("lanthanum", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CERIUM = ITEMS.register("cerium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PRASEODYMIUM = ITEMS.register("praseodymium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NEODYMIUM = ITEMS.register("neodymium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PROMETHIUM = ITEMS.register("promethium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SAMARIUM = ITEMS.register("samarium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> EUROPIUM = ITEMS.register("europium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> GADOLINIUM = ITEMS.register("gadolinium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TERBIUM = ITEMS.register("terbium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> DYSPROSIUM = ITEMS.register("dysprosium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> HOLMIUM = ITEMS.register("holmium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ERBIUM = ITEMS.register("erbium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> THULIUM = ITEMS.register("thulium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> YTTERBIUM = ITEMS.register("ytterbium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LUTETIUM = ITEMS.register("lutetium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> HAFNIUM = ITEMS.register("hafnium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TANTALUM = ITEMS.register("tantalum", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TUNGSTEN = ITEMS.register("tungsten", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RHENIUM = ITEMS.register("rhenium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> OSMIUM = ITEMS.register("osmium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> IRIDIUM = ITEMS.register("iridium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PLATINUM = ITEMS.register("platinum", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> GOLD = ITEMS.register("gold", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MERCURY = ITEMS.register("mercury", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> THALLIUM = ITEMS.register("thallium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LEAD = ITEMS.register("lead", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BISMUTH = ITEMS.register("bismuth", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> POLONIUM = ITEMS.register("polonium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ASTATINE = ITEMS.register("astatine", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RADON = ITEMS.register("radon", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> FRANCIUM = ITEMS.register("francium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RADIUM = ITEMS.register("radium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ACTINIUM = ITEMS.register("actinium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> THORIUM = ITEMS.register("thorium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PROTACTINIUM = ITEMS.register("protactinium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> URANIUM = ITEMS.register("uranium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NEPTUNIUM = ITEMS.register("neptunium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PLUTONIUM = ITEMS.register("plutonium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> AMERICIUM = ITEMS.register("americium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CURIUM = ITEMS.register("curium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BERKELLIUM = ITEMS.register("berkellium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CALIFORNIUM = ITEMS.register("californium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> EINSTEINIUM = ITEMS.register("einsteinium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> FERMIUM = ITEMS.register("fermium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MENDELEVIUM = ITEMS.register("mendelevium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NOBELLIUM = ITEMS.register("nobellium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LAWRENCIUM = ITEMS.register("lawrencium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RUTHERFORDIUM = ITEMS.register("rutherfordium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> DUBNIUM = ITEMS.register("dubnium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SEABROGIUM = ITEMS.register("seabrogium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BOHRIUM = ITEMS.register("bohrium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> HASSIUM = ITEMS.register("hassium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MEITNERIUM = ITEMS.register("meitnerium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> DARMSTADTIUM = ITEMS.register("darmstadtium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ROENTGENIUM = ITEMS.register("roentgenium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> COPERNICIUM = ITEMS.register("copernicium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NIHONIUM = ITEMS.register("nihonium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> FLEROVIUM = ITEMS.register("flerovium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MOSCOVIUM = ITEMS.register("moscovium", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LIVERMORIUM = ITEMS.register("livermorium",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TENNESSINE = ITEMS.register("tennessine", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> OGANESSON = ITEMS.register("oganesson", () -> new Item(makeItemProperties()));

	public static final RegistryObject<Item> REFINARY_GAS = ITEMS.register("refinary_gas",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PETROL = ITEMS.register("petrol", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NAPHTHA = ITEMS.register("naphtha", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> KEROSINE = ITEMS.register("kerosine", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> DIESEL = ITEMS.register("diesel", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LUBRICATING_OIL = ITEMS.register("lubricating_oil",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> FUEL_OIL = ITEMS.register("fuel_oil", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BITUMEN = ITEMS.register("bitumen", () -> new Item(makeItemProperties()));

	public static final RegistryObject<Item> AMMONIA = ITEMS.register("ammonia", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> HYDROCHLORIC_ACID = ITEMS.register("hydrochloric_acid",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ISOPROPYL_ALCOHOL = ITEMS.register("isopropyl_alcohol",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SULFURIC_ACID = ITEMS.register("sulfuric_acid",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> NITRIC_ACID = ITEMS.register("nitric_acid",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> SODIUM_HYDROXIDE = ITEMS.register("sodium_hydroxide",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> METHANE = ITEMS.register("methane", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CARBON_MONOXIDE = ITEMS.register("carbon_monoxide",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PHOSPHORIC_ACID = ITEMS.register("phosphoric_acid",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CHRYSOBERYL = ITEMS.register("chrysoberyl",
			() -> new Item(makeItemProperties()));

	public static final RegistryObject<Item> BALLOON = ITEMS.register("balloon", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> TEST_TUBE = ITEMS.register("test_tube", () -> new Item(makeItemProperties()));
	public static final RegistryObject<GreenAlgaeItem> GREEN_ALGAE = ITEMS.register("green_algae",
			() -> new GreenAlgaeItem(makeItemProperties()));
	public static final RegistryObject<Item> SAWDUST = ITEMS.register("sawdust", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BRIQUETTE = ITEMS.register("briquette", () -> new Item(makeItemProperties()));

	public static final RegistryObject<GasCanisterItem> GAS_CANISTER_S = ITEMS.register("gas_canister_small",
			() -> new GasCanisterItem(BlockInit.GAS_CANISTER_S.get(), makeItemProperties(), false));
	public static final RegistryObject<GasCanisterItem> GAS_CANISTER_L = ITEMS.register("gas_canister_large",
			() -> new GasCanisterItem(BlockInit.GAS_CANISTER_L.get(), makeItemProperties(), true));

	public static final RegistryObject<BucketItem> BRINE_BUCKET = ITEMS.register("brine_bucket",
			() -> new BucketItem(FluidInit.BRINE_STILL::get, makeItemProperties().maxStackSize(16)));

	public static final RegistryObject<Item> BUNSEN_FRAME = ITEMS.register("bunsen_frame",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> WIRE_GAUZE = ITEMS.register("wire_gauze", () -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible", () -> new Item(makeItemProperties()));

	public static final RegistryObject<GuideBookItem> GUIDE_BOOK = ITEMS.register("guide_book",
			() -> new GuideBookItem(makeItemProperties().maxStackSize(1)));

	public static final RegistryObject<BlueprintItem> BLUEPRINT = ITEMS.register("blueprint",
			() -> new BlueprintItem(makeItemProperties().maxStackSize(1)));

	public static final RegistryObject<Item> BLACK_TEST_TUBE = ITEMS.register("black_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> RED_TEST_TUBE = ITEMS.register("red_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> GREEN_TEST_TUBE = ITEMS.register("green_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BROWN_TEST_TUBE = ITEMS.register("brown_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> BLUE_TEST_TUBE = ITEMS.register("blue_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PURPLE_TEST_TUBE = ITEMS.register("purple_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> CYAN_TEST_TUBE = ITEMS.register("cyan_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LIGHT_GRAY_TEST_TUBE = ITEMS.register("light_gray_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> GRAY_TEST_TUBE = ITEMS.register("gray_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> PINK_TEST_TUBE = ITEMS.register("pink_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LIME_TEST_TUBE = ITEMS.register("lime_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> YELLOW_TEST_TUBE = ITEMS.register("yellow_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> LIGHT_BLUE_TEST_TUBE = ITEMS.register("light_blue_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> MAGENTA_TEST_TUBE = ITEMS.register("magenta_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> ORANGE_TEST_TUBE = ITEMS.register("orange_test_tube",
			() -> new Item(makeItemProperties()));
	public static final RegistryObject<Item> WHITE_TEST_TUBE = ITEMS.register("white_test_tube",
			() -> new Item(makeItemProperties()));

	public static final RegistryObject<CrystalBlockItem> PURE_BERYL = ITEMS.register("pure_beryl",
			() -> new CrystalBlockItem(BlockInit.PURE_BERYL.get(), makeItemProperties()));
}