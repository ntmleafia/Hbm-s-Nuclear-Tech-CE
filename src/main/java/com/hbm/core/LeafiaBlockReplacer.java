package com.hbm.core;

//import com.leafia.contents.AddonBlocks.LegacyBlocks;
import com.hbm.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSand.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/// stolen from leafai' cusred addon addon by leafia herself
public class LeafiaBlockReplacer {
	/**
	 * The blocks to be replaced.
	 * <p>Meta will be automatically copied. (you cannot set it manually)
	 * <p>Keep in mind that if SpecialReplacer for the same block exists, that is prioritized instead.
 	 */
	public static final Map<String,Block> replacementMap = new HashMap<>();
	/**
	 * Replacement function for more customizable replacement.
	 * <p>Used in leafia's cursed addon, for making meta < 2 waste dirts turn into normal dirt but meta >= 2 turn into addon waste variant.
	 */
	public static final Map<String,SpecialReplacer> specialReplacer = new HashMap<>();
	static {
		replacementMap.put("hbm:waste_ice",Blocks.ICE);
		replacementMap.put("hbm:waste_snow",Blocks.SNOW_LAYER);
		replacementMap.put("hbm:waste_snow_block",Blocks.SNOW);
		replacementMap.put("hbm:waste_dirt",Blocks.DIRT);
		replacementMap.put("hbm:waste_gravel",Blocks.GRAVEL);
		replacementMap.put("hbm:waste_sand",Blocks.SAND);
		replacementMap.put("hbm:waste_sandstone",Blocks.SANDSTONE);
		replacementMap.put("hbm:waste_sand_red",Blocks.SAND);
		replacementMap.put("hbm:waste_red_sandstone",Blocks.RED_SANDSTONE);
		replacementMap.put("hbm:waste_terracotta",Blocks.HARDENED_CLAY);
	}

	// internal code //

	public static IBlockState withProperties(Block newBlock,IBlockState missingBlock) {
		/*IBlockState state = newBlock.getDefaultState();
		for (IProperty<?> property : missingBlock.getPropertyKeys())
			copyProperty(missingBlock,state,property);
		return state;*/
		return newBlock.getStateFromMeta(missingBlock.getBlock().getMetaFromState(missingBlock));
	}
	public interface SpecialReplacer extends BiFunction<String,IBlockState,IBlockState> { }
	public static IBlockState replaceBlock(IBlockState missingBlock) {
		ResourceLocation reg = missingBlock.getBlock().getRegistryName();
		if (specialReplacer.containsKey(reg.toString())) {
			BiFunction<String,IBlockState,IBlockState> processor = specialReplacer.get(reg.toString());
			return processor.apply(reg.toString(),missingBlock);
		} else {
			Block newBlock = replacementMap.get(reg.toString());
			if (newBlock == null)
				return missingBlock;
			return withProperties(newBlock,missingBlock);
		}
	}
	/// thanks https://forums.minecraftforge.net/topic/117047-copy-all-property-values-from-one-blockstate-to-another/
	static <T extends Comparable<T>> IBlockState copyProperty(IBlockState from,IBlockState to,IProperty<T> property) {
		return from.withProperty(property,from.getValue(property));
	}
}
