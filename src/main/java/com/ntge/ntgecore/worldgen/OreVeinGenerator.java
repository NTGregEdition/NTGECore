package com.ntge.ntgecore.worldgen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public final class OreVeinGenerator implements IWorldGenerator {

    private static final Logger LOG = Logger.getLogger("NtgeCore");
    private static final int SEND_TO_CLIENTS = 2;

    private final VeinType[] definitions = VeinDefinitions.VEINS;
    private final Map<Integer, List<VeinType>> veinTypesByDimension = new HashMap<Integer, List<VeinType>>();
    private final Map<Integer, Block> customHostRocks = new HashMap<Integer, Block>();
    private final Set<Integer> planetDimensions = new HashSet<Integer>();
    private OreBlocks oreBlocks;
    private Method celestialGetMeta;
    private int reachCells;
    private boolean ready;

    private OreVeinGenerator() {
    }

    public static void register() {
        GameRegistry.registerWorldGenerator(new OreVeinGenerator(), Integer.MAX_VALUE);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        prepareOnce();

        int dimension = world.provider.dimensionId;
        List<VeinType> types = veinTypesByDimension.get(dimension);
        if (types == null) {
            return;
        }

        Block host = hostRock(dimension);
        int meta = oreMeta(world, dimension);
        processChunk(world, chunkX, chunkZ, types, host, meta);

        processPopulatedChunk(world, chunkProvider, chunkX + 1, chunkZ, types, host, meta);
        processPopulatedChunk(world, chunkProvider, chunkX, chunkZ + 1, types, host, meta);
        processPopulatedChunk(world, chunkProvider, chunkX + 1, chunkZ + 1, types, host, meta);
    }

    private void prepareOnce() {
        if (ready) {
            return;
        }

        Map<Integer, Integer> dimensionOverrides = SpacePlanets.resolveDimensionOverrides();

        for (VeinDefinitions.HostRock hostRock : VeinDefinitions.CUSTOM_HOST_ROCKS) {
            Block block = Block.getBlockFromName(hostRock.blockName);
            if (block == null) {
                LOG.warning("Host rock '" + hostRock.blockName + "' not found, skipping its planet");
                continue;
            }
            customHostRocks.put(actualDimension(dimensionOverrides, hostRock.dimension), block);
        }

        List<VeinType> usable = new ArrayList<VeinType>();
        int maxRadius = 0;
        for (VeinType type : definitions) {
            if (!type.resolve()) {
                LOG.warning("Vein '" + type.name + "' has no ores available, skipping it");
                continue;
            }
            if (needsCustomHost(type.dimension) && !customHostRocks.containsKey(actualDimension(dimensionOverrides, type.dimension))) {
                LOG.warning("Vein '" + type.name + "' skipped: its planet's host rock isn't installed");
                continue;
            }
            usable.add(type);
            addToDimension(type, actualDimension(dimensionOverrides, type.dimension));
            maxRadius = Math.max(maxRadius, (int) Math.ceil(type.size / 2F * (1F + VeinDefinitions.SHAPE_VARIATION)));
        }

        oreBlocks = OreBlocks.create(usable);
        reachCells = (maxRadius + VeinDefinitions.CELL_BLOCKS - 1) / VeinDefinitions.CELL_BLOCKS;

        for (int planetDimension : VeinDefinitions.PLANET_DIMENSIONS) {
            planetDimensions.add(actualDimension(dimensionOverrides, planetDimension));
        }
        try {
            celestialGetMeta = Class.forName("com.hbm.dim.CelestialBody").getMethod("getMeta", World.class);
        } catch (Exception e) {
            celestialGetMeta = null;
        }

        ready = true;
        LOG.info(usable.size() + " ore veins active");
    }

    private int oreMeta(World world, int dimension) {
        if (celestialGetMeta == null || !planetDimensions.contains(dimension)) {
            return 0;
        }
        try {
            return ((Integer) celestialGetMeta.invoke(null, world)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private static int actualDimension(Map<Integer, Integer> overrides, int defaultDimension) {
        Integer actual = overrides.get(defaultDimension);
        return actual != null ? actual.intValue() : defaultDimension;
    }

    private static boolean needsCustomHost(int dimension) {
        for (VeinDefinitions.HostRock hostRock : VeinDefinitions.CUSTOM_HOST_ROCKS) {
            if (hostRock.dimension == dimension) {
                return true;
            }
        }
        return false;
    }

    private void addToDimension(VeinType type, int actualDimension) {
        List<VeinType> inDimension = veinTypesByDimension.get(actualDimension);
        if (inDimension == null) {
            inDimension = new ArrayList<VeinType>();
            veinTypesByDimension.put(actualDimension, inDimension);
        }
        inDimension.add(type);
    }

    private Block hostRock(int dimension) {
        Block custom = customHostRocks.get(dimension);
        if (custom != null) {
            return custom;
        }
        if (dimension == VeinDefinitions.NETHER) {
            return Blocks.netherrack;
        }
        return dimension == VeinDefinitions.END ? Blocks.end_stone : Blocks.stone;
    }

    private void processPopulatedChunk(World world, IChunkProvider chunkProvider, int chunkX, int chunkZ, List<VeinType> types, Block host, int meta) {
        if (chunkProvider.chunkExists(chunkX, chunkZ) && world.getChunkFromChunkCoords(chunkX, chunkZ).isTerrainPopulated) {
            processChunk(world, chunkX, chunkZ, types, host, meta);
        }
    }

    private void processChunk(World world, int chunkX, int chunkZ, List<VeinType> types, Block host, int meta) {
        List<Vein> veins = findVeins(world, chunkX, chunkZ, types);
        removeUnplannedOres(world, chunkX, chunkZ, veins, host);
        for (Vein vein : veins) {
            vein.fill(world, chunkX, chunkZ, host, meta);
        }
    }

    private List<Vein> findVeins(World world, int chunkX, int chunkZ, List<VeinType> types) {
        long worldSeed = world.getSeed();
        int dimension = world.provider.dimensionId;
        int homeCellX = VeinMath.floorDiv(chunkX, VeinDefinitions.CELL_CHUNKS);
        int homeCellZ = VeinMath.floorDiv(chunkZ, VeinDefinitions.CELL_CHUNKS);

        List<Vein> veins = new ArrayList<Vein>();
        for (int cellX = homeCellX - reachCells; cellX <= homeCellX + reachCells; cellX++) {
            for (int cellZ = homeCellZ - reachCells; cellZ <= homeCellZ + reachCells; cellZ++) {
                Vein vein = createVein(worldSeed, dimension, cellX, cellZ, types);
                if (vein != null && vein.overlapsChunk(chunkX, chunkZ)) {
                    veins.add(vein);
                }
            }
        }
        return veins;
    }

    private static Vein createVein(long worldSeed, int dimension, int cellX, int cellZ, List<VeinType> types) {
        Random rand = new Random(VeinMath.hash64(worldSeed, cellX, dimension, cellZ));
        if (rand.nextInt(100) >= VeinDefinitions.VEIN_CHANCE_PERCENT) {
            return null;
        }
        return new Vein(pickType(types, rand), rand, cellX * VeinDefinitions.CELL_BLOCKS, cellZ * VeinDefinitions.CELL_BLOCKS);
    }

    private static VeinType pickType(List<VeinType> types, Random rand) {
        int totalWeight = 0;
        for (VeinType type : types) {
            totalWeight += type.weight;
        }

        int roll = rand.nextInt(totalWeight);
        for (VeinType type : types) {
            roll -= type.weight;
            if (roll < 0) {
                return type;
            }
        }
        return types.get(types.size() - 1);
    }

    private void removeUnplannedOres(World world, int chunkX, int chunkZ, List<Vein> veins, Block host) {
        Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        int maxY = world.getHeight() - 1;

        for (int y = 1; y <= maxY; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block == host || block == Blocks.air || !oreBlocks.contains(block)) {
                        continue;
                    }
                    if (!isPlannedOre(veins, baseX + x, y, baseZ + z, block)) {
                        world.setBlock(baseX + x, y, baseZ + z, host, 0, SEND_TO_CLIENTS);
                    }
                }
            }
        }
    }

    private static boolean isPlannedOre(List<Vein> veins, int x, int y, int z, Block block) {
        for (Vein vein : veins) {
            Block planned = vein.oreAt(x, y, z);
            if (planned != null) {
                return planned == block;
            }
        }
        return false;
    }
}
