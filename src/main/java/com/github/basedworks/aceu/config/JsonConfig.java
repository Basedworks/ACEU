package com.github.basedworks.aceu.config;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 * JSON implementation of the IConfigable interface for configuration management.
 * Provides functionality to read, write and manipulate JSON configuration files.
 */
public class JsonConfig implements IConfigable {
    private final File file;
    private JSONObject data;

    /**
     * Constructs a new JsonConfig with the specified file.
     *
     * @param file The file to read/write JSON configuration
     */
    public JsonConfig(File file) {
        this.file = file;
        this.data = new JSONObject();
    }

    /**
     * Loads the configuration from the file.
     * Creates a new file with empty JSON object if it doesn't exist.
     */
    @Override
    public void load() {
        if (!file.exists()) {
            createNewFile();
            return;
        }
        loadExistingFile();
    }

    private void createNewFile() {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create new config file", e);
        }
    }

    private void loadExistingFile() {
        try {
            String content = Files.readString(file.toPath());
            data = new JSONObject(content.isEmpty() ? "{}" : content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }

    /**
     * Saves the current configuration to the file.
     */
    @Override
    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file", e);
        }
    }

    /**
     * Deletes the configuration file if it exists.
     */
    @Override
    public void delete() {
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Failed to delete config file");
        }
    }

    /**
     * Reloads the configuration from the file.
     */
    @Override
    public void reload() {
        load();
    }

    /**
     * Sets a value at the specified path in the configuration.
     *
     * @param path The path where to set the value
     * @param value The value to set
     */
    @Override
    public void set(String path, Object value) {
        String[] parts = path.split("\\.");
        JSONObject current = data;
        
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            JSONObject next = current.optJSONObject(part);
            if (next == null) {
                next = new JSONObject();
                current.put(part, next);
            }
            current = next;
        }
        
        current.put(parts[parts.length - 1], value);
    }

    /**
     * Gets a value from the specified path in the configuration.
     *
     * @param path The path to get the value from
     * @return The value at the specified path, or null if not found
     */
    @Override
    public Object get(String path) {
        String[] parts = path.split("\\.");
        JSONObject current = data;
        
        for (int i = 0; i < parts.length - 1; i++) {
            current = current.optJSONObject(parts[i]);
            if (current == null) {
                return null;
            }
        }
        
        return current.opt(parts[parts.length - 1]);
    }

    /**
     * Gets a string value from the specified path.
     *
     * @param path The path to get the string from
     * @return The string value, or null if not found
     */
    @Override
    public String getString(String path) {
        return getString(path, null);
    }

    /**
     * Gets a string value from the specified path with a default value.
     *
     * @param path The path to get the string from
     * @param def The default value if not found
     * @return The string value, or the default if not found
     */
    @Override
    public String getString(String path, String def) {
        Object value = get(path);
        return value instanceof String ? (String) value : def;
    }

    /**
     * Gets an integer value from the specified path.
     *
     * @param path The path to get the integer from
     * @return The integer value, or 0 if not found
     */
    @Override
    public int getInt(String path) {
        return getInt(path, 0);
    }

    /**
     * Gets an integer value from the specified path with a default value.
     *
     * @param path The path to get the integer from
     * @param def The default value if not found
     * @return The integer value, or the default if not found
     */
    @Override
    public int getInt(String path, int def) {
        Object value = get(path);
        return value instanceof Number ? ((Number) value).intValue() : def;
    }

    /**
     * Gets a double value from the specified path.
     *
     * @param path The path to get the double from
     * @return The double value, or 0.0 if not found
     */
    @Override
    public double getDouble(String path) {
        return getDouble(path, 0.0);
    }

    /**
     * Gets a double value from the specified path with a default value.
     *
     * @param path The path to get the double from
     * @param def The default value if not found
     * @return The double value, or the default if not found
     */
    @Override
    public double getDouble(String path, double def) {
        Object value = get(path);
        return value instanceof Number ? ((Number) value).doubleValue() : def;
    }

    /**
     * Gets a long value from the specified path.
     *
     * @param path The path to get the long from
     * @return The long value, or 0L if not found
     */
    @Override
    public long getLong(String path) {
        return getLong(path, 0L);
    }

    /**
     * Gets a long value from the specified path with a default value.
     *
     * @param path The path to get the long from
     * @param def The default value if not found
     * @return The long value, or the default if not found
     */
    @Override
    public long getLong(String path, long def) {
        Object value = get(path);
        return value instanceof Number ? ((Number) value).longValue() : def;
    }

    /**
     * Gets a boolean value from the specified path.
     *
     * @param path The path to get the boolean from
     * @return The boolean value, or false if not found
     */
    @Override
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    /**
     * Gets a boolean value from the specified path with a default value.
     *
     * @param path The path to get the boolean from
     * @param def The default value if not found
     * @return The boolean value, or the default if not found
     */
    @Override
    public boolean getBoolean(String path, boolean def) {
        Object value = get(path);
        return value instanceof Boolean ? (Boolean) value : def;
    }

    /**
     * Gets a list from the specified path.
     *
     * @param path The path to get the list from
     * @return The list, or null if not found
     */
    @Override
    public List<?> getList(String path) {
        return getList(path, null);
    }

    /**
     * Gets a list from the specified path with a default value.
     *
     * @param path The path to get the list from
     * @param def The default value if not found
     * @return The list, or the default if not found
     */
    @Override
    public List<?> getList(String path, List<?> def) {
        Object value = get(path);
        if (!(value instanceof JSONArray)) {
            return def;
        }
        
        JSONArray array = (JSONArray) value;
        List<Object> result = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            result.add(array.get(i));
        }
        return result;
    }

    /**
     * Gets a list of strings from the specified path.
     *
     * @param path The path to get the string list from
     * @return The list of strings
     */
    @Override
    public List<String> getStringList(String path) {
        List<?> list = getList(path);
        if (list == null) {
            return new ArrayList<>();
        }
        
        List<String> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof String) {
                result.add((String) obj);
            }
        }
        return result;
    }

    /**
     * Gets a list of integers from the specified path.
     *
     * @param path The path to get the integer list from
     * @return The list of integers
     */
    @Override
    public List<Integer> getIntegerList(String path) {
        return getNumberList(path, Number::intValue);
    }

    /**
     * Gets a list of booleans from the specified path.
     *
     * @param path The path to get the boolean list from
     * @return The list of booleans
     */
    @Override
    public List<Boolean> getBooleanList(String path) {
        List<?> list = getList(path);
        if (list == null) {
            return new ArrayList<>();
        }
        
        List<Boolean> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Boolean) {
                result.add((Boolean) obj);
            }
        }
        return result;
    }

    /**
     * Gets a list of doubles from the specified path.
     *
     * @param path The path to get the double list from
     * @return The list of doubles
     */
    @Override
    public List<Double> getDoubleList(String path) {
        return getNumberList(path, Number::doubleValue);
    }

    /**
     * Gets a list of floats from the specified path.
     *
     * @param path The path to get the float list from
     * @return The list of floats
     */
    @Override
    public List<Float> getFloatList(String path) {
        return getNumberList(path, Number::floatValue);
    }

    /**
     * Gets a list of longs from the specified path.
     *
     * @param path The path to get the long list from
     * @return The list of longs
     */
    @Override
    public List<Long> getLongList(String path) {
        return getNumberList(path, Number::longValue);
    }

    /**
     * Gets a list of bytes from the specified path.
     *
     * @param path The path to get the byte list from
     * @return The list of bytes
     */
    @Override
    public List<Byte> getByteList(String path) {
        return getNumberList(path, Number::byteValue);
    }

    /**
     * Gets a list of characters from the specified path.
     *
     * @param path The path to get the character list from
     * @return The list of characters
     */
    @Override
    public List<Character> getCharacterList(String path) {
        List<?> list = getList(path);
        if (list == null) {
            return new ArrayList<>();
        }
        
        List<Character> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof String && ((String) obj).length() == 1) {
                result.add(((String) obj).charAt(0));
            }
        }
        return result;
    }

    /**
     * Gets a list of shorts from the specified path.
     *
     * @param path The path to get the short list from
     * @return The list of shorts
     */
    @Override
    public List<Short> getShortList(String path) {
        return getNumberList(path, Number::shortValue);
    }

    private <T> List<T> getNumberList(String path, java.util.function.Function<Number, T> converter) {
        List<?> list = getList(path);
        if (list == null) {
            return new ArrayList<>();
        }
        
        List<T> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) {
                result.add(converter.apply((Number) obj));
            }
        }
        return result;
    }

    /**
     * Gets a list of maps from the specified path.
     *
     * @param path The path to get the map list from
     * @return The list of maps
     */
    @Override
    public List<Map<?, ?>> getMapList(String path) {
        List<?> list = getList(path);
        if (list == null) {
            return new ArrayList<>();
        }
        
        List<Map<?, ?>> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof JSONObject) {
                result.add(((JSONObject) obj).toMap());
            }
        }
        return result;
    }

    /**
     * Checks if the configuration contains a value at the specified path.
     *
     * @param path The path to check
     * @return true if the path exists, false otherwise
     */
    @Override
    public boolean contains(String path) {
        String[] parts = path.split("\\.");
        JSONObject current = data;
        
        for (int i = 0; i < parts.length - 1; i++) {
            current = current.optJSONObject(parts[i]);
            if (current == null) {
                return false;
            }
        }
        
        return current.has(parts[parts.length - 1]);
    }

    /**
     * Creates a new section at the specified path.
     *
     * @param path The path where to create the section
     */
    @Override
    public void createSection(String path) {
        set(path, new JSONObject());
    }

    /**
     * Removes a section at the specified path.
     *
     * @param path The path of the section to remove
     */
    @Override
    public void removeSection(String path) {
        remove(path);
    }

    /**
     * Gets all keys in the configuration.
     *
     * @param deep Whether to get nested keys
     * @return Set of keys
     */
    @Override
    public Set<String> getKeys(boolean deep) {
        if (!deep) {
            return new HashSet<>(data.keySet());
        }
        
        Set<String> keys = new HashSet<>();
        collectKeys(data, "", keys);
        return keys;
    }

    private void collectKeys(JSONObject obj, String prefix, Set<String> keys) {
        for (String key : obj.keySet()) {
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;
            keys.add(fullKey);
            
            Object value = obj.opt(key);
            if (value instanceof JSONObject) {
                collectKeys((JSONObject) value, fullKey, keys);
            }
        }
    }

    /**
     * Gets a configuration section at the specified path.
     *
     * @param path The path to get the section from
     * @return The configuration section, or null if not found
     */
    @Override
    public IConfigableSection getConfigurationSection(String path) {
        Object obj = get(path);
        if (!(obj instanceof JSONObject)) {
            return null;
        }
        
        ConfigableSection section = new ConfigableSection();
        section.setDefaults(((JSONObject) obj).toMap());
        return section;
    }

    /**
     * Gets all values in the configuration.
     *
     * @param deep Whether to get nested values
     * @return Map of values
     */
    @Override
    public Map<String, Object> getValues(boolean deep) {
        if (!deep) {
            return new HashMap<>(data.toMap());
        }
        
        Map<String, Object> values = new HashMap<>();
        collectValues(data, "", values);
        return values;
    }

    private void collectValues(JSONObject obj, String prefix, Map<String, Object> values) {
        for (String key : obj.keySet()) {
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;
            Object value = obj.get(key);
            
            if (value instanceof JSONObject) {
                collectValues((JSONObject) value, fullKey, values);
            } else {
                values.put(fullKey, value);
            }
        }
    }

    /**
     * Adds a default value at the specified path if it doesn't exist.
     *
     * @param path The path to add the default value
     * @param value The default value
     */
    @Override
    public void addDefault(String path, Object value) {
        if (!contains(path)) {
            set(path, value);
        }
    }

    /**
     * Sets multiple default values.
     *
     * @param defaults Map of default values
     */
    @Override
    public void setDefaults(Map<String, Object> defaults) {
        defaults.forEach(this::addDefault);
    }

    /**
     * Sets options for the configuration.
     * Note: JSON doesn't support headers, so this method does nothing.
     *
     * @param header Header text
     * @param copyDefaults Whether to copy defaults
     */
    @Override
    public void options(String header, boolean copyDefaults) {
        // JSON doesn't support headers
    }

    /**
     * Saves the configuration to a string.
     *
     * @return The configuration as a JSON string
     */
    @Override
    public String saveToString() {
        return data.toString(2);
    }

    /**
     * Loads the configuration from a string.
     *
     * @param contents The JSON string to load
     */
    @Override
    public void loadFromString(String contents) {
        try {
            data = new JSONObject(contents);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config from string", e);
        }
    }

    /**
     * Removes a value at the specified path.
     *
     * @param path The path of the value to remove
     */
    @Override
    public void remove(String path) {
        String[] parts = path.split("\\.");
        JSONObject current = data;
        
        for (int i = 0; i < parts.length - 1; i++) {
            current = current.optJSONObject(parts[i]);
            if (current == null) {
                return;
            }
        }
        
        current.remove(parts[parts.length - 1]);
    }
}