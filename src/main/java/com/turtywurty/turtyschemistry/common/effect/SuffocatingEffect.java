package com.turtywurty.turtyschemistry.common.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class SuffocatingEffect extends Effect {

    private int time = 0;

    public SuffocatingEffect(final EffectType typeIn, final int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void performEffect(final LivingEntity entityLivingBaseIn, final int amplifier) {
        super.performEffect(entityLivingBaseIn, amplifier);
        this.time++;
        if (this.time > 100) {
            this.time = 0;
            entityLivingBaseIn.attackEntityFrom(DamageSource.DROWN, 1f);
        }
    }
}
