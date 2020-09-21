package com.turtywurty.turtyschemistry.core.init;

import com.turtywurty.turtyschemistry.TurtyChemistry;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit {

	public static final ResourceLocation BRINE_STILL_RL = new ResourceLocation(TurtyChemistry.MOD_ID,
			"blocks/brine_still");
	public static final ResourceLocation BRINE_FLOWING_RL = new ResourceLocation(TurtyChemistry.MOD_ID,
			"blocks/brine_flowing");
	public static final ResourceLocation BRINE_OVERLAY_RL = new ResourceLocation(TurtyChemistry.MOD_ID,
			"blocks/brine_overlay");

	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
			TurtyChemistry.MOD_ID);

	public static final RegistryObject<FlowingFluid> BRINE_STILL = FLUIDS.register("brine_still",
			() -> new ForgeFlowingFluid.Source(FluidInit.BRINE_PROPERTIES));

	public static final RegistryObject<FlowingFluid> BRINE_FLOWING = FLUIDS.register("brine_flowing",
			() -> new ForgeFlowingFluid.Flowing(FluidInit.BRINE_PROPERTIES));

	public static final RegistryObject<FlowingFluidBlock> BRINE_BLOCK = BlockInit.BLOCKS.register("brine",
			() -> new FlowingFluidBlock(() -> FluidInit.BRINE_STILL.get(), Block.Properties.create(Material.WATER)
					.doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops()));

	public static final ForgeFlowingFluid.Properties BRINE_PROPERTIES = new ForgeFlowingFluid.Properties(
			() -> BRINE_STILL.get(), () -> BRINE_FLOWING.get(),
			FluidAttributes.builder(BRINE_STILL_RL, BRINE_FLOWING_RL).density(50).rarity(Rarity.RARE)
					.sound(SoundEvents.AMBIENT_UNDERWATER_ENTER).overlay(BRINE_OVERLAY_RL))
							.block(() -> FluidInit.BRINE_BLOCK.get()).bucket(() -> ItemInit.BRINE_BUCKET.get());
}
