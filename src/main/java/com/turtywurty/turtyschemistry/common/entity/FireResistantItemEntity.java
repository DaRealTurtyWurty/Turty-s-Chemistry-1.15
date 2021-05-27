package com.turtywurty.turtyschemistry.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FireResistantItemEntity extends ItemEntity {

	public FireResistantItemEntity(EntityType<? extends ItemEntity> type, World world) {
		super(type, world);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source.isFireDamage();
	}
	
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
