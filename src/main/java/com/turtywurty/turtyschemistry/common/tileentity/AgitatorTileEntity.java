package com.turtywurty.turtyschemistry.common.tileentity;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;

public class AgitatorTileEntity extends TileEntity implements IClearable, ITickableTileEntity {

	private static final String PROCESSING_TIME_LEFT_TAG = "processingTimeLeft";
	private static final String MAX_PROCESSING_TIME_TAG = "maxProcessingTime";

	private final NonNullList<ItemStack> inventory = NonNullList.withSize(7, ItemStack.EMPTY);

	public short processTimeLeft = -1;
	public short maxProcessingTime = -1;
	public boolean lastProcessing = false;

	public AgitatorTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public AgitatorTileEntity() {
		this(TileEntityTypeInit.AGITATOR.get());
	}

	@Override
	public void tick() {
		if (isValidRecipe(this.getInventory())) {

		}
	}

	private boolean isValidRecipe(NonNullList<ItemStack> inputs) {
		return false;
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}

	public boolean addItem(ItemStack itemStackIn) {
		for (int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemstack = this.inventory.get(i);
			if (itemstack.isEmpty()) {
				this.inventory.set(i, itemStackIn.split(1));
				this.inventoryChanged();
				return true;
			}
		}
		return false;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.inventory.clear();
		ItemStackHelper.loadAllItems(compound, this.inventory);
		this.processTimeLeft = compound.getShort(PROCESSING_TIME_LEFT_TAG);
		this.maxProcessingTime = compound.getShort(MAX_PROCESSING_TIME_TAG);
	}

	public CompoundNBT write(CompoundNBT compound) {
		this.writeItems(compound);
		compound.putShort(PROCESSING_TIME_LEFT_TAG, this.processTimeLeft);
		compound.putShort(MAX_PROCESSING_TIME_TAG, this.maxProcessingTime);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.inventory, true);
		return compound;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 13, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.writeItems(new CompoundNBT());
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	private void inventoryChanged() {
		this.markDirty();
		this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	public void dropAllItems() {
		if (!this.world.isRemote) {
			InventoryHelper.dropItems(this.getWorld(), this.getPos(), this.getInventory());
		}

		this.inventoryChanged();
	}
}
