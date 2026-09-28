package com.ntge.ntgecore.worldgen;

final class VeinDefinitions {

    static final int OVERWORLD = 0;
    static final int NETHER = -1;
    static final int END = 1;

    static final int MOON = 413_015;
    static final int DUNA = 413_016;
    static final int IKE = 413_017;
    static final int EVE = 413_018;
    static final int DRES = 413_019;
    static final int MOHO = 413_020;
    static final int MINMUS = 413_021;
    static final int LAYTHE = 413_022;
    static final int TEKTO = 413_024;

    static final int[] PLANET_DIMENSIONS = {MOON, DUNA, IKE, EVE, DRES, MOHO, MINMUS, LAYTHE, TEKTO};

    // One vein per CELL_CHUNKS x CELL_CHUNKS chunks, placed at a random spot inside its cell.
    static final int CELL_CHUNKS = 3;
    static final int CELL_BLOCKS = CELL_CHUNKS * 16;
    // Chance that a cell holds a vein at all.
    static final int VEIN_CHANCE_PERCENT = 85;
    // Each vein's horizontal radii are randomly stretched by up to this fraction, so blobs aren't all round.
    static final float SHAPE_VARIATION = 0.25F;

    private static final String COAL = "minecraft:coal_ore";
    private static final String IRON = "minecraft:iron_ore";
    private static final String GOLD = "minecraft:gold_ore";
    private static final String LAPIS = "minecraft:lapis_ore";
    private static final String REDSTONE = "minecraft:redstone_ore";
    private static final String DIAMOND = "minecraft:diamond_ore";
    private static final String EMERALD = "minecraft:emerald_ore";
    private static final String QUARTZ = "minecraft:quartz_ore";

    private static final String URANIUM = "hbm:tile.ore_uranium";
    private static final String THORIUM = "hbm:tile.ore_thorium";
    private static final String TITANIUM = "hbm:tile.ore_titanium";
    private static final String SULFUR = "hbm:tile.ore_sulfur";
    private static final String NITER = "hbm:tile.ore_niter";
    private static final String COPPER = "hbm:tile.ore_copper";
    private static final String TUNGSTEN = "hbm:tile.ore_tungsten";
    private static final String ALUMINIUM = "hbm:tile.ore_aluminium";
    private static final String FLUORITE = "hbm:tile.ore_fluorite";
    private static final String LEAD = "hbm:tile.ore_lead";
    private static final String BERYLLIUM = "hbm:tile.ore_beryllium";
    private static final String RARE_EARTH = "hbm:tile.ore_rare";
    private static final String LIGNITE = "hbm:tile.ore_lignite";
    private static final String ASBESTOS = "hbm:tile.ore_asbestos";
    private static final String CINNABAR = "hbm:tile.ore_cinnebar";
    private static final String COBALT = "hbm:tile.ore_cobalt";
    private static final String NETHER_COAL = "hbm:tile.ore_nether_coal";
    private static final String NETHER_URANIUM = "hbm:tile.ore_nether_uranium";
    private static final String NETHER_PLUTONIUM = "hbm:tile.ore_nether_plutonium";
    private static final String NETHER_TUNGSTEN = "hbm:tile.ore_nether_tungsten";
    private static final String NETHER_SULFUR = "hbm:tile.ore_nether_sulfur";
    private static final String NETHER_PHOSPHORUS = "hbm:tile.ore_nether_fire";
    private static final String NETHER_COBALT = "hbm:tile.ore_nether_cobalt";
    private static final String TIKITE = "hbm:tile.ore_tikite";

    private static final String HBM_IRON = "hbm:tile.ore_iron";
    private static final String HBM_QUARTZ = "hbm:tile.ore_quartz";
    private static final String LITHIUM = "hbm:tile.ore_lithium";
    private static final String NIOBIUM = "hbm:tile.ore_niobium";
    private static final String IODINE = "hbm:tile.ore_iodine";
    private static final String ZINC = "hbm:tile.ore_zinc";
    private static final String NICKEL = "hbm:tile.ore_nickel";
    private static final String MINERAL = "hbm:tile.ore_mineral";
    private static final String MOHO_FIRE = "hbm:tile.ore_fire";
    private static final String SHALE = "hbm:tile.ore_shale";
    private static final String AUSTRALIUM = "hbm:tile.ore_australium";
    private static final String COLTAN = "hbm:tile.ore_coltan";
    private static final String LANTHANIUM = "hbm:tile.ore_lanthanium";
    private static final String GLOWSTONE_ORE = "hbm:tile.ore_glowstone";
    private static final String SCHRABIDIUM = "hbm:tile.ore_schrabidium";

    // new VeinType(name, dimension, spawn weight,
    //              lowest centre Y, highest centre Y,
    //              width in blocks, height in blocks,
    //              ore chance at the centre (0-1, fades to 0 at the edge),
    //              ore(block, weight)...)
    static final VeinType[] VEINS = {
            new VeinType("carbon", OVERWORLD, 40, 8, 52, 48, 8, 0.65F, ore(COAL, 98), ore(DIAMOND, 2)),
            new VeinType("iron", OVERWORLD, 34, 8, 50, 42, 8, 0.60F, ore(IRON, 88), ore(SULFUR, 12)),
            new VeinType("copper", OVERWORLD, 26, 10, 50, 40, 7, 0.55F, ore(COPPER, 85), ore(IRON, 10), ore(GOLD, 5)),
            new VeinType("gold", OVERWORLD, 10, 5, 30, 34, 6, 0.40F, ore(GOLD, 80), ore(COPPER, 12), ore(SULFUR, 8)),
            new VeinType("redstone", OVERWORLD, 14, 5, 20, 32, 6, 0.50F, ore(REDSTONE, 88), ore(CINNABAR, 12)),
            new VeinType("lapis", OVERWORLD, 10, 8, 34, 32, 6, 0.45F, ore(LAPIS, 85), ore(FLUORITE, 15)),
            new VeinType("lignite", OVERWORLD, 18, 35, 62, 46, 6, 0.60F, ore(LIGNITE, 88), ore(COAL, 12)),
            new VeinType("uranium", OVERWORLD, 12, 5, 26, 34, 6, 0.40F, ore(URANIUM, 70), ore(THORIUM, 25), ore(RARE_EARTH, 5)),
            new VeinType("titanium", OVERWORLD, 14, 6, 36, 36, 7, 0.45F, ore(TITANIUM, 80), ore(ALUMINIUM, 12), ore(BERYLLIUM, 8)),
            new VeinType("bauxite", OVERWORLD, 16, 20, 60, 40, 7, 0.55F, ore(ALUMINIUM, 88), ore(TITANIUM, 12)),
            new VeinType("galena", OVERWORLD, 14, 6, 36, 36, 7, 0.50F, ore(LEAD, 70), ore(TUNGSTEN, 20), ore(SULFUR, 10)),
            new VeinType("evaporite", OVERWORLD, 14, 15, 50, 38, 6, 0.50F, ore(SULFUR, 45), ore(NITER, 40), ore(FLUORITE, 15)),
            new VeinType("beryl", OVERWORLD, 8, 8, 34, 32, 6, 0.40F, ore(BERYLLIUM, 95), ore(EMERALD, 5)),
            new VeinType("cobalt", OVERWORLD, 8, 4, 14, 30, 5, 0.40F, ore(COBALT, 80), ore(COPPER, 20)),
            new VeinType("asbestos", OVERWORLD, 8, 12, 34, 32, 6, 0.45F, ore(ASBESTOS, 90), ore(FLUORITE, 10)),

            new VeinType("nether_quartz", NETHER, 40, 15, 110, 40, 12, 0.55F, ore(QUARTZ, 90), ore(NETHER_SULFUR, 10)),
            new VeinType("nether_uranium", NETHER, 15, 20, 100, 36, 10, 0.45F, ore(NETHER_URANIUM, 62), ore(NETHER_TUNGSTEN, 35), ore(NETHER_PLUTONIUM, 3)),
            new VeinType("nether_sulfur", NETHER, 20, 20, 105, 38, 10, 0.50F, ore(NETHER_SULFUR, 70), ore(NETHER_PHOSPHORUS, 30)),
            new VeinType("nether_coal", NETHER, 20, 20, 100, 40, 10, 0.50F, ore(NETHER_COAL, 85), ore(NETHER_COBALT, 15)),

            new VeinType("end_tikite", END, 1, 40, 62, 30, 6, 0.30F, ore(TIKITE, 100)),

            new VeinType("moon_quartz", MOON, 30, 10, 60, 44, 10, 0.55F, ore(HBM_QUARTZ, 85), ore(FLUORITE, 15)),
            new VeinType("moon_regolith", MOON, 24, 5, 40, 38, 8, 0.45F, ore(ALUMINIUM, 75), ore(LITHIUM, 25)),
            new VeinType("moon_shale", MOON, 18, 8, 40, 34, 7, 0.45F, ore(SHALE, 80), ore(ALUMINIUM, 20)),

            new VeinType("duna_iron", DUNA, 30, 10, 90, 46, 11, 0.55F, ore(HBM_IRON, 85), ore(ZINC, 15)),

            new VeinType("ike_metal", IKE, 26, 5, 40, 42, 9, 0.50F, ore(HBM_IRON, 55), ore(COPPER, 35), ore(LITHIUM, 10)),
            new VeinType("ike_asbestos", IKE, 20, 5, 30, 34, 7, 0.40F, ore(ASBESTOS, 75), ore(MINERAL, 25)),
            new VeinType("ike_coltan", IKE, 8, 15, 55, 28, 6, 0.25F, ore(COLTAN, 100)),

            new VeinType("eve_niobium", EVE, 26, 5, 45, 40, 8, 0.45F, ore(NIOBIUM, 80), ore(IODINE, 20)),
            new VeinType("eve_schrabidium", EVE, 5, 2, 16, 22, 5, 0.18F, ore(SCHRABIDIUM, 100)),

            new VeinType("dres_metal", DRES, 28, 5, 40, 42, 9, 0.50F, ore(HBM_IRON, 45), ore(COBALT, 28), ore(NIOBIUM, 17), ore(SHALE, 10)),
            new VeinType("dres_rare", DRES, 12, 15, 60, 32, 7, 0.30F, ore(LANTHANIUM, 60), ore(COLTAN, 40)),

            new VeinType("moho_volcanic", MOHO, 30, 10, 70, 44, 11, 0.50F, ore(MOHO_FIRE, 50), ore(GLOWSTONE_ORE, 25), ore(MINERAL, 15), ore(SHALE, 10)),
            new VeinType("moho_australium", MOHO, 8, 0, 24, 26, 6, 0.20F, ore(AUSTRALIUM, 100)),

            new VeinType("minmus_metal", MINMUS, 30, 10, 55, 42, 9, 0.55F, ore(COPPER, 50), ore(NICKEL, 30), ore(TITANIUM, 20)),

            new VeinType("laythe_beryl", LAYTHE, 24, 8, 32, 36, 7, 0.40F, ore(BERYLLIUM, 60), ore(RARE_EARTH, 25), ore(ASBESTOS, 15)),

            new VeinType("tekto_cobalt", TEKTO, 20, 3, 14, 30, 6, 0.45F, ore(COBALT, 100))
    };

    static final class HostRock {

        final int dimension;
        final String blockName;

        HostRock(int dimension, String blockName) {
            this.dimension = dimension;
            this.blockName = blockName;
        }
    }

    static final HostRock[] CUSTOM_HOST_ROCKS = {
            new HostRock(MOON, "hbm:tile.moon_rock"),
            new HostRock(DUNA, "hbm:tile.duna_rock"),
            new HostRock(IKE, "hbm:tile.ike_stone"),
            new HostRock(EVE, "hbm:tile.eve_rock"),
            new HostRock(DRES, "hbm:tile.dres_rock"),
            new HostRock(MOHO, "hbm:tile.moho_stone"),
            new HostRock(MINMUS, "hbm:tile.minmus_stone"),
            new HostRock(TEKTO, "hbm:tile.basalt")
    };

    // Ores that no ore dictionary entry points to but that another mod still generates.
    static final String[] EXTRA_FOREIGN_ORES = {
            "hbm:tile.cluster_iron",
            "hbm:tile.cluster_titanium",
            "hbm:tile.cluster_aluminium",
            "hbm:tile.cluster_copper",
            "hbm:tile.ore_mineral",
            "hbm:tile.ore_shale",
            "hbm:tile.ore_glowstone"
    };

    // Never removed. These are stone types or belong to a mechanic (depth drilling, gneiss, basalt) rather than being plain ore.
    static final String[] PROTECTED_BLOCKS = {
            "hbm:tile.stone_resource",
            "hbm:tile.ore_basalt",
            "hbm:tile.ore_gneiss_iron",
            "hbm:tile.ore_gneiss_gold",
            "hbm:tile.ore_gneiss_uranium",
            "hbm:tile.ore_gneiss_uranium_scorched",
            "hbm:tile.ore_gneiss_copper",
            "hbm:tile.ore_gneiss_asbestos",
            "hbm:tile.ore_gneiss_lithium",
            "hbm:tile.ore_gneiss_schrabidium",
            "hbm:tile.ore_gneiss_rare",
            "hbm:tile.ore_gneiss_gas",
            "hbm:tile.ore_depth_cinnebar",
            "hbm:tile.ore_depth_zirconium",
            "hbm:tile.ore_depth_borax",
            "hbm:tile.ore_depth_nether_neodymium"
    };

    private VeinDefinitions() {
    }

    private static VeinType.Ore ore(String blockName, int weight) {
        return new VeinType.Ore(blockName, weight);
    }
}
