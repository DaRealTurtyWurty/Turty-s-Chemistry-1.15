package com.turtywurty.turtyschemistry.common.blocks.electrolyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.fluidhandlers.TankFluidStackHandler;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class ElectrolyzerTileEntity extends InventoryTile {

	protected int runningTime, maxRunningTime = 40, maxInput = 10000, maxOutput1 = 10000, maxOutput2 = 10000;
	protected boolean converting;
	protected DumpMode dumpingInput = DumpMode.NONE, dumpingOutput1 = DumpMode.NONE, dumpingOutput2 = DumpMode.NONE;
	public final TankFluidStackHandler fluidInventory;
	protected LazyOptional<TankFluidStackHandler> fluidHandler;

	public ElectrolyzerTileEntity() {
		this(TileEntityTypeInit.ELECTROLYZER.get());
	}

	public ElectrolyzerTileEntity(final TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 3);
		this.fluidInventory = createFluidHandler();
		this.fluidHandler = LazyOptional.of(() -> this.fluidInventory);
	}

	public TankFluidStackHandler createFluidHandler() {
		return new TankFluidStackHandler(3, this.maxInput, this.maxOutput1, this.maxOutput2);
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> cap, final Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return this.fluidHandler.cast();
		return super.getCapability(cap, side);
	}

	@Override
	public void read(final BlockState state, final CompoundNBT compound) {
		super.read(state, compound);
		this.dumpingInput = DumpMode.valueOf(compound.getString("DumpingInput"));
		this.dumpingOutput1 = DumpMode.valueOf(compound.getString("DumpingOutput1"));
		this.dumpingOutput2 = DumpMode.valueOf(compound.getString("DumpingOutput2"));
		this.maxInput = compound.getInt("MaxInput");
		this.maxOutput1 = compound.getInt("MaxOutput1");
		this.maxOutput2 = compound.getInt("MaxOutput2");
		this.runningTime = compound.getInt("RunningTime");
		this.maxRunningTime = compound.getInt("MaxRunningTime");
	}

	@Override
	public void tick() {
		super.tick();
		boolean isDirty = false;
		if (!this.world.isRemote) {
			extractBucket();
			if (dump()) {
				isDirty = true;
			}

			if (validateInput()) {
				final ElectrolyzerRecipe recipe = getRecipe();
				if(recipe == null) return;

				if (++this.runningTime >= this.maxRunningTime) {
					this.runningTime = 0;
					this.converting = false;
					fillAndDrain(recipe);
				}
			}

			exportToCanisters();
		}

		if (isDirty) {
			updateTile();
		}
	}

	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		compound.putString("DumpingInput", this.dumpingInput.name());
		compound.putString("DumpingOutput1", this.dumpingOutput1.name());
		compound.putString("DumpingOutput2", this.dumpingOutput2.name());
		compound.putInt("MaxInput", this.maxInput);
		compound.putInt("MaxOutput1", this.maxOutput1);
		compound.putInt("MaxOutput2", this.maxOutput2);
		compound.putInt("RunningTime", this.runningTime);
		compound.putInt("MaxRunningTime", this.maxRunningTime);
		return super.write(compound);
	}

	protected ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".electrolyzer");
	}

	private boolean dump() {
		boolean isDirty = false;
		if (!this.fluidInventory.getFluidInTank(0).isEmpty() && (this.dumpingInput == DumpMode.DUMP
				|| this.dumpingInput == DumpMode.DUMP_EXCESS && this.fluidInventory.getFluidInTank(0)
						.getAmount() >= this.fluidInventory.getTankCapacity(0))) {
			this.fluidInventory.drain(0, new FluidStack(this.fluidInventory.getFluidInTank(0).getFluid(), 1),
					FluidAction.EXECUTE);
			isDirty = true;
		}

		if (!this.fluidInventory.getFluidInTank(1).isEmpty() && (this.dumpingOutput1 == DumpMode.DUMP
				|| this.dumpingOutput1 == DumpMode.DUMP_EXCESS && this.fluidInventory.getFluidInTank(1)
						.getAmount() >= this.fluidInventory.getTankCapacity(1))) {
			this.fluidInventory.drain(1, new FluidStack(this.fluidInventory.getFluidInTank(1).getFluid(), 1),
					FluidAction.EXECUTE);
			isDirty = true;
		}

		if (!this.fluidInventory.getFluidInTank(2).isEmpty() && (this.dumpingOutput2 == DumpMode.DUMP
				|| this.dumpingOutput2 == DumpMode.DUMP_EXCESS && this.fluidInventory.getFluidInTank(2)
						.getAmount() >= this.fluidInventory.getTankCapacity(2))) {
			this.fluidInventory.drain(2, new FluidStack(this.fluidInventory.getFluidInTank(2).getFluid(), 1),
					FluidAction.EXECUTE);
			isDirty = true;
		}

		return isDirty;
	}

	private void exportToCanisters() {
		for (int tank = 1; tank <= 2; tank++) {
			if (getItemInSlot(tank).getItem().equals(ItemInit.GAS_CANISTER_S.get())
					|| getItemInSlot(tank).getItem().equals(ItemInit.GAS_CANISTER_L.get())) {
				final CompoundNBT nbt = getItemInSlot(tank).getOrCreateChildTag("BlockEntityTag");
				if (nbt.contains("GasName")) {
					if (nbt.getInt("GasStored") < nbt.getInt("MaxAmount")
							&& !this.fluidInventory.getFluidInTank(1).isEmpty()) {
						nbt.putInt("GasStored", nbt.getInt("GasStored") + 1);
						this.fluidInventory.drain(tank, new FluidStack(this.fluidInventory.getFluidInTank(tank), 1),
								FluidAction.EXECUTE);
					}
				} else {
					nbt.putString("GasName",
							this.fluidInventory.getFluidInTank(tank).getFluid().getRegistryName().toString());
				}
			}
		}
	}

	private boolean extractBucket() {
		if (getItemInSlot(0).getItem() instanceof BucketItem
				&& this.fluidInventory.getFluidInTank(0).getAmount() <= this.maxInput - 1000) {
			final BucketItem bucket = (BucketItem) getItemInSlot(0).getItem();
			if (bucket.getFluid() != Fluids.EMPTY && bucket.getFluid() != null
					&& (bucket.getFluid() == this.fluidInventory.getFluidInTank(0).getFluid()
							|| this.fluidInventory.getFluidInTank(0).isEmpty())
					&& this.fluidInventory.fill(0, new FluidStack(bucket.getFluid(), 1000), FluidAction.EXECUTE) != 0) {
				this.converting = true;
				getInventory().setStackInSlot(0, Items.BUCKET.getDefaultInstance());
				return true;
			}
		}
		return false;
	}

	private boolean fillAndDrain(ElectrolyzerRecipe recipe) {
		if (this.fluidInventory.getFluidInTank(0).getAmount() > recipe.ratio0
				&& this.fluidInventory.getFluidInTank(1).getAmount() < this.maxOutput1
				&& this.fluidInventory.getFluidInTank(2).getAmount() < this.maxOutput2) {
			this.fluidInventory.drain(0,
					new FluidStack(this.fluidInventory.getFluidInTank(0).getFluid(), recipe.ratio0),
					FluidAction.EXECUTE);
			this.fluidInventory.fill(1, new FluidStack(recipe.output1, recipe.ratio1), FluidAction.EXECUTE);
			this.fluidInventory.fill(2, new FluidStack(recipe.output2, recipe.ratio2), FluidAction.EXECUTE);
			this.converting = true;
			return true;
		}
		this.converting = false;
		return false;
	}

	@Nullable
	private ElectrolyzerRecipe getRecipe() {
		Optional<ElectrolyzerRecipe> recipeOptional;
		try {
			recipeOptional = getRecipes().stream().filter(recipe -> this.fluidInventory.isFluidEqual(0, recipe.input))
					.findFirst();
		} catch (final NullPointerException exception) {
			// throw new JsonParseException("Error parsing JSON: " +
			// exception.getLocalizedMessage());
			return null;
		}

		return recipeOptional.isPresent() ? recipeOptional.get() : null;
	}

	private List<ElectrolyzerRecipe> getRecipes() {
		final List<ElectrolyzerRecipe> recipes = new ArrayList<>();
		ClientUtils.ELECTROLYZER_DATA.getData().forEach((loc, recipe) -> recipes.add(recipe));
		return recipes;
	}

	private boolean isFluidEqualOrEmpty(final FluidStack fluid1, final FluidStack fluid2) {
		return fluid1.isFluidEqual(fluid2) || fluid1.isEmpty() || fluid2.isEmpty();
	}

	private boolean validateInput() {
		final ElectrolyzerRecipe recipe = getRecipe();
		List<FluidStack> fluids;
		if (recipe != null) {
			fluids = Arrays.asList(recipe.input, recipe.output1, recipe.output2);
		} else {
			fluids = Arrays.asList(FluidStack.EMPTY, FluidStack.EMPTY, FluidStack.EMPTY);
		}

		final boolean[] valid = new boolean[fluids.size()];
		for (int tank = 0; tank < fluids.size(); tank++) {
			valid[tank] = isFluidEqualOrEmpty(fluids.get(tank), this.fluidInventory.getFluidInTank(tank));
		}

		boolean invalid = false;
		for (final boolean v : valid) {
			if (!v) {
				invalid = true;
			}
		}

		if (invalid) {
			this.converting = false;
		}
		return !invalid;
	}
}
