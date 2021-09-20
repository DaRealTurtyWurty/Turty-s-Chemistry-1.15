package com.turtywurty.turtyschemistry.client.screen.book;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.core.init.ItemInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

public class GuideBookScreen extends Screen {

    private static final ResourceLocation PAGE_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
            "textures/gui/encyclopedia/page.png");

    private static final ResourceLocation BOOK_COVER_TEXTURE = new ResourceLocation(TurtyChemistry.MOD_ID,
            "textures/gui/encyclopedia/book_cover.png");

    private final int xSize, ySize;
    private int guiLeft;
    private int guiTop;
    public int currentPage;
    public final int maxPages;

    public GuideBookScreen(final ITextComponent titleIn) {
        super(titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 256;
        this.ySize = 175;
        this.maxPages = 9;
    }

    @Override
    public void init(final Minecraft minecraft, final int mouseX, final int mouseY) {
        super.init(minecraft, mouseX, mouseY);
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.addButton(new NextPageButton(this, this.guiLeft + 224, this.guiTop + 151, 18, 10, 0, 179,
                ipressable -> {
                    if (this.currentPage + 1 < this.maxPages) {
                        this.currentPage++;
                    }
                }));

        this.addButton(
                new BackPageButton(this, this.guiLeft + 14, this.guiTop + 151, 18, 10, 0, 192, ipressable -> {
                    if (this.currentPage > 0) {
                        this.currentPage--;
                    }
                }));
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int partialTicks) {
        // Elements
        if (ClientUtils.isMouseInArea((int) mouseX, (int) mouseY, this.guiLeft + 11, this.guiTop + 21, 50,
                5)) {
            this.currentPage = 2;
        }
        return super.mouseClicked(mouseX, mouseY, partialTicks);
    }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        this.renderBackground(stack);
        setListener((IGuiEventListener) null);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.currentPage == 0) {
            ClientUtils.MC.getTextureManager().bindTexture(BOOK_COVER_TEXTURE);
            ClientUtils.blit(stack, this, (this.width - 125) / 2, (this.height - 175) / 2, 0, 0, 125, 175);
        } else {
            ClientUtils.MC.getTextureManager().bindTexture(PAGE_TEXTURE);
            ClientUtils.blit(stack, this, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
            switch (this.currentPage) {
            case 1:
                renderContentsPage(stack, mouseX, mouseY, partialTicks);
                break;
            case 2:
                renderElementPage1(stack, mouseX, mouseY, partialTicks);
                break;
            case 3:
                renderElementPage2(stack, mouseX, mouseY, partialTicks);
                break;
            case 4:
                renderElementPage3(stack, mouseX, mouseY, partialTicks);
                break;
            case 5:
                renderElementPage4(stack, mouseX, mouseY, partialTicks);
                break;
            case 6:
                renderElementPage5(stack, mouseX, mouseY, partialTicks);
                break;
            case 7:
                renderElementPage6(stack, mouseX, mouseY, partialTicks);
                break;
            case 8:
                renderElementPage7(stack, mouseX, mouseY, partialTicks);
                break;
            default:
                renderContentsPage(stack, mouseX, mouseY, partialTicks);
                break;
            }
        }

        for (final Widget element : this.buttons) {
            element.render(stack, mouseX, mouseY, partialTicks);
        }
    }

    public void renderContentsPage(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        final float startX = this.guiLeft + 11;
        final float startY = this.guiTop + 11;
        final int defaultColor = 0x404040;
        final String title = new StringTextComponent("Table Of Contents")
                .setStyle(Style.EMPTY.setUnderlined(true).setBold(true)).getString();
        this.font.drawString(stack, title, startX, startY, defaultColor);
        if (ClientUtils.isMouseInArea(mouseX, mouseY, this.guiLeft + 11, this.guiTop + 21, 50, 5)) {
            final StringTextComponent text = new StringTextComponent("• Elements");
            text.setStyle(Style.EMPTY.setUnderlined(true).setBold(true));
            this.font.drawString(stack, text.getString(), startX, startY + 10, defaultColor);
        } else {
            this.font.drawString(stack, "• Elements", startX, startY + 10, defaultColor);
        }
        this.font.drawString(stack, "• Compounds", startX, startY + 20, defaultColor);
        this.font.drawString(stack, "• Machines", startX, startY + 30, defaultColor);
        this.font.drawString(stack, "• Bunsen Burner", startX, startY + 40, defaultColor);
        this.font.drawString(stack, "• Miscellaneous", startX, startY + 50, defaultColor);
    }

    public void renderElementPage1(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        int startX = this.guiLeft + 28;
        int itemStartX = this.guiLeft + 10;
        final int startY = this.guiTop + 9;
        final int itemStartY = this.guiTop + 13;
        final int defaultColor = 0x404040;

        // Page 1
        this.font.drawString(stack, "Hydrogen", startX, itemStartY, defaultColor);
        renderItem(new ItemStack(ItemInit.HYDROGEN.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Helium", startX, startY + 19, defaultColor);
        renderItem(new ItemStack(ItemInit.HELIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Lithium", startX, startY + 34, defaultColor);
        renderItem(new ItemStack(ItemInit.LITHIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Beryllium", startX, startY + 49, defaultColor);
        renderItem(new ItemStack(ItemInit.BERYLLIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Boron", startX, startY + 64, defaultColor);
        renderItem(new ItemStack(ItemInit.BORON.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Carbon", startX, startY + 79, defaultColor);
        renderItem(new ItemStack(ItemInit.CARBON.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Nitrogen", startX, startY + 94, defaultColor);
        renderItem(new ItemStack(ItemInit.NITROGEN.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Oxygen", startX, startY + 109, defaultColor);
        renderItem(new ItemStack(ItemInit.OXYGEN.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Fluorine", startX, startY + 124, defaultColor);
        renderItem(new ItemStack(ItemInit.FLUORINE.get()), itemStartX, itemStartY + 118);

        // Page 2
        startX = this.guiLeft + 165;
        itemStartX = this.guiLeft + 148;
        this.font.drawString(stack, "Neon", startX, itemStartY + 3, defaultColor);
        renderItem(new ItemStack(ItemInit.NEON.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Sodium", startX, startY + 22, defaultColor);
        renderItem(new ItemStack(ItemInit.SODIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Magnesium", startX, startY + 37, defaultColor);
        renderItem(new ItemStack(ItemInit.MAGNESIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Aluminium", startX, startY + 52, defaultColor);
        renderItem(new ItemStack(ItemInit.ALUMINIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Silicon", startX, startY + 67, defaultColor);
        renderItem(new ItemStack(ItemInit.SILICON.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Phosphorus", startX, startY + 82, defaultColor);
        renderItem(new ItemStack(ItemInit.PHOSPHORUS.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Sulfur", startX, startY + 97, defaultColor);
        renderItem(new ItemStack(ItemInit.SULFUR.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Chlorine", startX, startY + 112, defaultColor);
        renderItem(new ItemStack(ItemInit.CHLORINE.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Argon", startX, startY + 127, defaultColor);
        renderItem(new ItemStack(ItemInit.ARGON.get()), itemStartX, itemStartY + 118);
        this.font.drawString(stack, "Potassium", startX, startY + 142, defaultColor);
        renderItem(new ItemStack(ItemInit.POTASSIUM.get()), itemStartX, itemStartY + 133);
    }

    public void renderElementPage2(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        int startX = this.guiLeft + 28;
        int itemStartX = this.guiLeft + 10;
        final int startY = this.guiTop + 9;
        final int itemStartY = this.guiTop + 13;
        final int defaultColor = 0x404040;

        // Page 1
        this.font.drawString(stack, "Calcium", startX, itemStartY, defaultColor);
        renderItem(new ItemStack(ItemInit.CALCIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Scandium", startX, startY + 19, defaultColor);
        renderItem(new ItemStack(ItemInit.SCANDIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Titanium", startX, startY + 34, defaultColor);
        renderItem(new ItemStack(ItemInit.TITANIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Vanadium", startX, startY + 49, defaultColor);
        renderItem(new ItemStack(ItemInit.VANADIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Chromium", startX, startY + 64, defaultColor);
        renderItem(new ItemStack(ItemInit.CHROMIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Manganese", startX, startY + 79, defaultColor);
        renderItem(new ItemStack(ItemInit.MANGANESE.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Iron", startX, startY + 94, defaultColor);
        renderItem(new ItemStack(ItemInit.IRON.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Cobalt", startX, startY + 109, defaultColor);
        renderItem(new ItemStack(ItemInit.COBALT.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Nickel", startX, startY + 124, defaultColor);
        renderItem(new ItemStack(ItemInit.NICKEL.get()), itemStartX, itemStartY + 118);

        // Page 2
        startX = this.guiLeft + 165;
        itemStartX = this.guiLeft + 148;
        this.font.drawString(stack, "Copper", startX, itemStartY + 3, defaultColor);
        renderItem(new ItemStack(ItemInit.COPPER.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Zinc", startX, startY + 22, defaultColor);
        renderItem(new ItemStack(ItemInit.ZINC.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Gallium", startX, startY + 37, defaultColor);
        renderItem(new ItemStack(ItemInit.GALLIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Germanium", startX, startY + 52, defaultColor);
        renderItem(new ItemStack(ItemInit.GERMANIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Arsenic", startX, startY + 67, defaultColor);
        renderItem(new ItemStack(ItemInit.ARSENIC.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Selenium", startX, startY + 82, defaultColor);
        renderItem(new ItemStack(ItemInit.SELENIUM.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Bromine", startX, startY + 97, defaultColor);
        renderItem(new ItemStack(ItemInit.BROMINE.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Krypton", startX, startY + 112, defaultColor);
        renderItem(new ItemStack(ItemInit.KRYPTON.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Rubidium", startX, startY + 127, defaultColor);
        renderItem(new ItemStack(ItemInit.RUBIDIUM.get()), itemStartX, itemStartY + 118);
        this.font.drawString(stack, "Strontium", startX, startY + 142, defaultColor);
        renderItem(new ItemStack(ItemInit.STRONTIUM.get()), itemStartX, itemStartY + 133);
    }

    public void renderElementPage3(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        int startX = this.guiLeft + 28;
        int itemStartX = this.guiLeft + 10;
        final int startY = this.guiTop + 9;
        final int itemStartY = this.guiTop + 13;
        final int defaultColor = 0x404040;

        // Page 1
        this.font.drawString(stack, "Yttrium", startX, itemStartY, defaultColor);
        renderItem(new ItemStack(ItemInit.YTTRIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Zirconium", startX, startY + 19, defaultColor);
        renderItem(new ItemStack(ItemInit.ZIRCONIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Niobium", startX, startY + 34, defaultColor);
        renderItem(new ItemStack(ItemInit.NIOBIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Molydbenum", startX, startY + 49, defaultColor);
        renderItem(new ItemStack(ItemInit.MOLYDBENUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Technetium", startX, startY + 64, defaultColor);
        renderItem(new ItemStack(ItemInit.TECHNETIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Ruthenium", startX, startY + 79, defaultColor);
        renderItem(new ItemStack(ItemInit.RUTHENIUM.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Rhodium", startX, startY + 94, defaultColor);
        renderItem(new ItemStack(ItemInit.RHODIUM.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Palladium", startX, startY + 109, defaultColor);
        renderItem(new ItemStack(ItemInit.PALLADIUM.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Silver", startX, startY + 124, defaultColor);
        renderItem(new ItemStack(ItemInit.SILVER.get()), itemStartX, itemStartY + 118);

        // Page 2
        startX = this.guiLeft + 165;
        itemStartX = this.guiLeft + 148;
        this.font.drawString(stack, "Cadmium", startX, itemStartY + 3, defaultColor);
        renderItem(new ItemStack(ItemInit.CADMIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Indium", startX, startY + 22, defaultColor);
        renderItem(new ItemStack(ItemInit.INDIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Tin", startX, startY + 37, defaultColor);
        renderItem(new ItemStack(ItemInit.TIN.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Antimony", startX, startY + 52, defaultColor);
        renderItem(new ItemStack(ItemInit.ANTIMONY.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Tellurium", startX, startY + 67, defaultColor);
        renderItem(new ItemStack(ItemInit.TELLURIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Iodine", startX, startY + 82, defaultColor);
        renderItem(new ItemStack(ItemInit.IODINE.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Xenon", startX, startY + 97, defaultColor);
        renderItem(new ItemStack(ItemInit.XENON.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Cesium", startX, startY + 112, defaultColor);
        renderItem(new ItemStack(ItemInit.CESIUM.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Barium", startX, startY + 127, defaultColor);
        renderItem(new ItemStack(ItemInit.BARIUM.get()), itemStartX, itemStartY + 118);
        this.font.drawString(stack, "Lanthanum", startX, startY + 142, defaultColor);
        renderItem(new ItemStack(ItemInit.LANTHANUM.get()), itemStartX, itemStartY + 133);
    }

    public void renderElementPage4(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        int startX = this.guiLeft + 28;
        int itemStartX = this.guiLeft + 10;
        final int startY = this.guiTop + 9;
        final int itemStartY = this.guiTop + 13;
        final int defaultColor = 0x404040;

        // Page 1
        this.font.drawString(stack, "Cerium", startX, itemStartY, defaultColor);
        renderItem(new ItemStack(ItemInit.CERIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Praseodynium", startX, startY + 19, defaultColor);
        renderItem(new ItemStack(ItemInit.PRASEODYMIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Neodynium", startX, startY + 34, defaultColor);
        renderItem(new ItemStack(ItemInit.NEODYMIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Promethium", startX, startY + 49, defaultColor);
        renderItem(new ItemStack(ItemInit.PROMETHIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Samarium", startX, startY + 64, defaultColor);
        renderItem(new ItemStack(ItemInit.SAMARIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Europium", startX, startY + 79, defaultColor);
        renderItem(new ItemStack(ItemInit.EUROPIUM.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Gadolinium", startX, startY + 94, defaultColor);
        renderItem(new ItemStack(ItemInit.GADOLINIUM.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Terbium", startX, startY + 109, defaultColor);
        renderItem(new ItemStack(ItemInit.TERBIUM.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Dysprosium", startX, startY + 124, defaultColor);
        renderItem(new ItemStack(ItemInit.DYSPROSIUM.get()), itemStartX, itemStartY + 118);

        // Page 2
        startX = this.guiLeft + 165;
        itemStartX = this.guiLeft + 148;
        this.font.drawString(stack, "Holmium", startX, itemStartY + 3, defaultColor);
        renderItem(new ItemStack(ItemInit.HOLMIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Erbium", startX, startY + 22, defaultColor);
        renderItem(new ItemStack(ItemInit.ERBIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Thulium", startX, startY + 37, defaultColor);
        renderItem(new ItemStack(ItemInit.THULIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Ytterbium", startX, startY + 52, defaultColor);
        renderItem(new ItemStack(ItemInit.YTTERBIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Lutetium", startX, startY + 67, defaultColor);
        renderItem(new ItemStack(ItemInit.LUTETIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Hafnium", startX, startY + 82, defaultColor);
        renderItem(new ItemStack(ItemInit.HAFNIUM.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Tantalum", startX, startY + 97, defaultColor);
        renderItem(new ItemStack(ItemInit.TANTALUM.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Tungsten", startX, startY + 112, defaultColor);
        renderItem(new ItemStack(ItemInit.TUNGSTEN.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Rhenium", startX, startY + 127, defaultColor);
        renderItem(new ItemStack(ItemInit.RHENIUM.get()), itemStartX, itemStartY + 118);
        this.font.drawString(stack, "Osmium", startX, startY + 142, defaultColor);
        renderItem(new ItemStack(ItemInit.OSMIUM.get()), itemStartX, itemStartY + 133);
    }

    public void renderElementPage5(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        int startX = this.guiLeft + 28;
        int itemStartX = this.guiLeft + 10;
        final int startY = this.guiTop + 9;
        final int itemStartY = this.guiTop + 13;
        final int defaultColor = 0x404040;

        // Page 1
        this.font.drawString(stack, "Iridium", startX, itemStartY, defaultColor);
        renderItem(new ItemStack(ItemInit.IRIDIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Platinum", startX, startY + 19, defaultColor);
        renderItem(new ItemStack(ItemInit.PLATINUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Gold", startX, startY + 34, defaultColor);
        renderItem(new ItemStack(ItemInit.GOLD.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Mercury", startX, startY + 49, defaultColor);
        renderItem(new ItemStack(ItemInit.MERCURY.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Thallium", startX, startY + 64, defaultColor);
        renderItem(new ItemStack(ItemInit.THALLIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Lead", startX, startY + 79, defaultColor);
        renderItem(new ItemStack(ItemInit.LEAD.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Bismuth", startX, startY + 94, defaultColor);
        renderItem(new ItemStack(ItemInit.BISMUTH.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Polonium", startX, startY + 109, defaultColor);
        renderItem(new ItemStack(ItemInit.POLONIUM.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Astatine", startX, startY + 124, defaultColor);
        renderItem(new ItemStack(ItemInit.ASTATINE.get()), itemStartX, itemStartY + 118);

        // Page 2
        startX = this.guiLeft + 165;
        itemStartX = this.guiLeft + 148;
        this.font.drawString(stack, "Radon", startX, itemStartY + 3, defaultColor);
        renderItem(new ItemStack(ItemInit.RADON.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Francium", startX, startY + 22, defaultColor);
        renderItem(new ItemStack(ItemInit.FRANCIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Radium", startX, startY + 37, defaultColor);
        renderItem(new ItemStack(ItemInit.RADIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Actinium", startX, startY + 52, defaultColor);
        renderItem(new ItemStack(ItemInit.ACTINIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Thorium", startX, startY + 67, defaultColor);
        renderItem(new ItemStack(ItemInit.THORIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Protactinium", startX, startY + 82, defaultColor);
        renderItem(new ItemStack(ItemInit.PROTACTINIUM.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Uranium", startX, startY + 97, defaultColor);
        renderItem(new ItemStack(ItemInit.URANIUM.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Neptunium", startX, startY + 112, defaultColor);
        renderItem(new ItemStack(ItemInit.NEPTUNIUM.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Plutonium", startX, startY + 127, defaultColor);
        renderItem(new ItemStack(ItemInit.PLUTONIUM.get()), itemStartX, itemStartY + 118);
        this.font.drawString(stack, "Americium", startX, startY + 142, defaultColor);
        renderItem(new ItemStack(ItemInit.AMERICIUM.get()), itemStartX, itemStartY + 133);
    }

    public void renderElementPage6(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        int startX = this.guiLeft + 28;
        int itemStartX = this.guiLeft + 10;
        final int startY = this.guiTop + 9;
        final int itemStartY = this.guiTop + 13;
        final int defaultColor = 0x404040;

        // Page 1
        this.font.drawString(stack, "Curium", startX, itemStartY, defaultColor);
        renderItem(new ItemStack(ItemInit.CURIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Berkellium", startX, startY + 19, defaultColor);
        renderItem(new ItemStack(ItemInit.BERKELLIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Californium", startX, startY + 34, defaultColor);
        renderItem(new ItemStack(ItemInit.CALIFORNIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Einsteinium", startX, startY + 49, defaultColor);
        renderItem(new ItemStack(ItemInit.EINSTEINIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Fermium", startX, startY + 64, defaultColor);
        renderItem(new ItemStack(ItemInit.FERMIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Mendelevium", startX, startY + 79, defaultColor);
        renderItem(new ItemStack(ItemInit.MENDELEVIUM.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Nobelium", startX, startY + 94, defaultColor);
        renderItem(new ItemStack(ItemInit.NOBELLIUM.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Lawrencium", startX, startY + 109, defaultColor);
        renderItem(new ItemStack(ItemInit.LAWRENCIUM.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Rutherfordium", startX, startY + 124, defaultColor);
        renderItem(new ItemStack(ItemInit.RUTHERFORDIUM.get()), itemStartX, itemStartY + 118);

        // Page 2
        startX = this.guiLeft + 165;
        itemStartX = this.guiLeft + 148;
        this.font.drawString(stack, "Dubnium", startX, itemStartY + 3, defaultColor);
        renderItem(new ItemStack(ItemInit.DUBNIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Seaborgium", startX, startY + 22, defaultColor);
        renderItem(new ItemStack(ItemInit.SEABROGIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Bohrium", startX, startY + 37, defaultColor);
        renderItem(new ItemStack(ItemInit.BOHRIUM.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Hassium", startX, startY + 52, defaultColor);
        renderItem(new ItemStack(ItemInit.HASSIUM.get()), itemStartX, itemStartY + 43);
        this.font.drawString(stack, "Meitnerium", startX, startY + 67, defaultColor);
        renderItem(new ItemStack(ItemInit.MEITNERIUM.get()), itemStartX, itemStartY + 58);
        this.font.drawString(stack, "Darmstadtium", startX, startY + 82, defaultColor);
        renderItem(new ItemStack(ItemInit.DARMSTADTIUM.get()), itemStartX, itemStartY + 73);
        this.font.drawString(stack, "Roentgenium", startX, startY + 97, defaultColor);
        renderItem(new ItemStack(ItemInit.ROENTGENIUM.get()), itemStartX, itemStartY + 88);
        this.font.drawString(stack, "Copernicium", startX, startY + 112, defaultColor);
        renderItem(new ItemStack(ItemInit.COPERNICIUM.get()), itemStartX, itemStartY + 103);
        this.font.drawString(stack, "Nihonium", startX, startY + 127, defaultColor);
        renderItem(new ItemStack(ItemInit.NIHONIUM.get()), itemStartX, itemStartY + 118);
        this.font.drawString(stack, "Flerovium", startX, startY + 142, defaultColor);
        renderItem(new ItemStack(ItemInit.FLEROVIUM.get()), itemStartX, itemStartY + 133);
    }

    public void renderElementPage7(final MatrixStack stack, final int mouseX, final int mouseY,
            final float partialTicks) {
        final int startX = this.guiLeft + 28;
        final int itemStartX = this.guiLeft + 10;
        final int startY = this.guiTop + 9;
        final int itemStartY = this.guiTop + 13;
        final int defaultColor = 0x404040;

        // Page 1
        this.font.drawString(stack, "Moscovium", startX, itemStartY, defaultColor);
        renderItem(new ItemStack(ItemInit.MOSCOVIUM.get()), itemStartX, itemStartY);
        this.font.drawString(stack, "Livermorium", startX, startY + 19, defaultColor);
        renderItem(new ItemStack(ItemInit.LIVERMORIUM.get()), itemStartX, itemStartY + 13);
        this.font.drawString(stack, "Tennessine", startX, startY + 34, defaultColor);
        renderItem(new ItemStack(ItemInit.TENNESSINE.get()), itemStartX, itemStartY + 28);
        this.font.drawString(stack, "Oganesson", startX, startY + 49, defaultColor);
        renderItem(new ItemStack(ItemInit.OGANESSON.get()), itemStartX, itemStartY + 43);
    }

    public void renderItem(final ItemStack stack, final int x, final int y) {
        ClientUtils.MC.getItemRenderer().renderItemIntoGUI(stack, x, y);
    }

    public class BackPageButton extends Button {
        private final int texX, texY;
        private final GuideBookScreen screen;

        public BackPageButton(final GuideBookScreen screen, final int widthIn, final int heightIn,
                final int width, final int height, final int textureX, final int textureY,
                final Button.IPressable ipressable) {
            super(widthIn, heightIn, width, height, new StringTextComponent(""), ipressable);
            this.texX = textureX;
            this.texY = textureY;
            this.screen = screen;
        }

        @Override
        public void renderWidget(final MatrixStack stack, final int mouseX, final int mouseY,
                final float partialTicks) {
            if (isHovered()) {
                if (this.screen.currentPage != 0) {
                    this.active = true;
                    ClientUtils.renderButton(stack, PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX + 23,
                            this.texY, 18, 10, 256, 256);
                } else {
                    this.active = false;
                }
            } else if (this.screen.currentPage != 0) {
                ClientUtils.renderButton(stack, PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX, this.texY,
                        18, 10, 256, 256);
            }
        }
    }

    public class NextPageButton extends Button {
        private final int texX, texY;
        private final GuideBookScreen screen;

        public NextPageButton(final GuideBookScreen screen, final int widthIn, final int heightIn,
                final int width, final int height, final int textureX, final int textureY,
                final Button.IPressable ipressable) {
            super(widthIn, heightIn, width, height, new StringTextComponent(""), ipressable);
            this.texX = textureX;
            this.texY = textureY;
            this.screen = screen;
        }

        @Override
        public void renderWidget(final MatrixStack stack, final int mouseX, final int mouseY,
                final float partialTicks) {
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

                ClientUtils.renderButton(stack, PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX + 23,
                        this.texY, 18, 10, 256, 256);
            } else {
                if (this.screen.currentPage == 0) {
                    this.x = (this.screen.width - 125) / 2 + 98;
                    this.y = (this.screen.height - 175) / 2 + 156;
                } else {
                    this.x = this.screen.guiLeft + 224;
                    this.y = (this.screen.height - 175) / 2 + 151;
                }

                ClientUtils.renderButton(stack, PAGE_TEXTURE, this, partialTicks, 1.0f, this.texX, this.texY,
                        18, 10, 256, 256);
            }
        }
    }
}
