package com.turtywurty.turtyschemistry.core.util;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.turtywurty.turtyschemistry.common.blocks.AbstractConveyorBlock;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConveyorState {

	private final World world;
	private final BlockPos pos;
	private final AbstractConveyorBlock block;
	private BlockState newState;
	private final boolean disableCorners;
	private final List<BlockPos> connectedConveyors = Lists.newArrayList();
	private final boolean canMakeSlopes;

	public ConveyorState(World worldIn, BlockPos pos, BlockState state) {
		this.world = worldIn;
		this.pos = pos;
		this.newState = state;
		this.block = (AbstractConveyorBlock) state.getBlock();
		ConveyorShape conveyorshape = this.block.getConveyorDirection(newState, worldIn, pos);
		this.disableCorners = false;
		this.canMakeSlopes = true;
		this.reset(conveyorshape);
	}

	public List<BlockPos> getConnectedConveyors() {
		return this.connectedConveyors;
	}

	private void reset(ConveyorShape shape) {
		this.connectedConveyors.clear();
		switch (shape) {
		case NORTH_SOUTH:
			this.connectedConveyors.add(this.pos.north());
			this.connectedConveyors.add(this.pos.south());
			break;
		case EAST_WEST:
			this.connectedConveyors.add(this.pos.west());
			this.connectedConveyors.add(this.pos.east());
			break;
		case ASCENDING_EAST:
			this.connectedConveyors.add(this.pos.west());
			this.connectedConveyors.add(this.pos.east().up());
			break;
		case ASCENDING_WEST:
			this.connectedConveyors.add(this.pos.west().up());
			this.connectedConveyors.add(this.pos.east());
			break;
		case ASCENDING_NORTH:
			this.connectedConveyors.add(this.pos.north().up());
			this.connectedConveyors.add(this.pos.south());
			break;
		case ASCENDING_SOUTH:
			this.connectedConveyors.add(this.pos.north());
			this.connectedConveyors.add(this.pos.south().up());
			break;
		case SOUTH_EAST:
			this.connectedConveyors.add(this.pos.east());
			this.connectedConveyors.add(this.pos.south());
			break;
		case SOUTH_WEST:
			this.connectedConveyors.add(this.pos.west());
			this.connectedConveyors.add(this.pos.south());
			break;
		case NORTH_WEST:
			this.connectedConveyors.add(this.pos.west());
			this.connectedConveyors.add(this.pos.north());
			break;
		case NORTH_EAST:
			this.connectedConveyors.add(this.pos.east());
			this.connectedConveyors.add(this.pos.north());
		}

	}

	private void checkConnected() {
		for (int i = 0; i < this.connectedConveyors.size(); ++i) {
			ConveyorState conveyorstate = this.createForAdjacent(this.connectedConveyors.get(i));
			if (conveyorstate != null && conveyorstate.isConnectedTo(this)) {
				this.connectedConveyors.set(i, conveyorstate.pos);
			} else {
				this.connectedConveyors.remove(i--);
			}
		}

	}

	private boolean isAdjacentConveyor(BlockPos pos) {
		return AbstractConveyorBlock.isConveyor(this.world, pos)
				|| AbstractConveyorBlock.isConveyor(this.world, pos.up())
				|| AbstractConveyorBlock.isConveyor(this.world, pos.down());
	}

	@Nullable
	private ConveyorState createForAdjacent(BlockPos pos) {
		BlockState blockstate = this.world.getBlockState(pos);
		if (AbstractConveyorBlock.isConveyor(blockstate)) {
			return new ConveyorState(this.world, pos, blockstate);
		} else {
			BlockPos pos1 = pos.up();
			blockstate = this.world.getBlockState(pos1);
			if (AbstractConveyorBlock.isConveyor(blockstate)) {
				return new ConveyorState(this.world, pos1, blockstate);
			} else {
				pos1 = pos.down();
				blockstate = this.world.getBlockState(pos1);
				return AbstractConveyorBlock.isConveyor(blockstate) ? new ConveyorState(this.world, pos1, blockstate)
						: null;
			}
		}
	}

	private boolean isConnectedTo(ConveyorState state) {
		return this.isConnectedTo(state.pos);
	}

	private boolean isConnectedTo(BlockPos pos) {
		for (int i = 0; i < this.connectedConveyors.size(); ++i) {
			BlockPos blockpos = this.connectedConveyors.get(i);
			if (blockpos.getX() == pos.getX() && blockpos.getZ() == pos.getZ()) {
				return true;
			}
		}

		return false;
	}

	protected int countAdjacentConveyors() {
		int i = 0;

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			if (this.isAdjacentConveyor(this.pos.offset(direction))) {
				++i;
			}
		}

		return i;
	}

	private boolean hasConnection(ConveyorState state) {
		return this.isConnectedTo(state) || this.connectedConveyors.size() != 2;
	}

	private void connectConveyor(ConveyorState state) {
		this.connectedConveyors.add(state.pos);
		BlockPos blockpos = this.pos.north();
		BlockPos blockpos1 = this.pos.south();
		BlockPos blockpos2 = this.pos.west();
		BlockPos blockpos3 = this.pos.east();
		boolean flag = this.isConnectedTo(blockpos);
		boolean flag1 = this.isConnectedTo(blockpos1);
		boolean flag2 = this.isConnectedTo(blockpos2);
		boolean flag3 = this.isConnectedTo(blockpos3);
		ConveyorShape conveyorshape = null;
		if (flag || flag1) {
			conveyorshape = ConveyorShape.NORTH_SOUTH;
		}

		if (flag2 || flag3) {
			conveyorshape = ConveyorShape.EAST_WEST;
		}

		if (!this.disableCorners) {
			if (flag1 && flag3 && !flag && !flag2) {
				conveyorshape = ConveyorShape.SOUTH_EAST;
			}

			if (flag1 && flag2 && !flag && !flag3) {
				conveyorshape = ConveyorShape.SOUTH_WEST;
			}

			if (flag && flag2 && !flag1 && !flag3) {
				conveyorshape = ConveyorShape.NORTH_WEST;
			}

			if (flag && flag3 && !flag1 && !flag2) {
				conveyorshape = ConveyorShape.NORTH_EAST;
			}
		}

		if (conveyorshape == ConveyorShape.NORTH_SOUTH && canMakeSlopes) {
			if (AbstractConveyorBlock.isConveyor(this.world, blockpos.up())) {
				conveyorshape = ConveyorShape.ASCENDING_NORTH;
			}

			if (AbstractConveyorBlock.isConveyor(this.world, blockpos1.up())) {
				conveyorshape = ConveyorShape.ASCENDING_SOUTH;
			}
		}

		if (conveyorshape == ConveyorShape.EAST_WEST && canMakeSlopes) {
			if (AbstractConveyorBlock.isConveyor(this.world, blockpos3.up())) {
				conveyorshape = ConveyorShape.ASCENDING_EAST;
			}

			if (AbstractConveyorBlock.isConveyor(this.world, blockpos2.up())) {
				conveyorshape = ConveyorShape.ASCENDING_WEST;
			}
		}

		if (conveyorshape == null) {
			conveyorshape = ConveyorShape.NORTH_SOUTH;
		}

		this.newState = this.newState.with(this.block.getShapeProperty(), conveyorshape);
		this.world.setBlockState(this.pos, this.newState, 3);
	}

	private boolean isConnected(BlockPos pos) {
		ConveyorState conveyorstate = this.createForAdjacent(pos);
		if (conveyorstate == null) {
			return false;
		} else {
			conveyorstate.checkConnected();
			return conveyorstate.hasConnection(this);
		}
	}

	public ConveyorState getState(boolean powered, boolean isMoving, ConveyorShape shape) {
		BlockPos blockpos = this.pos.north();
		BlockPos blockpos1 = this.pos.south();
		BlockPos blockpos2 = this.pos.west();
		BlockPos blockpos3 = this.pos.east();
		boolean north = this.isConnected(blockpos);
		boolean south = this.isConnected(blockpos1);
		boolean west = this.isConnected(blockpos2);
		boolean east = this.isConnected(blockpos3);
		ConveyorShape conveyorshape = null;
		boolean northsouth = north || south;
		boolean westeast = west || east;
		if (northsouth && !westeast) {
			conveyorshape = ConveyorShape.NORTH_SOUTH;
		}

		if (westeast && !northsouth) {
			conveyorshape = ConveyorShape.EAST_WEST;
		}

		boolean southeast = south && east;
		boolean southwest = south && west;
		boolean northeast = north && east;
		boolean northwest = north && west;
		if (!this.disableCorners) {
			if (southeast && !north && !west) {
				conveyorshape = ConveyorShape.SOUTH_EAST;
			}

			if (southwest && !north && !east) {
				conveyorshape = ConveyorShape.SOUTH_WEST;
			}

			if (northwest && !south && !east) {
				conveyorshape = ConveyorShape.NORTH_WEST;
			}

			if (northeast && !south && !west) {
				conveyorshape = ConveyorShape.NORTH_EAST;
			}
		}

		if (conveyorshape == null) {
			if (northsouth && westeast) {
				conveyorshape = shape;
			} else if (northsouth) {
				conveyorshape = ConveyorShape.NORTH_SOUTH;
			} else if (westeast) {
				conveyorshape = ConveyorShape.EAST_WEST;
			}

			if (!this.disableCorners) {
				if (powered) {
					if (southeast) {
						conveyorshape = ConveyorShape.SOUTH_EAST;
					}

					if (southwest) {
						conveyorshape = ConveyorShape.SOUTH_WEST;
					}

					if (northeast) {
						conveyorshape = ConveyorShape.NORTH_EAST;
					}

					if (northwest) {
						conveyorshape = ConveyorShape.NORTH_WEST;
					}
				} else {
					if (northwest) {
						conveyorshape = ConveyorShape.NORTH_WEST;
					}

					if (northeast) {
						conveyorshape = ConveyorShape.NORTH_EAST;
					}

					if (southwest) {
						conveyorshape = ConveyorShape.SOUTH_WEST;
					}

					if (southeast) {
						conveyorshape = ConveyorShape.SOUTH_EAST;
					}
				}
			}
		}

		if (conveyorshape == ConveyorShape.NORTH_SOUTH && canMakeSlopes) {
			if (AbstractConveyorBlock.isConveyor(this.world, blockpos.up())) {
				conveyorshape = ConveyorShape.ASCENDING_NORTH;
			}

			if (AbstractConveyorBlock.isConveyor(this.world, blockpos1.up())) {
				conveyorshape = ConveyorShape.ASCENDING_SOUTH;
			}
		}

		if (conveyorshape == ConveyorShape.EAST_WEST && canMakeSlopes) {
			if (AbstractConveyorBlock.isConveyor(this.world, blockpos3.up())) {
				conveyorshape = ConveyorShape.ASCENDING_EAST;
			}

			if (AbstractConveyorBlock.isConveyor(this.world, blockpos2.up())) {
				conveyorshape = ConveyorShape.ASCENDING_WEST;
			}
		}

		if (conveyorshape == null) {
			conveyorshape = shape;
		}

		this.reset(conveyorshape);
		this.newState = this.newState.with(this.block.getShapeProperty(), conveyorshape);
		if (isMoving || this.world.getBlockState(this.pos) != this.newState) {
			this.world.setBlockState(this.pos, this.newState, 3);

			for (int i = 0; i < this.connectedConveyors.size(); ++i) {
				ConveyorState conveyorstate = this.createForAdjacent(this.connectedConveyors.get(i));
				if (conveyorstate != null) {
					conveyorstate.checkConnected();
					if (conveyorstate.hasConnection(this)) {
						conveyorstate.connectConveyor(this);
					}
				}
			}
		}

		return this;
	}

	public BlockState getNewState() {
		return this.newState;
	}

	public enum ConveyorShape implements IStringSerializable {
		NORTH_SOUTH("north_south"), EAST_WEST("east_west"), ASCENDING_EAST("ascending_east"),
		ASCENDING_WEST("ascending_west"), ASCENDING_NORTH("ascending_north"), ASCENDING_SOUTH("ascending_south"),
		SOUTH_EAST("south_east"), SOUTH_WEST("south_west"), NORTH_WEST("north_west"), NORTH_EAST("north_east");

		private final String name;

		private ConveyorShape(String nameIn) {
			this.name = nameIn;
		}

		public String toString() {
			return this.name;
		}

		public boolean isAscending() {
			return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH
					|| this == ASCENDING_WEST;
		}

		public String getName() {
			return this.name;
		}
	}
}