package com.turtywurty.turtyschemistry.common.blocks.agitator;

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
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AgitatorContainer extends Container {

	public static AgitatorTileEntity tile;

	public static AgitatorContainer getClientContainer(final int id, final PlayerInventory playerInventory) {
		return new AgitatorContainer(id, playerInventory, new ItemStackHandler(8), BlockPos.ZERO, new IntArray(1),
				AgitatorType.LIQUID_ONLY);
	}

	public static IContainerProvider getServerContainerProvider(final AgitatorTileEntity te,
			final BlockPos activationPos, final AgitatorType type) {
		tile = te;
		return (id, playerInventory, serverPlayer) -> new AgitatorContainer(id, playerInventory, te.getInventory(),
				activationPos, new AgitatorSyncData(te), type);
	}

	private final IWorldPosCallable callable;
	private FluidStack outputFluid = FluidStack.EMPTY;

	public AgitatorType type;

	public final IIntArray data;

	public AgitatorContainer(final int id, final PlayerInventory playerInventory, final IItemHandler slots,
			final BlockPos pos, final IIntArray data, final AgitatorType type) {
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
				addSlot(new AgitatorSlot(slots, row * 3 + column, invStartX + column * slotSizePlus2,
						invStartY + row * topToBottom));
			}
		}
		addSlot(new AgitatorSlot(slots, 7, 133, 55));

		// Main Inventory
		final int startX = 8;
		final int startY = 84;
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
		this.type = type;
	}

	@Override
	public boolean canInteractWith(final PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.AGITATOR.get());
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
						TurtyChemistry.PACKET_HANDLER.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new AgitatorFluidPacket(tile.getFluidHandler().getFluidInTank(5), this.windowId));
					}
				}
			}

			if (!this.type.equals(tile.getAgitatorType())) {
				this.type = tile.getAgitatorType();
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.PACKET_HANDLER.send(
								PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new AgitatorTypePacket(tile.getAgitatorType(), this.windowId));
					}
				}
			}
		}
	}

	public FluidStack getOutputFluid() {
		return this.outputFluid;
	}

	public int getScaledProgress() {
		return tile.getRunningTime() != 0 && AgitatorTileEntity.getMaxRunningTime() != 0
				? tile.getRunningTime() * 24 / AgitatorTileEntity.getMaxRunningTime()
				: 0;
	}

	public AgitatorTileEntity getTile() {
		return tile;
	}

	public void recieveFluid(final FluidStack fluid) {
		this.outputFluid = fluid;
	}

	public void recieveType(final AgitatorType type) {
		this.type = type;
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
