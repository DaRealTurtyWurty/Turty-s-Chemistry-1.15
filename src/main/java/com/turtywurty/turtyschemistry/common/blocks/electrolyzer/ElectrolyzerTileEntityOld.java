package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ElectrolyzerTileEntityOld extends InventoryTile {

    private int storedHydrogen, storedOxygen, storedWater;
    private boolean converting = false, reachedMax = false;
    private int runningTime;

    private int maxRunningTime = 40, maxHydrogen = 10000, maxOxygen = 10000, maxWater = 10000;
    private final int bucketHydrogen = 500, bucketOxygen = 250;

    public ElectrolyzerTileEntityOld() {
        this(TileEntityTypeInit.ELECTROLYZER.get());
    }

    public ElectrolyzerTileEntityOld(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 3);
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".electrolyzer");
    }

    public int getMaxHydrogen() {
        return this.maxHydrogen;
    }

    public int getMaxOxygen() {
        return this.maxOxygen;
    }

    public int getMaxRunningTime() {
        return this.maxRunningTime;
    }

    public int getMaxWater() {
        return this.maxWater;
    }

    public int getRunningTime() {
        return this.runningTime;
    }

    public int getStoredHydrogen() {
        return this.storedHydrogen;
    }

    public int getStoredOxygen() {
        return this.storedOxygen;
    }

    public int getStoredWater() {
        return this.storedWater;
    }

    public boolean isConverting() {
        return this.converting;
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        super.read(state, compound);
        this.storedHydrogen = compound.getInt("StoredHydrogen");
        this.storedOxygen = compound.getInt("StoredOxygen");
        this.storedWater = compound.getInt("StoredWater");
        this.runningTime = compound.getInt("RunningProgress");
        this.converting = compound.getBoolean("Converting");
        this.reachedMax = compound.getBoolean("ReachedMax");
    }

    @Override
    public void remove() {
        super.remove();
        invalidateCaps();
        warnInvalidBlock();
    }

    public void setConverting(final boolean converting) {
        this.converting = converting;
    }

    public void setMaxHydrogen(final int maxHydrogen) {
        this.maxHydrogen = maxHydrogen;
    }

    public void setMaxOxygen(final int maxOxygen) {
        this.maxOxygen = maxOxygen;
    }

    public void setMaxRunningTime(final int maxRunningTime) {
        this.maxRunningTime = maxRunningTime;
    }

    public void setMaxWater(final int maxWater) {
        this.maxWater = maxWater;
    }

    public void setRunningTime(final int runningTime) {
        this.runningTime = runningTime;
    }

    public void setStoredHydrogen(final int storedHydrogen) {
        this.storedHydrogen = storedHydrogen;
    }

    public void setStoredOxygen(final int storedOxygen) {
        this.storedOxygen = storedOxygen;
    }

    public void setStoredWater(final int storedWater) {
        this.storedWater = storedWater;
    }

    @Override
    public void tick() {
        super.tick();

        boolean dirty = false;

        if (getItemInSlot(0).getItem() instanceof BucketItem && this.storedWater <= this.maxWater - 1000) {
            final BucketItem bucket = (BucketItem) getItemInSlot(0).getItem();
            if (bucket.getFluid().equals(Fluids.FLOWING_WATER) || bucket.getFluid().equals(Fluids.WATER)) {
                getInventory().setStackInSlot(0, new ItemStack(Items.BUCKET));
                this.converting = true;
                this.storedWater += 1000;
                dirty = true;
            }
        }

        if (this.storedWater <= 0) {
            this.converting = false;
        }

        if (this.runningTime > this.maxRunningTime) {
            this.runningTime = 0;
        }

        if (this.converting && (this.world.isBlockPowered(this.pos) && !this.reachedMax)) {
            this.runningTime++;
            if (this.timer % 2 == 0) {
                this.storedWater--;
                if (this.storedWater % 2 == 0) {
                    if (this.storedHydrogen != this.maxHydrogen) {
                        this.storedHydrogen += this.world.rand.nextInt(2);
                    } else {
                        this.reachedMax = true;
                    }

                    if (this.storedWater % 4 == 0) {
                        if (this.storedOxygen != this.maxOxygen) {
                            this.storedOxygen += this.world.rand.nextInt(2);
                        } else {
                            this.reachedMax = true;
                        }
                    }
                }
                dirty = true;
            }
        } else {
            this.runningTime = 0;
        }

        if (this.timer % 40 == 0) {
            if (this.storedHydrogen > 0 && this.world.rand.nextInt(5) == 0) {
                this.storedHydrogen--;
            }

            if (this.storedOxygen > 0 && this.world.rand.nextInt(7) == 0) {
                this.storedOxygen--;
            }
        }

        if (this.storedHydrogen < 0) {
            this.storedHydrogen = 0;
            dirty = true;
        }

        if (this.storedOxygen < 0) {
            this.storedOxygen = 0;
            dirty = true;
        }

        if (this.storedWater < 0) {
            this.storedWater = 0;
            dirty = true;
        }

        if (getItemInSlot(1).getItem().equals(ItemInit.GAS_CANISTER_S.get())
                || getItemInSlot(1).getItem().equals(ItemInit.GAS_CANISTER_L.get())) {
            final CompoundNBT nbt = getItemInSlot(1).getOrCreateChildTag("BlockEntityTag");
            if (nbt.contains("GasName")) {
                if ("turtychemistry:hydrogen".equalsIgnoreCase(nbt.getString("GasName"))
                        && nbt.getInt("GasStored") < nbt.getInt("MaxAmount") && this.storedHydrogen > 0) {
                    nbt.putInt("GasStored", nbt.getInt("GasStored") + 1);
                    this.storedHydrogen--;
                }
            } else {
                nbt.putString("GasName", "turtychemistry:hydrogen");
            }
        }

        if (getItemInSlot(2).getItem().equals(ItemInit.GAS_CANISTER_S.get())
                || getItemInSlot(2).getItem().equals(ItemInit.GAS_CANISTER_L.get())) {
            final CompoundNBT nbt = getItemInSlot(2).getOrCreateChildTag("BlockEntityTag");
            if (nbt.contains("GasName")) {
                if ("turtychemistry:oxygen".equalsIgnoreCase(nbt.getString("GasName"))
                        && nbt.getInt("GasStored") < nbt.getInt("MaxAmount") && this.storedOxygen > 0) {
                    nbt.putInt("GasStored", nbt.getInt("GasStored") + 1);
                    this.storedOxygen--;
                }
            } else {
                nbt.putString("GasName", "turtychemistry:oxygen");
            }
        }

        if (dirty) {
            markDirty();
        }
    }

    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        compound.putInt("StoredHydrogen", this.storedHydrogen);
        compound.putInt("StoredOxygen", this.storedOxygen);
        compound.putInt("StoredWater", this.storedWater);
        compound.putInt("RunningProgress", this.runningTime);
        compound.putBoolean("Converting", this.converting);
        compound.putBoolean("ReachedMax", this.reachedMax);
        return super.write(compound);
    }
}
