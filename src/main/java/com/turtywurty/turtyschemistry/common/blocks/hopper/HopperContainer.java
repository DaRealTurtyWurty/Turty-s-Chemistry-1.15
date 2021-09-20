package com.turtywurty.turtyschemistry.common.blocks.hopper;

import java.util.Objects;

import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;

public class HopperContainer extends Container {

    public static final int INVENTORY_SLOTS = 54;
    public static final int FIRST_PLAYER_SLOT = INVENTORY_SLOTS;

    public static final int PLAYER_SLOTS = 36;

    public static final int INDEX_AFTER_LAST_PLAYER_SLOT = FIRST_PLAYER_SLOT + PLAYER_SLOTS;

    private final IWorldPosCallable callable;

    public final HopperTileEntity tileEntity;

    public HopperContainer(final int id, final PlayerInventory playerInventory,
            final HopperTileEntity tileEntityIn) {
        super(ContainerTypeInit.HOPPER.get(), id);
        this.tileEntity = tileEntityIn;
        this.callable = IWorldPosCallable.of(tileEntityIn.getWorld(), tileEntityIn.getPos());

        final int slotSizePlus2 = 18;

        // Hopper Inventory
        final int hopperX = 8;
        final int hopperY = 18;
        int index = 0;
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new SlotItemHandler(tileEntityIn.getInventory(), index,
                        hopperX + column * slotSizePlus2, hopperY + row * slotSizePlus2));
                index++;
            }
        }

        // Player Inventory
        final int startX = 8;
        final int startY = 140;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                addSlot(new Slot(playerInventory, 9 + row * 9 + column, startX + column * slotSizePlus2,
                        startY + row * slotSizePlus2));
            }
        }

        // Hotbar
        final int hotbarY = 198;
        for (int column = 0; column < 9; ++column) {
            addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
        }
    }

    public HopperContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
        this(windowId, playerInv, getTileEntity(playerInv, data));
    }

    public static HopperTileEntity getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
        Objects.requireNonNull(playerInv, "playerInv cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final TileEntity tile = playerInv.player.world.getTileEntity(data.readBlockPos());
        if (tile instanceof HopperTileEntity)
            return (HopperTileEntity) tile;

        throw new IllegalStateException("Tile entity is not correct! " + tile);
    }

    @Override
    public boolean canInteractWith(final PlayerEntity playerIn) {
        return isWithinUsableDistance(this.callable, playerIn, BlockInit.HOPPER.get());
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this
     * moves the stack between the player inventory and the other inventory(s).
     */
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity playerIn, final int index) {
        ItemStack copiedStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack stackFromSlot = slot.getStack();
            copiedStack = stackFromSlot.copy();
            if (index < FIRST_PLAYER_SLOT) {
                if (!mergeItemStack(stackFromSlot, FIRST_PLAYER_SLOT, INDEX_AFTER_LAST_PLAYER_SLOT, true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(stackFromSlot, 0, FIRST_PLAYER_SLOT, false))
                return ItemStack.EMPTY;

            if (stackFromSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stackFromSlot.getCount() == copiedStack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, stackFromSlot);
        }

        return copiedStack;
    }
}
