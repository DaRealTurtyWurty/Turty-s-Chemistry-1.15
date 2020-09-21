package com.turtywurty.turtyschemistry.common.container;

import java.util.Objects;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.slots.AgitatorSlot;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.packets.AgitatorFluidPacket;
import com.turtywurty.turtyschemistry.core.util.FunctionalIntReferenceHolder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;

public class AgitatorContainer extends Container {

	private final IWorldPosCallable callable;
	public final AgitatorTileEntity tileEntity;
	private FluidStack outputFluid = FluidStack.EMPTY;

	public AgitatorContainer(int id, final PlayerInventory playerInventory, AgitatorTileEntity tileEntityIn) {
		super(ContainerTypeInit.AGITATOR.get(), id);
		this.tileEntity = tileEntityIn;
		this.callable = IWorldPosCallable.of(tileEntityIn.getWorld(), tileEntityIn.getPos());

		final int slotSizePlus2 = 18;

		// Agitator Inventory
		final int topToBottom = 35;
		final int invStartX = 8;
		final int invStartY = 17;
		for (int row = 0; row < 2; row++) {
			for (int column = 0; column < 3; column++) {
				this.addSlot(new AgitatorSlot(this.tileEntity.getInventory(), (row * 3) + column,
						invStartX + (column * slotSizePlus2), invStartY + (row * topToBottom)));
			}
		}
		this.addSlot(new AgitatorSlot(this.tileEntity.getInventory(), 7, 133, 55));

		// Main Inventory
		final int startX = 8;
		final int startY = 84;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * slotSizePlus2),
						startY + (row * slotSizePlus2)));
			}
		}

		// Hotbar
		int hotbarY = 142;
		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
		}

		this.trackInt(new FunctionalIntReferenceHolder(() -> this.tileEntity.getRunningTime(),
				this.tileEntity::setRunningTime));
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

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack returnStack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			returnStack = slotStack.copy();

			final int containerSlots = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(slotStack, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
			if (slotStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (slotStack.getCount() == returnStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, slotStack);
		}
		return returnStack;
	}

	@OnlyIn(Dist.CLIENT)
	public int getScaledProgress() {
		return this.tileEntity.getRunningTime() != 0 && this.tileEntity.getMaxRunningTime() != 0
				? this.tileEntity.getRunningTime() * 24 / this.tileEntity.getMaxRunningTime()
				: 0;
	}

	public void recieveFluid(FluidStack fluid) {
		this.outputFluid = fluid;
	}

	public FluidStack getOutputFluid() {
		return this.outputFluid;
	}

	@SuppressWarnings("resource")
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!this.tileEntity.getWorld().isRemote) {
			if (!this.outputFluid.isFluidStackIdentical(this.tileEntity.getFluidHandler().getFluidInTank(5))) {
				this.outputFluid = this.tileEntity.getFluidHandler().getFluidInTank(5);
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.packetHandler.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new AgitatorFluidPacket(this.tileEntity.getFluidHandler().getFluidInTank(5),
										this.windowId));
					}
				}
			}
		}
	}
}
