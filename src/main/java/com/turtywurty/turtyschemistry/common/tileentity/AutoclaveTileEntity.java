package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.AutoclaveContainer;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AutoclaveTileEntity extends InventoryTile implements INamedContainerProvider {

	private final int maxRunningTime = 400;
	private Map<Integer, Integer> runningMap = new HashMap<Integer, Integer>(3);

	public AutoclaveTileEntity(TileEntityType<?> typeIn) {
		super(typeIn, 3);
	}

	public AutoclaveTileEntity() {
		this(TileEntityTypeInit.AUTOCLAVE.get());
		this.runningMap.put(0, 0);
		this.runningMap.put(1, 0);
		this.runningMap.put(2, 0);
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
		return new AutoclaveContainer(id, playerInv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".autoclave");
	}

	@Override
	public void tick() {
		super.tick();

		boolean dirty = false;

		for (int index = 0; index < this.getSize(); index++) {
			if (!this.getItemInSlot(index).isEmpty() && this.getItemInSlot(index).isDamaged()) {
				if (this.runningMap.get(index) < this.getMaxRunningTime(index)) {
					this.runningMap.put(index, this.runningMap.get(index) + 1);
				} else {
					ItemStack copy = this.getItemInSlot(index);
					copy.setDamage(0);
					this.getInventory().setStackInSlot(index, copy);
					this.runningMap.put(index, 0);
				}
				dirty = true;
			} else {
				this.runningMap.put(index, 0);
			}
		}

		if (dirty)
			this.markDirty();
	}

	public int getMaxRunningTime(int slot) {
		ItemStack stack = this.getItemInSlot(slot);
		int currentDmg = stack.getDamage();
		System.out.println(currentDmg * 10);
		return currentDmg * 10;
	}

	public boolean insertIntoSlots(ItemStack stack) {
		ItemStack insertStack = stack.copy();
		for (int index = 0; index < this.getSize(); index++) {
			insertStack = this.insertItem(index, insertStack);
		}

		if (!insertStack.isEmpty()) {
			this.world.addEntity(
					new ItemEntity(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), insertStack));
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		Map<Integer, Integer> map = Maps.newHashMap();
		for (int slotNo = 0; slotNo < this.runningMap.size(); slotNo++) {
			map.put(slotNo, compound.getCompound("RunningMap").getInt("Slot" + slotNo));
		}
		this.runningMap = map;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		for (int slotNo : this.runningMap.keySet()) {
			compound.getCompound("RunningMap").putInt("Slot" + slotNo, this.runningMap.get(slotNo));
		}
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

	public int getCurrentRunningTime(int slot) {
		return this.runningMap.get(slot);
	}

	public void setCurrentRunningTime(int slot, int currentRunningTime) {
		this.runningMap.put(slot, currentRunningTime);
	}
}
