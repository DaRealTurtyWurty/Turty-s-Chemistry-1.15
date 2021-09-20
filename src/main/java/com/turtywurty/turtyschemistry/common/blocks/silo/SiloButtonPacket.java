package com.turtywurty.turtyschemistry.common.blocks.silo;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SiloButtonPacket {

    private final int option;

    public SiloButtonPacket(final int optionIn) {
        this.option = optionIn;
    }

    public static SiloButtonPacket decode(final PacketBuffer buffer) {
        return new SiloButtonPacket(buffer.readInt());
    }

    public void encode(final PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.option);
    }

    public void onRecieved(final Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (context.get().getSender().openContainer instanceof SiloContainer) {
                final SiloContainer container = (SiloContainer) context.get().getSender().openContainer;
                if (this.option == 0) {
                    final int page = container.currentPage.get(0) - 1;
                    final ServerPlayerEntity serverPlayer = context.get().getSender();
                    final IContainerProvider provider = SiloContainer.getServerContainerProvider(
                            container.getTile(), container.getTile().getPos(), page);
                    final INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider,
                            container.getTile().getDisplayName());

                    serverPlayer.openContainer(namedProvider);
                    if (serverPlayer.openContainer instanceof SiloContainer) {
                        container.currentPage.set(0, page);
                    }
                    container.detectAndSendChanges();
                } else if (this.option == 1) {
                    final int page = container.currentPage.get(0) + 1;
                    final ServerPlayerEntity serverPlayer = context.get().getSender();
                    final IContainerProvider provider = SiloContainer.getServerContainerProvider(
                            container.getTile(), container.getTile().getPos(), page);
                    final INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider,
                            container.getTile().getDisplayName());

                    serverPlayer.openContainer(namedProvider);
                    if (serverPlayer.openContainer instanceof SiloContainer) {
                        ((SiloContainer) serverPlayer.openContainer).currentPage.set(0, page);
                        container.detectAndSendChanges();
                    }
                }
            }

        });
        context.get().setPacketHandled(true);
    }
}
