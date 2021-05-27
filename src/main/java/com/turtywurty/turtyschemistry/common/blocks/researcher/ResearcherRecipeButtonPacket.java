package com.turtywurty.turtyschemistry.common.blocks.researcher;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class ResearcherRecipeButtonPacket {

	public final ResourceLocation recipeID;

	public ResearcherRecipeButtonPacket(ResourceLocation recipeID) {
		this.recipeID = recipeID;
	}

	public void write(PacketBuffer packet) {
		packet.writeResourceLocation(this.recipeID);
	}

	public static ResearcherRecipeButtonPacket read(PacketBuffer packet) {
		return new ResearcherRecipeButtonPacket(packet.readResourceLocation());
	}

	public void handle(Supplier<NetworkEvent.Context> contextGetter) {
		contextGetter.get().enqueueWork(() -> this.handleThreadsafe(contextGetter.get()));
		contextGetter.get().setPacketHandled(true);
	}

	public void handleThreadsafe(NetworkEvent.Context context) {
		ServerPlayerEntity player = context.getSender();
		if (player != null) {
			Container container = player.openContainer;
			if (container instanceof ResearcherContainer) {
				//((ResearcherRecipeButtonPacket) container).onPlayerChoseRecipe(this.recipeID);
			}
		}
	}
}
