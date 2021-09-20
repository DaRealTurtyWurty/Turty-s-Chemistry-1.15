package com.turtywurty.turtyschemistry.common.blocks.researcher;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.researcher.ResearcherBlock.Processor;
import com.turtywurty.turtyschemistry.common.items.BlueprintItem;
import com.turtywurty.turtyschemistry.common.tileentity.InventoryTile;
import com.turtywurty.turtyschemistry.core.init.ItemInit;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants.BlockFlags;

public class ResearcherTileEntity extends InventoryTile {

    private static final int DEFAULT_PROCESS_TIME = 500;
    private Processor processor;
    private int processingTime = 0;
    private final String currentRecipe = "test uwu";
    public boolean moveLeft = true;
    public float time;

    public ResearcherTileEntity() {
        this(TileEntityTypeInit.RESEARCHER.get());
    }

    public ResearcherTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 2);
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".researcher");
    }

    public int getMaxProcessingTime() {
        return DEFAULT_PROCESS_TIME / (this.processor.ordinal() + 1);
    }

    public int getProcessingTime() {
        return this.processingTime;
    }

    @Override
    public void read(final BlockState state, final CompoundNBT compound) {
        super.read(state, compound);
        this.processingTime = compound.getInt("ProcessingTime");
    }

    public void setBlueprintStage(final int max, final float stages) {
        final float distBetweenPoints = (stages + 1) / max;
        final int closestPointValue = (int) Math.floor(this.processingTime / distBetweenPoints);
        if (closestPointValue != getItemInSlot(1).getOrCreateTag().getInt("Progress")
                && closestPointValue > getItemInSlot(0).getOrCreateTag().getInt("Progress")) {
            getItemInSlot(1).getOrCreateTag().putInt("Progress", closestPointValue);
            this.world.notifyBlockUpdate(this.pos, getBlockState(), getBlockState(), BlockFlags.BLOCK_UPDATE);
        }
    }

    public void setProcessingTime(final int processingTime) {
        this.processingTime = processingTime;
    }

    @Override
    public void tick() {
        super.tick();
        boolean dirty = false;
        this.processor = getBlockState().get(ResearcherBlock.PROCESSOR);
        if (!this.world.isRemote) {
            if (getItemInSlot(0).getItem() instanceof BlueprintItem && getItemInSlot(1).isEmpty()
                    && this.processingTime == 0 && !getItemInSlot(0).getOrCreateTag().contains("Recipe")) {
                this.processingTime++;
                getItemInSlot(0).shrink(1);
                ItemStack blueprint = new ItemStack(ItemInit.BLUEPRINT.get());
                blueprint.getOrCreateTag().putString("Recipe", this.currentRecipe);
                blueprint.getOrCreateTag().putInt("Progress", 0);
                blueprint = getInventory().insertItem(1, blueprint, false);
                if (!blueprint.isEmpty()) {
                    this.world.addEntity(new ItemEntity(this.world, this.pos.getX() + 0.5D,
                            this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, blueprint));
                }
                dirty = true;
                this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
                        BlockFlags.BLOCK_UPDATE);
            }

            if (this.processingTime > 0 && this.processingTime < getMaxProcessingTime()) {
                this.processingTime++;
                dirty = true;
            }

            setBlueprintStage(11, getMaxProcessingTime());

            if (this.processingTime >= getMaxProcessingTime()) {
                this.processingTime = 0;
                dirty = true;
                this.world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(),
                        BlockFlags.BLOCK_UPDATE);
            }
        }

        if (dirty) {
            markDirty();
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);
        compound.putInt("ProcessingTime", this.processingTime);
        return compound;
    }
}
