package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import io.aa.common.util.Strings;

abstract class StringMultimap implements StringMultimapGetters {

    private final ListMultimap<String, String> container;

    StringMultimap() {
        container = LinkedListMultimap.create();
    }

    @Override
    @Nullable
    public final String get(String name) {
        requireNonNull(name, "name");
        final List<String> values = container.get(name);
        if (values.isEmpty()) {
            return null;
        }
        return values.iterator().next();
    }

    @Override
    public final String get(String name, String defaultValue) {
        return getOrDefault(() -> get(name), defaultValue);
    }

    @Override
    @Nullable
    public final String getLast(String name) {
        requireNonNull(name, "name");
        final List<String> values = container.get(name);
        if (values.isEmpty()) {
            return null;
        }
        return values.listIterator(values.size()).previous();
    }

    @Override
    public final String getLast(String name, String defaultValue) {
        return getOrDefault(() -> getLast(name), defaultValue);
    }

    private static String getOrDefault(Supplier<String> supplier, String defaultValue) {
        requireNonNull(supplier, "supplier");
        requireNonNull(defaultValue, "defaultValue");
        final String value = supplier.get();
        return value != null ? value : defaultValue;
    }

    @Override
    public final List<String> getAll(String name) {
        requireNonNull(name, "name");
        return List.copyOf(container.get(name));
    }

    @Override
    public final List<String> getAllReversed(String name) {
        requireNonNull(name, "name");
        return List.copyOf(new ArrayList<>(container.get(name)));
    }

    @Override
    @Nullable
    public final Boolean getBoolean(String name) {
        return toBoolean(get(name));
    }

    @Override
    public final boolean getBoolean(String name, boolean defaultValue) {
        return toBoolean(get(name), defaultValue);
    }

    @Override
    @Nullable
    public final Boolean getLastBoolean(String name) {
        return toBoolean(getLast(name));
    }

    @Override
    public final boolean getLastBoolean(String name, boolean defaultValue) {
        return toBoolean(getLast(name), defaultValue);
    }

    @Nullable
    private static Boolean toBoolean(@Nullable String value) {
        return value != null ? Strings.toBoolean(value) : null;
    }

    private static boolean toBoolean(@Nullable String value, boolean defaultValue) {
        return value != null ? Strings.toBoolean(value) : defaultValue;
    }

    @Override
    @Nullable
    public final Integer getInt(String name) {
        return toInt(get(name));
    }

    @Override
    public final int getInt(String name, int defaultValue) {
        return toInt(get(name), defaultValue);
    }

    @Override
    @Nullable
    public final Integer getLastInt(String name) {
        return toInt(getLast(name));
    }

    @Override
    public final int getLastInt(String name, int defaultValue) {
        return toInt(getLast(name), defaultValue);
    }

    @Nullable
    private static Integer toInt(@Nullable String value) {
        return value != null ? Strings.toInt(value) : null;
    }

    private static int toInt(@Nullable String value, int defaultValue) {
        return value != null ? Strings.toInt(value) : defaultValue;
    }

    @Override
    @Nullable
    public final Long getLong(String name) {
        return toLong(get(name));
    }

    @Override
    public final long getLong(String name, long defaultValue) {
        return toLong(get(name), defaultValue);
    }

    @Override
    @Nullable
    public final Long getLastLong(String name) {
        return toLong(getLast(name));
    }

    @Override
    public final long getLastLong(String name, long defaultValue) {
        return toLong(getLast(name), defaultValue);
    }

    @Nullable
    private static Long toLong(@Nullable String value) {
        return value != null ? Strings.toLong(value) : null;
    }

    private static long toLong(@Nullable String value, long defaultValue) {
        return value != null ? Strings.toLong(value) : defaultValue;
    }

    @Override
    @Nullable
    public final Float getFloat(String name) {
        return toFloat(get(name));
    }

    @Override
    public final float getFloat(String name, float defaultValue) {
        return toFloat(get(name), defaultValue);
    }

    @Override
    @Nullable
    public final Float getLastFloat(String name) {
        return toFloat(getLast(name));
    }

    @Override
    public final float getLastFloat(String name, float defaultValue) {
        return toFloat(getLast(name), defaultValue);
    }

    @Nullable
    private static Float toFloat(@Nullable String value) {
        return value != null ? Strings.toFloat(value) : null;
    }

    private static float toFloat(@Nullable String value, float defaultValue) {
        return value != null ? Strings.toFloat(value) : defaultValue;
    }

    @Override
    @Nullable
    public final Double getDouble(String name) {
        return toDouble(get(name));
    }

    @Override
    public final double getDouble(String name, double defaultValue) {
        return toDouble(get(name), defaultValue);
    }

    @Override
    @Nullable
    public final Double getLastDouble(String name) {
        return toDouble(getLast(name));
    }

    @Override
    public final double getLastDouble(String name, double defaultValue) {
        return toDouble(getLast(name), defaultValue);
    }

    @Nullable
    private static Double toDouble(@Nullable String value) {
        return value != null ? Strings.toDouble(value) : null;
    }

    private static double toDouble(@Nullable String value, double defaultValue) {
        return value != null ? Strings.toDouble(value) : defaultValue;
    }

    @Override
    public final boolean contains(String name) {
        requireNonNull(name, "name");
        return container.containsKey(name);
    }

    @Override
    public final boolean contains(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        for (String val : container.get(name)) {
            if (val.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean containsObject(String name, Object value) {
        return contains(name, Strings.fromObject(value));
    }

    @Override
    public final boolean containsBoolean(String name, boolean value) {
        return contains(name, Strings.fromBoolean(value));
    }

    @Override
    public final boolean containsInt(String name, int value) {
        return contains(name, Strings.fromInt(value));
    }

    @Override
    public final boolean containsLong(String name, long value) {
        return contains(name, Strings.fromLong(value));
    }

    @Override
    public final boolean containsFloat(String name, float value) {
        return contains(name, Strings.fromFloat(value));
    }

    @Override
    public final boolean containsDouble(String name, double value) {
        return contains(name, Strings.fromDouble(value));
    }

    @Override
    public final int size() {
        return container.size();
    }

    @Override
    public final boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public final Collection<Entry<String, String>> entries() {
        return container.entries();
    }

    public final void add(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        if (!contains(name, value)) {
            container.put(name, value);
        }
    }

    public final void add(String name, Iterable<String> values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        for (String value : values) {
            add(name, value);
        }
    }

    public final void add(String name, String... values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        add(name, List.of(values));
    }

    public final void add(Iterable<? extends Entry<String, String>> entries) {
        requireNonNull(entries, "entries");
        for (Entry<String, String> entry : entries) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public final void addObject(String name, Object value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        add(name, Strings.fromObject(value));
    }

    public final void addObject(String name, Iterable<?> values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        for (Object value : values) {
            add(name, Strings.fromObject(value));
        }
    }

    public final void addObject(String name, Object... values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        addObject(name, List.of(values));
    }

    public final void addObject(Iterable<? extends Entry<String, ?>> entries) {
        requireNonNull(entries, "entries");
        for (Entry<String, ?> entry : entries) {
            add(entry.getKey(), Strings.fromObject(entry.getValue()));
        }
    }

    public final void addInt(String name, int value) {
        requireNonNull(name, "name");
        add(name, Strings.fromInt(value));
    }

    public final void addLong(String name, long value) {
        requireNonNull(name, "name");
        add(name, Strings.fromLong(value));
    }

    public final void addFloat(String name, float value) {
        requireNonNull(name, "name");
        add(name, Strings.fromFloat(value));
    }

    public final void addDouble(String name, double value) {
        requireNonNull(name, "name");
        add(name, Strings.fromDouble(value));
    }

    public final void set(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        remove(name);
        add(name, value);
    }

    public final void set(String name, Iterable<String> values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        remove(name);
        add(name, values);
    }

    public final void set(String name, String... values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        set(name, List.of(values));
    }

    public final void set(Iterable<? extends Entry<String, String>> entries) {
        requireNonNull(entries, "entries");
        for (Entry<String, String> entry : entries) {
            set(entry.getKey(), entry.getValue());
        }
    }

    public final void setObject(String name, Object value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        set(name, Strings.fromObject(value));
    }

    public final void setObject(String name, Iterable<?> values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        for (Object value : values) {
            set(name, Strings.fromObject(value));
        }
    }

    public final void setObject(String name, Object... values) {
        requireNonNull(name, "name");
        requireNonNull(values, "values");
        setObject(name, List.of(values));
    }

    public final void setObject(Iterable<? extends Entry<String, ?>> entries) {
        requireNonNull(entries, "entries");
        for (Entry<String, ?> entry : entries) {
            set(entry.getKey(), Strings.fromObject(entry.getValue()));
        }
    }

    public final void setInt(String name, int value) {
        requireNonNull(name, "name");
        set(name, Strings.fromInt(value));
    }

    public final void setLong(String name, long value) {
        requireNonNull(name, "name");
        set(name, Strings.fromLong(value));
    }

    public final void setFloat(String name, float value) {
        requireNonNull(name, "name");
        set(name, Strings.fromFloat(value));
    }

    public final void setDouble(String name, double value) {
        requireNonNull(name, "name");
        set(name, Strings.fromDouble(value));
    }

    public final void remove(String name) {
        requireNonNull(name, "name");
        container.removeAll(name);
    }

    public final void clear() {
        container.clear();
    }
}
