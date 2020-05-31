package com.turtywurty.turtyschemistry.common.network;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class MessageNoSpamChatComponents {

	private ITextComponent[] chatMessages;

	public MessageNoSpamChatComponents(ITextComponent... chatMessages) {
		this.chatMessages = chatMessages;
	}

	public MessageNoSpamChatComponents(PacketBuffer buf) {
		int l = buf.readInt();
		chatMessages = new ITextComponent[l];
		for (int i = 0; i < l; i++)
			chatMessages[i] = ITextComponent.Serializer.fromJson(buf.readString(1000));
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeInt(chatMessages.length);
		for (ITextComponent component : chatMessages)
			buf.writeString(ITextComponent.Serializer.toJson(component));
	}

	public void process(Supplier<Context> context) {
		context.get().enqueueWork(() -> ClientUtils.sendClientNoSpamMessages(chatMessages));
	}
}