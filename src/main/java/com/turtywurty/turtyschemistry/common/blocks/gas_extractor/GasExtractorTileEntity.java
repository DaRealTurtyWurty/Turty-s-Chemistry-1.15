package com.turtywurty.turtyschemistry.common.blocks.gas_extractor;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class GasExtractorTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	private int gasStored = 0;

	public GasExtractorTileEntity() {
		this(TileEntityTypeInit.GAS_EXTRACTOR.get());
	}

	public GasExtractorTileEntity(final TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public Container createMenu(final int windowId, final PlayerInventory playerInv, final PlayerEntity playerIn) {
		return new GasExtractorContainer(windowId, playerInv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".gas_extractor");
	}

	public int getGasStored() {
		return this.gasStored;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);

		return new SUpdateTileEntityPacket(getPos(), 1, nbt);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
		this.read(state, tag);
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
		this.read(this.world.getBlockState(packet.getPos()), packet.getNbtCompound());
	}

	@Override
	public void read(final BlockState state, final CompoundNBT compound) {
		super.read(state, compound);
		this.gasStored = compound.getInt("gasStored");
	}

	@Override
	public void remove() {
		super.remove();
		invalidateCaps();
		warnInvalidBlock();
	}

	@Override
	public void tick() {
		if (!this.world.isRemote && this.gasStored < 3000
				&& this.world.getBlockState(this.pos.down()).getBlock().equals(BlockInit.HELIUM_GAS.get())) {
			this.world.setBlockState(this.pos.down(), Blocks.AIR.getDefaultState(), Constants.BlockFlags.DEFAULT);
			this.gasStored += 1000;
			markDirty();
			this.world.markAndNotifyBlock(this.pos, this.world.getChunkAt(this.pos), getBlockState(), getBlockState(),
					Constants.BlockFlags.BLOCK_UPDATE, 0);
		}
	}

	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		compound.putInt("gasStored", getGasStored());
		return super.write(compound);
	}
}
