package com.turtywurty.turtyschemistry.common.blocks.agitator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.common.fluidhandlers.TankFluidStackHandler;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.util.FluidStackHandler;

import net.minecraft.block.BlockState;
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

    private static final int MAX_RUNNING_TIME = 250;

    private int runningTime;

    private AgitatorType type = AgitatorType.LIQUID_ONLY;

    public TankFluidStackHandler fluidHandler = new TankFluidStackHandler(8, 1000) {
        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            AgitatorTileEntity.this.markDirty();
        }
    };

    private final LazyOptional<FluidStackHandler> optional = LazyOptional.of(() -> this.fluidHandler);

    public AgitatorTileEntity() {
        this(TileEntityTypeInit.AGITATOR.get());
    }

    public AgitatorTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 8);
    }

    public static int getMaxRunningTime() {
        return MAX_RUNNING_TIME;
    }

    public void drainTanks() {
        for (int tank = 0; tank < getFluidHandler().getTanks(); tank++) {
            getFluidHandler().drain(tank, 10000, FluidAction.EXECUTE);
        }
    }

    public AgitatorType getAgitatorType() {
        return this.type;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap,
            @Nullable final Direction side) {
        super.getCapability(cap, side);
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? this.optional.cast()
                : super.getCapability(cap, side);
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".agitator");
    }

    public FluidStackHandler getFluidHandler() {
        return this.fluidHandler;
    }

    public int getRunningTime() {
        return this.runningTime;
    }

    public void loadRestorable(@Nullable final CompoundNBT compound) {
        if (compound != null && compound.contains("FluidInv")) {
            final CompoundNBT tanks = (CompoundNBT) compound.get("FluidInv");
            this.fluidHandler.deserializeNBT(tanks);
        }
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        loadRestorable(compound);
        super.read(state, compound);
        this.runningTime = compound.getInt("RunningTime");
        this.type = AgitatorType.byName(compound.getString("Type"));
    }

    @Override
    public void remove() {
        super.remove();
        this.optional.invalidate();
    }

    public void setAgitatorType(final AgitatorType type) {
        this.type = type;
    }

    public void setRunningTime(final int runningTime) {
        this.runningTime = runningTime;
    }

    @Override
    public void tick() {
        boolean dirty = false;

        if (!this.world.isRemote) {
            for (int slot = 0; slot < getSize() - 2; slot++) {
                if (getItemInSlot(slot).getItem() instanceof BucketItem) {
                    final BucketItem bucket = (BucketItem) getItemInSlot(slot).getItem();
                    if (!bucket.getFluid().equals(Fluids.EMPTY)
                            && getFluidHandler().getFluidInTank(slot).isEmpty()) {
                        getFluidHandler().fill(slot, new FluidStack(bucket.getFluid(), 1000),
                                FluidAction.EXECUTE);
                        getInventory().setStackInSlot(slot, new ItemStack(Items.BUCKET));
                        this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
                                Constants.BlockFlags.BLOCK_UPDATE);
                    }
                }
            }

            if (!getFluidHandler().isEmpty()) {
                for (final AgitatorData recipe : getRecipes()) {
                    final Map<Integer, Fluid> recipeMap = Maps.newHashMap();
                    final Map<Integer, Fluid> tankFluidMap = Maps.newHashMap();
                    recipeMap.put(0,
                            ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid1())));
                    recipeMap.put(1,
                            ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid2())));
                    recipeMap.put(2,
                            ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid3())));
                    recipeMap.put(3,
                            ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid4())));
                    recipeMap.put(4,
                            ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid5())));
                    recipeMap.put(5, Fluids.EMPTY);

                    for (int tank = 0; tank < getFluidHandler().getTanks(); tank++) {
                        tankFluidMap.put(tank, getFluidHandler().getFluidInTank(tank).getFluid());
                    }

                    final boolean contentsEqual = recipeMap.keySet().equals(tankFluidMap.keySet())
                            && new HashSet<>(recipeMap.values()).equals(new HashSet<>(tankFluidMap.values()));

                    if (contentsEqual) {
                        if (this.runningTime < MAX_RUNNING_TIME) {
                            this.runningTime++;
                            dirty = true;
                        }

                        if (this.runningTime >= MAX_RUNNING_TIME) {
                            this.runningTime = 0;
                            drainTanks();
                            getFluidHandler().fill(5,
                                    new FluidStack(ForgeRegistries.FLUIDS
                                            .getValue(new ResourceLocation(recipe.getOutputfluid())), 1000),
                                    FluidAction.EXECUTE);
                            this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
                                    Constants.BlockFlags.BLOCK_UPDATE);
                        }
                    } else {
                        this.runningTime = 0;
                    }
                }
            }

            if (!getFluidHandler().getFluidInTank(5).isEmpty()
                    && getItemInSlot(7).getItem() instanceof BucketItem) {
                final ItemStack stack = FluidUtil.getFluidHandler(getItemInSlot(7)).map(handler -> {
                    handler.fill(getFluidHandler().getFluidInTank(5), FluidAction.EXECUTE);
                    return handler.getContainer();
                }).orElse(getItemInSlot(7));
                getInventory().setStackInSlot(7, stack);
                getFluidHandler().drain(getFluidHandler().getFluidInTank(5), FluidAction.EXECUTE);
                this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
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

        if (dirty) {
            markDirty();
        }

    }

    @Nonnull
    @Override
    public CompoundNBT write(final CompoundNBT compound) {
        final CompoundNBT tanks = this.fluidHandler.serializeNBT();
        compound.put("FluidInv", tanks);
        compound.putInt("RunningTime", this.runningTime);
        compound.putString("Type", this.type.getString());
        return super.write(compound);
    }

    private List<AgitatorData> getRecipes() {
        final List<AgitatorData> aData = new ArrayList<>();
        ClientUtils.AGITATOR_DATA.getData().forEach((name, data) -> aData.add(data));
        return aData;
    }
}
