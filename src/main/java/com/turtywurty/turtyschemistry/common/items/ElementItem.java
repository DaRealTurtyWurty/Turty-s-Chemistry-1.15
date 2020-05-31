package com.turtywurty.turtyschemistry.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ElementItem extends Item {

	private int ticks = 0;

	private HalfLife life;
	private RoomTempState state;
	private float radioactivity;

	public ElementItem(Properties properties, HalfLife lifeIn, RoomTempState stateIn, float radioactivityIn) {
		super(properties);
		this.life = lifeIn;
		this.state = stateIn;
		this.radioactivity = radioactivityIn;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		if (this.life.getTime() == 0) {
			return;
		} else if (this.ticks != this.life.getTime() * 1200) {
			this.ticks++;
		} else if (this.ticks == this.life.getTime() * 1200) {
			this.ticks = 0;
			stack.setCount(0);
		}
	}

	public enum HalfLife {

		NONE(0), HALF_HOUR(30), HOUR(60), HOUR2(120), HOUR6(360), HOUR12(720), HOUR24(1440);

		private int time;

		private HalfLife(int timeIn) {
			this.time = timeIn;
		}

		public int getTime() {
			return this.time;
		}
	}

	public enum RoomTempState {
		SOLID(), LIQUID(), GAS();
	}
}
