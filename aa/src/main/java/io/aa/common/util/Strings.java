package io.aa.common.util;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public final class Strings {

    public static boolean toBoolean(String value) {
        requireNonNull(value, "value");
        return Boolean.parseBoolean(value);
    }

    public static int toInt(String value) {
        requireNonNull(value, "value");
        return Integer.parseInt(value);
    }

    public static long toLong(String value) {
        requireNonNull(value, "value");
        return Long.parseLong(value);
    }

    public static float toFloat(String value) {
        requireNonNull(value, "value");
        return Float.parseFloat(value);
    }

    public static double toDouble(String value) {
        requireNonNull(value, "value");
        return Double.parseDouble(value);
    }

    public static String fromObject(Object value) {
        requireNonNull(value, "value");
        return Objects.toString(value);
    }

    public static String fromBoolean(boolean value) {
        return Boolean.toString(value);
    }

    public static String fromInt(int value) {
        return Integer.toString(value);
    }

    public static String fromLong(long value) {
        return Long.toString(value);
    }

    public static String fromFloat(float value) {
        return Float.toString(value);
    }

    public static String fromDouble(double value) {
        return Double.toString(value);
    }

    private Strings() {}
}
