package com.turtywurty.turtyschemistry.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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

    public InventoryTile(final TileEntityType<?> tileEntityTypeIn, final int size) {
        super(tileEntityTypeIn);
        this.size = size;
        this.inventory = createHandler();
        this.handler = LazyOptional.of(() -> this.inventory);
    }

    public IItemHandlerModifiable createHandler() {
        return new ItemStackHandler(this.size) {
            @Override
            public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
                InventoryTile.this.world.notifyBlockUpdate(InventoryTile.this.pos, getBlockState(),
                        getBlockState(), BlockFlags.BLOCK_UPDATE);
                return super.extractItem(slot, amount, simulate);
            }

            @Override
            public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
                InventoryTile.this.world.notifyBlockUpdate(InventoryTile.this.pos, getBlockState(),
                        getBlockState(), BlockFlags.BLOCK_UPDATE);
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    public ItemStack extractItem(final int slot) {
        final int count = getItemInSlot(slot).getCount();
        this.requiresUpdate = true;
        return this.handler.map(inv -> inv.extractItem(slot, count, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> cap, @Nullable final Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return this.handler.cast();
        return super.getCapability(cap, side);
    }

    public LazyOptional<IItemHandlerModifiable> getHandler() {
        return this.handler;
    }

    public IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    public ItemStack getItemInSlot(final int slot) {
        return this.handler.map(inv -> inv.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
    }

    public int getSize() {
        return this.size;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), -1, getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return serializeNBT();
    }

    @Override
    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        deserializeNBT(tag);
    }

    public ItemStack insertItem(final int slot, final ItemStack stack) {
        final ItemStack itemIn = stack.copy();
        stack.shrink(itemIn.getCount());
        this.requiresUpdate = true;
        return this.handler.map(inv -> inv.insertItem(slot, itemIn, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        handleUpdateTag(this.world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        super.read(state, compound);
        this.requiresUpdate = true;
    }

    @Override
    public void tick() {
        this.timer++;
        if (this.world != null && this.requiresUpdate) {
            updateTile();
            this.requiresUpdate = false;
        }
    }

    public void updateTile() {
        requestModelDataUpdate();
        markDirty();
        if (getWorld() != null) {
            getWorld().notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), 3);
        }
    }
}
