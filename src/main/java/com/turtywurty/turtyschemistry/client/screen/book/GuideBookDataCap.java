package com.turtywurty.turtyschemistry.client.screen.book;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class GuideBookDataCap {

    @CapabilityInject(IGuideBookData.class)
    public static final Capability<IGuideBookData> INSTANCE = null;

    private GuideBookDataCap() {
    }

    public static class Storage implements Capability.IStorage<IGuideBookData> {
        public static final String UUID = "uuid";

        @Override
        public void readNBT(final Capability<IGuideBookData> capability, final IGuideBookData instance,
                final Direction side, final INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                // instance.setPlayerUUID(((CompoundNBT) nbt).getUniqueId(UUID));
            }
        }

        @Override
        public INBT writeNBT(final Capability<IGuideBookData> capability, final IGuideBookData instance,
                final Direction side) {

            // nbt.putUniqueId(UUID, instance.getPlayerUUID());
            return new CompoundNBT();
        }
    }
}
