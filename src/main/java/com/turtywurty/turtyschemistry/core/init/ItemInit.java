package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.TurtyChemistry.ChemistryItemGroup;
import com.turtywurty.turtyschemistry.common.items.GasCanisterItem;
import com.turtywurty.turtyschemistry.common.items.GreenAlgaeItem;

import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			TurtyChemistry.MOD_ID);

	private static final Item.Properties properties = new Item.Properties().group(ChemistryItemGroup.instance);

	public static final RegistryObject<Item> HYDROGEN = ITEMS.register("hydrogen", () -> new Item(properties));
	public static final RegistryObject<Item> HELIUM = ITEMS.register("helium", () -> new Item(properties));
	public static final RegistryObject<Item> LITHIUM = ITEMS.register("lithium", () -> new Item(properties));
	public static final RegistryObject<Item> BERYLLIUM = ITEMS.register("beryllium", () -> new Item(properties));
	public static final RegistryObject<Item> BORON = ITEMS.register("boron", () -> new Item(properties));
	public static final RegistryObject<Item> CARBON = ITEMS.register("carbon", () -> new Item(properties));
	public static final RegistryObject<Item> NITROGEN = ITEMS.register("nitrogen", () -> new Item(properties));
	public static final RegistryObject<Item> OXYGEN = ITEMS.register("oxygen", () -> new Item(properties));
	public static final RegistryObject<Item> FLUORINE = ITEMS.register("fluorine", () -> new Item(properties));
	public static final RegistryObject<Item> NEON = ITEMS.register("neon", () -> new Item(properties));
	public static final RegistryObject<Item> SODIUM = ITEMS.register("sodium", () -> new Item(properties));
	public static final RegistryObject<Item> MAGNESIUM = ITEMS.register("magnesium", () -> new Item(properties));
	public static final RegistryObject<Item> ALUMINIUM = ITEMS.register("aluminium", () -> new Item(properties));
	public static final RegistryObject<Item> SILICON = ITEMS.register("silicon", () -> new Item(properties));
	public static final RegistryObject<Item> PHOSPHORUS = ITEMS.register("phosphorus", () -> new Item(properties));
	public static final RegistryObject<Item> SULFUR = ITEMS.register("sulfur", () -> new Item(properties));
	public static final RegistryObject<Item> CHLORINE = ITEMS.register("chlorine", () -> new Item(properties));
	public static final RegistryObject<Item> ARGON = ITEMS.register("argon", () -> new Item(properties));
	public static final RegistryObject<Item> POTASSIUM = ITEMS.register("potassium", () -> new Item(properties));
	public static final RegistryObject<Item> CALCIUM = ITEMS.register("calcium", () -> new Item(properties));
	public static final RegistryObject<Item> SCANDIUM = ITEMS.register("scandium", () -> new Item(properties));
	public static final RegistryObject<Item> TITANIUM = ITEMS.register("titanium", () -> new Item(properties));
	public static final RegistryObject<Item> VANADIUM = ITEMS.register("vanadium", () -> new Item(properties));
	public static final RegistryObject<Item> CHROMIUM = ITEMS.register("chromium", () -> new Item(properties));
	public static final RegistryObject<Item> MANGANESE = ITEMS.register("manganese", () -> new Item(properties));
	public static final RegistryObject<Item> IRON = ITEMS.register("iron", () -> new Item(properties));
	public static final RegistryObject<Item> COBALT = ITEMS.register("cobalt", () -> new Item(properties));
	public static final RegistryObject<Item> NICKEL = ITEMS.register("nickel", () -> new Item(properties));
	public static final RegistryObject<Item> COPPER = ITEMS.register("copper", () -> new Item(properties));
	public static final RegistryObject<Item> ZINC = ITEMS.register("zinc", () -> new Item(properties));
	public static final RegistryObject<Item> GALLIUM = ITEMS.register("gallium", () -> new Item(properties));
	public static final RegistryObject<Item> GERMANIUM = ITEMS.register("germanium", () -> new Item(properties));
	public static final RegistryObject<Item> ARSENIC = ITEMS.register("arsenic", () -> new Item(properties));
	public static final RegistryObject<Item> SELENIUM = ITEMS.register("selenium", () -> new Item(properties));
	public static final RegistryObject<Item> BROMINE = ITEMS.register("bromine", () -> new Item(properties));
	public static final RegistryObject<Item> KRYPTON = ITEMS.register("krypton", () -> new Item(properties));
	public static final RegistryObject<Item> RUBIDIUM = ITEMS.register("rubidium", () -> new Item(properties));
	public static final RegistryObject<Item> STRONTIUM = ITEMS.register("strontium", () -> new Item(properties));
	public static final RegistryObject<Item> YTTRIUM = ITEMS.register("yttrium", () -> new Item(properties));
	public static final RegistryObject<Item> ZIRCONIUM = ITEMS.register("zirconium", () -> new Item(properties));
	public static final RegistryObject<Item> NIOBIUM = ITEMS.register("niobium", () -> new Item(properties));
	public static final RegistryObject<Item> MOLYDBENUM = ITEMS.register("molydbenum", () -> new Item(properties));
	public static final RegistryObject<Item> TECHNETIUM = ITEMS.register("technetium", () -> new Item(properties));
	public static final RegistryObject<Item> RUTHENIUM = ITEMS.register("ruthenium", () -> new Item(properties));
	public static final RegistryObject<Item> RHODIUM = ITEMS.register("rhodium", () -> new Item(properties));
	public static final RegistryObject<Item> PALLADIUM = ITEMS.register("palladium", () -> new Item(properties));
	public static final RegistryObject<Item> SILVER = ITEMS.register("silver", () -> new Item(properties));
	public static final RegistryObject<Item> CADMIUM = ITEMS.register("cadmium", () -> new Item(properties));
	public static final RegistryObject<Item> INDIUM = ITEMS.register("indium", () -> new Item(properties));
	public static final RegistryObject<Item> TIN = ITEMS.register("tin", () -> new Item(properties));
	public static final RegistryObject<Item> ANTIMONY = ITEMS.register("antimony", () -> new Item(properties));
	public static final RegistryObject<Item> TELLURIUM = ITEMS.register("tellurium", () -> new Item(properties));
	public static final RegistryObject<Item> IODINE = ITEMS.register("iodine", () -> new Item(properties));
	public static final RegistryObject<Item> XENON = ITEMS.register("xenon", () -> new Item(properties));
	public static final RegistryObject<Item> CESIUM = ITEMS.register("cesium", () -> new Item(properties));
	public static final RegistryObject<Item> BARIUM = ITEMS.register("barium", () -> new Item(properties));
	public static final RegistryObject<Item> LANTHANUM = ITEMS.register("lanthanum", () -> new Item(properties));
	public static final RegistryObject<Item> CERIUM = ITEMS.register("cerium", () -> new Item(properties));
	public static final RegistryObject<Item> PRASEODYMIUM = ITEMS.register("praseodymium", () -> new Item(properties));
	public static final RegistryObject<Item> NEODYMIUM = ITEMS.register("neodymium", () -> new Item(properties));
	public static final RegistryObject<Item> PROMETHIUM = ITEMS.register("promethium", () -> new Item(properties));
	public static final RegistryObject<Item> SAMARIUM = ITEMS.register("samarium", () -> new Item(properties));
	public static final RegistryObject<Item> EUROPIUM = ITEMS.register("europium", () -> new Item(properties));
	public static final RegistryObject<Item> GADOLINIUM = ITEMS.register("gadolinium", () -> new Item(properties));
	public static final RegistryObject<Item> TERBIUM = ITEMS.register("terbium", () -> new Item(properties));
	public static final RegistryObject<Item> DYSPROSIUM = ITEMS.register("dysprosium", () -> new Item(properties));
	public static final RegistryObject<Item> HOLMIUM = ITEMS.register("holmium", () -> new Item(properties));
	public static final RegistryObject<Item> ERBIUM = ITEMS.register("erbium", () -> new Item(properties));
	public static final RegistryObject<Item> THULIUM = ITEMS.register("thulium", () -> new Item(properties));
	public static final RegistryObject<Item> YTTERBIUM = ITEMS.register("ytterbium", () -> new Item(properties));
	public static final RegistryObject<Item> LUTETIUM = ITEMS.register("lutetium", () -> new Item(properties));
	public static final RegistryObject<Item> HAFNIUM = ITEMS.register("hafnium", () -> new Item(properties));
	public static final RegistryObject<Item> TANTALUM = ITEMS.register("tantalum", () -> new Item(properties));
	public static final RegistryObject<Item> TUNGSTEN = ITEMS.register("tungsten", () -> new Item(properties));
	public static final RegistryObject<Item> RHENIUM = ITEMS.register("rhenium", () -> new Item(properties));
	public static final RegistryObject<Item> OSMIUM = ITEMS.register("osmium", () -> new Item(properties));
	public static final RegistryObject<Item> IRIDIUM = ITEMS.register("iridium", () -> new Item(properties));
	public static final RegistryObject<Item> PLATINUM = ITEMS.register("platinum", () -> new Item(properties));
	public static final RegistryObject<Item> GOLD = ITEMS.register("gold", () -> new Item(properties));
	public static final RegistryObject<Item> MERCURY = ITEMS.register("mercury", () -> new Item(properties));
	public static final RegistryObject<Item> THALLIUM = ITEMS.register("thallium", () -> new Item(properties));
	public static final RegistryObject<Item> LEAD = ITEMS.register("lead", () -> new Item(properties));
	public static final RegistryObject<Item> BISMUTH = ITEMS.register("bismuth", () -> new Item(properties));
	public static final RegistryObject<Item> POLONIUM = ITEMS.register("polonium", () -> new Item(properties));
	public static final RegistryObject<Item> ASTATINE = ITEMS.register("astatine", () -> new Item(properties));
	public static final RegistryObject<Item> RADON = ITEMS.register("radon", () -> new Item(properties));
	public static final RegistryObject<Item> FRANCIUM = ITEMS.register("francium", () -> new Item(properties));
	public static final RegistryObject<Item> RADIUM = ITEMS.register("radium", () -> new Item(properties));
	public static final RegistryObject<Item> ACTINIUM = ITEMS.register("actinium", () -> new Item(properties));
	public static final RegistryObject<Item> THROIUM = ITEMS.register("throium", () -> new Item(properties));
	public static final RegistryObject<Item> PROTACTINIUM = ITEMS.register("protactinium", () -> new Item(properties));
	public static final RegistryObject<Item> URANIUM = ITEMS.register("uranium", () -> new Item(properties));
	public static final RegistryObject<Item> NEPTUNIUM = ITEMS.register("neptunium", () -> new Item(properties));
	public static final RegistryObject<Item> PLUTONIUM = ITEMS.register("plutonium", () -> new Item(properties));
	public static final RegistryObject<Item> AMERICIUM = ITEMS.register("americium", () -> new Item(properties));
	public static final RegistryObject<Item> CURIUM = ITEMS.register("curium", () -> new Item(properties));
	public static final RegistryObject<Item> BERKELLIUM = ITEMS.register("berkellium", () -> new Item(properties));
	public static final RegistryObject<Item> CALIFORNIUM = ITEMS.register("californium", () -> new Item(properties));
	public static final RegistryObject<Item> EINSTEINIUM = ITEMS.register("einsteinium", () -> new Item(properties));
	public static final RegistryObject<Item> FERMIUM = ITEMS.register("fermium", () -> new Item(properties));
	public static final RegistryObject<Item> MENDELEVIUM = ITEMS.register("mendelevium", () -> new Item(properties));
	public static final RegistryObject<Item> NOBELLIUM = ITEMS.register("nobellium", () -> new Item(properties));
	public static final RegistryObject<Item> LAWRENCIUM = ITEMS.register("lawrencium", () -> new Item(properties));
	public static final RegistryObject<Item> RUTHERFORDIUM = ITEMS.register("rutherfordium", () -> new Item(properties));
	public static final RegistryObject<Item> DUBNIUM = ITEMS.register("dubnium", () -> new Item(properties));
	public static final RegistryObject<Item> SEABROGIUM = ITEMS.register("seabrogium", () -> new Item(properties));
	public static final RegistryObject<Item> BOHRIUM = ITEMS.register("bohrium", () -> new Item(properties));
	public static final RegistryObject<Item> HASSIUM = ITEMS.register("hassium", () -> new Item(properties));
	public static final RegistryObject<Item> MEITNERIUM = ITEMS.register("meitnerium", () -> new Item(properties));
	public static final RegistryObject<Item> DARMSTADTIUM = ITEMS.register("darmstadtium", () -> new Item(properties));
	public static final RegistryObject<Item> ROENTGENIUM = ITEMS.register("roentgenium", () -> new Item(properties));
	public static final RegistryObject<Item> COPERNICIUM = ITEMS.register("copernicium", () -> new Item(properties));
	public static final RegistryObject<Item> NIHONIUM = ITEMS.register("nihonium", () -> new Item(properties));
	public static final RegistryObject<Item> FLEROVIUM = ITEMS.register("flerovium", () -> new Item(properties));
	public static final RegistryObject<Item> MOSCOVIUM = ITEMS.register("moscovium", () -> new Item(properties));
	public static final RegistryObject<Item> LIVERMORIUM = ITEMS.register("livermorium", () -> new Item(properties));
	public static final RegistryObject<Item> TENNESSINE = ITEMS.register("tennessine", () -> new Item(properties));
	public static final RegistryObject<Item> OGANESSON = ITEMS.register("oganesson", () -> new Item(properties));

	public static final RegistryObject<Item> REFINARY_GAS = ITEMS.register("refinary_gas", () -> new Item(properties));
	public static final RegistryObject<Item> PETROL = ITEMS.register("petrol", () -> new Item(properties));
	public static final RegistryObject<Item> NAPHTHA = ITEMS.register("naphtha", () -> new Item(properties));
	public static final RegistryObject<Item> KEROSINE = ITEMS.register("kerosine", () -> new Item(properties));
	public static final RegistryObject<Item> DIESEL = ITEMS.register("diesel", () -> new Item(properties));
	public static final RegistryObject<Item> LUBRICATING_OIL = ITEMS.register("lubricating_oil", () -> new Item(properties));
	public static final RegistryObject<Item> FUEL_OIL = ITEMS.register("fuel_oil", () -> new Item(properties));
	public static final RegistryObject<Item> BITUMEN = ITEMS.register("bitumen", () -> new Item(properties));
	
	public static final RegistryObject<Item> AMMONIA = ITEMS.register("ammonia", () -> new Item(properties));
	public static final RegistryObject<Item> HYDROCHLORIC_ACID = ITEMS.register("hydrochloric_acid", () -> new Item(properties));
	public static final RegistryObject<Item> ISOPROPYL_ALCOHOL = ITEMS.register("isopropyl_alcohol", () -> new Item(properties));
	public static final RegistryObject<Item> SULFURIC_ACID = ITEMS.register("sulfuric_acid", () -> new Item(properties));
	public static final RegistryObject<Item> NITRIC_ACID = ITEMS.register("nitric_acid", () -> new Item(properties));
	public static final RegistryObject<Item> SODIUM_HYDROXIDE = ITEMS.register("sodium_hydroxide", () -> new Item(properties));
	public static final RegistryObject<Item> METHANE = ITEMS.register("methane", () -> new Item(properties));
	public static final RegistryObject<Item> CARBON_MONOXIDE = ITEMS.register("carbon_monoxide", () -> new Item(properties));
	public static final RegistryObject<Item> PHOSPHORIC_ACID = ITEMS.register("phosphoric_acid", () -> new Item(properties));
	public static final RegistryObject<Item> CHRYSOBERYL = ITEMS.register("chrysoberyl", () -> new Item(properties));
	
	public static final RegistryObject<Item> BALLOON = ITEMS.register("balloon", () -> new Item(properties));
	public static final RegistryObject<Item> TEST_TUBE = ITEMS.register("test_tube", () -> new Item(properties));
	public static final RegistryObject<GreenAlgaeItem> GREEN_ALGAE = ITEMS.register("green_algae", () -> new GreenAlgaeItem(properties));
	public static final RegistryObject<Item> SAWDUST = ITEMS.register("sawdust", () -> new Item(properties));
	public static final RegistryObject<Item> BRIQUETTE = ITEMS.register("briquette", () -> new Item(properties));
	
	public static final RegistryObject<GasCanisterItem> GAS_CANISTER_S = ITEMS.register("gas_canister_small", () -> new GasCanisterItem(BlockInit.GAS_CANISTER_S.get(), properties, false));
	public static final RegistryObject<GasCanisterItem> GAS_CANISTER_L = ITEMS.register("gas_canister_large", () -> new GasCanisterItem(BlockInit.GAS_CANISTER_L.get(), properties, true));

	public static final RegistryObject<BucketItem> BRINE_BUCKET = ITEMS.register("brine_bucket", () -> new BucketItem(() -> FluidInit.BRINE_STILL.get(), properties.maxStackSize(16)));
	
	public static final RegistryObject<Item> BUNSEN_FRAME = ITEMS.register("bunsen_frame", () -> new Item(properties));
	public static final RegistryObject<Item> WIRE_GAUZE = ITEMS.register("wire_gauze", () -> new Item(properties));
	public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible", () -> new Item(properties));
}