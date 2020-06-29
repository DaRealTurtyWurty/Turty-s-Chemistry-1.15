package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.AgitatorContainer;
import com.turtywurty.turtyschemistry.common.fluidhandlers.AgitatorFluidStackHandler;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;
import com.turtywurty.turtyschemistry.core.util.AgitatorData;
import com.turtywurty.turtyschemistry.core.util.FluidStackHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class AgitatorTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public AgitatorFluidStackHandler handler = new AgitatorFluidStackHandler(6, 1) {
		@Override
		public void onContentsChanged() {
			super.onContentsChanged();
			markDirty();
		}
	};
	private LazyOptional<FluidStackHandler> optional = LazyOptional.of(() -> handler);

	public AgitatorTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public AgitatorTileEntity() {
		this(TileEntityTypeInit.AGITATOR.get());
	}

	public FluidStackHandler getHandler() {
		return handler;
	}

	@Override
	public void read(CompoundNBT compound) {
		loadRestorable(compound);
		super.read(compound);
	}

	public void loadRestorable(@Nullable CompoundNBT compound) {
		if (compound != null && compound.contains("fluidinv")) {
			CompoundNBT tanks = (CompoundNBT) compound.get("fluidinv");
			handler.deserializeNBT(tanks);
		}
	}

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT tanks = handler.serializeNBT();
		compound.put("fluidinv", tanks);
		return super.write(compound);
	}

	@Override
	public void tick() {
		int tank = 0;
		if (!this.getHandler().isEmpty()) {
			for (FluidStack stack : this.getHandler().getContents()) {
				if (this.getHandler().isFluidValid(tank, stack) && !this.getHandler().getFluidInTank(tank).isEmpty()) {
					// System.out.println(this.getHandler().getFluidInTank(tank).getFluid().getRegistryName()
					// + " " + tank);
				}
				tank++;
			}
		}
	}

	private List<AgitatorData> getRecipes() {
		List<AgitatorData> aData = new ArrayList<AgitatorData>();
		TurtyChemistry.AGITATOR_DATA.getData().forEach((name, data) -> {
			aData.add(data);
		});
		return aData;
	}

	private boolean isValidRecipe() {
		List<Fluid> recipeFluids = new ArrayList<Fluid>();

		for (AgitatorData recipe : this.getRecipes()) {
			Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(recipe.getInputfluid1()));
			recipeFluids.add(fluid);
		}

		for (Fluid fluid : (Fluid[]) this.getHandler().getContents().stream().map(stack -> stack.getFluid())
				.toArray()) {
			for (Fluid fluid1 : recipeFluids) {
				if (fluid1 != fluid) {
					return false;
				}
			}
		}
		return true;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? optional.cast()
				: super.getCapability(cap, side);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("fluidinv", handler.serializeNBT());
		return new SUpdateTileEntityPacket(getPos(), 1, nbt);
	}

	@Override
	@Nonnull
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		nbt.put("fluidinv", handler.serializeNBT());
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		handler.deserializeNBT((CompoundNBT) packet.getNbtCompound().get("fluidinv"));
	}

	@Override
	public void remove() {
		super.remove();
		optional.invalidate();
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		super.handleUpdateTag(tag);
		handler.deserializeNBT(tag.getCompound("fluidinv"));
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
}
