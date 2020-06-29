package com.turtywurty.turtyschemistry.common.container;

import java.util.Objects;

import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class AgitatorContainer extends Container {

	private final IWorldPosCallable callable;
	private final AgitatorTileEntity tileEntity;

	public AgitatorContainer(int id, final PlayerInventory playerInventory, AgitatorTileEntity tileEntityIn) {
		super(ContainerTypeInit.AGITATOR.get(), id);
		this.tileEntity = tileEntityIn;
		this.callable = IWorldPosCallable.of(tileEntityIn.getWorld(), tileEntityIn.getPos());
		
		
	}

	public AgitatorContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	public static AgitatorTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "playerInv cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final TileEntity tile = playerInv.player.world.getTileEntity(data.readBlockPos());
		if (tile instanceof AgitatorTileEntity) {
			return (AgitatorTileEntity) tile;
		}

		throw new IllegalStateException("Tile entity is not correct! " + tile);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.AGITATOR.get());
	}
}
