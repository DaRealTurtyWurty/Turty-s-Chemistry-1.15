package com.turtywurty.turtyschemistry.common.blocks.autoclave;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
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

    private static final int MAX_RUNNING_TIME = 400;

    private Map<Integer, Integer> runningMap = new HashMap<>(3);

    public AutoclaveTileEntity() {
        this(TileEntityTypeInit.AUTOCLAVE.get());
        this.runningMap.put(0, 0);
        this.runningMap.put(1, 0);
        this.runningMap.put(2, 0);
    }

    public AutoclaveTileEntity(final TileEntityType<?> typeIn) {
        super(typeIn, 3);
    }

    @Override
    public Container createMenu(final int id, final PlayerInventory playerInv, final PlayerEntity player) {
        return new AutoclaveContainer(id, playerInv, this);
    }

    public int getCurrentRunningTime(final int slot) {
        return this.runningMap.get(slot);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".autoclave");
    }

    public int getMaxRunningTime(final int slot) {
        final ItemStack stack = getItemInSlot(slot);
        final int currentDmg = stack.getDamage();
        return currentDmg * 10;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        final CompoundNBT nbt = new CompoundNBT();
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

    public boolean insertIntoSlots(final ItemStack stack) {
        ItemStack insertStack = stack.copy();
        for (int index = 0; index < getSize(); index++) {
            insertStack = insertItem(index, insertStack);
        }

        if (!insertStack.isEmpty()) {
            this.world.addEntity(new ItemEntity(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(),
                    insertStack));
            return false;
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
        final Map<Integer, Integer> map = Maps.newHashMap();
        for (int slotNo = 0; slotNo < this.runningMap.size(); slotNo++) {
            map.put(slotNo, compound.getCompound("RunningMap").getInt("Slot" + slotNo));
        }
        this.runningMap = map;
    }

    public void setCurrentRunningTime(final int slot, final int currentRunningTime) {
        this.runningMap.put(slot, currentRunningTime);
    }

    @Override
    public void tick() {
        super.tick();

        boolean dirty = false;

        for (int index = 0; index < getSize(); index++) {
            if (!getItemInSlot(index).isEmpty() && getItemInSlot(index).isDamaged()) {
                if (this.runningMap.get(index) < getMaxRunningTime(index)) {
                    this.runningMap.put(index, this.runningMap.get(index) + 1);
                } else {
                    final ItemStack copy = getItemInSlot(index);
                    copy.setDamage(0);
                    getInventory().setStackInSlot(index, copy);
                    this.runningMap.put(index, 0);
                }
                dirty = true;
            } else {
                this.runningMap.put(index, 0);
            }
        }

        if (dirty) {
            markDirty();
        }
    }

    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        super.write(compound);
        int slotIndex = 0;
        for (final Entry<Integer, Integer> entry : this.runningMap.entrySet()) {
            compound.getCompound("RunningMap").putInt("Slot" + slotIndex, entry.getValue());
            slotIndex++;
        }
        return compound;
    }
}
