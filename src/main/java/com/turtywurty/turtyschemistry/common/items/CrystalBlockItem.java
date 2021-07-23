package com.turtywurty.turtyschemistry.common.items;

import com.turtywurty.turtyschemistry.core.init.EntityTypeInit;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CrystalBlockItem extends BlockItem {

	public CrystalBlockItem(final Block blockIn, final Properties builder) {
		super(blockIn, builder);
	}

	@Override
	public Entity createEntity(final World world, final Entity location, final ItemStack itemstack) {
		if (!world.isRemote) {
			ItemEntity item = (ItemEntity) EntityTypeInit.FIRE_RES_ITEM.get().spawn((ServerWorld) world, itemstack,
					null, location.getPosition(), SpawnReason.NATURAL, false, false);
			item.setItem(itemstack);
			item.setMotion(location.getMotion().getX(), location.getMotion().getX(), location.getMotion().getX());
			if (location instanceof ItemEntity) {
				item.setPosition(location.getPosX(), location.getPosY(), location.getPosZ());
				item.setPickupDelay(((ItemEntity) location).pickupDelay);
				item.setCustomName(location.getCustomName());
				item.setGlowing(location.isGlowing());
				item.setInvisible(location.isInvisible());
				item.setInvulnerable(location.isInvulnerable());
				item.setNoGravity(location.hasNoGravity());
				item.setOwnerId(((ItemEntity) location).getOwnerId());
				item.setSilent(location.isSilent());
				item.setThrowerId(((ItemEntity) location).getThrowerId());
				item.setWorld(location.getEntityWorld());
			}

			return item;
		}
		return super.createEntity(world, location, itemstack);
	}

	@Override
	public boolean hasCustomEntity(final ItemStack stack) {
		return true;
	}
}
