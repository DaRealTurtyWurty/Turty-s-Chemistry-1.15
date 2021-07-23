package com.turtywurty.turtyschemistry.common.blocks.agitator;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class AgitatorTypePacket {

	public static AgitatorTypePacket decode(final PacketBuffer bufferIn) {
		return new AgitatorTypePacket(AgitatorType.byName(bufferIn.readString()), bufferIn.readInt());
	}

	private final AgitatorType type;

	private final int windowId;

	public AgitatorTypePacket(final AgitatorType typeIn, final int windowId) {
		this.type = typeIn;
		this.windowId = windowId;
	}

	public void encode(final PacketBuffer bufferIn) {
		bufferIn.writeString(this.type.getString());
		bufferIn.writeInt(this.windowId);
	}

	public void onRecieved(final Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (ClientUtils.getClientPlayer().openContainer instanceof AgitatorContainer
					&& ClientUtils.getClientPlayer().openContainer.windowId == this.windowId) {
				((AgitatorContainer) ClientUtils.getClientPlayer().openContainer).recieveType(this.type);
			}
		});

		context.get().setPacketHandled(true);
	}
}
