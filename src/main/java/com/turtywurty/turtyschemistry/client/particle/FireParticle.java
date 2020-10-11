package com.turtywurty.turtyschemistry.client.particle;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.turtywurty.turtyschemistry.core.init.ParticleInit;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireParticle extends SpriteTexturedParticle {

	public FireParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double ySpeedIn,
			FireColourData data) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, ySpeedIn, 0.0D);
		this.motionX = this.motionX * (double) 0.01F;
		this.motionY = this.motionY * (double) 0.01F + ySpeedIn;
		this.motionZ = this.motionZ * (double) 0.01F;
		this.posX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		this.posZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
		float f = (float) Math.random() * 0.4F + 0.6F;
		this.particleRed = ((float) (Math.random() * (double) 0.2F) + 0.8F) * data.getRed() * f;
		this.particleGreen = ((float) (Math.random() * (double) 0.2F) + 0.8F) * data.getGreen() * f;
		this.particleBlue = ((float) (Math.random() * (double) 0.2F) + 0.8F) * data.getBlue() * f;
		this.particleAlpha = data.getAlpha();
		this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
	}

	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	public float getScale(float scaleFactor) {
		float f = ((float) this.age + scaleFactor) / (float) this.maxAge;
		return this.particleScale * (1.0F - f * f * 0.2F);
	}

	public int getBrightnessForRender(float partialTick) {
		float f = ((float) this.age + partialTick) / (float) this.maxAge;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		int i = super.getBrightnessForRender(partialTick);
		int j = i & 255;
		int k = i >> 16 & 255;
		j = j + (int) (f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.age++ >= this.maxAge) {
			this.setExpired();
		} else {
			this.move(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double) 0.96F;
			this.motionY *= (double) 0.96F;
			this.motionZ *= (double) 0.96F;
			if (this.onGround) {
				this.motionX *= (double) 0.7F;
				this.motionZ *= (double) 0.7F;
			}

		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<FireColourData> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		public IAnimatedSprite getSpriteSet() {
			return this.spriteSet;
		}

		@Override
		public Particle makeParticle(FireColourData typeIn, World worldIn, double x, double y, double z, double xSpeed,
				double ySpeed, double zSpeed) {
			FireParticle fireParticle = new FireParticle(worldIn, x, y, z, ySpeed, typeIn);
			fireParticle.selectSpriteRandomly(this.spriteSet);
			return fireParticle;
		}
	}

	public static class FireColourData implements IParticleData {
		public static final IParticleData.IDeserializer<FireColourData> DESERIALIZER = new IParticleData.IDeserializer<FireColourData>() {
			public FireColourData deserialize(ParticleType<FireColourData> particleTypeIn, StringReader reader)
					throws CommandSyntaxException {
				reader.expect(' ');
				float red = (float) reader.readDouble();
				reader.expect(' ');
				float green = (float) reader.readDouble();
				reader.expect(' ');
				float blue = (float) reader.readDouble();
				reader.expect(' ');
				float alpha = (float) reader.readDouble();
				return new FireColourData(red, green, blue, alpha);
			}

			public FireColourData read(ParticleType<FireColourData> particleTypeIn, PacketBuffer buffer) {
				return new FireColourData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
						buffer.readFloat());
			}
		};

		private final float red;
		private final float green;
		private final float blue;
		private final float alpha;

		public FireColourData(float redIn, float greenIn, float blueIn, float alphaIn) {
			this.red = redIn;
			this.green = greenIn;
			this.blue = blueIn;
			this.alpha = MathHelper.clamp(alphaIn, 0.01f, 4.0f);
		}

		@Override
		public void write(PacketBuffer buffer) {
			buffer.writeFloat(this.red);
			buffer.writeFloat(this.green);
			buffer.writeFloat(this.blue);
			buffer.writeFloat(this.alpha);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String getParameters() {
			return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(this.getType()),
					this.red, this.green, this.blue, this.alpha);
		}

		@Override
		public ParticleType<FireColourData> getType() {
			return ParticleInit.FIRE_PARTICLE.get();
		}

		@OnlyIn(Dist.CLIENT)
		public float getRed() {
			return this.red;
		}

		@OnlyIn(Dist.CLIENT)
		public float getGreen() {
			return this.green;
		}

		@OnlyIn(Dist.CLIENT)
		public float getBlue() {
			return this.blue;
		}

		@OnlyIn(Dist.CLIENT)
		public float getAlpha() {
			return this.alpha;
		}
	}
}