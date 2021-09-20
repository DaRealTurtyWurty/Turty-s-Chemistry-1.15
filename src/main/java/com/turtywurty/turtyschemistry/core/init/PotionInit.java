package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.effect.SuffocatingEffect;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class PotionInit {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS,
            TurtyChemistry.MOD_ID);

    public static final RegistryObject<Effect> SUFFOCATING_EFFECT = EFFECTS.register("suffocating",
            () -> new SuffocatingEffect(EffectType.HARMFUL, 0xE9EDEA));

    private PotionInit() {
    }
}
