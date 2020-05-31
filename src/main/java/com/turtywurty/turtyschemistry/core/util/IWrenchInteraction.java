package com.turtywurty.turtyschemistry.core.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;

public interface IWrenchInteraction {

	boolean wrenchUseSide(Direction side, PlayerEntity player, Vec3d hitVec);
}
