package com.ntge.ntgecore.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

final class Vein {

    private static final long ORE_ROLL_SALT = 0x2545F4914F6CDD1DL;
    private static final int SEND_TO_CLIENTS = 2;

    private final VeinType type;
    private final long seed;
    private final int centerX;
    private final int centerY;
    private final int centerZ;
    private final float radiusX;
    private final float radiusY;
    private final float radiusZ;

    Vein(VeinType type, Random rand, int cellMinX, int cellMinZ) {
        this.type = type;
        this.seed = rand.nextLong();
        this.centerX = cellMinX + rand.nextInt(VeinDefinitions.CELL_BLOCKS);
        this.centerZ = cellMinZ + rand.nextInt(VeinDefinitions.CELL_BLOCKS);
        this.centerY = type.minY + rand.nextInt(type.maxY - type.minY + 1);
        this.radiusX = type.size / 2F * randomStretch(rand);
        this.radiusZ = type.size / 2F * randomStretch(rand);
        this.radiusY = type.height / 2F;
    }

    private static float randomStretch(Random rand) {
        return 1F + (rand.nextFloat() * 2F - 1F) * VeinDefinitions.SHAPE_VARIATION;
    }

    boolean overlapsChunk(int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        return centerX + radiusX >= minX && centerX - radiusX <= minX + 15
                && centerZ + radiusZ >= minZ && centerZ - radiusZ <= minZ + 15;
    }

    Block oreAt(int x, int y, int z) {
        float nx = (x - centerX) / radiusX;
        float ny = (y - centerY) / radiusY;
        float nz = (z - centerZ) / radiusZ;
        float distanceSq = nx * nx + ny * ny + nz * nz;
        if (distanceSq >= 1F) {
            return null;
        }
        if (VeinMath.hash01(seed, x, y, z) >= VeinMath.oreChance(type.density, distanceSq)) {
            return null;
        }
        return type.blockFor(VeinMath.hash01(seed ^ ORE_ROLL_SALT, x, y, z));
    }

    void fill(World world, int chunkX, int chunkZ, Block host, int meta) {
        int minX = Math.max((int) Math.floor(centerX - radiusX), chunkX << 4);
        int maxX = Math.min((int) Math.ceil(centerX + radiusX), (chunkX << 4) + 15);
        int minZ = Math.max((int) Math.floor(centerZ - radiusZ), chunkZ << 4);
        int maxZ = Math.min((int) Math.ceil(centerZ + radiusZ), (chunkZ << 4) + 15);
        int minY = Math.max((int) Math.floor(centerY - radiusY), 1);
        int maxY = Math.min((int) Math.ceil(centerY + radiusY), world.getHeight() - 2);

        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
                    Block ore = oreAt(x, y, z);
                    if (ore != null && world.getBlock(x, y, z).isReplaceableOreGen(world, x, y, z, host)) {
                        world.setBlock(x, y, z, ore, meta, SEND_TO_CLIENTS);
                    }
                }
            }
        }
    }
}
