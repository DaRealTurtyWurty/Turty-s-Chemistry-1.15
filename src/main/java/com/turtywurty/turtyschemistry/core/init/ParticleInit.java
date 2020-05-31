package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.particle.GasParticle;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ParticleInit {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = new DeferredRegister<ParticleType<?>>(
			ForgeRegistries.PARTICLE_TYPES, TurtyChemistry.MOD_ID);

	public static final RegistryObject<BasicParticleType> GAS_PARTICLE = PARTICLE_TYPES.register("gas_particle",
			() -> new BasicParticleType(true));

	@SubscribeEvent
	public static void registerParticles(ParticleFactoryRegisterEvent event) {
		ClientUtils.MC.particles.registerFactory(ParticleInit.GAS_PARTICLE.get(), GasParticle.Factory::new);
	}
}
