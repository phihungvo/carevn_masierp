package com.carevn.masi.utils;

import java.util.UUID;

public class StringUtils {
    public static String normalizeName(String name) {
        // un accent
        // remove special character (only keep a-z, A-Z, 0-9)
        // remove space
        // to upper case
        return org.apache.commons.lang3.StringUtils.stripAccents(name)
                .replaceAll("[^a-zA-Z0-9]", "")
                .replaceAll("\\s+", "")
                .toUpperCase();
    }

    public static String toString(float value, int decimal) {
        var format = "%." + decimal + "f";
        return String.format(format, value);
    }

    public static String toString(float value) {
        return toString(value, 1);
    }

    public static UUID tryToUUID(String value) {
        return tryToUUID(value, null);
    }

    public static UUID tryToUUID(String value, UUID defaultValue) {
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
