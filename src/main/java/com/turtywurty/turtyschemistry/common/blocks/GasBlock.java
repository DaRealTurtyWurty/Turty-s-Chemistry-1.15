package com.turtywurty.turtyschemistry.common.blocks;

import java.util.Random;

import com.turtywurty.turtyschemistry.core.init.ParticleInit;
import com.turtywurty.turtyschemistry.core.init.PotionInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class GasBlock extends Block {

	public GasBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean isAir(BlockState state) {
		return true;
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof LivingEntity) {
			((LivingEntity) entityIn).addPotionEffect(new EffectInstance(PotionInit.SUFFOCATING_EFFECT.get(), 200, 3));
		}
	}

	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		worldIn.addParticle(ParticleInit.GAS_PARTICLE.get(), (double) pos.getX() + (double) rand.nextFloat(),
				(double) pos.getY() + (double) rand.nextFloat(), (double) pos.getZ() + (double) rand.nextFloat(),
				rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (worldIn.getBlockState(pos.up()).getBlock().equals(Blocks.AIR)
				|| worldIn.getBlockState(pos.up()).getBlock().equals(Blocks.CAVE_AIR)) {
			worldIn.setBlockState(pos.up(), state);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}
}