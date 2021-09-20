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
    public void deserializeNBT(final CompoundNBT nbt) {
        GuideBookDataCap.INSTANCE.getStorage().readNBT(GuideBookDataCap.INSTANCE, this, null, nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> cap, final Direction side) {
        if (cap == GuideBookDataCap.INSTANCE)
            return GuideBookDataCap.INSTANCE.orEmpty(cap, this.holder);
        return LazyOptional.empty();
    }

    @Override
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) GuideBookDataCap.INSTANCE.getStorage().writeNBT(GuideBookDataCap.INSTANCE, this,
                null);
    }

    @Override
    public void setPlayerUUID(final UUID playerUUID) {
        this.playerUUID = playerUUID;
    }
}
