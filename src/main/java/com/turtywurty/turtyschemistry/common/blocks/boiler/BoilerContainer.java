package com.turtywurty.turtyschemistry.common.blocks.boiler;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.GasBlock;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
import net.minecraftforge.items.SlotItemHandler;

public class BoilerContainer extends Container {

	private final IWorldPosCallable callable;
	public static BoilerTileEntity tile;
	private Block outputGas = Blocks.AIR;
	private FluidStack inputFluid = FluidStack.EMPTY, outputFluid = FluidStack.EMPTY;
	public final IIntArray data;

	public BoilerContainer(int id, final PlayerInventory playerInventory, IItemHandler slots, BlockPos pos, IIntArray data) {
		super(ContainerTypeInit.BOILER.get(), id);
		this.callable = IWorldPosCallable.of(playerInventory.player.getEntityWorld(), pos);
		this.data = data;

		final int slotSizePlus2 = 18;
		// Boiler Inventory
		this.addSlot(new SlotItemHandler(slots, 0, 8, 56));
		this.addSlot(new SlotItemHandler(slots, 1, 98, 56));
		this.addSlot(new SlotItemHandler(slots, 2, 134, 56));

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
	}

	public static IContainerProvider getServerContainerProvider(BoilerTileEntity te, BlockPos activationPos) {
		tile = te;
		return (id, playerInventory, serverPlayer) -> new BoilerContainer(id, playerInventory, te.getInventory(),
				activationPos, new BoilerSyncData(te));
	}

	public static BoilerContainer getClientContainer(int id, PlayerInventory playerInventory) {
		return new BoilerContainer(id, playerInventory, new ItemStackHandler(3), BlockPos.ZERO, new IntArray(3));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(this.callable, playerIn, BlockInit.BOILER.get());
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

	public int getScaledProgress() {
		return this.data.get(0) != 0 && this.data.get(1) != 0 ? this.data.get(0) * 24 / this.data.get(1) : 0;
	}

	public void recieveGas(GasBlock gas) {
		this.outputGas = gas;
	}

	public Block getOutputGas() {
		return this.outputGas;
	}

	public BoilerTileEntity getTile() {
		return tile;
	}

	public void recieveInputFluid(FluidStack fluid) {
		this.inputFluid = fluid;
	}

	public FluidStack getInputFluid() {
		return this.inputFluid;
	}

	public void recieveOutputFluid(FluidStack fluid) {
		this.outputFluid = fluid;
	}

	public FluidStack getOutputFluid() {
		return this.outputFluid;
	}

	@SuppressWarnings("resource")
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (!tile.getWorld().isRemote) {
			if (!this.inputFluid.isFluidStackIdentical(tile.getFluidHandler().getFluidInTank(0))) {
				this.inputFluid = tile.getFluidHandler().getFluidInTank(0);
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new BoilerFluidPacket(tile.getFluidHandler().getFluidInTank(0), this.windowId, 0));
					}
				}
			}

			if (!this.outputFluid.isFluidStackIdentical(tile.getFluidHandler().getFluidInTank(1))) {
				this.outputFluid = tile.getFluidHandler().getFluidInTank(1);
				for (IContainerListener listener : this.listeners) {
					if (listener instanceof ServerPlayerEntity) {
						TurtyChemistry.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) listener),
								new BoilerFluidPacket(tile.getFluidHandler().getFluidInTank(1), this.windowId, 1));
					}
				}
			}
		}
	}
}
