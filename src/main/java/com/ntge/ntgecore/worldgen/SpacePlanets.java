package com.ntge.ntgecore.worldgen;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

final class SpacePlanets {

    private static final Logger LOG = Logger.getLogger("NtgeCore");
    private static final String CONFIG_CLASS = "com.hbm.config.SpaceConfig";

    private SpacePlanets() {
    }

    static Map<Integer, Integer> resolveDimensionOverrides() {
        Map<Integer, Integer> overrides = new HashMap<Integer, Integer>();
        addOverride(overrides, VeinDefinitions.MOON, "moonDimension");
        addOverride(overrides, VeinDefinitions.DUNA, "dunaDimension");
        addOverride(overrides, VeinDefinitions.IKE, "ikeDimension");
        addOverride(overrides, VeinDefinitions.EVE, "eveDimension");
        addOverride(overrides, VeinDefinitions.DRES, "dresDimension");
        addOverride(overrides, VeinDefinitions.MOHO, "mohoDimension");
        addOverride(overrides, VeinDefinitions.MINMUS, "minmusDimension");
        addOverride(overrides, VeinDefinitions.LAYTHE, "laytheDimension");
        addOverride(overrides, VeinDefinitions.TEKTO, "tektoDimension");
        return overrides;
    }

    private static void addOverride(Map<Integer, Integer> overrides, int defaultDimension, String fieldName) {
        try {
            int actual = Class.forName(CONFIG_CLASS).getField(fieldName).getInt(null);
            if (actual != defaultDimension) {
                overrides.put(defaultDimension, actual);
                LOG.info("NTM: Space '" + fieldName + "' is " + actual + " (default " + defaultDimension + "), adjusting");
            }
        } catch (Exception e) {
            // No
        }
    }
}
