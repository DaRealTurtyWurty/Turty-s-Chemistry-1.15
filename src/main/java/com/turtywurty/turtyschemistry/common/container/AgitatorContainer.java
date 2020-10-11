package com.turtywurty.turtyschemistry.common.container;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.slots.AgitatorSlot;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorTileEntity;
import com.turtywurty.turtyschemistry.common.tileentity.AgitatorType;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;
import com.turtywurty.turtyschemistry.core.packets.AgitatorFluidPacket;
import com.turtywurty.turtyschemistry.core.packets.AgitatorTypePacket;
import com.turtywurty.turtyschemistry.core.util.syncdata.AgitatorSyncData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AgitatorContainer extends Container {

	private final IWorldPosCallable callable;
	public static AgitatorTileEntity tile;
	private FluidStack outputFluid = FluidStack.EMPTY;
	public AgitatorType type;
	public final IIntArray data;

	public AgitatorContainer(int id, final PlayerInventory playerInventory, IItemHandler slots, BlockPos pos,
			IIntArray data, AgitatorType type) {
		super(ContainerTypeInit.AGITATOR.get(), id);
		this.callable = IWorldPosCallable.of(playerInventory.player.getEntityWorld(), pos);
		this.data = data;

		final int slotSizePlus2 = 18;

		// Agitator Inventory
		final int topToBottom = 35;
		final int invStartX = 8;
		final int invStartY = 17;
		for (int row = 0; row < 2; row++) {
			for (int column = 0; column < 3; column++) {
				this.addSlot(new AgitatorSlot(slots, (row * 3) + column, invStartX + (column * slotSizePlus2),
						invStartY + (row * topToBottom)));
			}
		}
		this.addSlot(new AgitatorSlot(slots, 7, 133, 55));

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

		this.trackIntArray(data);
		this.type = type;
	}

	public static IContainerProvider getServerContainerProvider(AgitatorTileEntity te, BlockPos activationPos,
			AgitatorType type) {
		tile = te;
		return (id, playerInventory, serverPlayer) -> new AgitatorContainer(id, playerInventory, te.getInventory(),
				activationPos, new AgitatorSyncData(te), type);
	}

	public static AgitatorContainer getClientContainer(int id, PlayerInventory playerInventory) {
		return new AgitatorContainer(id, playerInventory, new ItemStackHandler(8), BlockPos.ZERO, new IntArray(1),
				AgitatorType.LIQUID_ONLY);
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
		return tile.getRunningTime() != 0 && tile.getMaxRunningTime() != 0
				? tile.getRunningTime() * 24 / tile.getMaxRunningTime()
				: 0;
	}

	public void recieveFluid(FluidStack fluid) {
		this.outputFluid = fluid;
	}

	public FluidStack getOutputFluid() {
		return this.outputFluid;
	}

	public void recieveType(AgitatorType type) {
		this.type = type;
	}

	@SuppressWarnings("resource")
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!tile.getWorld().isRemote) {
			if (!this.outputFluid.isFluidStackIdentical(tile.getFluidHandler().getFluidInTank(5))) {
				this.outputFluid = tile.getFluidHandler().getFluidInTank(5);
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.packetHandler.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new AgitatorFluidPacket(tile.getFluidHandler().getFluidInTank(5), this.windowId));
					}
				}
			}

			if (!this.type.equals(tile.getAgitatorType())) {
				this.type = tile.getAgitatorType();
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.packetHandler.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new AgitatorTypePacket(tile.getAgitatorType(), this.windowId));
					}
				}
			}
		}
	}

	public AgitatorTileEntity getTile() {
		return tile;
	}
}
