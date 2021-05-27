package com.turtywurty.turtyschemistry.common.blocks.boiler;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class BoilerFluidPacket {

	public final int windowId, index;
	public final FluidStack fluidContents;

	public BoilerFluidPacket(FluidStack fluid, int windowId, int index) {
		this.windowId = windowId;
		this.fluidContents = fluid;
		this.index = index;
	}

	public void encode(PacketBuffer packetBuffer) {
		packetBuffer.writeFluidStack(this.fluidContents);
		packetBuffer.writeInt(this.windowId);
		packetBuffer.writeInt(this.index);
	}

	public static BoilerFluidPacket decode(PacketBuffer buffer) {
		return new BoilerFluidPacket(buffer.readFluidStack(), buffer.readInt(), buffer.readInt());
	}

	public void onRecieved(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (ClientUtils.getClientPlayer().openContainer.windowId == this.windowId) {
				if (this.index == 0) {
					((BoilerContainer) ClientUtils.getClientPlayer().openContainer)
							.recieveInputFluid(this.fluidContents);
				} else if (this.index == 1) {
					((BoilerContainer) ClientUtils.getClientPlayer().openContainer)
							.recieveOutputFluid(this.fluidContents);
				}
			}
		});
		context.get().setPacketHandled(true);
	}
}
