package com.turtywurty.turtyschemistry.common.event;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;
import com.turtywurty.turtyschemistry.core.init.ItemInit;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = TurtyChemistry.MOD_ID, bus = Bus.FORGE)
public class ElementReactWithWaterEvent {

	private static ItemEntity ie;

	@SubscribeEvent
	public static void itemEnterWater(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			if (ClientUtils.getClientWorld() != null) {
				ClientWorld world = ClientUtils.getClientWorld();
				world.getAllEntities().forEach(entity -> {
					if (entity instanceof ItemEntity) {
						ie = (ItemEntity) entity;
					}
				});
			}
		}
	}

	@SubscribeEvent
	public static void doStuff(TickEvent.WorldTickEvent event) {
		World world = event.world;
		if (ie != null) {
			ItemEntity itemEntity = (ItemEntity) ie;
			if (itemEntity.isInWater()) {
				if (itemEntity.getItem().getItem() == ItemInit.LITHIUM.get()) {
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					itemEntity.remove();
				} else if (itemEntity.getItem().getItem() == ItemInit.SODIUM.get()) {
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					itemEntity.remove();
				} else if (itemEntity.getItem().getItem() == ItemInit.POTASSIUM.get()) {
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.createExplosion(itemEntity, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ(),
							5, false, Mode.DESTROY);
					itemEntity.remove();
				} else if (itemEntity.getItem().getItem() == ItemInit.RUBIDIUM.get()) {
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.createExplosion(itemEntity, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ(),
							10, false, Mode.DESTROY);
					itemEntity.remove();
				} else if (itemEntity.getItem().getItem() == ItemInit.CESIUM.get()) {
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.createExplosion(itemEntity, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ(),
							10, true, Mode.DESTROY);
					world.createExplosion(itemEntity, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ(),
							10, true, Mode.DESTROY);
					itemEntity.remove();
				} else if (itemEntity.getItem().getItem() == ItemInit.FRANCIUM.get()) {
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.addParticle(ParticleTypes.FLAME, itemEntity.getPosX(), itemEntity.getPosY(),
							itemEntity.getPosZ(), 0.0f, 0.3f, 0.0f);
					world.createExplosion(itemEntity, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ(),
							100, true, Mode.DESTROY);
					itemEntity.remove();
				}
			}
		}
	}
}
