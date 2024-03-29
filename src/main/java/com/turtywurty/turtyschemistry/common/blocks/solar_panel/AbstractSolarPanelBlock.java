package com.turtywurty.turtyschemistry.common.blocks.solar_panel;

import java.util.function.Supplier;

import com.turtywurty.turtyschemistry.TurtyChemistry;
import com.turtywurty.turtyschemistry.common.blocks.BaseHorizontalBlock;

import net.minecraft.block.BlockState;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public abstract class AbstractSolarPanelBlock extends BaseHorizontalBlock {

	protected AbstractSolarPanelBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if (worldIn.getTileEntity(pos) instanceof AbstractSolarPanelTileEntity) {
			if (player instanceof ClientPlayerEntity && FMLEnvironment.dist == Dist.CLIENT
					&& this.getPanelInfo().get() != null) {
				// TODO: Open GUI
			}
		} else {
			TurtyChemistry.LOGGER.warn("No valid solar panel tile entity found at X: %s, Y: %s, Z: %s!", pos.getX(),
					pos.getY(), pos.getZ());
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return this.getPanelInfo().get().getTileType().create();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public abstract Supplier<PanelInfo<? extends AbstractSolarPanelTileEntity>> getPanelInfo();

	protected class PanelInfo<TILE extends AbstractSolarPanelTileEntity> {

		private final IContainerProvider containerProvider;
		private final ITextComponent displayName;
		private final int amountPerTick;
		private final TileEntityType<TILE> tileType;

		public PanelInfo(final IContainerProvider containerProviderIn, final ITextComponent displayNameIn,
				final int amountPerTickIn, TileEntityType<TILE> tileTypeIn) {
			this.containerProvider = containerProviderIn;
			this.displayName = displayNameIn;
			this.amountPerTick = amountPerTickIn;
			this.tileType = tileTypeIn;
		}

		public IContainerProvider getContainerProvider() {
			return this.containerProvider;
		}

		public ITextComponent getDisplayName() {
			return this.displayName;
		}

		public int perTick() {
			return this.amountPerTick;
		}

		public TileEntityType<TILE> getTileType() {
			return this.tileType;
		}
	}
}
