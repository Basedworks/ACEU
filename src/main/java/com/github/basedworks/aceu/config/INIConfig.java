package com.github.basedworks.aceu.config;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import java.io.*;
import java.util.*;

/**
 * INI configuration implementation of IConfigable interface.
 * Provides functionality to read, write and manage INI format configuration files.
 */
public class INIConfig implements IConfigable {
    private final File file;
    private Ini ini;

    /**
     * Constructs a new INIConfig with the specified file.
     *
     * @param file The INI file to read from/write to
     */
    public INIConfig(File file) {
        this.file = file;
        this.ini = new Ini();
    }

    /**
     * Loads the configuration from the file.
     * Creates a new file if it doesn't exist.
     */
    @Override
    public void load() {
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                save();
                return;
            }
            ini.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration to the file.
     */
    @Override
    public void save() {
        try {
            ini.store(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the configuration file if it exists.
     */
    @Override
    public void delete() {
        if (file.exists()) {
            file.delete();
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
     * Gets a value from the configuration using dot notation (section.key).
     *
     * @param path The path to the value
     * @return The value at the specified path, or null if not found
     */
    @Override
    public Object get(String path) {
        String[] parts = splitPath(path);
        if (parts == null) return null;
        Section section = ini.get(parts[0]);
        return section != null ? section.get(parts[1]) : null;
    }

    /**
     * Gets a string value from the configuration.
     *
     * @param path The path to the value
     * @return The string value, or null if not found
     */
    @Override
    public String getString(String path) {
        Object value = get(path);
        return value != null ? value.toString() : null;
    }

    /**
     * Gets a string value with a default fallback.
     *
     * @param path The path to the value
     * @param def The default value
     * @return The string value, or default if not found
     */
    @Override
    public String getString(String path, String def) {
        String value = getString(path);
        return value != null ? value : def;
    }

    /**
     * Gets an integer value from the configuration.
     *
     * @param path The path to the value
     * @return The integer value, or 0 if not found/invalid
     */
    @Override
    public int getInt(String path) {
        return parseNumber(getString(path), Integer::parseInt, 0);
    }

    /**
     * Gets an integer value with a default fallback.
     *
     * @param path The path to the value
     * @param def The default value
     * @return The integer value, or default if not found/invalid
     */
    @Override
    public int getInt(String path, int def) {
        return parseNumber(getString(path), Integer::parseInt, def);
    }

    /**
     * Gets a boolean value from the configuration.
     *
     * @param path The path to the value
     * @return The boolean value, or false if not found
     */
    @Override
    public boolean getBoolean(String path) {
        String value = getString(path);
        return value != null && Boolean.parseBoolean(value);
    }

    /**
     * Gets a boolean value with a default fallback.
     *
     * @param path The path to the value
     * @param def The default value
     * @return The boolean value, or default if not found
     */
    @Override
    public boolean getBoolean(String path, boolean def) {
        String value = getString(path);
        return value != null ? Boolean.parseBoolean(value) : def;
    }

    /**
     * Gets a double value from the configuration.
     *
     * @param path The path to the value
     * @return The double value, or 0.0 if not found/invalid
     */
    @Override
    public double getDouble(String path) {
        return parseNumber(getString(path), Double::parseDouble, 0.0);
    }

    /**
     * Gets a double value with a default fallback.
     *
     * @param path The path to the value
     * @param def The default value
     * @return The double value, or default if not found/invalid
     */
    @Override
    public double getDouble(String path, double def) {
        return parseNumber(getString(path), Double::parseDouble, def);
    }

    /**
     * Gets a long value from the configuration.
     *
     * @param path The path to the value
     * @return The long value, or 0L if not found/invalid
     */
    @Override
    public long getLong(String path) {
        return parseNumber(getString(path), Long::parseLong, 0L);
    }

    /**
     * Gets a long value with a default fallback.
     *
     * @param path The path to the value
     * @param def The default value
     * @return The long value, or default if not found/invalid
     */
    @Override
    public long getLong(String path, long def) {
        return parseNumber(getString(path), Long::parseLong, def);
    }

    /**
     * Gets a list from the configuration.
     *
     * @param path The path to the list
     * @return The list of values, or empty list if not found
     */
    @Override
    public List<?> getList(String path) {
        String[] parts = splitPath(path);
        if (parts == null) return Collections.emptyList();
        
        Section section = ini.get(parts[0]);
        if (section == null) return Collections.emptyList();
        
        String value = section.get(parts[1]);
        if (value == null) return Collections.emptyList();
        
        return Arrays.asList(value.split(","));
    }

    /**
     * Gets a list with a default fallback.
     *
     * @param path The path to the list
     * @param def The default list
     * @return The list of values, or default if not found
     */
    @Override
    public List<?> getList(String path, List<?> def) {
        List<?> list = getList(path);
        return list.isEmpty() ? def : list;
    }

    /**
     * Gets a list of strings from the configuration.
     *
     * @param path The path to the list
     * @return The list of strings
     */
    @Override
    public List<String> getStringList(String path) {
        return convertList(getList(path), Object::toString);
    }

    /**
     * Gets a list of integers from the configuration.
     *
     * @param path The path to the list
     * @return The list of integers
     */
    @Override
    public List<Integer> getIntegerList(String path) {
        return convertList(getStringList(path), s -> {
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    /**
     * Gets a list of booleans from the configuration.
     *
     * @param path The path to the list
     * @return The list of booleans
     */
    @Override
    public List<Boolean> getBooleanList(String path) {
        return convertList(getStringList(path), s -> Boolean.parseBoolean(s.trim()));
    }

    /**
     * Gets a list of doubles from the configuration.
     *
     * @param path The path to the list
     * @return The list of doubles
     */
    @Override
    public List<Double> getDoubleList(String path) {
        return convertList(getStringList(path), s -> {
            try {
                return Double.parseDouble(s.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    /**
     * Gets a list of floats from the configuration.
     *
     * @param path The path to the list
     * @return The list of floats
     */
    @Override
    public List<Float> getFloatList(String path) {
        return convertList(getStringList(path), s -> {
            try {
                return Float.parseFloat(s.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    /**
     * Gets a list of longs from the configuration.
     *
     * @param path The path to the list
     * @return The list of longs
     */
    @Override
    public List<Long> getLongList(String path) {
        return convertList(getStringList(path), s -> {
            try {
                return Long.parseLong(s.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    /**
     * Gets a list of bytes from the configuration.
     *
     * @param path The path to the list
     * @return The list of bytes
     */
    @Override
    public List<Byte> getByteList(String path) {
        return convertList(getStringList(path), s -> {
            try {
                return Byte.parseByte(s.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    /**
     * Gets a list of characters from the configuration.
     *
     * @param path The path to the list
     * @return The list of characters
     */
    @Override
    public List<Character> getCharacterList(String path) {
        return convertList(getStringList(path), s -> !s.isEmpty() ? s.charAt(0) : null);
    }

    /**
     * Gets a list of shorts from the configuration.
     *
     * @param path The path to the list
     * @return The list of shorts
     */
    @Override
    public List<Short> getShortList(String path) {
        return convertList(getStringList(path), s -> {
            try {
                return Short.parseShort(s.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    /**
     * Gets a list of maps from the configuration.
     *
     * @param path The path to the list
     * @return The list of maps
     */
    @Override
    public List<Map<?, ?>> getMapList(String path) {
        List<String> list = getStringList(path);
        List<Map<?, ?>> result = new ArrayList<>();
        
        for (String str : list) {
            Map<String, String> map = new HashMap<>();
            for (String pair : str.split(";")) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    map.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
            result.add(map);
        }
        return result;
    }

    /**
     * Sets a value in the configuration.
     *
     * @param path The path to set
     * @param value The value to set
     */
    @Override
    public void set(String path, Object value) {
        String[] parts = splitPath(path);
        if (parts == null) return;
        
        Section section = ini.get(parts[0]);
        if (section == null) {
            section = ini.add(parts[0]);
        }
        section.put(parts[1], value != null ? value.toString() : null);
    }

    /**
     * Checks if a path exists in the configuration.
     *
     * @param path The path to check
     * @return true if the path exists
     */
    @Override
    public boolean contains(String path) {
        String[] parts = splitPath(path);
        if (parts == null) return false;
        Section section = ini.get(parts[0]);
        return section != null && section.containsKey(parts[1]);
    }

    /**
     * Creates a new section in the configuration.
     *
     * @param path The section path
     */
    @Override
    public void createSection(String path) {
        ini.add(path);
    }

    /**
     * Removes a section from the configuration.
     *
     * @param path The section path
     */
    @Override
    public void removeSection(String path) {
        ini.remove(path);
    }

    /**
     * Gets all section keys from the configuration.
     *
     * @param deep Whether to get nested keys
     * @return Set of keys
     */
    @Override
    public Set<String> getKeys(boolean deep) {
        return ini.keySet();
    }

    /**
     * Gets a configuration section.
     *
     * @param path The section path
     * @return The configuration section
     */
    @Override
    public IConfigableSection getConfigurationSection(String path) {
        return null;
    }

    /**
     * Gets all values from the configuration.
     *
     * @param deep Whether to get nested values
     * @return Map of all values
     */
    @Override
    public Map<String, Object> getValues(boolean deep) {
        Map<String, Object> values = new HashMap<>();
        for (String sectionName : ini.keySet()) {
            Section section = ini.get(sectionName);
            for (String key : section.keySet()) {
                values.put(sectionName + "." + key, section.get(key));
            }
        }
        return values;
    }

    /**
     * Adds a default value if the path doesn't exist.
     *
     * @param path The path
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
     * Loads configuration from a string.
     *
     * @param contents The configuration contents
     */
    @Override
    public void loadFromString(String contents) {
        try {
            ini = new Ini(new ByteArrayInputStream(contents.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves configuration to a string.
     *
     * @return The configuration as a string
     */
    @Override
    public String saveToString() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ini.store(outputStream);
            return outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Sets options for a section.
     *
     * @param path The section path
     * @param deep Whether to set nested options
     */
    @Override
    public void options(String path, boolean deep) {
        if (!ini.containsKey(path)) {
            ini.add(path);
        }
    }

    /**
     * Removes a value from the configuration.
     *
     * @param path The path to remove
     */
    @Override
    public void remove(String path) {
        String[] parts = splitPath(path);
        if (parts == null) return;
        Section section = ini.get(parts[0]);
        if (section != null) {
            section.remove(parts[1]);
        }
    }

    // Helper methods

    private String[] splitPath(String path) {
        String[] parts = path.split("\\.");
        return parts.length >= 2 ? parts : null;
    }

    private <T> T parseNumber(String value, ThrowingFunction<String, T> parser, T defaultValue) {
        if (value == null) return defaultValue;
        try {
            return parser.apply(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private <T> List<T> convertList(List<?> list, ThrowingFunction<String, T> converter) {
        List<T> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj != null) {
                try {
                  T converted = converter.apply(obj.toString());
                  if (converted != null) {
                      result.add(converted);
                  }
                } catch (Exception e) {
                  
                }
            }
        }
        return result;
    }

    @FunctionalInterface
    private interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
