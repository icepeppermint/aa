package io.aa.common;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

interface StringMultimapGetters {

    @Nullable
    String get(String name);

    String get(String name, String defaultValue);

    @Nullable
    String getLast(String name);

    String getLast(String name, String defaultValue);

    List<String> getAll(String name);

    List<String> getAllReversed(String name);

    @Nullable
    Boolean getBoolean(String name);

    boolean getBoolean(String name, boolean defaultValue);

    @Nullable
    Boolean getLastBoolean(String name);

    boolean getLastBoolean(String name, boolean defaultValue);

    @Nullable
    Integer getInt(String name);

    int getInt(String name, int defaultValue);

    @Nullable
    Integer getLastInt(String name);

    int getLastInt(String name, int defaultValue);

    @Nullable
    Long getLong(String name);

    long getLong(String name, long defaultValue);

    @Nullable
    Long getLastLong(String name);

    long getLastLong(String name, long defaultValue);

    @Nullable
    Float getFloat(String name);

    float getFloat(String name, float defaultValue);

    @Nullable
    Float getLastFloat(String name);

    float getLastFloat(String name, float defaultValue);

    @Nullable
    Double getDouble(String name);

    double getDouble(String name, double defaultValue);

    @Nullable
    Double getLastDouble(String name);

    double getLastDouble(String name, double defaultValue);

    boolean contains(String name);

    boolean contains(String name, String value);

    boolean containsObject(String name, Object value);

    boolean containsBoolean(String name, boolean value);

    boolean containsInt(String name, int value);

    boolean containsLong(String name, long value);

    boolean containsFloat(String name, float value);

    boolean containsDouble(String name, double value);

    int size();

    boolean isEmpty();

    Collection<Entry<String, String>> entries();
}
