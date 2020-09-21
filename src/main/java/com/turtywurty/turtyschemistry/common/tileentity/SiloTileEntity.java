package com.turtywurty.turtyschemistry.common.tileentity;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.SiloBlock;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

public class SiloTileEntity extends InventoryTile {

	private int currentPage = 0;

	public SiloTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 624);
	}

	public SiloTileEntity() {
		this(TileEntityTypeInit.SILO.get());
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".silo");
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.currentPage = compound.getInt("CurrentPage");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT nbt = new CompoundNBT();
		super.write(nbt);
		nbt.putInt("CurrentPage", this.currentPage);
		return nbt;
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

	public void setCurrentPage(int currentPage) {
		System.out.println(String.format("Changed page from %s to %s", this.currentPage, currentPage));
		if (currentPage > 5) {
			currentPage = 5;
		} else if (currentPage < 0) {
			currentPage = 0;
		}

		this.currentPage = currentPage;
		this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
				Constants.BlockFlags.DEFAULT);
		this.markDirty();
	}

	@Override
	public void tick() {
		super.tick();

		if (this.world != null && !this.world.isRemote) {
			final IItemHandler inventory = this.getHandler().orElse(this.createHandler());
			final int levels = 7;
			final int maxItems = inventory.getSlots() * 64;
			int itemCount = 0;

			for (int i = 0; i < inventory.getSlots(); i++) {
				if (!inventory.getStackInSlot(i).isEmpty()) {
					itemCount += inventory.getStackInSlot(i).getCount();
				}
			}

			if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 0
					&& this.getBlockState().get(SiloBlock.LEVEL) != 0) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 0));
			} else if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 1
					&& this.getBlockState().get(SiloBlock.LEVEL) != 1) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 1));
			} else if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 2
					&& this.getBlockState().get(SiloBlock.LEVEL) != 2) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 2));
			} else if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 3
					&& this.getBlockState().get(SiloBlock.LEVEL) != 3) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 3));
			} else if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 4
					&& this.getBlockState().get(SiloBlock.LEVEL) != 4) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 4));
			} else if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 5
					&& this.getBlockState().get(SiloBlock.LEVEL) != 5) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 5));
			} else if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 6
					&& this.getBlockState().get(SiloBlock.LEVEL) != 6) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 6));
			} else if (Math.floor(((double) itemCount / (double) maxItems) * levels) == 7
					&& this.getBlockState().get(SiloBlock.LEVEL) != 7) {
				this.world.setBlockState(this.pos, this.getBlockState().with(SiloBlock.LEVEL, 7));
			}
		}
	}
}
