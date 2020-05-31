package com.turtywurty.turtyschemistry.client.event;

/*import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID)*/
public class AddStatisticsMenu {

	/*private final static StatisticsManager stats = new StatisticsManager();
	private static StatsScreen gui;
	private static MachinesStatList machineStats = new MachinesStatList(gui.minecraft);

	@SubscribeEvent
	public static void changeGUI(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof StatsScreen) {
			gui = (StatsScreen) event.getGui();
			Button button = new Button(gui.width / 2 + 160, gui.height - 52, 20, 20, I18n.format("stat.machines"),
					(pressable) -> {
						showScreen(machineStats);
					});
		}
	}

	public static void showScreen(@Nullable ExtendedList<?> list) {
		gui.children.remove(gui.generalStats);
		gui.children.remove(gui.itemStats);
		gui.children.remove(gui.mobStats);
		if (list != null) {
			gui.children.add(0, list);
			gui.displaySlot = list;
		}

	}

	@OnlyIn(Dist.CLIENT)
	class MachinesStatList extends ExtendedList<MachinesStatList.Entry> {
		public MachinesStatList(Minecraft mcIn) {
			super(mcIn, AddStatisticsMenu.gui.width, AddStatisticsMenu.gui.height, 32,
					AddStatisticsMenu.gui.height - 64, 10);

			for (Stat<ResourceLocation> stat : Stats.CUSTOM) {
				this.addEntry(new MachinesStatList.Entry(stat));
			}
		}

		protected void renderBackground() {
			AddStatisticsMenu.gui.renderBackground();
		}

		@OnlyIn(Dist.CLIENT)
		class Entry extends ExtendedList.AbstractListEntry<MachinesStatList.Entry> {
			private final Stat<ResourceLocation> stat;

			private Entry(Stat<ResourceLocation> stat) {
				this.stat = stat;
			}

			public void render(int p_render_1_, int p_render_2_, int p_render_3_, int p_render_4_, int p_render_5_,
					int p_render_6_, int p_render_7_, boolean p_render_8_, float p_render_9_) {
				ITextComponent itextcomponent = (new TranslationTextComponent(
						"stat." + this.stat.getValue().toString().replace(':', '.')))
								.applyTextStyle(TextFormatting.GRAY);
				MachinesStatList.this.drawString(AddStatisticsMenu.gui.font, itextcomponent.getString(),
						p_render_3_ + 2, p_render_2_ + 1, p_render_1_ % 2 == 0 ? 16777215 : 9474192);
				String s = this.stat.format(AddStatisticsMenu.stats.getValue(this.stat));
				MachinesStatList.this.drawString(AddStatisticsMenu.gui.font, s,
						p_render_3_ + 2 + 213 - AddStatisticsMenu.gui.font.getStringWidth(s), p_render_2_ + 1,
						p_render_1_ % 2 == 0 ? 16777215 : 9474192);
			}
		}
	}*/
}
