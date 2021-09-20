package com.turtywurty.turtyschemistry.common.blocks.researcher;

import com.turtywurty.turtyschemistry.common.items.BlueprintItem;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ResearcherContainer extends Container {

    public static ResearcherTileEntity tile;
    private final IWorldPosCallable callable;
    public final IIntArray data;

    public ResearcherContainer(final int id, final PlayerInventory playerInventory,
            final IItemHandlerModifiable slots, final BlockPos pos, final IIntArray dataIn) {
        super(ContainerTypeInit.RESEARCHER.get(), id);
        this.callable = IWorldPosCallable.of(playerInventory.player.getEntityWorld(), pos);
        this.data = dataIn;

        trackIntArray(this.data);

        final int slotSizePlus2 = 18;

        // Researcher Inventory
        addSlot(new SlotItemHandler(slots, 0, 41, 36) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return stack.getItem() instanceof BlueprintItem;
            }
        });
        addSlot(new SlotItemHandler(slots, 1, 120, 36) {
            @Override
            public boolean isEnabled() {
                return ResearcherContainer.this.data.get(0) == 0;
            }

            @Override
            public boolean isItemValid(final ItemStack stack) {
                return stack.getItem() instanceof BlueprintItem;
            }

            @SuppressWarnings("resource")
            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                if (!tile.getWorld().isRemote) {
                    tile.requiresUpdate = true;
                }
            }
        });

        // Main Player Inventory
        final int startX = 8;
        final int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                addSlot(new Slot(playerInventory, 9 + row * 9 + column, startX + column * slotSizePlus2,
                        startY + row * slotSizePlus2));
            }
        }

        // Hotbar
        final int hotbarY = 142;
        for (int column = 0; column < 9; ++column) {
            addSlot(new Slot(playerInventory, column, startX + column * slotSizePlus2, hotbarY));
        }
    }

    public static ResearcherContainer getClientContainer(final int id,
            final PlayerInventory playerInventory) {
        return new ResearcherContainer(id, playerInventory, new ItemStackHandler(2), BlockPos.ZERO,
                new IntArray(2));
    }

    public static IContainerProvider getServerContainerProvider(final ResearcherTileEntity te,
            final BlockPos activationPos) {
        tile = te;
        return (id, playerInventory, serverPlayer) -> new ResearcherContainer(id, playerInventory,
                te.getInventory(), activationPos, new ResearcherSyncData(te));
    }

    public static ResearcherTileEntity getTile() {
        return tile;
    }

    @Override
    public boolean canInteractWith(final PlayerEntity playerIn) {
        return isWithinUsableDistance(this.callable, playerIn, BlockInit.RESEARCHER.get());
    }

    @Override
    public ItemStack transferStackInSlot(final PlayerEntity playerIn, final int index) {
        ItemStack returnStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack slotStack = slot.getStack();
            returnStack = slotStack.copy();

            final int containerSlots = this.inventorySlots.size() - playerIn.inventory.mainInventory.size();
            if (index < containerSlots) {
                if (!mergeItemStack(slotStack, containerSlots, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(slotStack, 0, containerSlots, false))
                return ItemStack.EMPTY;
            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == returnStack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, slotStack);
        }
        return returnStack;
    }
}
