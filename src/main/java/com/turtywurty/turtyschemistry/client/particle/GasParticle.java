package com.turtywurty.turtyschemistry.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GasParticle extends SpriteTexturedParticle {

	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(final IAnimatedSprite spriteIn) {
			this.spriteSet = spriteIn;
		}

		@Override
		public Particle makeParticle(final BasicParticleType typeIn, final ClientWorld worldIn, final double x,
				final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed) {
			GasParticle underwaterparticle = new GasParticle(worldIn, x, y, z);
			underwaterparticle.selectSpriteRandomly(this.spriteSet);
			return underwaterparticle;
		}
	}

	private GasParticle(final ClientWorld worldIn, final double xSpeed, final double ySpeed, final double zSpeed) {
		super(worldIn, xSpeed, ySpeed - 0.125D, zSpeed);
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		setSize(0.01F, 0.01F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
		this.maxAge = (int) (16.0D / (this.rand.nextInt() * 0.8D + 0.2D));
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.maxAge-- <= 0) {
			setExpired();
		} else {
			move(this.motionX, this.motionY, this.motionZ);
		}
	}
}