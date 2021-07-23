package com.turtywurty.turtyschemistry.client.particle;

public class FireParticle /* extends SpriteTexturedParticle */ {

	/*
	 * public static class Factory implements IParticleFactory<FireColourData> {
	 * private final IAnimatedSprite spriteSet;
	 * 
	 * public Factory(final IAnimatedSprite sprite) { this.spriteSet = sprite; }
	 * 
	 * public IAnimatedSprite getSpriteSet() { return this.spriteSet; }
	 * 
	 * @Override public Particle makeParticle(final FireColourData typeIn, final
	 * ClientWorld worldIn, final double x, final double y, final double z, final
	 * double xSpeed, final double ySpeed, final double zSpeed) { FireParticle
	 * fireParticle = new FireParticle(worldIn, x, y, z, ySpeed, typeIn);
	 * fireParticle.selectSpriteRandomly(this.spriteSet); return fireParticle; } }
	 * 
	 * public static class FireColourData implements IParticleData { public static
	 * final IParticleData.IDeserializer<FireParticle.FireColourData> DESERIALIZER =
	 * new IParticleData.IDeserializer<FireParticle.FireColourData>() {
	 * 
	 * @Override public FireColourData deserialize(final
	 * ParticleType<FireParticle.FireColourData> particleTypeIn, final StringReader
	 * reader) throws CommandSyntaxException { reader.expect(' '); float r = (float)
	 * reader.readDouble(); reader.expect(' '); float g = (float)
	 * reader.readDouble(); reader.expect(' '); float b = (float)
	 * reader.readDouble(); reader.expect(' '); float a = (float)
	 * reader.readDouble(); return new FireColourData(r, g, b, a); }
	 * 
	 * @Override public FireColourData read(final
	 * ParticleType<FireParticle.FireColourData> particleTypeIn, final PacketBuffer
	 * buffer) { return new FireColourData(buffer.readFloat(), buffer.readFloat(),
	 * buffer.readFloat(), buffer.readFloat()); } };
	 * 
	 * private final float red; private final float green; private final float blue;
	 * private final float alpha;
	 * 
	 * public FireColourData(final float redIn, final float greenIn, final float
	 * blueIn, final float alphaIn) { this.red = redIn; this.green = greenIn;
	 * this.blue = blueIn; this.alpha = MathHelper.clamp(alphaIn, 0.01f, 4.0f); }
	 * 
	 * public float getAlpha() { return this.alpha; }
	 * 
	 * public float getBlue() { return this.blue; }
	 * 
	 * public float getGreen() { return this.green; }
	 * 
	 * @SuppressWarnings("deprecation")
	 * 
	 * @Override public String getParameters() { return String.format(Locale.ROOT,
	 * "%s %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getKey(getType()), this.red,
	 * this.green, this.blue, this.alpha); }
	 * 
	 * public float getRed() { return this.red; }
	 * 
	 * @Override public ParticleType<FireColourData> getType() { return
	 * ParticleInit.FIRE_PARTICLE.get(); }
	 * 
	 * @Override public void write(final PacketBuffer buffer) {
	 * buffer.writeFloat(this.red); buffer.writeFloat(this.green);
	 * buffer.writeFloat(this.blue); buffer.writeFloat(this.alpha); } }
	 * 
	 * public FireParticle(final ClientWorld worldIn, final double xCoordIn, final
	 * double yCoordIn, final double zCoordIn, final double ySpeedIn, final
	 * FireColourData data) { super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D,
	 * ySpeedIn, 0.0D); this.motionX = this.motionX * 0.01F; this.motionY =
	 * this.motionY * 0.01F + ySpeedIn; this.motionZ = this.motionZ * 0.01F;
	 * this.posX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
	 * this.posY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
	 * this.posZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F; float f
	 * = (float) Math.random() * 0.4F + 0.6F; this.particleRed = ((float)
	 * (this.rand.nextInt() * (double) 0.2F) + 0.8F) * data.getRed() * f;
	 * this.particleGreen = ((float) (this.rand.nextInt() * (double) 0.2F) + 0.8F) *
	 * data.getGreen() * f; this.particleBlue = ((float) (this.rand.nextInt() *
	 * (double) 0.2F) + 0.8F) * data.getBlue() * f; this.particleAlpha =
	 * data.getAlpha(); this.maxAge = (int) (8.0D / (this.rand.nextInt() * 0.8D +
	 * 0.2D)) + 4; }
	 * 
	 * @Override public int getBrightnessForRender(final float partialTick) { float
	 * f = (this.age + partialTick) / this.maxAge; f = MathHelper.clamp(f, 0.0F,
	 * 1.0F); int i = super.getBrightnessForRender(partialTick); int j = i & 255;
	 * int k = i >> 16 & 255; j = j + (int) (f * 15.0F * 16.0F); if (j > 240) { j =
	 * 240; }
	 * 
	 * return j | k << 16; }
	 * 
	 * @Override public IParticleRenderType getRenderType() { return
	 * IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }
	 * 
	 * @Override public float getScale(final float scaleFactor) { float f =
	 * (this.age + scaleFactor) / this.maxAge; return this.particleScale * (1.0F - f
	 * * f * 0.2F); }
	 * 
	 * @Override public void move(final double x, final double y, final double z) {
	 * setBoundingBox(getBoundingBox().offset(x, y, z)); resetPositionToBB(); }
	 * 
	 * @Override public void tick() { this.prevPosX = this.posX; this.prevPosY =
	 * this.posY; this.prevPosZ = this.posZ; if (this.age++ >= this.maxAge) {
	 * setExpired(); } else { move(this.motionX, this.motionY, this.motionZ);
	 * this.motionX *= 0.96F; this.motionY *= 0.96F; this.motionZ *= 0.96F; if
	 * (this.onGround) { this.motionX *= 0.7F; this.motionZ *= 0.7F; }
	 * 
	 * } }
	 */
}