package com.turtywurty.turtyschemistry.common.blocks.gas_extractor;

import java.util.Objects;

import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class GasExtractorContainer extends Container {

	public final GasExtractorTileEntity tileEntity;
	private final IWorldPosCallable canInteractWithCallable;

	public GasExtractorContainer(final int windowId, final PlayerInventory playerInventory,
			final GasExtractorTileEntity tileEntity) {
		super(ContainerTypeInit.GAS_EXTRACTOR.get(), windowId);
		this.tileEntity = tileEntity;
		this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

		final int playerInventoryStartX = 8;
		final int playerInventoryStartY = 84;
		final int slotSizePlus2 = 18;

		// Main Inventory
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column,
						playerInventoryStartX + (column * slotSizePlus2),
						playerInventoryStartY + (row * slotSizePlus2)));
			}
		}

		// Hotbar
		final int playerHotbarY = playerInventoryStartY + slotSizePlus2 * 3 + 4;
		for (int column = 0; column < 9; ++column) {
			this.addSlot(
					new Slot(playerInventory, column, playerInventoryStartX + (column * slotSizePlus2), playerHotbarY));
		}
	}

	private static GasExtractorTileEntity getTileEntity(final PlayerInventory playerInventory,
			final PacketBuffer data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
		Objects.requireNonNull(data, "data cannot be null!");
		final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
		if (tileAtPos instanceof GasExtractorTileEntity)
			return (GasExtractorTileEntity) tileAtPos;
		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	public GasExtractorContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(canInteractWithCallable, playerIn, BlockInit.GAS_EXTRACTOR.get());
	}
}
