package com.turtywurty.turtyschemistry.core.world.features;

import java.util.Random;
import java.util.function.Predicate;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.template.IRuleTestType;
import net.minecraft.world.gen.feature.template.RuleTest;

public class TagsRuleTest extends RuleTest {

    private final Predicate<BlockState> predicate;

    public TagsRuleTest(final Predicate<BlockState> predicateIn) {
        this.predicate = predicateIn;
    }

    @Override
    public boolean test(final BlockState state, final Random rand) {
        return this.predicate.test(state);
    }

    @Override
    protected IRuleTestType<TagsRuleTest> getType() {
        return () -> PredicateCodec.CODEC;
    }

    public static class PredicateCodec implements Codec<TagsRuleTest> {

        public static final PredicateCodec CODEC = new PredicateCodec();

        @Override
        public <T> DataResult<Pair<TagsRuleTest, T>> decode(final DynamicOps<T> ops, final T input) {
            return null;
        }

        @Override
        public <T> DataResult<T> encode(final TagsRuleTest input, final DynamicOps<T> ops, final T prefix) {
            return null;
        }
    }
}
