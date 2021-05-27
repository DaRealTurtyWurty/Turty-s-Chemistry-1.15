package com.turtywurty.turtyschemistry.common.blocks.baler;

import java.util.ArrayList;
import java.util.List;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

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

	private final String[] recipes = new String[] { "minecraft:string", "minecraft:string", WHEAT, WHEAT, WHEAT, WHEAT,
			WHEAT, WHEAT, WHEAT, WHEAT, WHEAT };

	protected NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(11, ItemStack.EMPTY);

	public BalerTileEntity(TileEntityType<?> typeIn) {
		super(typeIn);
	}

	public BalerTileEntity() {
		this(TileEntityTypeInit.BALER.get());
	}

	public boolean isValidRecipe(String[] recipesIn, List<ItemStack> itemsIn) {
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
	public void tick() {
		boolean dirty = false;

		if (world == null || world.isRemote)
			return;

		if (this.items.size() == 11 && isValidRecipe(this.recipes, this.items)) {
			compressingTime += 1;
			// TODO: Consume energy

			if (compressingTime >= maxCompressingTime) {
				compressingTime = 0;
				for (ItemStack stack : this.items) {
					stack.shrink(1);
				}

				this.setComplete(true);
				dirty = true;

				if (!isValidRecipe(this.recipes, this.items) && !isCompressing()) {
					compressingTime = 0;
				}
			} else {
				dirty = true;
			}
		} else {
			if (!isValidRecipe(this.recipes, this.items)) {
				compressingTime = 0;
			}
		}

		if (this.completed && this.itemTicksExisted < 200) {
			this.itemTicksExisted += 1;
			dirty = true;
		} else {
			if (this.itemTicksExisted >= 200) {
				this.itemTicksExisted = 0;
				this.setComplete(false);
				dirty = true;

				ItemEntity item = new ItemEntity(EntityType.ITEM, this.world);
				item.setItem(new ItemStack(Items.HAY_BLOCK));
				Direction facing = this.getBlockState().get(BaseHorizontalBlock.HORIZONTAL_FACING);
				item.setPosition(this.getPos().offset(facing).getX(), this.getPos().offset(facing).getY(),
						this.getPos().offset(facing).getZ());
				this.world.addEntity(item);
			}
		}

		if (dirty)
			this.markDirty();
	}

	public int getItemTicks() {
		return this.itemTicksExisted;
	}

	public boolean isCompressing() {
		return this.compressingTime > 0 && this.compressingTime < this.maxCompressingTime;
	}

	public boolean isFull() {
		int index = 0;
		for (ItemStack stack : this.items) {
			if (stack.isEmpty())
				return false;
			if (index <= 9) {
				if (!this.items.get(index).getItem().equals(Items.WHEAT)) {
					return false;
				}
			} else if (index > 9 && index <= this.getSizeInventory() && !this.items.get(index).getItem().equals(Items.STRING)) {
				return false;
			}
			index++;
		}
		return true;

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
	public int getSizeInventory() {
		return this.items.size();
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
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
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
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".baler");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new BalerContainer(id, player, this);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.compressingTime = compound.getInt("CompressingTime");
		this.maxCompressingTime = compound.getInt("MaxCompressingTime");
		this.completed = compound.getBoolean("Completed");
		this.itemTicksExisted = compound.getInt("ItemTicksExisted");
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);

	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("CompressingTime", this.compressingTime);
		compound.putInt("MaxCompressingTime", this.maxCompressingTime);
		compound.putBoolean("Completed", this.completed);
		compound.putInt("ItemTicksExisted", this.itemTicksExisted);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	public void setComplete(boolean complete) {
		this.completed = complete;
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

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index <= 9 ? stack.getItem().equals(Items.WHEAT) : stack.getItem().equals(Items.STRING);
	}
}
