package com.ntge.ntgecore.worldgen;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

final class OreBlocks {

    private final Set<Block> blocks;

    private OreBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    static OreBlocks create(List<VeinType> veinTypes) {
        Set<Block> blocks = new HashSet<Block>();
        addOreDictionaryBlocks(blocks);
        blocks.addAll(resolve(VeinDefinitions.EXTRA_FOREIGN_ORES));
        for (VeinType type : veinTypes) {
            Collections.addAll(blocks, type.resolvedBlocks());
        }
        blocks.removeAll(resolve(VeinDefinitions.PROTECTED_BLOCKS));
        return new OreBlocks(blocks);
    }

    boolean contains(Block block) {
        return blocks.contains(block);
    }

    private static void addOreDictionaryBlocks(Set<Block> blocks) {
        for (String name : OreDictionary.getOreNames()) {
            if (!isOreName(name)) {
                continue;
            }
            for (ItemStack stack : OreDictionary.getOres(name)) {
                Item item = stack.getItem();
                Block block = item == null ? Blocks.air : Block.getBlockFromItem(item);
                if (block != Blocks.air) {
                    blocks.add(block);
                }
            }
        }
    }

    private static boolean isOreName(String name) {
        return name.length() > 3 && name.startsWith("ore") && Character.isUpperCase(name.charAt(3));
    }

    private static Set<Block> resolve(String[] blockNames) {
        Set<Block> resolved = new HashSet<Block>();
        for (String blockName : blockNames) {
            Block block = Block.getBlockFromName(blockName);
            if (block != null) {
                resolved.add(block);
            }
        }
        return resolved;
    }
}
