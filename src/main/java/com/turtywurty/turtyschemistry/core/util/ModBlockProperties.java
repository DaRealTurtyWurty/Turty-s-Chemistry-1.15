package com.turtywurty.turtyschemistry.core.util;

import java.util.Collection;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.ModelProperty;

public final class ModBlockProperties {

    public static final PropertyBoolInverted MULTIBLOCKSLAVE = PropertyBoolInverted.create("multiblockslave");

    public static final PropertyBoolInverted MIRRORED = PropertyBoolInverted.create("mirrored");
    public static final ModelProperty<TileEntity> TILEENTITY_PASSTHROUGH = new ModelProperty<>();
    public static final DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("facing",
            Direction.Plane.HORIZONTAL);

    private ModBlockProperties() {
    }

    public static class PropertyBoolInverted extends Property<Boolean> {
        private static final ImmutableList<Boolean> ALLOWED_VALUES = ImmutableList.of(false, true);

        protected PropertyBoolInverted(final String name) {
            super(name, Boolean.class);
        }

        public static PropertyBoolInverted create(final String name) {
            return new PropertyBoolInverted(name);
        }

        @Override
        public Collection<Boolean> getAllowedValues() {
            return ALLOWED_VALUES;
        }

        @Override
        public String getName(final Boolean value) {
            return value.toString();
        }

        @Override
        public Optional<Boolean> parseValue(final String value) {
            return Optional.of(Boolean.parseBoolean(value));
        }
    }

}
