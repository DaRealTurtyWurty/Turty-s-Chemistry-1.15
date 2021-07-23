package com.turtywurty.turtyschemistry.common.blocks.briquetting_press;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;
import com.turtywurty.turtyschemistry.common.tileentity.TileEntityUtils;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
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

	public BriquettingPressTileEntity() {
		super(TileEntityTypeInit.BRIQUETTING_PRESS.get());
	}

	public BriquettingPressTileEntity(final TileEntityType<?> typeIn) {
		super(typeIn);
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	protected Container createMenu(final int id, final PlayerInventory player) {
		return new BriquettingPressContainer(id, player, this);
	}

	@Override
	public ItemStack decrStackSize(final int index, final int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	public int getCurrentRunningTime() {
		return this.currentRunningTime;
	}

	@Override
	public ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".briquetting_press");
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	public int getMaxRunningTime() {
		return this.maxRunningTime;
	}

	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public ItemStack getStackInSlot(final int index) {
		return this.items.get(index);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);

		return new SUpdateTileEntityPacket(getPos(), 1, nbt);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
		read(state, tag);
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public boolean isUsableByPlayer(final PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this)
			return false;
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
	}

	private boolean moveItems() {
		for (int i = 0; i < this.items.size(); i++) {
			if (!this.items.get(i).isEmpty() && i + 1 != this.items.size() && this.items.get(i + 1).isEmpty()) {
				this.items.set(i + 1, this.items.get(i));
				this.items.set(i, ItemStack.EMPTY);
				markDirty();
			} else if (!this.items.get(i).isEmpty() && i + 1 != this.items.size() && !this.items.get(i + 1).isEmpty()) {
				if (this.items.get(i + 1).isItemEqual(this.items.get(i))
						&& this.items.get(i + 1).getCount() != getInventoryStackLimit()) {
					int availableSpace = getInventoryStackLimit() - this.items.get(i + 1).getCount();
					int neededSpace = this.items.get(i).getCount();
					if (availableSpace >= neededSpace) {
						this.items.set(i, ItemStack.EMPTY);
						this.items.get(i + 1).grow(neededSpace);
					} else {
						int newAmount = neededSpace - availableSpace;
						this.items.get(i).setCount(newAmount);
						this.items.get(i + 1).setCount(getInventoryStackLimit());
					}
				}
				markDirty();
			}
		}
		return true;
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
		read(this.world.getBlockState(packet.getPos()), packet.getNbtCompound());
	}

	@Override
	public void read(final BlockState state, final CompoundNBT compound) {
		super.read(state, compound);
		this.buttonPressed = compound.getBoolean("ButtonPressed");
		this.currentRunningTime = compound.getInt("RunningTime");
		this.maxRunningTime = compound.getInt("MaxRunningTime");
		this.briquetteSize = compound.getInt("BriquetteSize");
		this.turnerRotation = compound.getFloat("TurnerRotation");
		this.items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);
	}

	@Override
	public ItemStack removeStackFromSlot(final int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(final int index, final ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			markDirty();
		}
	}

	@Override
	public void setItems(final NonNullList<ItemStack> itemsIn) {
		this.items = itemsIn;
	}

	public void setPressed(final boolean pressed) {
		this.buttonPressed = pressed;
	}

	@Override
	public void tick() {
		boolean dirty = false;
		if (!isEmpty()) {
			int time = 0;
			this.test = 0;
			for (ItemStack stack : this.items) {
				if (!stack.isEmpty()) {
					time += stack.getCount();
				} else {
					time = 0;
				}
			}
			this.maxRunningTime = time * 30;
			dirty = true;
		} else if (this.test < 1) {
			this.maxRunningTime = 0;
			dirty = true;
			this.test++;
		}

		moveItems();
		if (!isEmpty() && this.buttonPressed) {
			this.currentRunningTime++;
			if (this.currentRunningTime % 60 == 0) {
				this.currentRunningTime -= 60;
				this.items.get(3).shrink(2);
				dirty = true;

				Direction side = Direction.fromAngle(
						getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING).getHorizontalAngle() + 90);

				IInventory inv = TileEntityUtils.getValidContainer(this.world, side, this.pos, getBlockState(),
						new ItemStack(ItemInit.SAWDUST.get()));
				if (inv != null) {
					TileEntityUtils.insertItem(inv, new ItemStack(ItemInit.BRIQUETTE.get()));
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
				setPressed(false);
				dirty = true;
			}
		}

		if (dirty) {
			markDirty();
		}
	}

	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		super.write(compound);
		compound.putBoolean("ButtonPressed", this.buttonPressed);
		compound.putInt("RunningTime", this.currentRunningTime);
		compound.putInt("MaxRunningTime", this.maxRunningTime);
		compound.putFloat("BriquetteSize", this.briquetteSize);
		compound.putFloat("TurnerRotation", this.turnerRotation);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}
}
