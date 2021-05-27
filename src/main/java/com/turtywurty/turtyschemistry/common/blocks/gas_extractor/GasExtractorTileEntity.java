package com.turtywurty.turtyschemistry.common.blocks.gas_extractor;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.core.init.BlockInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

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

	public GasExtractorTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public GasExtractorTileEntity() {
		this(TileEntityTypeInit.GAS_EXTRACTOR.get());
	}

	@Override
	public void tick() {
		if (!this.world.isRemote && this.gasStored < 3000
				&& this.world.getBlockState(pos.down()).getBlock().equals(BlockInit.HELIUM_GAS.get())) {
			this.world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), Constants.BlockFlags.DEFAULT);
			this.gasStored += 1000;
			this.markDirty();
			this.world.markAndNotifyBlock(pos, this.world.getChunkAt(pos), this.getBlockState(), this.getBlockState(),
					Constants.BlockFlags.BLOCK_UPDATE);
		}
	}

	public int getGasStored() {
		return this.gasStored;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.gasStored = compound.getInt("gasStored");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("gasStored", getGasStored());
		return super.write(compound);
	}

	@Override
	public void remove() {
		super.remove();
		this.invalidateCaps();
		this.warnInvalidBlock();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);

		return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(packet.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		this.read(tag);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity playerIn) {
		return new GasExtractorContainer(windowId, playerInv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".gas_extractor");
	}
}
