package com.turtywurty.turtyschemistry.common.blocks.researcher;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class ResearcherRecipeButtonPacket {

    public final ResourceLocation recipeID;

    public ResearcherRecipeButtonPacket(final ResourceLocation recipeID) {
        this.recipeID = recipeID;
    }

    public static ResearcherRecipeButtonPacket read(final PacketBuffer packet) {
        return new ResearcherRecipeButtonPacket(packet.readResourceLocation());
    }

    public void handle(final Supplier<NetworkEvent.Context> contextGetter) {
        contextGetter.get().enqueueWork(() -> handleThreadsafe(contextGetter.get()));
        contextGetter.get().setPacketHandled(true);
    }

    public void handleThreadsafe(final NetworkEvent.Context context) {
        final ServerPlayerEntity player = context.getSender();
        if (player != null) {
            final Container container = player.openContainer;
            if (container instanceof ResearcherContainer) {
                // ((ResearcherRecipeButtonPacket)
                // container).onPlayerChoseRecipe(this.recipeID);
            }
        }
    }

    public void write(final PacketBuffer packet) {
        packet.writeResourceLocation(this.recipeID);
    }
}
