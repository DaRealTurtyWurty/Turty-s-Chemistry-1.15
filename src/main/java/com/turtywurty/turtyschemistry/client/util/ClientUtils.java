package com.turtywurty.turtyschemistry.client.util;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.network.MessageNoSpamChatComponents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {

	public static final Minecraft MC = Minecraft.getInstance();
	private static final int DELETION_ID = 3718126;
	private static int lastAdded;

	public static ClientPlayerEntity getClientPlayer() {
		return MC.player;
	}
	
	public static ClientWorld getClientWorld() {
		return MC.world;
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendClientNoSpamMessages(ITextComponent[] messages) {
		NewChatGui chat = MC.ingameGUI.getChatGUI();
		for (int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++)
			chat.deleteChatLine(i);
		for (int i = 0; i < messages.length; i++)
			chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
		lastAdded = DELETION_ID + messages.length - 1;
	}

	public static void sendServerNoSpamMessages(PlayerEntity player, ITextComponent... messages) {
		if (messages.length > 0 && player instanceof ServerPlayerEntity)
			TurtyChemistry.packetHandler.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
					new MessageNoSpamChatComponents(messages));
	}
}
