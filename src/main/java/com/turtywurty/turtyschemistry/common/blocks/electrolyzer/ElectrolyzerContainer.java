package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElectrolyzerContainer extends Container {

	private static ElectrolyzerTileEntity tileEntity;

	public static ElectrolyzerContainer getClientContainer(final int id, final PlayerInventory playerInv) {
		return new ElectrolyzerContainer(id, playerInv, new ItemStackHandler(3), BlockPos.ZERO, new IntArray(5));
	}

	public static IContainerProvider getServerContainerProvider(final ElectrolyzerTileEntity te,
			final BlockPos activationPos) {
		tileEntity = te;
		return (id, playerInv, serverPlayer) -> new ElectrolyzerContainer(id, playerInv, te.getInventory(),
				activationPos, new ElectrolyzerSyncData(te));
	}

	private final IWorldPosCallable callable;

	public final IIntArray data;
	protected FluidStack inputFluid = FluidStack.EMPTY, outputFluid1 = FluidStack.EMPTY,
			outputFluid2 = FluidStack.EMPTY;
	private int windowId;

	public ElectrolyzerContainer(final int id, final PlayerInventory playerInventory, final IItemHandler slots,
			final BlockPos pos, final IIntArray data) {
		super(ContainerTypeInit.ELECTROLYZER.get(), id);
		this.callable = IWorldPosCallable.of(playerInventory.player.getEntityWorld(), pos);
		this.data = data;
		this.windowId = id;

		int slotSizePlus2 = 18;

		// Electrolyzer Inventory
		addSlot(new SlotItemHandler(slots, 0, 27, 55) {
			@Override
			public boolean isItemValid(final ItemStack stack) {
				return stack.getItem() instanceof BucketItem;
			}
		});
		addSlot(new ElectrolyzerOutputSlot(slots, 1, 79, 55));
		addSlot(new ElectrolyzerOutputSlot(slots, 2, 133, 55));

		// Main Inventory
		int startX = 8;
		int startY = 84;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				addSlot(new Slot(playerInventory, 9 + row * 9 + column, startX + column * slotSizePlus2,
						startY + row * slotSizePlus2));
			}
		}

		// Hotbar
		int hotbarY = 142;
		for (int column = 0; column < 9; ++column) {
			addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
		}

		trackIntArray(data);
	}

	@Override
	public boolean canInteractWith(final PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.ELECTROLYZER.get());
	}

	@SuppressWarnings("resource")
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (!tileEntity.getWorld().isRemote) {
			if (!this.inputFluid.isFluidStackIdentical(tileEntity.fluidInventory.getFluidInTank(0))) {
				this.inputFluid = tileEntity.fluidInventory.getFluidInTank(0).copy();
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.PACKET_HANDLER.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new ElectrolyzerFluidPacket(tileEntity.fluidInventory.getFluidInTank(0), this.windowId,
										0));
					}
				}
			}

			if (!this.outputFluid1.isFluidStackIdentical(tileEntity.fluidInventory.getFluidInTank(1))) {
				this.outputFluid1 = tileEntity.fluidInventory.getFluidInTank(1).copy();
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.PACKET_HANDLER.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new ElectrolyzerFluidPacket(tileEntity.fluidInventory.getFluidInTank(1), this.windowId,
										1));
					}
				}
			}

			if (!this.outputFluid2.isFluidStackIdentical(tileEntity.fluidInventory.getFluidInTank(2))) {
				this.outputFluid2 = tileEntity.fluidInventory.getFluidInTank(2).copy();
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.PACKET_HANDLER.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new ElectrolyzerFluidPacket(tileEntity.fluidInventory.getFluidInTank(2), this.windowId,
										2));
					}
				}
			}
		}
	}

	public int getScaledProgress() {
		return this.data.get(0) != 0 && this.data.get(1) != 0 ? this.data.get(0) * 24 / this.data.get(1) : 0;
	}

	@Override
	public ItemStack transferStackInSlot(final PlayerEntity playerIn, final int index) {
		ItemStack returnStack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			returnStack = slotStack.copy();

			final int containerSlots = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
			if (index < containerSlots) {
				if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(slotStack, 0, containerSlots, false))
				return ItemStack.EMPTY;
			if (slotStack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (slotStack.getCount() == returnStack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, slotStack);
		}
		return returnStack;
	}
}
