package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.AgitatorContainer;
import com.turtywurty.turtyschemistry.common.fluidhandlers.AgitatorFluidStackHandler;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.util.AgitatorData;
import com.turtywurty.turtyschemistry.core.util.FluidStackHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

public class AgitatorTileEntity extends InventoryTile implements INamedContainerProvider {

	private int runningTime;
	private final int maxRunningTime = 250;

	public AgitatorFluidStackHandler fluidHandler = new AgitatorFluidStackHandler(6, 1000) {
		@Override
		public void onContentsChanged() {
			super.onContentsChanged();
			markDirty();
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
		loadRestorable(compound);
		super.read(compound);
		this.runningTime = compound.getInt("RunningTime");
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT tanks = this.fluidHandler.serializeNBT();
		compound.put("fluidinv", tanks);
		compound.putInt("RunningTime", this.runningTime);
		return super.write(compound);
	}

	public void loadRestorable(@Nullable CompoundNBT compound) {
		if (compound != null && compound.contains("fluidinv")) {
			CompoundNBT tanks = (CompoundNBT) compound.get("fluidinv");
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
					if (!bucket.getFluid().equals(Fluids.EMPTY)
							&& this.getFluidHandler().getFluidInTank(slot).isEmpty()) {
						this.getFluidHandler().fill(slot, new FluidStack(bucket.getFluid(), 1000), FluidAction.EXECUTE);
						this.getInventory().setStackInSlot(slot, new ItemStack(Items.BUCKET));
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
							&& new HashSet(recipeMap.values()).equals(new HashSet(tankFluidMap.values()));

					if (contentsEqual) {
						// System.out.println(this.runningTime);
						if (this.runningTime < this.maxRunningTime) {
							this.runningTime++;
							dirty = true;
						}

						if (this.runningTime >= this.maxRunningTime) {
							this.runningTime = 0;
							this.drainTanks();
							this.getFluidHandler().fill(5,
									new FluidStack(ForgeRegistries.FLUIDS
											.getValue(new ResourceLocation(recipe.getOutputfluid())), 1000),
									FluidAction.EXECUTE);
						}
					} else {
						this.runningTime = 0;
					}
				}
			}

			if (!this.getFluidHandler().getFluidInTank(5).isEmpty()
					&& this.getItemInSlot(7).getItem() instanceof BucketItem) {
				System.out.println("helo");
				ItemStack stack = FluidUtil.getFluidHandler(this.getItemInSlot(7)).map((handler) -> {
					handler.fill(this.getFluidHandler().getFluidInTank(5), FluidAction.EXECUTE);
					return handler.getContainer();
				}).orElse(this.getItemInSlot(7));
				this.getInventory().setStackInSlot(7, stack);
				this.getFluidHandler().drain(this.getFluidHandler().getFluidInTank(5), FluidAction.EXECUTE);
				dirty = true;
			}

			if (this.runningTime < 0) {
				this.runningTime = 0;
				dirty = true;
			}

			if (this.runningTime > this.maxRunningTime) {
				this.runningTime = this.maxRunningTime;
				dirty = true;
			}
		}

		if (dirty)
			this.markDirty();

	}

	private List<AgitatorData> getRecipes() {
		List<AgitatorData> aData = new ArrayList<AgitatorData>();
		TurtyChemistry.AGITATOR_DATA.getData().forEach((name, data) -> {
			aData.add(data);
		});
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

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".agitator");
	}

	@Nullable
	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new AgitatorContainer(windowID, playerInventory, this);
	}

	public int getMaxRunningTime() {
		return this.maxRunningTime;
	}

	public int getRunningTime() {
		return this.runningTime;
	}

	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}
}
