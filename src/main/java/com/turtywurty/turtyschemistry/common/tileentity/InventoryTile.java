package com.turtywurty.turtyschemistry.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryTile extends TileEntity implements ITickableTileEntity {

	public final int size;
	public int timer;
	public boolean requiresUpdate = true;

	public final IItemHandlerModifiable inventory;
	protected LazyOptional<IItemHandlerModifiable> handler;

	public InventoryTile(TileEntityType<?> tileEntityTypeIn, int size) {
		super(tileEntityTypeIn);
		this.size = size;
		inventory = this.createHandler();
		handler = LazyOptional.of(() -> inventory);
	}

	@Override
	public void tick() {
		this.timer++;
		if (this.world != null && this.requiresUpdate) {
			updateTile();
			this.requiresUpdate = false;
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return this.handler.cast();
		}
		return super.getCapability(cap, side);
	}

	public LazyOptional<IItemHandlerModifiable> getHandler() {
		return this.handler;
	}

	public IItemHandlerModifiable getInventory() {
		return this.inventory;
	}

	public IItemHandlerModifiable createHandler() {
		return new ItemStackHandler(this.size) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), BlockFlags.BLOCK_UPDATE);
				return super.insertItem(slot, stack, simulate);
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), BlockFlags.BLOCK_UPDATE);
				return super.extractItem(slot, amount, simulate);
			}
		};
	}

	public ItemStack getItemInSlot(int slot) {
		return this.handler.map(inv -> inv.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
	}

	public ItemStack insertItem(int slot, ItemStack stack) {
		ItemStack itemIn = stack.copy();
		stack.shrink(itemIn.getCount());
		this.requiresUpdate = true;
		return this.handler.map(inv -> inv.insertItem(slot, itemIn, false)).orElse(ItemStack.EMPTY);
	}

	public ItemStack extractItem(int slot) {
		int count = getItemInSlot(slot).getCount();
		this.requiresUpdate = true;
		return this.handler.map(inv -> inv.extractItem(slot, count, false)).orElse(ItemStack.EMPTY);
	}

	public int getSize() {
		return this.size;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		ListNBT list = compound.getList("Items", 10);
		for (int x = 0; x < list.size(); ++x) {
			CompoundNBT nbt = list.getCompound(x);
			int r = nbt.getByte("Slot") & 255;
			this.handler.ifPresent(inv -> {
				int invslots = inv.getSlots();
				if (r >= 0 && r < invslots) {
					inv.setStackInSlot(r, ItemStack.read(nbt));
				}
			});
		}
		this.requiresUpdate = true;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ListNBT list = new ListNBT();
		int slots = inventory.getSlots();
		for (int x = 0; x < slots; ++x) {
			ItemStack stack = inventory.getStackInSlot(x);
			CompoundNBT nbt = new CompoundNBT();
			nbt.putByte("Slot", (byte) x);
			stack.write(nbt);
			list.add(nbt);
		}

		compound.put("Items", list);
		return compound;
	}

	public void updateTile() {
		this.requestModelDataUpdate();
		this.markDirty();
		if (this.getWorld() != null) {
			this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	@Nonnull
	public CompoundNBT getUpdateTag() {
		return this.serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.deserializeNBT(tag);
	}
}
