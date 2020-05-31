package com.turtywurty.turtyschemistry.common.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class SuffocatingEffect extends Effect {

	private int time = 0;

	public SuffocatingEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		super.performEffect(entityLivingBaseIn, amplifier);
		time++;
		if (time > 100) {
			time = 0;
			entityLivingBaseIn.attackEntityFrom(DamageSource.DROWN, 1f);
		}
	}
}
