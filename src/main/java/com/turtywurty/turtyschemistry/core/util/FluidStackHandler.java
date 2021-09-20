package com.turtywurty.turtyschemistry.core.util;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackHandler implements IMultiTank, INBTSerializable<CompoundNBT> {

    protected NonNullList<FluidStack> stacks;
    protected int[] capacity;

    public FluidStackHandler(final int... capacity) {
        this(1, capacity);
    }

    public FluidStackHandler(final int tanks, final int... capacity) {
        this.stacks = NonNullList.withSize(tanks, FluidStack.EMPTY);
        this.capacity = capacity;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : this.stacks.size());
        final ListNBT tagList = nbt.getList("Fluids", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            final CompoundNBT fluidTags = tagList.getCompound(i);
            final int tank = fluidTags.getInt("Tank");
            if (tank >= 0 && tank < this.stacks.size()) {
                this.stacks.set(tank, FluidStack.loadFluidStackFromNBT(fluidTags));
            }
            setCapacity(nbt.contains("Capacity", Constants.NBT.TAG_INT) ? nbt.getInt("Capacity")
                    : this.capacity[tank], tank);
        }
        onLoad();
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the
     * IFluidHandler.
     *
     * @param resource FluidStack representing the Fluid and maximum amount of fluid
     *                 to be drained.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have
     *         been, if simulated) drained.
     */
    @Nonnull
    @Override
    public FluidStack drain(final FluidStack resource, final FluidAction action) {
        FluidStack totalDrain = FluidStack.EMPTY;
        FluidStack toDrain = resource.copy();
        for (int i = 0; i < getTanks(); i++) {
            if (!toDrain.isEmpty()) {
                final FluidStack singleDrain = drain(i, toDrain, action);
                if (totalDrain.isEmpty()) {
                    totalDrain = singleDrain.copy();
                } else {
                    totalDrain.grow(singleDrain.getAmount());
                }
                toDrain = new FluidStack(toDrain, toDrain.getAmount() - singleDrain.getAmount());
            }
        }
        return totalDrain;
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the
     * IFluidHandler.
     * <p/>
     * This method is not Fluid-sensitive.
     *
     * @param maxDrain Maximum amount of fluid to drain.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have
     *         been, if simulated) drained.
     */
    @Nonnull
    @Override
    public FluidStack drain(final int maxDrain, final FluidAction action) {
        FluidStack totalDrainStack = FluidStack.EMPTY;
        int toDrain = maxDrain;
        for (int i = 0; i < getTanks(); i++) {
            if (toDrain > 0) {
                final FluidStack singleDrain = drain(i, toDrain, action);
                if (totalDrainStack.isEmpty()) {
                    totalDrainStack = singleDrain.copy();
                } else {
                    totalDrainStack.grow(singleDrain.getAmount());
                }
                toDrain -= singleDrain.getAmount();
            }
        }
        return totalDrainStack;
    }

    /**
     * Drains fluid out of selected tank.
     *
     * @param tank     Tank to extract from.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid
     *                 to be drained.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have
     *         been, if simulated) drained.
     */
    @Nonnull
    @Override
    public FluidStack drain(final int tank, final FluidStack resource, final FluidAction action) {
        if (resource.isEmpty() || !resource.isFluidEqual(getFluidInTank(tank))
                || !isFluidValid(tank, resource))
            return FluidStack.EMPTY;
        return drain(tank, resource.getAmount(), action);
    }

    /**
     * Drains fluid out of selected tank.
     * <p/>
     * This method is not Fluid-sensitive.
     *
     * @param tank     Tank to extract from.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have
     *         been, if simulated) drained.
     */
    @Nonnull
    @Override
    public FluidStack drain(final int tank, final int maxDrain, final FluidAction action) {
        if (maxDrain <= 0)
            return FluidStack.EMPTY;
        validateSlotIndex(tank);
        final FluidStack existingFluid = getFluidInTank(tank);
        if (existingFluid.isEmpty())
            return FluidStack.EMPTY;
        int drained = maxDrain;
        if (existingFluid.getAmount() <= maxDrain) {
            drained = existingFluid.getAmount();
            if (action.execute()) {
                this.stacks.set(tank, FluidStack.EMPTY);
                onContentsChanged();
            }
        } else if (action.execute()) {
            this.stacks.set(tank, new FluidStack(existingFluid, existingFluid.getAmount() - drained));
            onContentsChanged();
        }
        return new FluidStack(existingFluid, drained);
    }

    /**
     * Fills fluid into internal tanks, distribution is left entirely to the
     * IFluidHandler.
     *
     * @param resource FluidStack representing the Fluid and maximum amount of fluid
     *                 to be filled.
     * @param action   If SIMULATE, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated)
     *         filled.
     */
    @Override
    public int fill(final FluidStack resource, final FluidAction action) {
        int totalFill = 0;
        FluidStack rem = resource.copy();
        for (int i = 0; i < getTanks(); i++) {
            if (!rem.isEmpty()) {
                final int singleFill = fill(i, rem, action);
                totalFill += singleFill;
                rem = new FluidStack(resource, rem.getAmount() - singleFill);
            }
        }
        return totalFill;
    }

    /**
     * Fills fluid into selected tank.
     *
     * @param tank     Tank to insert into.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid
     *                 to be filled.
     * @param action   If SIMULATE, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated)
     *         filled.
     */
    @Override
    public int fill(final int tank, final FluidStack resource, final FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(tank, resource))
            return 0;
        final FluidStack existingFluid = getFluidInTank(tank);
        if (!existingFluid.isFluidEqual(resource) && !existingFluid.isEmpty())
            return 0;
        validateSlotIndex(tank);
        if (existingFluid.isEmpty()) {
            if (action.execute()) {
                this.stacks.set(tank,
                        new FluidStack(resource, Math.min(this.capacity[tank], resource.getAmount())));
                onContentsChanged();
            }
            return resource.getAmount();
        }
        int filled = this.capacity[tank] - existingFluid.getAmount();
        if (resource.getAmount() < filled) {
            if (action.execute()) {
                existingFluid.grow(resource.getAmount());
            }
            filled = resource.getAmount();
        } else if (action.execute()) {
            existingFluid.setAmount(this.capacity[tank]);
        }
        if (filled > 0 && action.execute()) {
            onContentsChanged();
        }
        return filled;
    }

    public NonNullList<FluidStack> getContents() {
        return this.stacks;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(final int tank) {
        return this.stacks.get(tank);
    }

    @Override
    public int getTankCapacity(final int tank) {
        return this.capacity[tank];
    }

    @Override
    public int getTanks() {
        return this.stacks.size();
    }

    public boolean isEmpty() {
        return this.stacks.stream().allMatch(FluidStack::isEmpty);
    }

    public boolean isFluidEqual(final int tank, final FluidStack fluid) {
        return getFluidInTank(tank).isFluidEqual(fluid);
    }

    /**
     * This function is a way to determine which fluids can exist inside a given
     * handler. General purpose tanks will basically always return TRUE for this.
     *
     * @param tank  Tank to query for validity
     * @param stack Stack to test with for validity
     * @return TRUE if the tank can hold the FluidStack, not considering current
     *         state. (Basically, is a given fluid EVER allowed in this tank?)
     *         Return FALSE if the answer to that question is 'no.'
     */
    @Override
    public boolean isFluidValid(final int tank, @Nonnull final FluidStack stack) {
        return true;
    }

    public void onContentsChanged() {

    }

    @Override
    public CompoundNBT serializeNBT() {
        final ListNBT nbtTagList = new ListNBT();
        for (int i = 0; i < this.stacks.size(); i++) {
            if (!this.stacks.get(i).isEmpty()) {
                final CompoundNBT fluidTag = new CompoundNBT();
                fluidTag.putInt("Tank", i);
                this.stacks.get(i).writeToNBT(fluidTag);
                nbtTagList.add(fluidTag);
            }
        }
        final CompoundNBT nbt = new CompoundNBT();
        nbt.put("Fluids", nbtTagList);
        nbt.putInt("Size", this.stacks.size());
        nbt.putIntArray("Capacity", this.capacity);
        return nbt;
    }

    public FluidStackHandler setCapacity(final int capacity, final int tank) {
        this.capacity[tank] = capacity;
        return this;
    }

    public FluidStackHandler setSize(final int tanks) {
        this.stacks = NonNullList.withSize(tanks, FluidStack.EMPTY);
        return this;
    }

    protected void onLoad() {
    }

    protected void validateSlotIndex(final int tank) {
        if (tank < 0 || tank >= this.stacks.size())
            throw new IndexOutOfBoundsException(
                    "Tank " + tank + " not in valid range - [0," + this.stacks.size() + ")");
    }
}