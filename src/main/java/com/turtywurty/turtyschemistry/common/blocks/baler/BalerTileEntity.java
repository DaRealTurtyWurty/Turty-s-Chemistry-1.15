package com.turtywurty.turtyschemistry.common.blocks.baler;

import java.util.ArrayList;
import java.util.List;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class BalerTileEntity extends LockableLootTileEntity implements ITickableTileEntity, INamedContainerProvider {

	private static final String WHEAT = "minecraft:wheat";

	public int compressingTime = 0;
	public int maxCompressingTime = 400;
	public boolean completed = false;
	public int itemTicksExisted = 0;

	private final String[] recipes = { "minecraft:string", "minecraft:string", WHEAT, WHEAT, WHEAT, WHEAT, WHEAT, WHEAT,
			WHEAT, WHEAT, WHEAT };

	protected NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(11, ItemStack.EMPTY);

	public BalerTileEntity() {
		this(TileEntityTypeInit.BALER.get());
	}

	public BalerTileEntity(final TileEntityType<?> typeIn) {
		super(typeIn);
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	protected Container createMenu(final int id, final PlayerInventory player) {
		return new BalerContainer(id, player, this);
	}

	@Override
	public ItemStack decrStackSize(final int index, final int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".baler");
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	public int getItemTicks() {
		return this.itemTicksExisted;
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

	public boolean isCompressing() {
		return this.compressingTime > 0 && this.compressingTime < this.maxCompressingTime;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty())
				return false;
		}
		return true;
	}

	public boolean isFull() {
		int index = 0;
		for (ItemStack stack : this.items) {
			if (stack.isEmpty())
				return false;
			if (index <= 9) {
				if (!this.items.get(index).getItem().equals(Items.WHEAT))
					return false;
			} else if (index > 9 && index <= getSizeInventory()
					&& !this.items.get(index).getItem().equals(Items.STRING))
				return false;
			index++;
		}
		return true;

	}

	@Override
	public boolean isItemValidForSlot(final int index, final ItemStack stack) {
		return index <= 9 ? stack.getItem().equals(Items.WHEAT) : stack.getItem().equals(Items.STRING);
	}

	@Override
	public boolean isUsableByPlayer(final PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this)
			return false;
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public boolean isValidRecipe(final String[] recipesIn, final List<ItemStack> itemsIn) {
		boolean flag = true;
		List<String> names = new ArrayList<>();
		for (ItemStack stack : itemsIn) {
			names.add(stack.getItem().getRegistryName().toString());
		}

		int index = 0;
		for (String name : names) {
			if (!name.equals(recipesIn[index])) {
				flag = false;
				break;
			}
			index++;
		}

		return flag;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
		read(this.world.getBlockState(packet.getPos()), packet.getNbtCompound());
	}

	@Override
	public void read(final BlockState state, final CompoundNBT compound) {
		super.read(state, compound);
		this.compressingTime = compound.getInt("CompressingTime");
		this.maxCompressingTime = compound.getInt("MaxCompressingTime");
		this.completed = compound.getBoolean("Completed");
		this.itemTicksExisted = compound.getInt("ItemTicksExisted");
		this.items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);

	}

	@Override
	public ItemStack removeStackFromSlot(final int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	public void setComplete(final boolean complete) {
		this.completed = complete;
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

	@Override
	public void tick() {
		boolean dirty = false;

		if (this.world == null || this.world.isRemote)
			return;

		if (this.items.size() == 11 && isValidRecipe(this.recipes, this.items)) {
			this.compressingTime += 1;
			// TODO: Consume energy

			if (this.compressingTime >= this.maxCompressingTime) {
				this.compressingTime = 0;
				for (ItemStack stack : this.items) {
					stack.shrink(1);
				}

				setComplete(true);
				dirty = true;

				if (!isValidRecipe(this.recipes, this.items) && !isCompressing()) {
					this.compressingTime = 0;
				}
			} else {
				dirty = true;
			}
		} else if (!isValidRecipe(this.recipes, this.items)) {
			this.compressingTime = 0;
		}

		if (this.completed && this.itemTicksExisted < 200) {
			this.itemTicksExisted += 1;
			dirty = true;
		} else if (this.itemTicksExisted >= 200) {
			this.itemTicksExisted = 0;
			setComplete(false);
			dirty = true;

			ItemEntity item = new ItemEntity(EntityType.ITEM, this.world);
			item.setItem(new ItemStack(Items.HAY_BLOCK));
			Direction facing = getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING);
			item.setPosition(getPos().offset(facing).getX(), getPos().offset(facing).getY(),
					getPos().offset(facing).getZ());
			this.world.addEntity(item);
		}

		if (dirty) {
			markDirty();
		}
	}

	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		super.write(compound);
		compound.putInt("CompressingTime", this.compressingTime);
		compound.putInt("MaxCompressingTime", this.maxCompressingTime);
		compound.putBoolean("Completed", this.completed);
		compound.putInt("ItemTicksExisted", this.itemTicksExisted);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}
}
