package com.turtywurty.turtyschemistry.common.blocks.researcher;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.researcher.ResearcherBlock.Processor;
import com.turtywurty.turtyschemistry.common.items.BlueprintItem;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants.BlockFlags;

public class ResearcherTileEntity extends InventoryTile {

	private Processor processor;
	private int processingTime = 0;
	private static final int DEFAULT_PROCESS_TIME = 500;
	private String currentRecipe = "test uwu";
	public boolean moveLeft = true;
	public float time;

	public ResearcherTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 2);
	}

	public ResearcherTileEntity() {
		this(TileEntityTypeInit.RESEARCHER.get());
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".researcher");
	}

	@Override
	public void tick() {
		super.tick();
		boolean dirty = false;
		this.processor = this.getBlockState().get(ResearcherBlock.PROCESSOR);
		if (!this.world.isRemote) {
			if (this.getItemInSlot(0).getItem() instanceof BlueprintItem && this.getItemInSlot(1).isEmpty()
					&& this.processingTime == 0 && !this.getItemInSlot(0).getOrCreateTag().contains("Recipe")) {
				this.processingTime++;
				this.getItemInSlot(0).shrink(1);
				ItemStack blueprint = new ItemStack(ItemInit.BLUEPRINT.get());
				blueprint.getOrCreateTag().putString("Recipe", this.currentRecipe);
				blueprint.getOrCreateTag().putInt("Progress", 0);
				blueprint = this.getInventory().insertItem(1, blueprint, false);
				if (!blueprint.isEmpty()) {
					this.world.addEntity(new ItemEntity(this.world, this.pos.getX() + 0.5D, this.pos.getY() + 1.5D,
							this.pos.getZ() + 0.5D, blueprint));
				}
				dirty = true;
				this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
						BlockFlags.BLOCK_UPDATE);
			}

			if (this.processingTime > 0 && this.processingTime < this.getMaxProcessingTime()) {
				this.processingTime++;
				dirty = true;
			}

			this.setBlueprintStage(11, this.getMaxProcessingTime());

			if (this.processingTime >= this.getMaxProcessingTime()) {
				this.processingTime = 0;
				dirty = true;
				this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
						BlockFlags.BLOCK_UPDATE);
			}
		}

		if (dirty) {
			this.markDirty();
		}
	}

	public void setBlueprintStage(int max, float stages) {
		float distBetweenPoints = (stages + 1) / max;
		int closestPointValue = (int) Math.floor(this.processingTime / distBetweenPoints);
		if (closestPointValue != this.getItemInSlot(1).getOrCreateTag().getInt("Progress")
				&& closestPointValue > this.getItemInSlot(0).getOrCreateTag().getInt("Progress")) {
			this.getItemInSlot(1).getOrCreateTag().putInt("Progress", closestPointValue);
			this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), BlockFlags.BLOCK_UPDATE);
		}
	}

	public int getProcessingTime() {
		return this.processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	public int getMaxProcessingTime() {
		return DEFAULT_PROCESS_TIME / (this.processor.ordinal() + 1);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.processingTime = compound.getInt("ProcessingTime");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("ProcessingTime", this.processingTime);
		return compound;
	}
}
