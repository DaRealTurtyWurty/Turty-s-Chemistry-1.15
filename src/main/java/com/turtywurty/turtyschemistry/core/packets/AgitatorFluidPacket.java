package com.turtywurty.turtyschemistry.core.packets;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.container.AgitatorContainer;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class AgitatorFluidPacket {

	public final int windowId;
	public final FluidStack fluidContents;

	public AgitatorFluidPacket(FluidStack fluid, int windowId) {
		this.windowId = windowId;
		this.fluidContents = fluid;
	}

	public void encode(PacketBuffer packetBuffer) {
		packetBuffer.writeFluidStack(this.fluidContents);
		packetBuffer.writeInt(this.windowId);
	}

	public static AgitatorFluidPacket decode(PacketBuffer buffer) {
		return new AgitatorFluidPacket(buffer.readFluidStack(), buffer.readInt());
	}

	public void onRecieved(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (ClientUtils.getClientPlayer().openContainer.windowId == this.windowId) {
				((AgitatorContainer) ClientUtils.getClientPlayer().openContainer).recieveFluid(this.fluidContents);
			}
		});
		context.get().setPacketHandled(true);
	}
}
