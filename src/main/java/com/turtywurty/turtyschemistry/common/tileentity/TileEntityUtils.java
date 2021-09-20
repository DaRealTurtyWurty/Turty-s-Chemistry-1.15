package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class TileEntityUtils {

    private TileEntityUtils() {
    }

    public static boolean canInsertIntoContainer(final IInventory container, final ItemStack stack) {
        for (int index = 0; index < container.getSizeInventory(); index++) {
            if (container.isItemValidForSlot(index, stack))
                return true;
        }
        return false;
    }

    public static List<Integer> getFreeSlotsForStack(final IInventory inv, final ItemStack stack) {
        final List<Integer> slots = new ArrayList<>();
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            if (inv.isItemValidForSlot(index, stack)
                    && inv.getStackInSlot(index).getCount() != inv.getInventoryStackLimit()) {
                slots.add(index);
            }
        }

        return slots;
    }

    public static List<ItemStack> getInvItems(final IInventory inv) {
        final List<ItemStack> items = new ArrayList<>();
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            items.add(inv.getStackInSlot(index));
        }

        return items;
    }

    @Nullable
    public static IInventory getValidContainer(final World world, final Direction side, final BlockPos pos,
            final BlockState state, final ItemStack stack) {
        if (world.getBlockState(pos.offset(side)).hasTileEntity()) {
            final TileEntity tile = world.getTileEntity(pos.offset(side));
            if (tile instanceof IInventory)
                return (IInventory) tile;
        }
        return null;
    }

    public static boolean insertItem(final IInventory inv, final ItemStack stack) {
        final ItemStack itemstack = stack.copy();

        if (canInsertIntoContainer(inv, stack)) {
            final List<Integer> freeSlots = getFreeSlotsForStack(inv, stack);
            for (final int index : freeSlots) {
                final int availableSpace = inv.getInventoryStackLimit()
                        - inv.getStackInSlot(index).getCount();
                final int neededSpace = stack.getCount();
                if (!inv.getStackInSlot(index).isEmpty()) {
                    if (availableSpace >= neededSpace) {
                        inv.getStackInSlot(index).grow(neededSpace);
                    } else {
                        inv.getStackInSlot(index).setCount(inv.getInventoryStackLimit());
                        stack.shrink(availableSpace - neededSpace);
                    }
                    return true;
                }
                if (availableSpace >= neededSpace) {
                    inv.setInventorySlotContents(index, new ItemStack(itemstack.getItem(), neededSpace));
                } else {
                    inv.setInventorySlotContents(index,
                            new ItemStack(itemstack.getItem(), inv.getInventoryStackLimit()));
                    stack.shrink(availableSpace - neededSpace);
                }
                return true;
            }
        }
        return false;
    }
}
