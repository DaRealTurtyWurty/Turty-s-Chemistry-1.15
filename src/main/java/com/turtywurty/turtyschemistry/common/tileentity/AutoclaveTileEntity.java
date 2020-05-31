package com.turtywurty.turtyschemistry.common.tileentity;

import javax.annotation.Nullable;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.AutoclaveBlock;
import com.turtywurty.turtyschemistry.common.container.AutoclaveContainer;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AutoclaveTileEntity extends LockableLootTileEntity
		implements INamedContainerProvider, ITickableTileEntity, IClearable, ISidedInventory {

	private static final int[] SLOTS = new int[] { 0, 1, 2 };

	protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
	public int steamTime;
	public int claveTime;
	public int claveTimeTotal;

	public AutoclaveTileEntity(TileEntityType<?> typeIn) {
		super(typeIn);
	}

	public AutoclaveTileEntity() {
		this(TileEntityTypeInit.AUTOCLAVE.get());
	}

	private boolean isSteaming() {
		return this.steamTime > 0;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);
		this.steamTime = compound.getInt("SteamTime");
		this.claveTime = compound.getInt("ClaveTime");
		this.claveTimeTotal = compound.getInt("ClaveTimeTotal");
	}

	protected int getSteamTime(ItemStack stack) {
		return 50;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("SteamTime", this.steamTime);
		compound.putInt("ClaveTime", this.claveTime);
		compound.putInt("ClaveTimeTotal", this.claveTimeTotal);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	@Override
	public void tick() {
		boolean markDirty = false;
		boolean running = this.isSteaming();
		if (isSteaming()) {
			System.out.println("runnning");
			this.steamTime--;
		}
		if (!this.world.isRemote) {
			ItemStack stack = this.items.get(1);
			if (this.isSteaming() || !stack.isEmpty()) {
				System.out.println("running and not empty");
				if (!this.isSteaming() && isItemValidForSlot(1, stack)) {
					System.out.println("not running and valid");
					this.steamTime = this.getSteamTime(stack);
					if (this.isSteaming()) {
						System.out.println("running2");
						markDirty = true;
						if (stack.hasContainerItem()) {
							this.items.set(1, stack.getContainerItem());
						} else if (!stack.isEmpty()) {
							stack.shrink(1);
							if (stack.isEmpty()) {
								this.items.set(1, stack.getContainerItem());
							}
						}
					}
				}

				if (this.isSteaming() && isItemValidForSlot(1, stack)) {
					System.out.println("running and valid");
					++this.claveTime;
					if (this.claveTime == this.claveTimeTotal) {
						System.out.println("clave time equal to total");
						this.claveTime = 0;
						ItemStack itemstack = stack;
						itemstack.setDamage(stack.getMaxDamage());
						this.setInventorySlotContents(1, itemstack);
						markDirty = true;
					}
				} else {
					System.out.println("not running and/or not valid");
					this.claveTime = 0;
				}
			} else if (!this.isSteaming() && this.claveTime > 0) {
				System.out.println("not running and clave time greater than 0");
				this.claveTime = MathHelper.clamp(this.claveTime - 1, 0, this.claveTimeTotal);
			}

			if (running != this.isSteaming()) {
				System.out.println("has changed running state");
				markDirty = true;
				this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AutoclaveBlock.PROCESSING,
						Boolean.valueOf(this.isSteaming())), 3);
			}
		}

		if (markDirty) {
			System.out.println("marking as dirty");
			this.markDirty();
		}
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public void setItems(NonNullList<ItemStack> itemsIn) {
		this.items = itemsIn;
	}

	protected int getClaveTime() {
		return 50;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(TurtyChemistry.MOD_ID + ".container.autoclave");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory playerInv) {
		return new AutoclaveContainer(id, playerInv, this);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if (direction == Direction.DOWN && index == 1) {
			Item item = stack.getItem();
			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.claveTimeTotal = this.getClaveTime();
			this.claveTime = 0;
			this.markDirty();
		}

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers = net.minecraftforge.items.wrapper.SidedInvWrapper
			.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(
			net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
		if (!this.removed && facing != null
				&& capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == Direction.UP)
				return handlers[0].cast();
			else if (facing == Direction.DOWN)
				return handlers[1].cast();
			else
				return handlers[2].cast();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.isDamaged();
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	// we can sync a TileEntity from the server to all tracking clients by calling
	// world.notifyBlockUpdate

	// when that happens, this method is called on the server to generate a packet
	// to send to the client
	// if you have lots of data, it's a good idea to keep track of which data has
	// changed since the last time
	// this TE was synced, and then only send the changed data;
	// this reduces the amount of packets sent, which is good
	// we only have one value to sync so we'll just write everything into the NBT
	// again
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);

		return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
	}

	/*
	 * this method gets called on the client when it receives the packet that was
	 * sent in getUpdatePacket()
	 */
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(packet.getNbtCompound());
	}

	// called to generate NBT for a syncing packet when a client loads a chunk that
	// this TE is in
	@Override
	public CompoundNBT getUpdateTag() {
		// we want to tell the client about as much data as it needs to know
		// since it doesn't know any data at this point, we can usually just defer to
		// write() above
		// if you have data that would be written to the disk but the client doesn't
		// ever need to know,
		// you can just sync the need-to-know data instead of calling write()
		// there's an equivalent method for reading the update tag but it just defaults
		// to read() anyway
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	private static void calculateExperience(PlayerEntity player, int chance, float amount) {
		if (amount == 0.0F) {
			chance = 0;
		} else if (amount < 1.0F) {
			int i = MathHelper.floor((float) chance * amount);
			if (i < MathHelper.ceil((float) chance * amount)
					&& Math.random() < (double) ((float) chance * amount - (float) i)) {
				++i;
			}

			chance = i;
		}

		while (chance > 0) {
			int j = ExperienceOrbEntity.getXPSplit(chance);
			chance -= j;
			player.world.addEntity(new ExperienceOrbEntity(player.world, player.getPosX(), player.getPosY() + 0.5D,
					player.getPosZ() + 0.5D, j));
		}

	}

	@Override
	public void remove() {
		super.remove();
		for (int x = 0; x < handlers.length; x++)
			handlers[x].invalidate();
	}
}
