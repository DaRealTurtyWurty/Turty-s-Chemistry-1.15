package com.turtywurty.turtyschemistry.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FireResistantItemEntity extends ItemEntity {

    public FireResistantItemEntity(final EntityType<? extends ItemEntity> type, final World world) {
        super(type, world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isInvulnerableTo(final DamageSource source) {
        return source.isFireDamage();
    }
}
