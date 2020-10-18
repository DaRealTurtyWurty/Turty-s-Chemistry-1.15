package com.turtywurty.turtyschemistry.client.screen.book;

import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class GuideBookData implements IGuideBookData, ICapabilityProvider, INBTSerializable<CompoundNBT> {
	private final LazyOptional<IGuideBookData> holder = LazyOptional.of(() -> this);
	
	private UUID playerUUID = UUID.randomUUID();
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == GuideBookDataCap.INSTANCE) {
			return GuideBookDataCap.INSTANCE.orEmpty(cap, this.holder);
		} else {
			return LazyOptional.empty();
		}
	}
	
	@Override
	public UUID getPlayerUUID() {
		return this.playerUUID;
	}
	
	@Override
	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT)GuideBookDataCap.INSTANCE.getStorage().writeNBT(GuideBookDataCap.INSTANCE, this, null);
	}
	
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		GuideBookDataCap.INSTANCE.getStorage().readNBT(GuideBookDataCap.INSTANCE, this, null, nbt);
	}	
}
