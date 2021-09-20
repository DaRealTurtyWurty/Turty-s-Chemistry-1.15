package com.turtywurty.turtyschemistry.common.blocks.agitator;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class AgitatorFluidPacket {

    public final int windowId;
    public final FluidStack fluidContents;

    public AgitatorFluidPacket(final FluidStack fluid, final int windowId) {
        this.windowId = windowId;
        this.fluidContents = fluid;
    }

    public static AgitatorFluidPacket decode(final PacketBuffer buffer) {
        return new AgitatorFluidPacket(buffer.readFluidStack(), buffer.readInt());
    }

    public void encode(final PacketBuffer packetBuffer) {
        packetBuffer.writeFluidStack(this.fluidContents);
        packetBuffer.writeInt(this.windowId);
    }

    public void onRecieved(final Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (ClientUtils.getClientPlayer().openContainer.windowId == this.windowId) {
                ((AgitatorContainer) ClientUtils.getClientPlayer().openContainer)
                        .recieveFluid(this.fluidContents);
            }
        });
        context.get().setPacketHandled(true);
    }
}
