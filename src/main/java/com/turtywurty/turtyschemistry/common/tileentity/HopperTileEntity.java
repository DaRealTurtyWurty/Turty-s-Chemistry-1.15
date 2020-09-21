package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.List;
import java.util.stream.Collectors;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.container.HopperContainer;
import com.turtywurty.turtyschemistry.core.init.TileEntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class HopperTileEntity extends InventoryTile implements IProductionLineTile, INamedContainerProvider {

	public HopperTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn, 54);
	}

	public HopperTileEntity() {
		this(TileEntityTypeInit.HOPPER.get());
	}

	@Override
	public boolean hasValidNeighbours(World world, BlockPos pos, Block... exludingBlocks) {
		return false;
	}

	@Override
	public void tick() {
		// Pickup above and in items
		for (ItemEntity itemEntity : collectItems()) {
			System.out.println(insertDrop(itemEntity));
		}
	}

	public boolean insertDrop(ItemEntity itemEntity) {
		boolean captured = false;
		ItemStack stack1 = itemEntity.getItem().copy();
		for (int index = 0; index < this.getInventory().getSlots(); index++) {
			stack1 = this.getInventory().insertItem(index, stack1, false);
		}

		if (stack1.isEmpty()) {
			captured = true;
			itemEntity.remove();
		} else {
			itemEntity.setItem(stack1);
		}
		return captured;
	}

	public List<ItemEntity> collectItems() {
		return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 32.0D, 32.0D, 32.0D).toBoundingBoxList().stream()
				.flatMap((boundingBox) -> {
					return this.world.getEntitiesWithinAABB(ItemEntity.class,
							boundingBox.offset(this.pos.getX(), this.pos.getY(), this.pos.getZ())).stream();
				}).collect(Collectors.toList());
	}

	@Override
	public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity player) {
		return new HopperContainer(windowID, playerInv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + TurtyChemistry.MOD_ID + ".hopper");
	}
}
