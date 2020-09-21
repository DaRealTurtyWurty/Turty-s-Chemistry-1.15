package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.common.blocks.GasifierBlock;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GasifierTileEntity extends TileEntity implements ITickableTileEntity, IProductionLineTile {

	private int timer;
	private boolean requiresUpdate = true;
	private final int size = 10;
	private final String[] versions = new String[] { "biomass", "oil", "coal", "" };
	private String version = versions[3];
	private int runningTime, maxRunningTime;

	protected LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

	public GasifierTileEntity(TileEntityType<?> tileEntityIn) {
		super(tileEntityIn);
	}

	public GasifierTileEntity() {
		this(TileEntityTypeInit.GASIFIER.get());
	}

	@Override
	public void tick() {
		doUpdates();

		if (this.getItemInSlot(0).getItem().equals(ItemInit.GREEN_ALGAE.get())) {
			this.version = versions[0];
		}

		if (hasValidNeighbours(this.getWorld(), this.getPos())) {
			ItemEntity item = new ItemEntity(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
			item.setItem(new ItemStack(Items.APPLE));
			this.world.addEntity(item);
		}
	}

	private void moveStackIntoMainInventory(ItemStack stack) {
		List<ItemStack> inventory = new ArrayList<ItemStack>();
		for (int slot = 1; slot < this.getSize() - 1; slot++) {
			final ItemStack item = this.getItemInSlot(slot);
			inventory.add(item);
		}
	}

	private void doUpdates() {
		if (this.world != null) {
			if (this.requiresUpdate) {
				this.updateTile();
				this.requiresUpdate = false;
			}
		}
		timer++;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

	public LazyOptional<IItemHandler> getHandler() {
		return handler;
	}

	private IItemHandler createHandler() {
		return new ItemStackHandler(size) {
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
			}
		};
	}

	public ItemStack getItemInSlot(int slot) {
		return handler.map(inventory -> inventory.getStackInSlot(slot)).orElse(ItemStack.EMPTY);
	}

	public ItemStack insertItem(int slot, ItemStack stack) {
		ItemStack itemIn = stack.copy();
		stack.shrink(itemIn.getCount());
		requiresUpdate = true;
		return handler.map(inventory -> inventory.insertItem(slot, itemIn, false)).orElse(ItemStack.EMPTY);
	}

	public ItemStack extractItem(int slot) {
		int count = getItemInSlot(slot).getCount();
		requiresUpdate = true;
		return handler.map(inventory -> inventory.extractItem(slot, count, false)).orElse(ItemStack.EMPTY);
	}

	public int getSize() {
		return size;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		ListNBT list = compound.getList("Items", 10);
		for (int x = 0; x < list.size(); ++x) {
			CompoundNBT nbt = list.getCompound(x);
			int r = nbt.getByte("Slot") & 255;
			handler.ifPresent(inventory -> {
				int invslots = inventory.getSlots();
				if (r >= 0 && r < invslots) {
					inventory.insertItem(r, ItemStack.read(nbt), false);
				}
			});
		}
		requiresUpdate = true;

		this.version = compound.getString("Version");
		this.runningTime = compound.getInt("RunningTime");
		this.maxRunningTime = compound.getInt("MaxRunningTime");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ListNBT list = new ListNBT();
		handler.ifPresent(inventory -> {
			int slots = inventory.getSlots();
			for (int x = 0; x < slots; ++x) {
				ItemStack stack = inventory.getStackInSlot(x);
				if (!stack.isEmpty()) {
					CompoundNBT nbt = new CompoundNBT();
					nbt.putByte("Slot", (byte) x);
					stack.write(nbt);
					list.add(nbt);
				}
			}
		});
		if (!list.isEmpty()) {
			compound.put("Items", list);
		}

		compound.putString("Version", this.version);
		compound.putInt("RunningTime", this.runningTime);
		compound.putInt("MaxRunningTime", this.maxRunningTime);

		return compound;
	}

	public void updateTile() {
		requestModelDataUpdate();
		this.markDirty();
		if (this.getWorld() != null) {
			this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	@Nonnull
	public CompoundNBT getUpdateTag() {
		return this.serializeNBT();
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		deserializeNBT(tag);
	}

	@Override
	public boolean hasValidNeighbours(World world, BlockPos pos, Block... exludingBlocks) {
		boolean hopper = false;
		for (Direction direction : HORIZONTAL_DIRECTIONS) {
			BlockPos offset = pos.offset(direction);
			while (world.getBlockState(offset).getBlock() instanceof GasifierBlock) {
				offset.offset(direction);
			}
			// new hopper
			if (world.getBlockState(offset).getBlock().equals(Blocks.HOPPER)
					&& world.getBlockState(offset.up()).getBlock().equals(Blocks.HOPPER)) {
				// check that hopper has valid neighbours
				hopper = true;
			}
		}

		if (hopper) {
			for (Direction direction : HORIZONTAL_DIRECTIONS) {
				BlockPos offset = pos.offset(direction);
				while (world.getBlockState(offset).getBlock() instanceof GasifierBlock) {
					offset.offset(direction);
				}

				// gas retormulator
				if (world.getBlockState(offset).getBlock().equals(Blocks.GLASS)
						&& world.getBlockState(offset.offset(direction)).getBlock().equals(Blocks.GLASS)
						&& world.getBlockState(offset.offset(direction, 2)).getBlock().equals(Blocks.GLASS)
						&& world.getBlockState(offset.offset(direction, 3)).getBlock().equals(Blocks.GLASS)) {
					// check that gas retormulator has valid neighbours
					return true;
				}
			}
		}
		return false;
	}
}
