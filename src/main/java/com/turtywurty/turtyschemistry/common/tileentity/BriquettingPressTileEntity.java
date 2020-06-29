package com.turtywurty.turtyschemistry.common.tileentity;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.BriquettingPressBlock;
import com.turtywurty.turtyschemistry.common.container.BriquettingPressContainer;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BriquettingPressTileEntity extends LockableLootTileEntity
		implements ITickableTileEntity, INamedContainerProvider {

	private NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
	public boolean buttonPressed = false;
	private int currentRunningTime, maxRunningTime;
	private float briquetteSize;
	public float turnerRotation;
	private int test = 0;

	public BriquettingPressTileEntity(TileEntityType<?> typeIn) {
		super(typeIn);
	}

	public BriquettingPressTileEntity() {
		super(TileEntityTypeInit.BRIQUETTING_PRESS.get());
	}

	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public void tick() {
		boolean dirty = false;
		if (!this.isEmpty()) {
			int time = 0;
			test = 0;
			for (ItemStack stack : this.items) {
				if (!stack.isEmpty()) {
					time += stack.getCount();
				} else {
					time = 0;
				}
			}
			this.maxRunningTime = time * 30;
			dirty = true;
		} else {
			if (test < 1) {
				this.maxRunningTime = 0;
				dirty = true;
				test++;
			}
		}

		this.moveItems();
		if (!this.isEmpty()) {
			if (this.buttonPressed) {
				this.currentRunningTime++;
				if (this.currentRunningTime % 60 == 0) {
					this.currentRunningTime -= 60;
					this.items.get(3).shrink(2);
					dirty = true;

					Direction side = Direction.fromAngle(
							this.getBlockState().get(BriquettingPressBlock.HORIZONTAL_FACING).getHorizontalAngle()
									+ 90);

					IInventory inv = TileEntityUtils.getValidContainer(this.world, side, this.pos, this.getBlockState(),
							new ItemStack(ItemInit.SAWDUST.get()));
					if (inv != null) {
						TileEntityUtils.insertItem(inv, new ItemStack(ItemInit.BRIQUETTE.get()));
						dirty = true;
					} else {
						ItemEntity item = new ItemEntity(this.world, this.pos.offset(side).getX(),
								this.pos.add(0, 0.5D, 0).offset(side).getY(), this.pos.offset(side).getZ(),
								new ItemStack(ItemInit.BRIQUETTE.get()));
						this.world.addEntity(item);
					}
				}

				if (this.currentRunningTime >= this.maxRunningTime) {
					this.maxRunningTime = 0;
					this.currentRunningTime = 0;
					this.setPressed(false);
					dirty = true;
				}
			}
		}

		if (dirty) {
			this.markDirty();
		}
	}

	public int getCurrentRunningTime() {
		return this.currentRunningTime;
	}

	public int getMaxRunningTime() {
		return this.maxRunningTime;
	}

	public void setPressed(boolean pressed) {
		this.buttonPressed = pressed;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private boolean moveItems() {
		for (int i = 0; i < this.items.size(); i++) {
			if ((!this.items.get(i).isEmpty() && i + 1 != this.items.size()) && this.items.get(i + 1).isEmpty()) {
				this.items.set(i + 1, this.items.get(i));
				this.items.set(i, ItemStack.EMPTY);
				this.markDirty();
			} else if ((!this.items.get(i).isEmpty() && i + 1 != this.items.size())
					&& !this.items.get(i + 1).isEmpty()) {
				if (this.items.get(i + 1).isItemEqual(this.items.get(i))
						&& this.items.get(i + 1).getCount() != this.getInventoryStackLimit()) {
					int availableSpace = this.getInventoryStackLimit() - this.items.get(i + 1).getCount();
					int neededSpace = this.items.get(i).getCount();
					if (availableSpace >= neededSpace) {
						this.items.set(i, ItemStack.EMPTY);
						this.items.get(i + 1).grow(neededSpace);
					} else {
						int newAmount = neededSpace - availableSpace;
						this.items.get(i).setCount(newAmount);
						this.items.get(i + 1).setCount(this.getInventoryStackLimit());
					}
				}
				this.markDirty();
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.markDirty();
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public void setItems(NonNullList<ItemStack> itemsIn) {
		this.items = itemsIn;
	}

	@Override
	public ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".briquetting_press");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new BriquettingPressContainer(id, player, this);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.buttonPressed = compound.getBoolean("ButtonPressed");
		this.currentRunningTime = compound.getInt("RunningTime");
		this.maxRunningTime = compound.getInt("MaxRunningTime");
		this.briquetteSize = compound.getInt("BriquetteSize");
		this.turnerRotation = compound.getFloat("TurnerRotation");
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putBoolean("ButtonPressed", this.buttonPressed);
		compound.putInt("RunningTime", this.currentRunningTime);
		compound.putInt("MaxRunningTime", this.maxRunningTime);
		compound.putFloat("BriquetteSize", this.briquetteSize);
		compound.putFloat("TurnerRotation", this.turnerRotation);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);

		return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(packet.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}
}
