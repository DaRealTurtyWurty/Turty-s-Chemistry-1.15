package com.turtywurty.turtyschemistry.common.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ElementItem extends Item {
	
	private static final String TICKS = "Ticks";

	private HalfLife halfLife;
	private RoomTempState roomTempState;
	private float radioactivity;
	private int magnetism;

	public ElementItem(ElementProperties properties) {
		super(properties);
		this.halfLife = properties.getHalfLife();
		this.roomTempState = properties.getRoomTempState();
		this.radioactivity = properties.getRadioactivity();
		this.magnetism = properties.getMagnetism();
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (this.magnetism > 0.0f) {
			if (entityIn != null && worldIn != null && isSelected) {
				List<ItemEntity> nearbyItems = worldIn.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(
						new BlockPos(entityIn.getPosition().add(-this.magnetism, -this.magnetism, -this.magnetism)),
						new BlockPos(entityIn.getPosition().add(this.magnetism, this.magnetism, this.magnetism))));
				for (ItemEntity item : nearbyItems) {
					item.move(MoverType.SELF, entityIn.getPositionVector());
				}
			}
		}

		if (!stack.getOrCreateTag().contains(TICKS)) {
			stack.getOrCreateTag().putInt(TICKS, 0);
		}

		if (this.halfLife.getTime() == 0) {
			//return;
		} else if (stack.getOrCreateTag().getInt(TICKS) != this.halfLife.getTime() * 1200) {
			stack.getOrCreateTag().putInt(TICKS, stack.getOrCreateTag().getInt(TICKS) + 1);
		} else if (stack.getOrCreateTag().getInt(TICKS) == this.halfLife.getTime() * 1200) {
			stack.getOrCreateTag().putInt(TICKS, 0);
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

	public static class ElementProperties extends Properties {
		private int magnetism;
		private RoomTempState roomTempState;
		private HalfLife halfLife;
		private float radioactivity;

		public void setMagnetic(int magnetismIn) {
			this.magnetism = magnetismIn;
		}

		public void setRoomTempState(RoomTempState stateIn) {
			this.roomTempState = stateIn;
		}

		public void setHalfLife(HalfLife lifeIn) {
			this.halfLife = lifeIn;
		}

		public void setRadioactivity(float amount) {
			this.radioactivity = amount;
		}

		public int getMagnetism() {
			return this.magnetism;
		}

		public RoomTempState getRoomTempState() {
			return this.roomTempState;
		}

		public HalfLife getHalfLife() {
			return this.halfLife;
		}

		public float getRadioactivity() {
			return this.radioactivity;
		}
	}
}
