package com.turtywurty.turtyschemistry.common.blocks.particle_collider;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleColliderControllerTileEntity extends TileEntity implements ITickableTileEntity {

    public ParticleColliderControllerTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public static void mineRing(final World worldIn, final BlockPos startPos, final int radius) {

    }

    @Override
    public void tick() {

    }
}
