package com.turtywurty.turtyschemistry.client.screen.book;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class GuideBookDataCap {
	
	private GuideBookDataCap() {}

	@CapabilityInject(IGuideBookData.class)
	public static final Capability<IGuideBookData> INSTANCE = null;

	public static class Storage implements Capability.IStorage<IGuideBookData> {
		public static final String UUID = "uuid";

		@Override
		public INBT writeNBT(Capability<IGuideBookData> capability, IGuideBookData instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putUniqueId(UUID, instance.getPlayerUUID());
			return nbt;
		}

		@Override
		public void readNBT(Capability<IGuideBookData> capability, IGuideBookData instance, Direction side, INBT nbt) {
			if (nbt instanceof CompoundNBT) {
				instance.setPlayerUUID(((CompoundNBT) nbt).getUniqueId(UUID));
			}
		}
	}
}
