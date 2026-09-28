package com.ntge.ntgecore.worldgen;

final class VeinMath {

    private static final long PRIME_X = 0x9E3779B97F4A7C15L;
    private static final long PRIME_Y = 0xC2B2AE3D27D4EB4FL;
    private static final long PRIME_Z = 0x165667B19E3779F9L;

    private VeinMath() {
    }

    static long mix64(long value) {
        value = (value ^ (value >>> 30)) * 0xBF58476D1CE4E5B9L;
        value = (value ^ (value >>> 27)) * 0x94D049BB133111EBL;
        return value ^ (value >>> 31);
    }

    static long hash64(long seed, int x, int y, int z) {
        return mix64(seed + x * PRIME_X + y * PRIME_Y + z * PRIME_Z);
    }

    static float hash01(long seed, int x, int y, int z) {
        return (hash64(seed, x, y, z) >>> 40) / 16777216F;
    }

    static float oreChance(float density, float distanceSq) {
        return distanceSq >= 1F ? 0F : density * (1F - distanceSq);
    }

    static int pickWeighted(int[] weights, int totalWeight, float roll) {
        int target = (int) (roll * totalWeight);
        for (int i = 0; i < weights.length; i++) {
            target -= weights[i];
            if (target < 0) {
                return i;
            }
        }
        return weights.length - 1;
    }

    static int floorDiv(int value, int divisor) {
        int quotient = value / divisor;
        return value % divisor != 0 && (value ^ divisor) < 0 ? quotient - 1 : quotient;
    }
}
