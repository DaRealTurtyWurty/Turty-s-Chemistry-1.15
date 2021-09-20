package com.turtywurty.turtyschemistry.common.blocks;

import java.util.Random;

import com.turtywurty.turtyschemistry.core.init.ParticleInit;
import com.turtywurty.turtyschemistry.core.init.PotionInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class GasBlock extends Block {

    private final ResourceLocation guiTexture;

    public GasBlock(final AbstractBlock.Properties properties, final ResourceLocation guiTextureIn) {
        super(properties);
        this.guiTexture = guiTextureIn;
    }

    @Override
    public void animateTick(final BlockState stateIn, final World worldIn, final BlockPos pos,
            final Random rand) {
        worldIn.addParticle(ParticleInit.GAS_PARTICLE.get(), (double) pos.getX() + (double) rand.nextFloat(),
                (double) pos.getY() + (double) rand.nextFloat(),
                (double) pos.getZ() + (double) rand.nextFloat(), rand.nextDouble(), rand.nextDouble(),
                rand.nextDouble());
    }

    public ResourceLocation getGuiTexture() {
        return this.guiTexture;
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos,
            final ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isAir(final BlockState state) {
        return true;
    }

    @Override
    public void onEntityCollision(final BlockState state, final World worldIn, final BlockPos pos,
            final Entity entityIn) {
        if (entityIn instanceof LivingEntity) {
            ((LivingEntity) entityIn)
                    .addPotionEffect(new EffectInstance(PotionInit.SUFFOCATING_EFFECT.get(), 200, 3));
        }
    }

    @Override
    public void randomTick(final BlockState state, final ServerWorld worldIn, final BlockPos pos,
            final Random rand) {
        if (worldIn.getBlockState(pos.up()).getBlock().equals(Blocks.AIR)
                || worldIn.getBlockState(pos.up()).getBlock().equals(Blocks.CAVE_AIR)) {
            worldIn.setBlockState(pos.up(), state);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}