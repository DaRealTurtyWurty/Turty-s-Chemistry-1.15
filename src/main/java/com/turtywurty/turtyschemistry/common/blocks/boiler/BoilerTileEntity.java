package com.turtywurty.turtyschemistry.common.blocks.boiler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.fluidhandlers.TankFluidStackHandler;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.util.FluidStackHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.registries.ForgeRegistries;

public class BoilerTileEntity extends InventoryTile {

    private int runningTime;
    private int maxRunningTime = 400;
    private int temperature;
    private Block outputGas = Blocks.AIR;

    private final TankFluidStackHandler fluidHandler = new TankFluidStackHandler(2, 1000) {
        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            BoilerTileEntity.this.markDirty();
        }
    };
    private final LazyOptional<FluidStackHandler> optional = LazyOptional.of(() -> this.fluidHandler);

    public BoilerTileEntity() {
        this(TileEntityTypeInit.BOILER.get());
    }

    public BoilerTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 3);
    }

    public void drainTanks() {
        for (int tank = 0; tank < getFluidHandler().getTanks(); tank++) {
            getFluidHandler().drain(tank, 10000, FluidAction.EXECUTE);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> cap, final Direction side) {
        super.getCapability(cap, side);
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? this.optional.cast()
                : super.getCapability(cap, side);
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".boiler");
    }

    public TankFluidStackHandler getFluidHandler() {
        return this.fluidHandler;
    }

    public int getMaxRunningTime() {
        return this.maxRunningTime;
    }

    public int getRunningTime() {
        return this.runningTime;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public void loadRestorable(@Nullable final CompoundNBT compound) {
        if (compound != null && compound.contains("FluidInv")) {
            final CompoundNBT tanks = (CompoundNBT) compound.get("FluidInv");
            this.fluidHandler.deserializeNBT(tanks);
        }
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        super.read(state, compound);
        loadRestorable(compound);
        this.runningTime = compound.getInt("RunningTime");
        this.maxRunningTime = compound.getInt("MaxRunningTime");
        this.temperature = compound.getInt("Temperature");
        this.outputGas = ForgeRegistries.BLOCKS
                .getValue(new ResourceLocation(compound.getString("OutputGas"))) instanceof Block
                        ? ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation(compound.getString("OutputGas")))
                        : BlockInit.HELIUM_GAS.get();
    }

    @Override
    public void remove() {
        super.remove();
        this.optional.invalidate();
    }

    public void setMaxRunningTime(final int maxRunningTime) {
        this.maxRunningTime = maxRunningTime;
    }

    public void setRunningTime(final int runningTime) {
        this.runningTime = runningTime;
    }

    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }

    @Override
    public void tick() {
        super.tick();
        boolean dirty = false;

        if (!this.world.isRemote) {
            if (getItemInSlot(0).getItem() instanceof BucketItem) {
                final BucketItem bucket = (BucketItem) getItemInSlot(0).getItem();
                if (!bucket.getFluid().equals(Fluids.EMPTY)
                        && getFluidHandler().getFluidInTank(0).isEmpty()) {
                    getFluidHandler().fill(0, new FluidStack(bucket.getFluid(), 1000), FluidAction.EXECUTE);
                    getInventory().setStackInSlot(0, new ItemStack(Items.BUCKET));
                    this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
                            Constants.BlockFlags.BLOCK_UPDATE);
                }
            }

            if (!this.fluidHandler.isEmpty()) {
                for (final BoilerRecipe recipe : getRecipes()) {
                    if (this.fluidHandler.getFluidInTank(0).getFluid() == ForgeRegistries.FLUIDS
                            .getValue(new ResourceLocation(recipe.getInputFluid()))) {
                        if (this.runningTime < this.maxRunningTime) {
                            this.runningTime++;
                            dirty = true;
                        }

                        if (this.runningTime >= this.maxRunningTime) {
                            this.runningTime = 0;
                            drainTanks();
                            getFluidHandler().fill(1,
                                    new FluidStack(ForgeRegistries.FLUIDS
                                            .getValue(new ResourceLocation(recipe.getOutputFluid())), 1000),
                                    FluidAction.EXECUTE);
                            getFluidHandler().fill(1,
                                    new FluidStack(ForgeRegistries.FLUIDS
                                            .getValue(new ResourceLocation(recipe.getOutputFluid2())), 1000),
                                    FluidAction.EXECUTE);
                            this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
                                    Constants.BlockFlags.BLOCK_UPDATE);
                        }
                    }
                }
            }
        }

        if (dirty) {
            markDirty();
        }
    }

    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        super.write(compound);
        final CompoundNBT tanks = this.fluidHandler.serializeNBT();
        compound.put("FluidInv", tanks);
        compound.putInt("RunningTime", this.runningTime);
        compound.putInt("MaxRunningTime", this.maxRunningTime);
        compound.putInt("Temperature", this.temperature);
        compound.putString("OutputGas", this.outputGas.getRegistryName().toString());
        return compound;
    }

    private List<BoilerRecipe> getRecipes() {
        final List<BoilerRecipe> rData = new ArrayList<>();
        ClientUtils.BOILER_DATA.getData().forEach((name, data) -> rData.add(data));
        return rData;
    }
}
