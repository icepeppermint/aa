package io.aa.common;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

abstract class StringMultimapBuilder<CONTAINER extends StringMultimap, SELF> {

    private final CONTAINER container;

    StringMultimapBuilder(CONTAINER container) {
        this.container = requireNonNull(container, "container");
    }

    @Nullable
    public final String get(String name) {
        return container.get(name);
    }

    public final String get(String name, String defaultValue) {
        return container.get(name, defaultValue);
    }

    @Nullable
    public final String getLast(String name) {
        return container.getLast(name);
    }

    public final String getLast(String name, String defaultValue) {
        return container.getLast(name, defaultValue);
    }

    public final List<String> getAll(String name) {
        return container.getAll(name);
    }

    public final List<String> getAllReversed(String name) {
        return container.getAllReversed(name);
    }

    @Nullable
    public final Boolean getBoolean(String name) {
        return container.getBoolean(name);
    }

    public final boolean getBoolean(String name, boolean defaultValue) {
        return container.getBoolean(name, defaultValue);
    }

    @Nullable
    public final Boolean getLastBoolean(String name) {
        return container.getLastBoolean(name);
    }

    public final boolean getLastBoolean(String name, boolean defaultValue) {
        return container.getLastBoolean(name, defaultValue);
    }

    @Nullable
    public final Integer getInt(String name) {
        return container.getInt(name);
    }

    public final int getInt(String name, int defaultValue) {
        return container.getInt(name, defaultValue);
    }

    @Nullable
    public final Integer getLastInt(String name) {
        return container.getLastInt(name);
    }

    public final int getLastInt(String name, int defaultValue) {
        return container.getLastInt(name, defaultValue);
    }

    @Nullable
    public final Long getLong(String name) {
        return container.getLong(name);
    }

    public final long getLong(String name, long defaultValue) {
        return container.getLong(name, defaultValue);
    }

    @Nullable
    public final Long getLastLong(String name) {
        return container.getLastLong(name);
    }

    public final long getLastLong(String name, long defaultValue) {
        return container.getLastLong(name, defaultValue);
    }

    @Nullable
    public final Float getFloat(String name) {
        return container.getFloat(name);
    }

    public final float getFloat(String name, float defaultValue) {
        return container.getFloat(name, defaultValue);
    }

    @Nullable
    public final Float getLastFloat(String name) {
        return container.getLastFloat(name);
    }

    public final float getLastFloat(String name, float defaultValue) {
        return container.getLastFloat(name, defaultValue);
    }

    @Nullable
    public final Double getDouble(String name) {
        return container.getDouble(name);
    }

    public final double getDouble(String name, double defaultValue) {
        return container.getDouble(name, defaultValue);
    }

    @Nullable
    public final Double getLastDouble(String name) {
        return container.getLastDouble(name);
    }

    public final double getLastDouble(String name, double defaultValue) {
        return container.getLastDouble(name, defaultValue);
    }

    public final boolean contains(String name) {
        return container.contains(name);
    }

    public final boolean contains(String name, String value) {
        return container.contains(name, value);
    }

    public final boolean containsObject(String name, Object value) {
        return container.containsObject(name, value);
    }

    public final boolean containsBoolean(String name, boolean value) {
        return container.containsBoolean(name, value);
    }

    public final boolean containsInt(String name, int value) {
        return container.containsInt(name, value);
    }

    public final boolean containsLong(String name, long value) {
        return container.containsLong(name, value);
    }

    public final boolean containsFloat(String name, float value) {
        return container.containsFloat(name, value);
    }

    public final boolean containsDouble(String name, double value) {
        return container.containsDouble(name, value);
    }

    public final int size() {
        return container.size();
    }

    public final boolean isEmpty() {
        return container.isEmpty();
    }

    public final Collection<Entry<String, String>> entries() {
        return container.entries();
    }

    public final SELF add(String name, String value) {
        container.add(name, value);
        return self();
    }

    public final SELF add(String name, Iterable<String> values) {
        container.add(name, values);
        return self();
    }

    public final SELF add(String name, String... values) {
        container.add(name, values);
        return self();
    }

    public final SELF add(Iterable<? extends Entry<String, String>> entries) {
        container.add(entries);
        return self();
    }

    public final SELF addObject(String name, Object value) {
        container.addObject(name, value);
        return self();
    }

    public final SELF addObject(String name, Iterable<?> values) {
        container.addObject(name, values);
        return self();
    }

    public final SELF addObject(String name, Object... values) {
        container.addObject(name, values);
        return self();
    }

    public final SELF addObject(Iterable<? extends Entry<String, ?>> entries) {
        container.addObject(entries);
        return self();
    }

    public final SELF addInt(String name, int value) {
        container.addInt(name, value);
        return self();
    }

    public final SELF addLong(String name, long value) {
        container.addLong(name, value);
        return self();
    }

    public final SELF addFloat(String name, float value) {
        container.addFloat(name, value);
        return self();
    }

    public final SELF addDouble(String name, double value) {
        container.addDouble(name, value);
        return self();
    }

    public final SELF set(String name, String value) {
        container.set(name, value);
        return self();
    }

    public final SELF set(String name, Iterable<String> values) {
        container.set(name, values);
        return self();
    }

    public final SELF set(String name, String... values) {
        container.set(name, values);
        return self();
    }

    public final SELF set(Iterable<? extends Entry<String, String>> entries) {
        container.set(entries);
        return self();
    }

    public final SELF setObject(String name, Object value) {
        container.setObject(name, value);
        return self();
    }

    public final SELF setObject(String name, Iterable<?> values) {
        container.setObject(name, values);
        return self();
    }

    public final SELF setObject(String name, Object... values) {
        container.setObject(name, values);
        return self();
    }

    public final SELF setObject(Iterable<? extends Entry<String, ?>> entries) {
        container.setObject(entries);
        return self();
    }

    public final SELF setInt(String name, int value) {
        container.setInt(name, value);
        return self();
    }

    public final SELF setLong(String name, long value) {
        container.setLong(name, value);
        return self();
    }

    public final SELF setFloat(String name, float value) {
        container.setFloat(name, value);
        return self();
    }

    public final SELF setDouble(String name, double value) {
        container.setDouble(name, value);
        return self();
    }

    public final SELF remove(String name) {
        container.remove(name);
        return self();
    }

    public final void clear() {
        container.clear();
    }

    @SuppressWarnings("unchecked")
    protected SELF self() {
        return (SELF) this;
    }
}
