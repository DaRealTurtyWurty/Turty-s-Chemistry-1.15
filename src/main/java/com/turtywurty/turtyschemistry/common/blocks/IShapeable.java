package com.turtywurty.turtyschemistry.common.blocks;

import java.util.Map;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public interface IShapeable {

	default void calculateShapes(Map<Direction, VoxelShape> shapes, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[] { shape, VoxelShapes.empty() };

		int times = (to.getHorizontalIndex() - Direction.NORTH.getHorizontalIndex() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1],
					VoxelShapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		shapes.put(to, buffer[0]);
	}
	
	default void runCalculation(Map<Direction, VoxelShape> shapes, VoxelShape shape) {
		for (Direction direction : Direction.values()) {
			calculateShapes(shapes, direction, shape);
		}
	}
}
