package com.ntge.ntgecore.proxy;

import com.ntge.ntgecore.config.NtgeConfig;
import com.ntge.ntgecore.worldgen.OreVeinGenerator;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        NtgeConfig config = NtgeConfig.load(event.getModConfigurationDirectory());
        if (config.oreGenEnabled) {
            OreVeinGenerator.register();
        }
    }
}
