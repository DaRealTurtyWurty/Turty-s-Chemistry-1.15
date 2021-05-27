package com.turtywurty.turtyschemistry.common.blocks.agitator;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class AgitatorTypePacket {

	private AgitatorType type;
	private int windowId;

	public AgitatorTypePacket(AgitatorType typeIn, int windowId) {
		this.type = typeIn;
		this.windowId = windowId;
	}

	public void encode(PacketBuffer bufferIn) {
		bufferIn.writeString(this.type.getName());
		bufferIn.writeInt(this.windowId);
	}

	public static AgitatorTypePacket decode(PacketBuffer bufferIn) {
		return new AgitatorTypePacket(AgitatorType.byName(bufferIn.readString()), bufferIn.readInt());
	}

	public void onRecieved(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (ClientUtils.getClientPlayer().openContainer instanceof AgitatorContainer
					&& ClientUtils.getClientPlayer().openContainer.windowId == this.windowId) {
				((AgitatorContainer) ClientUtils.getClientPlayer().openContainer).recieveType(this.type);
			}
		});

		context.get().setPacketHandled(true);
	}
}
