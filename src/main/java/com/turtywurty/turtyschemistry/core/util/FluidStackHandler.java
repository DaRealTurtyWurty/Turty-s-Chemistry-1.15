package com.turtywurty.turtyschemistry.core.util;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FluidStackHandler implements IMultiTank, INBTSerializable<CompoundNBT> {

	protected NonNullList<FluidStack> stacks;
	protected int[] capacity;

	public FluidStackHandler(int tanks, int... capacity) {
		this.stacks = NonNullList.withSize(tanks, FluidStack.EMPTY);
		this.capacity = capacity;
	}

	public FluidStackHandler(int... capacity) {
		this(1, capacity);
	}

	public FluidStackHandler setCapacity(int capacity, int tank) {
		this.capacity[tank] = capacity;
		return this;
	}

	public FluidStackHandler setSize(int tanks) {
		stacks = NonNullList.withSize(tanks, FluidStack.EMPTY);
		return this;
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return this.stacks.get(tank);
	}

	public NonNullList<FluidStack> getContents() {
		return stacks;
	}

	@Override
	public int getTanks() {
		return stacks.size();
	}

	@Override
	public int getTankCapacity(int tank) {
		return capacity[tank];
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
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
		return true;
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
	public int fill(FluidStack resource, FluidAction action) {
		int totalFill = 0;
		FluidStack rem = resource.copy();
		for (int i = 0; i < getTanks(); i++) {
			if (!rem.isEmpty()) {
				int singleFill = fill(i, rem, action);
				totalFill += singleFill;
				rem = new FluidStack(resource, rem.getAmount() - singleFill);
			}
		}
		return totalFill;
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
	public FluidStack drain(FluidStack resource, FluidAction action) {
		FluidStack totalDrain = FluidStack.EMPTY;
		FluidStack toDrain = resource.copy();
		for (int i = 0; i < getTanks(); i++) {
			if (!toDrain.isEmpty()) {
				FluidStack singleDrain = drain(i, toDrain, action);
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
	public FluidStack drain(int maxDrain, FluidAction action) {
		FluidStack totalDrainStack = FluidStack.EMPTY;
		int toDrain = maxDrain;
		for (int i = 0; i < getTanks(); i++) {
			if (toDrain > 0) {
				FluidStack singleDrain = drain(i, toDrain, action);
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
	public int fill(int tank, FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !isFluidValid(tank, resource)) {
			return 0;
		}
		FluidStack existingFluid = getFluidInTank(tank);
		if (!existingFluid.isFluidEqual(resource) && !existingFluid.isEmpty()) {
			return 0;
		}
		validateSlotIndex(tank);
		if (existingFluid.isEmpty()) {
			if (action.execute()) {
				stacks.set(tank, new FluidStack(resource, Math.min(capacity[tank], resource.getAmount())));
				onContentsChanged();
			}
			return resource.getAmount();
		} else {
			int filled = capacity[tank] - existingFluid.getAmount();
			if (resource.getAmount() < filled) {
				if (action.execute()) {
					existingFluid.grow(resource.getAmount());
				}
				filled = resource.getAmount();
			} else {
				if (action.execute()) {
					existingFluid.setAmount(capacity[tank]);
				}
			}
			if (filled > 0 && action.execute()) {
				onContentsChanged();
			}
			return filled;
		}
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
	public FluidStack drain(int tank, FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !resource.isFluidEqual(getFluidInTank(tank)) || !isFluidValid(tank, resource)) {
			return FluidStack.EMPTY;
		}
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
	public FluidStack drain(int tank, int maxDrain, FluidAction action) {
		if (maxDrain <= 0) {
			return FluidStack.EMPTY;
		}
		validateSlotIndex(tank);
		FluidStack existingFluid = getFluidInTank(tank);
		if (existingFluid.isEmpty()) {
			return FluidStack.EMPTY;
		}
		int drained = maxDrain;
		if (existingFluid.getAmount() <= maxDrain) {
			drained = existingFluid.getAmount();
			if (action.execute()) {
				stacks.set(tank, FluidStack.EMPTY);
				onContentsChanged();
			}
		} else {
			if (action.execute()) {
				stacks.set(tank, new FluidStack(existingFluid, existingFluid.getAmount() - drained));
				onContentsChanged();
			}
		}
		return new FluidStack(existingFluid, drained);
	}

	@Override
	public CompoundNBT serializeNBT() {
		ListNBT nbtTagList = new ListNBT();
		for (int i = 0; i < stacks.size(); i++) {
			if (!stacks.get(i).isEmpty()) {
				CompoundNBT fluidTag = new CompoundNBT();
				fluidTag.putInt("Tank", i);
				stacks.get(i).writeToNBT(fluidTag);
				nbtTagList.add(fluidTag);
			}
		}
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("Fluids", nbtTagList);
		nbt.putInt("Size", stacks.size());
		nbt.putIntArray("Capacity", capacity);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : stacks.size());
		ListNBT tagList = nbt.getList("Fluids", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.size(); i++) {
			CompoundNBT fluidTags = tagList.getCompound(i);
			int tank = fluidTags.getInt("Tank");
			if (tank >= 0 && tank < stacks.size()) {
				stacks.set(tank, FluidStack.loadFluidStackFromNBT(fluidTags));
			}
			setCapacity(nbt.contains("Capacity", Constants.NBT.TAG_INT) ? nbt.getInt("Capacity") : capacity[tank],
					tank);
		}
		onLoad();
	}

	protected void onLoad() {
	}

	public void onContentsChanged() {

	}

	protected void validateSlotIndex(int tank) {
		if (tank < 0 || tank >= stacks.size()) {
			throw new IndexOutOfBoundsException("Tank " + tank + " not in valid range - [0," + stacks.size() + ")");
		}
	}

	public boolean isEmpty() {
		return stacks.stream().allMatch(FluidStack::isEmpty);
	}
	
	public boolean isFluidEqual(int tank, FluidStack fluid) {
		return this.getFluidInTank(tank).isFluidEqual(fluid);
	}
}