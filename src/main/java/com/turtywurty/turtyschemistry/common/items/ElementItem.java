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

	public static class ElementProperties extends Properties {
		private int magnetism;
		private RoomTempState roomTempState;
		private HalfLife halfLife;
		private float radioactivity;

		public HalfLife getHalfLife() {
			return this.halfLife;
		}

		public int getMagnetism() {
			return this.magnetism;
		}

		public float getRadioactivity() {
			return this.radioactivity;
		}

		public RoomTempState getRoomTempState() {
			return this.roomTempState;
		}

		public void setHalfLife(final HalfLife lifeIn) {
			this.halfLife = lifeIn;
		}

		public void setMagnetic(final int magnetismIn) {
			this.magnetism = magnetismIn;
		}

		public void setRadioactivity(final float amount) {
			this.radioactivity = amount;
		}

		public void setRoomTempState(final RoomTempState stateIn) {
			this.roomTempState = stateIn;
		}
	}

	public enum HalfLife {

		NONE(0), HALF_HOUR(30), HOUR(60), HOUR2(120), HOUR6(360), HOUR12(720), HOUR24(1440);

		private int time;

		HalfLife(final int timeIn) {
			this.time = timeIn;
		}

		public int getTime() {
			return this.time;
		}
	}

	public enum RoomTempState {
		SOLID(), LIQUID(), GAS();
	}

	private static final String TICKS = "Ticks";
	private final HalfLife halfLife;

	private final RoomTempState roomTempState;

	private final float radioactivity;

	private final int magnetism;

	public ElementItem(final ElementProperties properties) {
		super(properties);
		this.halfLife = properties.getHalfLife();
		this.roomTempState = properties.getRoomTempState();
		this.radioactivity = properties.getRadioactivity();
		this.magnetism = properties.getMagnetism();
	}

	@Override
	public void inventoryTick(final ItemStack stack, final World worldIn, final Entity entityIn, final int itemSlot,
			final boolean isSelected) {
		if ((this.magnetism > 0.0f) && (entityIn != null && worldIn != null && isSelected)) {
			List<ItemEntity> nearbyItems = worldIn.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(
					new BlockPos(
							entityIn.getLeashStartPosition().add(-this.magnetism, -this.magnetism, -this.magnetism)),
					new BlockPos(
							entityIn.getLeashStartPosition().add(this.magnetism, this.magnetism, this.magnetism))));
			for (ItemEntity item : nearbyItems) {
				item.move(MoverType.SELF, entityIn.getPositionVec());
			}
		}

		if (!stack.getOrCreateTag().contains(TICKS)) {
			stack.getOrCreateTag().putInt(TICKS, 0);
		}

		if (this.halfLife.getTime() == 0) {
			// return;
		} else if (stack.getOrCreateTag().getInt(TICKS) != this.halfLife.getTime() * 1200) {
			stack.getOrCreateTag().putInt(TICKS, stack.getOrCreateTag().getInt(TICKS) + 1);
		} else if (stack.getOrCreateTag().getInt(TICKS) == this.halfLife.getTime() * 1200) {
			stack.getOrCreateTag().putInt(TICKS, 0);
			stack.setCount(0);
		}
	}
}
