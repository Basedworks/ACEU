package com.github.basedworks.aceu.config;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Properties-based implementation of the IConfigable interface.
 * Provides functionality to load, save and manipulate configuration data using Java Properties.
 * This implementation is thread-safe and optimized for performance.
 *
 * @author basedworks
 * @version 2.0
 */
public class PropertiesConfig implements IConfigable {
    private final File file;
    private final Properties properties;
    private final Map<String, Object> cache;

    /**
     * Constructs a new PropertiesConfig with the specified file.
     * Initializes an internal cache for optimized property access.
     *
     * @param file The file to store the properties
     * @throws NullPointerException if file is null
     */
    public PropertiesConfig(File file) {
        this.file = Objects.requireNonNull(file, "File cannot be null");
        this.properties = new Properties();
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Loads the properties from the file. Creates a new file if it doesn't exist.
     * 
     * @throws RuntimeException if loading fails
     */
    @Override
    public synchronized void load() {
        try {
            if (!file.exists()) {
                createNewFile();
                return;
            }
            loadExistingFile();
            refreshCache();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    /**
     * Creates a new properties file and its parent directories if they don't exist.
     *
     * @throws IOException if file creation fails
     */
    private void createNewFile() throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create parent directories");
        }
        if (!file.createNewFile()) {
            throw new IOException("Failed to create new file");
        }
        save();
    }

    /**
     * Loads properties from an existing file.
     *
     * @throws IOException if reading fails
     */
    private void loadExistingFile() throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            properties.load(bis);
        }
    }

    /**
     * Refreshes the internal cache with current property values.
     */
    private void refreshCache() {
        cache.clear();
        properties.stringPropertyNames().forEach(key -> 
            cache.put(key, properties.getProperty(key)));
    }

    /**
     * Saves the properties to the file.
     *
     * @throws RuntimeException if saving fails
     */
    @Override
    public synchronized void save() {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            properties.store(bos, null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save properties file", e);
        }
    }

    /**
     * Deletes the properties file.
     *
     * @throws RuntimeException if deletion fails
     */
    @Override
    public void delete() {
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Failed to delete file: " + file.getAbsolutePath());
        }
        cache.clear();
    }

    /**
     * Gets a property value from the cache.
     *
     * @param path the property key
     * @return the property value, or null if not found
     * @throws NullPointerException if path is null
     */
    @Override
    public Object get(String path) {
        Objects.requireNonNull(path, "Path cannot be null");
        return cache.get(path);
    }

    /**
     * Sets a property value and updates the cache.
     *
     * @param path the property key
     * @param value the property value
     * @throws NullPointerException if path or value is null
     */
    @Override
    public synchronized void set(String path, Object value) {
        Objects.requireNonNull(path, "Path cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        String stringValue = String.valueOf(value);
        properties.setProperty(path, stringValue);
        cache.put(path, stringValue);
    }

    /**
     * Checks if a property exists.
     *
     * @param path the property key
     * @return true if the property exists
     * @throws NullPointerException if path is null
     */
    @Override
    public boolean contains(String path) {
        Objects.requireNonNull(path, "Path cannot be null");
        return cache.containsKey(path);
    }

    /**
     * No-op as Properties don't support sections.
     *
     * @param path ignored
     */
    @Override
    public void createSection(String path) {
        // Properties don't support sections - no-op
    }

    /**
     * Removes a section (equivalent to removing a property).
     *
     * @param path the section/property to remove
     */
    @Override
    public void removeSection(String path) {
        remove(path);
    }

    /**
     * Gets all property keys.
     *
     * @param deep ignored as Properties are flat
     * @return set of property keys
     */
    @Override
    public Set<String> getKeys(boolean deep) {
        return new HashSet<>(cache.keySet());
    }

    /**
     * No-op as Properties don't support sections.
     *
     * @param path ignored
     * @return null
     */
    @Override
    public IConfigableSection getConfigurationSection(String path) {
        return null;
    }

    /**
     * Gets all properties as a map.
     *
     * @param deep ignored as Properties are flat
     * @return map of all properties
     */
    @Override
    public Map<String, Object> getValues(boolean deep) {
        return new HashMap<>(cache);
    }

    /**
     * Adds a default value if the property doesn't exist.
     *
     * @param path the property key
     * @param value the default value
     */
    @Override
    public synchronized void addDefault(String path, Object value) {
        if (!contains(path)) {
            set(path, value);
        }
    }

    /**
     * Sets multiple default values.
     *
     * @param defaults map of default values
     * @throws NullPointerException if defaults is null
     */
    @Override
    public void setDefaults(Map<String, Object> defaults) {
        Objects.requireNonNull(defaults, "Defaults map cannot be null");
        defaults.forEach(this::addDefault);
    }

    /**
     * Loads properties from a string.
     *
     * @param contents the string containing properties
     * @throws RuntimeException if loading fails
     * @throws NullPointerException if contents is null
     */
    @Override
    public synchronized void loadFromString(String contents) {
        Objects.requireNonNull(contents, "Contents cannot be null");
        try (StringReader reader = new StringReader(contents)) {
            properties.load(reader);
            refreshCache();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from string", e);
        }
    }

    /**
     * Saves properties to a string.
     *
     * @return string representation of properties
     * @throws RuntimeException if saving fails
     */
    @Override
    public String saveToString() {
        try (StringWriter writer = new StringWriter()) {
            properties.store(writer, null);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save properties to string", e);
        }
    }

    /**
     * No-op as Properties don't support options.
     *
     * @param path ignored
     * @param deep ignored
     */
    @Override
    public void options(String path, boolean deep) {
        // Properties don't support options - no-op
    }

    /**
     * Reloads properties from the file.
     */
    @Override
    public void reload() {
        load();
    }

    /**
     * Gets a property value as a String.
     *
     * @param path the property key
     * @return the property value as String, or null if not found
     */
    @Override
    public String getString(String path) {
        Object value = get(path);
        return value != null ? value.toString() : null;
    }

    /**
     * Gets a property value as a String with default.
     *
     * @param path the property key
     * @param def the default value
     * @return the property value as String, or default if not found
     */
    @Override
    public String getString(String path, String def) {
        String value = getString(path);
        return value != null ? value : def;
    }

    /**
     * Gets a property value as an int.
     *
     * @param path the property key
     * @return the property value as int, or 0 if not found/invalid
     */
    @Override
    public int getInt(String path) {
        return getInt(path, 0);
    }

    /**
     * Gets a property value as an int with default.
     *
     * @param path the property key
     * @param def the default value
     * @return the property value as int, or default if not found/invalid
     */
    @Override
    public int getInt(String path, int def) {
        String value = getString(path);
        try {
            return value != null ? Integer.parseInt(value.trim()) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Gets a property value as a boolean.
     *
     * @param path the property key
     * @return the property value as boolean, or false if not found
     */
    @Override
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    /**
     * Gets a property value as a boolean with default.
     *
     * @param path the property key
     * @param def the default value
     * @return the property value as boolean, or default if not found
     */
    @Override
    public boolean getBoolean(String path, boolean def) {
        String value = getString(path);
        return value != null ? Boolean.parseBoolean(value.trim()) : def;
    }

    /**
     * Gets a property value as a double.
     *
     * @param path the property key
     * @return the property value as double, or 0.0 if not found/invalid
     */
    @Override
    public double getDouble(String path) {
        return getDouble(path, 0.0);
    }

    /**
     * Gets a property value as a double with default.
     *
     * @param path the property key
     * @param def the default value
     * @return the property value as double, or default if not found/invalid
     */
    @Override
    public double getDouble(String path, double def) {
        String value = getString(path);
        try {
            return value != null ? Double.parseDouble(value.trim()) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Gets a property value as a long.
     *
     * @param path the property key
     * @return the property value as long, or 0L if not found/invalid
     */
    @Override
    public long getLong(String path) {
        return getLong(path, 0L);
    }

    /**
     * Gets a property value as a long with default.
     *
     * @param path the property key
     * @param def the default value
     * @return the property value as long, or default if not found/invalid
     */
    @Override
    public long getLong(String path, long def) {
        String value = getString(path);
        try {
            return value != null ? Long.parseLong(value.trim()) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Gets a property value as a List.
     *
     * @param path the property key
     * @return the property value as List, or null if not found
     */
    @Override
    public List<?> getList(String path) {
        return getList(path, null);
    }

    /**
     * Gets a property value as a List with default.
     *
     * @param path the property key
     * @param def the default value
     * @return the property value as List, or default if not found
     */
    @Override
    public List<?> getList(String path, List<?> def) {
        String value = getString(path);
        if (value == null) return def;
        return Arrays.stream(value.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
    }

    /**
     * Gets a property value as a List of Strings.
     *
     * @param path the property key
     * @return the property value as List of Strings
     */
    @Override
    public List<String> getStringList(String path) {
        return getTypedList(path, String::valueOf);
    }

    /**
     * Gets a property value as a List of Integers.
     *
     * @param path the property key
     * @return the property value as List of Integers
     */
    @Override
    public List<Integer> getIntegerList(String path) {
        return getTypedList(path, s -> Integer.parseInt(s.toString().trim()));
    }

    /**
     * Gets a property value as a List of Booleans.
     *
     * @param path the property key
     * @return the property value as List of Booleans
     */
    @Override
    public List<Boolean> getBooleanList(String path) {
        return getTypedList(path, s -> Boolean.parseBoolean(s.toString().trim()));
    }

    /**
     * Gets a property value as a List of Doubles.
     *
     * @param path the property key
     * @return the property value as List of Doubles
     */
    @Override
    public List<Double> getDoubleList(String path) {
        return getTypedList(path, s -> Double.parseDouble(s.toString().trim()));
    }

    /**
     * Gets a property value as a List of Floats.
     *
     * @param path the property key
     * @return the property value as List of Floats
     */
    @Override
    public List<Float> getFloatList(String path) {
        return getTypedList(path, s -> Float.parseFloat(s.toString().trim()));
    }

    /**
     * Gets a property value as a List of Longs.
     *
     * @param path the property key
     * @return the property value as List of Longs
     */
    @Override
    public List<Long> getLongList(String path) {
        return getTypedList(path, s -> Long.parseLong(s.toString().trim()));
    }

    /**
     * Gets a property value as a List of Bytes.
     *
     * @param path the property key
     * @return the property value as List of Bytes
     */
    @Override
    public List<Byte> getByteList(String path) {
        return getTypedList(path, s -> Byte.parseByte(s.toString().trim()));
    }

    /**
     * Gets a property value as a List of Characters.
     *
     * @param path the property key
     * @return the property value as List of Characters
     */
    @Override
    public List<Character> getCharacterList(String path) {
        return getTypedList(path, s -> s.toString().trim().charAt(0));
    }

    /**
     * Gets a property value as a List of Shorts.
     *
     * @param path the property key
     * @return the property value as List of Shorts
     */
    @Override
    public List<Short> getShortList(String path) {
        return getTypedList(path, s -> Short.parseShort(s.toString().trim()));
    }

    /**
     * Helper method to convert list elements to a specific type.
     *
     * @param path the property key
     * @param converter the conversion function
     * @return the converted list
     */
    private <T> List<T> getTypedList(String path, ThrowingFunction<Object, T> converter) {
        List<?> list = getList(path);
        if (list == null) return new ArrayList<>();
        
        return list.stream()
                .map(obj -> {
                    try {
                        return converter.apply(obj);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Functional interface for operations that might throw exceptions.
     *
     * @param <T> input type
     * @param <R> return type
     */
    @FunctionalInterface
    private interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }

    /**
     * Gets a property value as a List of Maps.
     *
     * @param path the property key
     * @return the property value as List of Maps
     */
    @Override
    public List<Map<?, ?>> getMapList(String path) {
        List<?> list = getList(path);
        if (list == null) return new ArrayList<>();
        
        return list.stream()
                .filter(obj -> obj instanceof Map)
                .map(obj -> (Map<?, ?>) obj)
                .collect(Collectors.toList());
    }

    /**
     * Removes a property and updates the cache.
     *
     * @param path the property key
     * @throws NullPointerException if path is null
     */
    @Override
    public synchronized void remove(String path) {
        Objects.requireNonNull(path, "Path cannot be null");
        properties.remove(path);
        cache.remove(path);
    }
}
