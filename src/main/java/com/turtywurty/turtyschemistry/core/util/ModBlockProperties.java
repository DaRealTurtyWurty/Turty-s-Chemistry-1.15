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
	
	private ModBlockProperties() {} 

	public static final PropertyBoolInverted MULTIBLOCKSLAVE = PropertyBoolInverted.create("multiblockslave");
	public static final PropertyBoolInverted MIRRORED = PropertyBoolInverted.create("mirrored");
	public static final ModelProperty<TileEntity> TILEENTITY_PASSTHROUGH = new ModelProperty<>();
	public static final DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("facing",
			Direction.Plane.HORIZONTAL);

	public static class PropertyBoolInverted extends Property<Boolean> {
		private static final ImmutableList<Boolean> ALLOWED_VALUES = ImmutableList.of(false, true);

		protected PropertyBoolInverted(String name) {
			super(name, Boolean.class);
		}

		@Override
		public Collection<Boolean> getAllowedValues() {
			return ALLOWED_VALUES;
		}

		@Override
		public Optional<Boolean> parseValue(String value) {
			return Optional.of(Boolean.parseBoolean(value));
		}

		public static PropertyBoolInverted create(String name) {
			return new PropertyBoolInverted(name);
		}

		@Override
		public String getName(Boolean value) {
			return value.toString();
		}
	}

}
