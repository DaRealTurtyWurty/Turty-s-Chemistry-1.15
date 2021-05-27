package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.entity.FireResistantItemEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EntityTypeInit {
	private EntityTypeInit() {}

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			TurtyChemistry.MOD_ID);

	public static final RegistryObject<EntityType<? extends ItemEntity>> FIRE_RES_ITEM = ENTITY_TYPES.register("item",
			() -> EntityType.Builder.create(FireResistantItemEntity::new, EntityClassification.MISC).immuneToFire()
					.size(0.25F, 0.25F).build(new ResourceLocation(TurtyChemistry.MOD_ID, "item").toString()));
}
