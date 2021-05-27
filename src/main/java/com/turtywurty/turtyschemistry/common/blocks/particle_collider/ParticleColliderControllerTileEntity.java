package com.turtywurty.turtyschemistry.common.blocks.particle_collider;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleColliderControllerTileEntity extends TileEntity implements ITickableTileEntity {

	public ParticleColliderControllerTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void tick() {
		
	}
	
	public static void mineRing(World worldIn, BlockPos startPos, int radius) {
		
	}
}
