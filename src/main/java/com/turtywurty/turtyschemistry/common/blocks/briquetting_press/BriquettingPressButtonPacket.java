package com.turtywurty.turtyschemistry.common.blocks.briquetting_press;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

public class BriquettingPressButtonPacket {

	private boolean buttonPressed;

	public BriquettingPressButtonPacket(boolean buttonPressedIn) {
		this.buttonPressed = buttonPressedIn;
	}

	public void encode(PacketBuffer packetBuffer) {
		packetBuffer.writeByte(buttonPressed ? 1 : 0);
	}

	public static BriquettingPressButtonPacket decode(PacketBuffer buffer) {
		return new BriquettingPressButtonPacket(buffer.readByte() > 0);
	}

	public void onRecieved(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (context.get().getSender().openContainer instanceof BriquettingPressContainer) {
				BriquettingPressContainer container = (BriquettingPressContainer) context.get()
						.getSender().openContainer;
				container.tileEntity.setPressed(this.buttonPressed);
				container.tileEntity.getWorld().notifyBlockUpdate(container.tileEntity.getPos(),
						container.tileEntity.getBlockState(), container.tileEntity.getBlockState(),
						Constants.BlockFlags.DEFAULT);
			}

		});
		context.get().setPacketHandled(true);
	}
}
