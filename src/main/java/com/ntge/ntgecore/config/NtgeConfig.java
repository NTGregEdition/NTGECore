package com.ntge.ntgecore.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class NtgeConfig {

    private static final String ORE_GEN_CATEGORY = "oregen";

    public final boolean oreGenEnabled;

    private NtgeConfig(boolean oreGenEnabled) {
        this.oreGenEnabled = oreGenEnabled;
    }

    public static NtgeConfig load(File configDir) {
        Configuration config = new Configuration(new File(configDir, "NtgeCore.cfg"));
        boolean oreGenEnabled = config.getBoolean("enabled", ORE_GEN_CATEGORY, true,
                "Replace ore generation from vanilla and every mod with NTGE ore veins. The vein layout is set in VeinDefinitions.java, not here.");
        if (config.hasChanged()) {
            config.save();
        }
        return new NtgeConfig(oreGenEnabled);
    }
}
