package com.turtywurty.turtyschemistry.client.screen.book;

import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.core.init.ItemInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuideBookScreen extends Screen {

	private UUID playerUUID;

	private static final ResourceLocation PAGE_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/encyclopedia/page.png");
	private static final ResourceLocation BOOK_COVER_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
			"textures/gui/encyclopedia/book_cover.png");

	private int xSize, ySize, guiLeft, guiTop;
	public int currentPage;
	public final int maxPages;

	public GuideBookScreen(ITextComponent titleIn) {
		super(titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 256;
		this.ySize = 175;
		this.maxPages = 9;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		this.setFocused((IGuiEventListener) null);
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		if (this.currentPage == 0) {
			ClientUtils.MC.getTextureManager().bindTexture(BOOK_COVER_TEXTURE);
			ClientUtils.blit(this, (this.width - 125) / 2, (this.height - 175) / 2, 0, 0, 125, 175);
		} else {
			ClientUtils.MC.getTextureManager().bindTexture(PAGE_TEXTURE);
			ClientUtils.blit(this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
			switch (this.currentPage) {
			case 1:
				renderContentsPage(mouseX, mouseY, partialTicks);
				break;
			case 2:
				renderElementPage1(mouseX, mouseY, partialTicks);
				break;
			case 3:
				renderElementPage2(mouseX, mouseY, partialTicks);
				break;
			case 4:
				renderElementPage3(mouseX, mouseY, partialTicks);
				break;
			case 5:
				renderElementPage4(mouseX, mouseY, partialTicks);
				break;
			case 6:
				renderElementPage5(mouseX, mouseY, partialTicks);
				break;
			case 7:
				renderElementPage6(mouseX, mouseY, partialTicks);
				break;
			case 8:
				renderElementPage7(mouseX, mouseY, partialTicks);
				break;
			default:
				renderContentsPage(mouseX, mouseY, partialTicks);
				break;
			}
		}

		for (int i = 0; i < this.buttons.size(); ++i) {
			this.buttons.get(i).render(mouseX, mouseY, partialTicks);
		}
	}

	public void renderContentsPage(int mouseX, int mouseY, float partialTicks) {
		float startX = this.guiLeft + 11;
		float startY = this.guiTop + 11;
		int defaultColor = 0x404040;
		String title = new StringTextComponent("Table Of Contents")
				.setStyle(new Style().setUnderlined(true).setBold(true)).getFormattedText();
		this.font.drawString(title, startX, startY, defaultColor);
		if (ClientUtils.isMouseInArea((int) mouseX, (int) mouseY, this.guiLeft + 11, this.guiTop + 21, 50, 5)) {
			this.font
					.drawString(
							new StringTextComponent("• Elements")
									.setStyle(new Style().setUnderlined(true).setBold(true)).getFormattedText(),
							startX, startY + 10, defaultColor);
		} else {
			this.font.drawString("• Elements", startX, startY + 10, defaultColor);
		}
		this.font.drawString("• Compounds", startX, startY + 20, defaultColor);
		this.font.drawString("• Machines", startX, startY + 30, defaultColor);
		this.font.drawString("• Bunsen Burner", startX, startY + 40, defaultColor);
		this.font.drawString("• Miscellaneous", startX, startY + 50, defaultColor);
	}

	public void renderElementPage1(int mouseX, int mouseY, float partialTicks) {
		int startX = this.guiLeft + 28;
		int itemStartX = this.guiLeft + 10;
		int startY = this.guiTop + 9;
		int itemStartY = this.guiTop + 13;
		int defaultColor = 0x404040;

		// Page 1
		this.font.drawString("Hydrogen", startX, itemStartY, defaultColor);
		this.renderItem(new ItemStack(ItemInit.HYDROGEN.get()), itemStartX, itemStartY);
		this.font.drawString("Helium", startX, startY + 19, defaultColor);
		this.renderItem(new ItemStack(ItemInit.HELIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Lithium", startX, startY + 34, defaultColor);
		this.renderItem(new ItemStack(ItemInit.LITHIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Beryllium", startX, startY + 49, defaultColor);
		this.renderItem(new ItemStack(ItemInit.BERYLLIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Boron", startX, startY + 64, defaultColor);
		this.renderItem(new ItemStack(ItemInit.BORON.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Carbon", startX, startY + 79, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CARBON.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Nitrogen", startX, startY + 94, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NITROGEN.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Oxygen", startX, startY + 109, defaultColor);
		this.renderItem(new ItemStack(ItemInit.OXYGEN.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Fluorine", startX, startY + 124, defaultColor);
		this.renderItem(new ItemStack(ItemInit.FLUORINE.get()), itemStartX, itemStartY + 118);

		// Page 2
		startX = this.guiLeft + 165;
		itemStartX = this.guiLeft + 148;
		this.font.drawString("Neon", startX, itemStartY + 3, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NEON.get()), itemStartX, itemStartY);
		this.font.drawString("Sodium", startX, startY + 22, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SODIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Magnesium", startX, startY + 37, defaultColor);
		this.renderItem(new ItemStack(ItemInit.MAGNESIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Aluminium", startX, startY + 52, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ALUMINIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Silicon", startX, startY + 67, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SILICON.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Phosphorus", startX, startY + 82, defaultColor);
		this.renderItem(new ItemStack(ItemInit.PHOSPHORUS.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Sulfur", startX, startY + 97, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SULFUR.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Chlorine", startX, startY + 112, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CHLORINE.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Argon", startX, startY + 127, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ARGON.get()), itemStartX, itemStartY + 118);
		this.font.drawString("Potassium", startX, startY + 142, defaultColor);
		this.renderItem(new ItemStack(ItemInit.POTASSIUM.get()), itemStartX, itemStartY + 133);
	}

	public void renderElementPage2(int mouseX, int mouseY, float partialTicks) {
		int startX = this.guiLeft + 28;
		int itemStartX = this.guiLeft + 10;
		int startY = this.guiTop + 9;
		int itemStartY = this.guiTop + 13;
		int defaultColor = 0x404040;

		// Page 1
		this.font.drawString("Calcium", startX, itemStartY, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CALCIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Scandium", startX, startY + 19, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SCANDIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Titanium", startX, startY + 34, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TITANIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Vanadium", startX, startY + 49, defaultColor);
		this.renderItem(new ItemStack(ItemInit.VANADIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Chromium", startX, startY + 64, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CHROMIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Manganese", startX, startY + 79, defaultColor);
		this.renderItem(new ItemStack(ItemInit.MANGANESE.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Iron", startX, startY + 94, defaultColor);
		this.renderItem(new ItemStack(ItemInit.IRON.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Cobalt", startX, startY + 109, defaultColor);
		this.renderItem(new ItemStack(ItemInit.COBALT.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Nickel", startX, startY + 124, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NICKEL.get()), itemStartX, itemStartY + 118);

		// Page 2
		startX = this.guiLeft + 165;
		itemStartX = this.guiLeft + 148;
		this.font.drawString("Copper", startX, itemStartY + 3, defaultColor);
		this.renderItem(new ItemStack(ItemInit.COPPER.get()), itemStartX, itemStartY);
		this.font.drawString("Zinc", startX, startY + 22, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ZINC.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Gallium", startX, startY + 37, defaultColor);
		this.renderItem(new ItemStack(ItemInit.GALLIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Germanium", startX, startY + 52, defaultColor);
		this.renderItem(new ItemStack(ItemInit.GERMANIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Arsenic", startX, startY + 67, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ARSENIC.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Selenium", startX, startY + 82, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SELENIUM.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Bromine", startX, startY + 97, defaultColor);
		this.renderItem(new ItemStack(ItemInit.BROMINE.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Krypton", startX, startY + 112, defaultColor);
		this.renderItem(new ItemStack(ItemInit.KRYPTON.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Rubidium", startX, startY + 127, defaultColor);
		this.renderItem(new ItemStack(ItemInit.RUBIDIUM.get()), itemStartX, itemStartY + 118);
		this.font.drawString("Strontium", startX, startY + 142, defaultColor);
		this.renderItem(new ItemStack(ItemInit.STRONTIUM.get()), itemStartX, itemStartY + 133);
	}

	public void renderElementPage3(int mouseX, int mouseY, float partialTicks) {
		int startX = this.guiLeft + 28;
		int itemStartX = this.guiLeft + 10;
		int startY = this.guiTop + 9;
		int itemStartY = this.guiTop + 13;
		int defaultColor = 0x404040;

		// Page 1
		this.font.drawString("Yttrium", startX, itemStartY, defaultColor);
		this.renderItem(new ItemStack(ItemInit.YTTRIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Zirconium", startX, startY + 19, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ZIRCONIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Niobium", startX, startY + 34, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NIOBIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Molydbenum", startX, startY + 49, defaultColor);
		this.renderItem(new ItemStack(ItemInit.MOLYDBENUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Technetium", startX, startY + 64, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TECHNETIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Ruthenium", startX, startY + 79, defaultColor);
		this.renderItem(new ItemStack(ItemInit.RUTHENIUM.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Rhodium", startX, startY + 94, defaultColor);
		this.renderItem(new ItemStack(ItemInit.RHODIUM.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Palladium", startX, startY + 109, defaultColor);
		this.renderItem(new ItemStack(ItemInit.PALLADIUM.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Silver", startX, startY + 124, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SILVER.get()), itemStartX, itemStartY + 118);

		// Page 2
		startX = this.guiLeft + 165;
		itemStartX = this.guiLeft + 148;
		this.font.drawString("Cadmium", startX, itemStartY + 3, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CADMIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Indium", startX, startY + 22, defaultColor);
		this.renderItem(new ItemStack(ItemInit.INDIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Tin", startX, startY + 37, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TIN.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Antimony", startX, startY + 52, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ANTIMONY.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Tellurium", startX, startY + 67, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TELLURIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Iodine", startX, startY + 82, defaultColor);
		this.renderItem(new ItemStack(ItemInit.IODINE.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Xenon", startX, startY + 97, defaultColor);
		this.renderItem(new ItemStack(ItemInit.XENON.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Cesium", startX, startY + 112, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CESIUM.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Barium", startX, startY + 127, defaultColor);
		this.renderItem(new ItemStack(ItemInit.BARIUM.get()), itemStartX, itemStartY + 118);
		this.font.drawString("Lanthanum", startX, startY + 142, defaultColor);
		this.renderItem(new ItemStack(ItemInit.LANTHANUM.get()), itemStartX, itemStartY + 133);
	}

	public void renderElementPage4(int mouseX, int mouseY, float partialTicks) {
		int startX = this.guiLeft + 28;
		int itemStartX = this.guiLeft + 10;
		int startY = this.guiTop + 9;
		int itemStartY = this.guiTop + 13;
		int defaultColor = 0x404040;

		// Page 1
		this.font.drawString("Cerium", startX, itemStartY, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CERIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Praseodynium", startX, startY + 19, defaultColor);
		this.renderItem(new ItemStack(ItemInit.PRASEODYMIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Neodynium", startX, startY + 34, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NEODYMIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Promethium", startX, startY + 49, defaultColor);
		this.renderItem(new ItemStack(ItemInit.PROMETHIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Samarium", startX, startY + 64, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SAMARIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Europium", startX, startY + 79, defaultColor);
		this.renderItem(new ItemStack(ItemInit.EUROPIUM.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Gadolinium", startX, startY + 94, defaultColor);
		this.renderItem(new ItemStack(ItemInit.GADOLINIUM.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Terbium", startX, startY + 109, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TERBIUM.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Dysprosium", startX, startY + 124, defaultColor);
		this.renderItem(new ItemStack(ItemInit.DYSPROSIUM.get()), itemStartX, itemStartY + 118);

		// Page 2
		startX = this.guiLeft + 165;
		itemStartX = this.guiLeft + 148;
		this.font.drawString("Holmium", startX, itemStartY + 3, defaultColor);
		this.renderItem(new ItemStack(ItemInit.HOLMIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Erbium", startX, startY + 22, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ERBIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Thulium", startX, startY + 37, defaultColor);
		this.renderItem(new ItemStack(ItemInit.THULIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Ytterbium", startX, startY + 52, defaultColor);
		this.renderItem(new ItemStack(ItemInit.YTTERBIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Lutetium", startX, startY + 67, defaultColor);
		this.renderItem(new ItemStack(ItemInit.LUTETIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Hafnium", startX, startY + 82, defaultColor);
		this.renderItem(new ItemStack(ItemInit.HAFNIUM.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Tantalum", startX, startY + 97, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TANTALUM.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Tungsten", startX, startY + 112, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TUNGSTEN.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Rhenium", startX, startY + 127, defaultColor);
		this.renderItem(new ItemStack(ItemInit.RHENIUM.get()), itemStartX, itemStartY + 118);
		this.font.drawString("Osmium", startX, startY + 142, defaultColor);
		this.renderItem(new ItemStack(ItemInit.OSMIUM.get()), itemStartX, itemStartY + 133);
	}

	public void renderElementPage5(int mouseX, int mouseY, float partialTicks) {
		int startX = this.guiLeft + 28;
		int itemStartX = this.guiLeft + 10;
		int startY = this.guiTop + 9;
		int itemStartY = this.guiTop + 13;
		int defaultColor = 0x404040;

		// Page 1
		this.font.drawString("Iridium", startX, itemStartY, defaultColor);
		this.renderItem(new ItemStack(ItemInit.IRIDIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Platinum", startX, startY + 19, defaultColor);
		this.renderItem(new ItemStack(ItemInit.PLATINUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Gold", startX, startY + 34, defaultColor);
		this.renderItem(new ItemStack(ItemInit.GOLD.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Mercury", startX, startY + 49, defaultColor);
		this.renderItem(new ItemStack(ItemInit.MERCURY.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Thallium", startX, startY + 64, defaultColor);
		this.renderItem(new ItemStack(ItemInit.THALLIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Lead", startX, startY + 79, defaultColor);
		this.renderItem(new ItemStack(ItemInit.LEAD.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Bismuth", startX, startY + 94, defaultColor);
		this.renderItem(new ItemStack(ItemInit.BISMUTH.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Polonium", startX, startY + 109, defaultColor);
		this.renderItem(new ItemStack(ItemInit.POLONIUM.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Astatine", startX, startY + 124, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ASTATINE.get()), itemStartX, itemStartY + 118);

		// Page 2
		startX = this.guiLeft + 165;
		itemStartX = this.guiLeft + 148;
		this.font.drawString("Radon", startX, itemStartY + 3, defaultColor);
		this.renderItem(new ItemStack(ItemInit.RADON.get()), itemStartX, itemStartY);
		this.font.drawString("Francium", startX, startY + 22, defaultColor);
		this.renderItem(new ItemStack(ItemInit.FRANCIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Radium", startX, startY + 37, defaultColor);
		this.renderItem(new ItemStack(ItemInit.RADIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Actinium", startX, startY + 52, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ACTINIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Thorium", startX, startY + 67, defaultColor);
		this.renderItem(new ItemStack(ItemInit.THROIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Protactinium", startX, startY + 82, defaultColor);
		this.renderItem(new ItemStack(ItemInit.PROTACTINIUM.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Uranium", startX, startY + 97, defaultColor);
		this.renderItem(new ItemStack(ItemInit.URANIUM.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Neptunium", startX, startY + 112, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NEPTUNIUM.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Plutonium", startX, startY + 127, defaultColor);
		this.renderItem(new ItemStack(ItemInit.PLUTONIUM.get()), itemStartX, itemStartY + 118);
		this.font.drawString("Americium", startX, startY + 142, defaultColor);
		this.renderItem(new ItemStack(ItemInit.AMERICIUM.get()), itemStartX, itemStartY + 133);
	}

	public void renderElementPage6(int mouseX, int mouseY, float partialTicks) {
		int startX = this.guiLeft + 28;
		int itemStartX = this.guiLeft + 10;
		int startY = this.guiTop + 9;
		int itemStartY = this.guiTop + 13;
		int defaultColor = 0x404040;

		// Page 1
		this.font.drawString("Curium", startX, itemStartY, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CURIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Berkellium", startX, startY + 19, defaultColor);
		this.renderItem(new ItemStack(ItemInit.BERKELLIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Californium", startX, startY + 34, defaultColor);
		this.renderItem(new ItemStack(ItemInit.CALIFORNIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Einsteinium", startX, startY + 49, defaultColor);
		this.renderItem(new ItemStack(ItemInit.EINSTEINIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Fermium", startX, startY + 64, defaultColor);
		this.renderItem(new ItemStack(ItemInit.FERMIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Mendelevium", startX, startY + 79, defaultColor);
		this.renderItem(new ItemStack(ItemInit.MENDELEVIUM.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Nobelium", startX, startY + 94, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NOBELLIUM.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Lawrencium", startX, startY + 109, defaultColor);
		this.renderItem(new ItemStack(ItemInit.LAWRENCIUM.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Rutherfordium", startX, startY + 124, defaultColor);
		this.renderItem(new ItemStack(ItemInit.RUTHERFORDIUM.get()), itemStartX, itemStartY + 118);

		// Page 2
		startX = this.guiLeft + 165;
		itemStartX = this.guiLeft + 148;
		this.font.drawString("Dubnium", startX, itemStartY + 3, defaultColor);
		this.renderItem(new ItemStack(ItemInit.DUBNIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Seaborgium", startX, startY + 22, defaultColor);
		this.renderItem(new ItemStack(ItemInit.SEABROGIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Bohrium", startX, startY + 37, defaultColor);
		this.renderItem(new ItemStack(ItemInit.BOHRIUM.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Hassium", startX, startY + 52, defaultColor);
		this.renderItem(new ItemStack(ItemInit.HASSIUM.get()), itemStartX, itemStartY + 43);
		this.font.drawString("Meitnerium", startX, startY + 67, defaultColor);
		this.renderItem(new ItemStack(ItemInit.MEITNERIUM.get()), itemStartX, itemStartY + 58);
		this.font.drawString("Darmstadtium", startX, startY + 82, defaultColor);
		this.renderItem(new ItemStack(ItemInit.DARMSTADTIUM.get()), itemStartX, itemStartY + 73);
		this.font.drawString("Roentgenium", startX, startY + 97, defaultColor);
		this.renderItem(new ItemStack(ItemInit.ROENTGENIUM.get()), itemStartX, itemStartY + 88);
		this.font.drawString("Copernicium", startX, startY + 112, defaultColor);
		this.renderItem(new ItemStack(ItemInit.COPERNICIUM.get()), itemStartX, itemStartY + 103);
		this.font.drawString("Nihonium", startX, startY + 127, defaultColor);
		this.renderItem(new ItemStack(ItemInit.NIHONIUM.get()), itemStartX, itemStartY + 118);
		this.font.drawString("Flerovium", startX, startY + 142, defaultColor);
		this.renderItem(new ItemStack(ItemInit.FLEROVIUM.get()), itemStartX, itemStartY + 133);
	}

	public void renderElementPage7(int mouseX, int mouseY, float partialTicks) {
		int startX = this.guiLeft + 28;
		int itemStartX = this.guiLeft + 10;
		int startY = this.guiTop + 9;
		int itemStartY = this.guiTop + 13;
		int defaultColor = 0x404040;

		// Page 1
		this.font.drawString("Moscovium", startX, itemStartY, defaultColor);
		this.renderItem(new ItemStack(ItemInit.MOSCOVIUM.get()), itemStartX, itemStartY);
		this.font.drawString("Livermorium", startX, startY + 19, defaultColor);
		this.renderItem(new ItemStack(ItemInit.LIVERMORIUM.get()), itemStartX, itemStartY + 13);
		this.font.drawString("Tennessine", startX, startY + 34, defaultColor);
		this.renderItem(new ItemStack(ItemInit.TENNESSINE.get()), itemStartX, itemStartY + 28);
		this.font.drawString("Oganesson", startX, startY + 49, defaultColor);
		this.renderItem(new ItemStack(ItemInit.OGANESSON.get()), itemStartX, itemStartY + 43);
	}

	public void renderItem(ItemStack stack, int x, int y) {
		ClientUtils.MC.getItemRenderer().renderItemIntoGUI(stack, x, y);
	}

	@Override
	public void init(Minecraft minecraft, int mouseX, int mouseY) {
		super.init(minecraft, mouseX, mouseY);
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		this.addButton(new NextPageButton(this, this.guiLeft + 224, this.guiTop + 151, 18, 10, 0, 179, (ipressable) -> {
			if (this.currentPage + 1 < this.maxPages)
				this.currentPage++;
		}));

		this.addButton(new BackPageButton(this, this.guiLeft + 14, this.guiTop + 151, 18, 10, 0, 192, (ipressable) -> {
			if (this.currentPage > 0)
				this.currentPage--;
		}));
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
		//Elements
		if (ClientUtils.isMouseInArea((int) mouseX, (int) mouseY, this.guiLeft + 11, this.guiTop + 21, 50, 5)) {
			this.currentPage = 2;
		}
		return super.mouseClicked(mouseX, mouseY, partialTicks);
	}

	public class NextPageButton extends Button {
		private int texX, texY;
		private GuideBookScreen screen;

		public NextPageButton(GuideBookScreen screen, int widthIn, int heightIn, int width, int height, int textureX,
				int textureY, Button.IPressable ipressable) {
			super(widthIn, heightIn, width, height, "", ipressable);
			this.texX = textureX;
			this.texY = textureY;
			this.screen = screen;
		}

		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks) {
			if (this.screen.currentPage + 1 >= this.screen.maxPages) {
				this.active = false;
				return;
			}
			this.active = true;

			if (isHovered()) {
				if (this.screen.currentPage == 0) {
					this.x = (this.screen.width - 125) / 2 + 98;
					this.y = (this.screen.height - 175) / 2 + 156;
				} else {
					this.x = this.screen.guiLeft + 224;
					this.y = (this.screen.height - 175) / 2 + 151;
				}

				ClientUtils.renderButton(PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX + 23, this.texY, 18, 10, 256,
						256);
			} else {
				if (this.screen.currentPage == 0) {
					this.x = (this.screen.width - 125) / 2 + 98;
					this.y = (this.screen.height - 175) / 2 + 156;
				} else {
					this.x = this.screen.guiLeft + 224;
					this.y = (this.screen.height - 175) / 2 + 151;
				}

				ClientUtils.renderButton(PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX, this.texY, 18, 10, 256,
						256);
			}
		}
	}

	public class BackPageButton extends Button {
		private int texX, texY;
		private GuideBookScreen screen;

		public BackPageButton(GuideBookScreen screen, int widthIn, int heightIn, int width, int height, int textureX,
				int textureY, Button.IPressable ipressable) {
			super(widthIn, heightIn, width, height, "", ipressable);
			this.texX = textureX;
			this.texY = textureY;
			this.screen = screen;
		}

		@Override
		public void renderButton(int mouseX, int mouseY, float partialTicks) {
			if (isHovered()) {
				if (this.screen.currentPage != 0) {
					this.active = true;
					ClientUtils.renderButton(PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX + 23, this.texY, 18, 10,
							256, 256);
				} else {
					this.active = false;
				}
			} else {
				if (this.screen.currentPage != 0) {
					ClientUtils.renderButton(PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX, this.texY, 18, 10, 256,
							256);
				}
			}
		}
	}
}
