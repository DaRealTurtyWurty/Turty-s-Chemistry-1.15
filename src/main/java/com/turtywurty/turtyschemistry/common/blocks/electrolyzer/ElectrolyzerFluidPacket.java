package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class ElectrolyzerFluidPacket {

	public static ElectrolyzerFluidPacket decode(final PacketBuffer buffer) {
		return new ElectrolyzerFluidPacket(buffer.readFluidStack(), buffer.readInt(), buffer.readInt());
	}

	public final int windowId, index;

	public final FluidStack fluidContents;

	public ElectrolyzerFluidPacket(final FluidStack fluid, final int windowId, final int index) {
		this.windowId = windowId;
		this.fluidContents = fluid;
		this.index = index;
	}

	public void encode(final PacketBuffer packetBuffer) {
		packetBuffer.writeFluidStack(this.fluidContents);
		packetBuffer.writeInt(this.windowId);
		packetBuffer.writeInt(this.index);
	}

	public void onRecieved(final Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (ClientUtils.getClientPlayer().openContainer.windowId == this.windowId) {
				ElectrolyzerContainer container = (ElectrolyzerContainer) ClientUtils.getClientPlayer().openContainer;
				switch (this.index) {
				case 0:
					container.inputFluid = this.fluidContents.copy();
					break;
				case 1:
					container.outputFluid1 = this.fluidContents.copy();
					break;
				case 2:
					container.outputFluid2 = this.fluidContents.copy();
					break;
				default:
					break;
				}
			}
		});
		context.get().setPacketHandled(true);
	}
}
