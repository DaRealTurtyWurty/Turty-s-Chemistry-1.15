package com.turtywurty.turtyschemistry.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.ConduitTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.JigsawTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityUtils {

	public static boolean canInsertIntoContainer(IInventory container, ItemStack stack) {
		for (int index = 0; index < container.getSizeInventory(); index++) {
			if (container.isItemValidForSlot(index, stack)) {
				return true;
			}
		}
		return false;
	}

	public static List<ItemStack> getInvItems(IInventory inv) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (int index = 0; index < inv.getSizeInventory(); index++) {
			items.add(inv.getStackInSlot(index));
		}

		return items;
	}

	@Nullable
	public static IInventory getValidContainer(World world, Direction side, BlockPos pos, BlockState state,
			ItemStack stack) {
		if (world.getBlockState(pos.offset(side)).hasTileEntity()) {
			TileEntity tile = world.getTileEntity(pos.offset(side));
			if (tile instanceof IInventory) {
				IInventory inv = (IInventory) tile;
				return inv;
			}
		}
		return null;
	}

	public static boolean insertItem(IInventory inv, ItemStack stack) {
		ItemStack itemstack = stack.copy();

		if (canInsertIntoContainer(inv, stack)) {
			List<Integer> freeSlots = getFreeSlotsForStack(inv, stack);
			for (int index : freeSlots) {
				int availableSpace = inv.getInventoryStackLimit() - inv.getStackInSlot(index).getCount();
				int neededSpace = stack.getCount();
				if (!inv.getStackInSlot(index).isEmpty()) {
					if (availableSpace >= neededSpace) {
						inv.getStackInSlot(index).grow(neededSpace);
						return true;
					} else {
						inv.getStackInSlot(index).setCount(inv.getInventoryStackLimit());
						stack.shrink(availableSpace - neededSpace);
						return true;
					}
				} else {
					if (availableSpace >= neededSpace) {
						inv.setInventorySlotContents(index, new ItemStack(itemstack.getItem(), neededSpace));
						return true;
					} else {
						inv.setInventorySlotContents(index,
								new ItemStack(itemstack.getItem(), inv.getInventoryStackLimit()));
						stack.shrink(availableSpace - neededSpace);
						return true;
					}
				}
			}
		}
		return false;
	}

	public static List<Integer> getFreeSlotsForStack(IInventory inv, ItemStack stack) {
		List<Integer> slots = new ArrayList<Integer>();
		for (int index = 0; index < inv.getSizeInventory(); index++) {
			if (inv.isItemValidForSlot(index, stack)
					&& inv.getStackInSlot(index).getCount() != inv.getInventoryStackLimit()) {
				slots.add(index);
			}
		}

		return slots;
	}

	public static boolean isValidBiomoassGasification(GasifierTileEntity tile, Direction lineDirection) {
		BlockPos startPos = tile.getPos();
		World world = tile.getWorld();

		TileEntity conveyorTile = world.getTileEntity(startPos.offset(lineDirection, 1));
		if (conveyorTile instanceof /* TODO: ConveyorTileEntity */ ConduitTileEntity) {
			TileEntity hopperTile = world.getTileEntity(startPos.offset(lineDirection, 2));
			if (hopperTile instanceof /* TODO: RealHopperTileEntity */ HopperTileEntity) {
				for (int retormSize = 1; retormSize < 6; retormSize++) {
					TileEntity gasRetormulaterTile = world
							.getTileEntity(startPos.offset(lineDirection.getOpposite(), retormSize));
					if (!(gasRetormulaterTile instanceof /* TODO: GasRetormulatorTileEntity */ FurnaceTileEntity)) {
						return false;
					}
				}

				TileEntity gasCooler1 = world.getTileEntity(startPos.offset(lineDirection, 7));
				TileEntity gasCooler2 = world.getTileEntity(startPos.offset(lineDirection, 8));
				if (gasCooler1 instanceof /* TODO: GasCoolerTileEntity */ BeaconTileEntity
						&& gasCooler2 instanceof /* TODO: GasCoolerTileEntity */ BeaconTileEntity) {
					// TODO: flare system, gas holder, gas engine checks
					TileEntity flareSystem1 = world
							.getTileEntity(startPos.offset(lineDirection, 9).offset(lineDirection.rotateYCCW(), 1));
					TileEntity flareSystem2 = world.getTileEntity(startPos.offset(lineDirection, 9));
					if (flareSystem1 instanceof /* TODO: FlareSystemTileEntity */ JigsawTileEntity
							&& flareSystem2 instanceof /* TODO: FlareSystemTileEntity */ JigsawTileEntity) {

					}
					return true;
				}
			}
		}

		return false;
	}
}
