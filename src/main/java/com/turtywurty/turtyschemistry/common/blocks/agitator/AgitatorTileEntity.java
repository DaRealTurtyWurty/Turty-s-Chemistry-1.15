package com.turtywurty.turtyschemistry.common.blocks.agitator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.fluidhandlers.TankFluidStackHandler;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.util.FluidStackHandler;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.registries.ForgeRegistries;

public class AgitatorTileEntity extends InventoryTile {

	private int runningTime;
	private static final int MAX_RUNNING_TIME = 250;
	private AgitatorType type = AgitatorType.LIQUID_ONLY;

	public TankFluidStackHandler fluidHandler = new TankFluidStackHandler(8, 1000) {
		@Override
		public void onContentsChanged() {
			super.onContentsChanged();
			AgitatorTileEntity.this.markDirty();
		}
	};

	private LazyOptional<FluidStackHandler> optional = LazyOptional.of(() -> this.fluidHandler);

	public AgitatorTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 8);
	}

	public AgitatorTileEntity() {
		this(TileEntityTypeInit.AGITATOR.get());
	}

	public FluidStackHandler getFluidHandler() {
		return this.fluidHandler;
	}

	@Override
	public void read(CompoundNBT compound) {
		this.loadRestorable(compound);
		super.read(compound);
		this.runningTime = compound.getInt("RunningTime");
		this.type = AgitatorType.byName(compound.getString("Type"));
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT tanks = this.fluidHandler.serializeNBT();
		compound.put("FluidInv", tanks);
		compound.putInt("RunningTime", this.runningTime);
		compound.putString("Type", this.type.getName());
		return super.write(compound);
	}

	public void loadRestorable(@Nullable CompoundNBT compound) {
		if (compound != null && compound.contains("FluidInv")) {
			CompoundNBT tanks = (CompoundNBT) compound.get("FluidInv");
			this.fluidHandler.deserializeNBT(tanks);
		}
	}

	@Override
	public void tick() {
		boolean dirty = false;

		if (!this.world.isRemote) {
			for (int slot = 0; slot < this.getSize() - 2; slot++) {
				if (this.getItemInSlot(slot).getItem() instanceof BucketItem) {
					BucketItem bucket = ((BucketItem) this.getItemInSlot(slot).getItem());
					if (!bucket.getFluid().equals(Fluids.EMPTY) && this.getFluidHandler().getFluidInTank(slot).isEmpty()) {
						this.getFluidHandler().fill(slot, new FluidStack(bucket.getFluid(), 1000), FluidAction.EXECUTE);
						this.getInventory().setStackInSlot(slot, new ItemStack(Items.BUCKET));
						this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
								Constants.BlockFlags.BLOCK_UPDATE);
					}
				}
			}

			if (!this.getFluidHandler().isEmpty()) {
				for (AgitatorData recipe : this.getRecipes()) {
					Map<Integer, Fluid> recipeMap = Maps.newHashMap();
					Map<Integer, Fluid> tankFluidMap = Maps.newHashMap();
					recipeMap.put(0, ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid1())));
					recipeMap.put(1, ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid2())));
					recipeMap.put(2, ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid3())));
					recipeMap.put(3, ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid4())));
					recipeMap.put(4, ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid5())));
					recipeMap.put(5, Fluids.EMPTY);

					for (int tank = 0; tank < this.getFluidHandler().getTanks(); tank++) {
						tankFluidMap.put(tank, this.getFluidHandler().getFluidInTank(tank).getFluid());
					}

					boolean contentsEqual = recipeMap.keySet().equals(tankFluidMap.keySet())
							&& new HashSet<>(recipeMap.values()).equals(new HashSet<>(tankFluidMap.values()));

					if (contentsEqual) {
						if (this.runningTime < MAX_RUNNING_TIME) {
							this.runningTime++;
							dirty = true;
						}

						if (this.runningTime >= MAX_RUNNING_TIME) {
							this.runningTime = 0;
							this.drainTanks();
							this.getFluidHandler().fill(5,
									new FluidStack(
											ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getOutputfluid())),
											1000),
									FluidAction.EXECUTE);
							this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
									Constants.BlockFlags.BLOCK_UPDATE);
						}
					} else {
						this.runningTime = 0;
					}
				}
			}

			if (!this.getFluidHandler().getFluidInTank(5).isEmpty()
					&& this.getItemInSlot(7).getItem() instanceof BucketItem) {
				ItemStack stack = FluidUtil.getFluidHandler(this.getItemInSlot(7)).map(handler -> {
					handler.fill(this.getFluidHandler().getFluidInTank(5), FluidAction.EXECUTE);
					return handler.getContainer();
				}).orElse(this.getItemInSlot(7));
				this.getInventory().setStackInSlot(7, stack);
				this.getFluidHandler().drain(this.getFluidHandler().getFluidInTank(5), FluidAction.EXECUTE);
				this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
						Constants.BlockFlags.BLOCK_UPDATE);
				dirty = true;
			}

			if (this.runningTime < 0) {
				this.runningTime = 0;
				dirty = true;
			}

			if (this.runningTime > MAX_RUNNING_TIME) {
				this.runningTime = MAX_RUNNING_TIME;
				dirty = true;
			}
		}

		if (dirty)
			this.markDirty();

	}

	private List<AgitatorData> getRecipes() {
		List<AgitatorData> aData = new ArrayList<>();
		TurtyChemistry.AGITATOR_DATA.getData().forEach((name, data) -> aData.add(data));
		return aData;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		super.getCapability(cap, side);
		return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? this.optional.cast()
				: super.getCapability(cap, side);
	}

	public void drainTanks() {
		for (int tank = 0; tank < this.getFluidHandler().getTanks(); tank++) {
			this.getFluidHandler().drain(tank, 10000, FluidAction.EXECUTE);
		}
	}

	@Override
	public void remove() {
		super.remove();
		this.optional.invalidate();
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".agitator");
	}

	public static int getMaxRunningTime() {
		return MAX_RUNNING_TIME;
	}

	public int getRunningTime() {
		return this.runningTime;
	}

	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	public AgitatorType getAgitatorType() {
		return this.type;
	}

	public void setAgitatorType(AgitatorType type) {
		this.type = type;
	}
}
