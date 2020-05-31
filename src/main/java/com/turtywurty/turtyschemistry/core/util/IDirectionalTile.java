package com.turtywurty.turtyschemistry.core.util;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface IDirectionalTile {
	Direction getFacing();

	void setFacing(Direction facing);

	/**
	 * @return 0 = side clicked, 1=piston behaviour, 2 = horizontal, 3 = vertical, 4
	 *         = x/z axis, 5 = horizontal based on quadrant, 6 = horizontal
	 *         preferring clicked side
	 */
	PlacementLimitation getFacingLimitation();

	default Direction getFacingForPlacement(LivingEntity placer, BlockPos pos, Direction side, float hitX, float hitY,
			float hitZ) {
		Direction f;
		PlacementLimitation limit = getFacingLimitation();
		switch (limit) {
		case SIDE_CLICKED:
			f = side;
			break;
		case PISTON_LIKE:
			f = Direction.getFacingDirections(placer)[0];
			break;
		case HORIZONTAL:
			f = Direction.fromAngle(placer.rotationYaw);
			break;
		case VERTICAL:
			f = (side != Direction.DOWN && (side == Direction.UP || hitY <= .5)) ? Direction.UP : Direction.DOWN;
			break;
		case HORIZONTAL_AXIS:
			f = Direction.fromAngle(placer.rotationYaw);
			if (f == Direction.SOUTH || f == Direction.WEST)
				f = f.getOpposite();
			break;
		case HORIZONTAL_QUADRANT:
			if (side.getAxis() != Axis.Y)
				f = side.getOpposite();
			else {
				float xFromMid = hitX - .5f;
				float zFromMid = hitZ - .5f;
				float max = Math.max(Math.abs(xFromMid), Math.abs(zFromMid));
				if (max == Math.abs(xFromMid))
					f = xFromMid < 0 ? Direction.WEST : Direction.EAST;
				else
					f = zFromMid < 0 ? Direction.NORTH : Direction.SOUTH;
			}
			break;
		case HORIZONTAL_PREFER_SIDE:
			f = side.getAxis() != Axis.Y ? side.getOpposite() : placer.getHorizontalFacing();
			break;
		case FIXED_DOWN:
			f = Direction.DOWN;
			break;
		default:
			throw new IllegalArgumentException("Invalid facing limitation: " + limit);
		}

		TurtyChemistry.LOGGER.debug("Setting facing to {}", f);
		return mirrorFacingOnPlacement(placer) ? f.getOpposite() : f;
	}

	boolean mirrorFacingOnPlacement(LivingEntity placer);

	boolean canHammerRotate(Direction side, Vec3d hit, LivingEntity entity);

	boolean canRotate(Direction axis);

	default void afterRotation(Direction oldDir, Direction newDir) {
	}

	enum PlacementLimitation {
		SIDE_CLICKED, PISTON_LIKE, HORIZONTAL, VERTICAL, HORIZONTAL_AXIS, HORIZONTAL_QUADRANT, HORIZONTAL_PREFER_SIDE,
		FIXED_DOWN
	}
}
