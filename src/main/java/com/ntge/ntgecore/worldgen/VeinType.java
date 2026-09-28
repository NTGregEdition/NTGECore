package com.ntge.ntgecore.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

final class VeinType {

    private static final Logger LOG = Logger.getLogger("NtgeCore");

    static final class Ore {

        final String blockName;
        final int weight;

        Ore(String blockName, int weight) {
            this.blockName = blockName;
            this.weight = weight;
        }
    }

    final String name;
    final int dimension;
    final int weight;
    final int minY;
    final int maxY;
    final int size;
    final int height;
    final float density;
    private final Ore[] ores;

    private Block[] blocks;
    private int[] blockWeights;
    private int totalWeight;

    VeinType(String name, int dimension, int weight, int minY, int maxY, int size, int height, float density, Ore... ores) {
        if (weight < 1 || minY > maxY || size < 1 || height < 1 || density <= 0F || density > 1F || ores.length == 0) {
            throw new IllegalArgumentException("Invalid ore vein definition: " + name);
        }
        this.name = name;
        this.dimension = dimension;
        this.weight = weight;
        this.minY = minY;
        this.maxY = maxY;
        this.size = size;
        this.height = height;
        this.density = density;
        this.ores = ores;
    }

    boolean resolve() {
        List<Block> resolvedBlocks = new ArrayList<Block>();
        List<Integer> resolvedWeights = new ArrayList<Integer>();
        for (Ore ore : ores) {
            Block block = Block.getBlockFromName(ore.blockName);
            if (block == null || block == Blocks.air) {
                LOG.warning("Vein '" + name + "': block '" + ore.blockName + "' not found, skipping it");
                continue;
            }
            resolvedBlocks.add(block);
            resolvedWeights.add(ore.weight);
        }

        blocks = resolvedBlocks.toArray(new Block[resolvedBlocks.size()]);
        blockWeights = new int[blocks.length];
        totalWeight = 0;
        for (int i = 0; i < blockWeights.length; i++) {
            blockWeights[i] = resolvedWeights.get(i);
            totalWeight += blockWeights[i];
        }
        return blocks.length > 0;
    }

    Block[] resolvedBlocks() {
        return blocks;
    }

    Block blockFor(float roll) {
        return blocks[VeinMath.pickWeighted(blockWeights, totalWeight, roll)];
    }
}
