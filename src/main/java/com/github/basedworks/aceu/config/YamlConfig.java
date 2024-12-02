package com.github.basedworks.aceu.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * YAML configuration implementation of IConfigable interface.
 * Provides methods to load, save and manipulate YAML configuration files.
 */
public class YamlConfig implements IConfigable {
    private final File file;
    private YamlConfiguration config;

    /**
     * Creates a new YamlConfig instance.
     *
     * @param file The file to load/save the configuration from/to
     */
    public YamlConfig(File file) {
        this.file = file;
        this.config = new YamlConfiguration();
    }

    /**
     * Loads the configuration from file. Creates new file if it doesn't exist.
     */
    @Override
    public void load() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration to file.
     */
    @Override
    public void save() {
        try {
            config.save(file);
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
     * Reloads the configuration by calling load().
     */
    @Override
    public void reload() {
        load();
    }

    /**
     * Sets a value at the specified path.
     *
     * @param path The path to set the value at
     * @param value The value to set
     */
    @Override
    public void set(String path, Object value) {
        config.set(path, value);
    }

    /**
     * Gets a value from the specified path.
     *
     * @param path The path to get the value from
     * @return The value at the specified path
     */
    @Override
    public Object get(String path) {
        return config.get(path);
    }

    /**
     * Gets a string from the specified path.
     *
     * @param path The path to get the string from
     * @return The string at the specified path
     */
    @Override
    public String getString(String path) {
        return config.getString(path);
    }

    /**
     * Gets a string from the specified path with a default value.
     *
     * @param path The path to get the string from
     * @param def The default value if the path doesn't exist
     * @return The string at the specified path or the default value
     */
    @Override
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    /**
     * Gets an integer from the specified path.
     *
     * @param path The path to get the integer from
     * @return The integer at the specified path
     */
    @Override
    public int getInt(String path) {
        return config.getInt(path);
    }

    /**
     * Gets an integer from the specified path with a default value.
     *
     * @param path The path to get the integer from
     * @param def The default value if the path doesn't exist
     * @return The integer at the specified path or the default value
     */
    @Override
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * Gets a double from the specified path.
     *
     * @param path The path to get the double from
     * @return The double at the specified path
     */
    @Override
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    /**
     * Gets a double from the specified path with a default value.
     *
     * @param path The path to get the double from
     * @param def The default value if the path doesn't exist
     * @return The double at the specified path or the default value
     */
    @Override
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    /**
     * Gets a long from the specified path.
     *
     * @param path The path to get the long from
     * @return The long at the specified path
     */
    @Override
    public long getLong(String path) {
        return config.getLong(path);
    }

    /**
     * Gets a long from the specified path with a default value.
     *
     * @param path The path to get the long from
     * @param def The default value if the path doesn't exist
     * @return The long at the specified path or the default value
     */
    @Override
    public long getLong(String path, long def) {
        return config.getLong(path, def);
    }

    /**
     * Gets a boolean from the specified path.
     *
     * @param path The path to get the boolean from
     * @return The boolean at the specified path
     */
    @Override
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    /**
     * Gets a boolean from the specified path with a default value.
     *
     * @param path The path to get the boolean from
     * @param def The default value if the path doesn't exist
     * @return The boolean at the specified path or the default value
     */
    @Override
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    /**
     * Gets a list from the specified path.
     *
     * @param path The path to get the list from
     * @return The list at the specified path
     */
    @Override
    public List<?> getList(String path) {
        return config.getList(path);
    }

    /**
     * Gets a list from the specified path with a default value.
     *
     * @param path The path to get the list from
     * @param def The default value if the path doesn't exist
     * @return The list at the specified path or the default value
     */
    @Override
    public List<?> getList(String path, List<?> def) {
        return config.getList(path, def);
    }

    /**
     * Gets a string list from the specified path.
     *
     * @param path The path to get the string list from
     * @return The string list at the specified path
     */
    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    /**
     * Gets an integer list from the specified path.
     *
     * @param path The path to get the integer list from
     * @return The integer list at the specified path
     */
    @Override
    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

    /**
     * Gets a boolean list from the specified path.
     *
     * @param path The path to get the boolean list from
     * @return The boolean list at the specified path
     */
    @Override
    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    /**
     * Gets a double list from the specified path.
     *
     * @param path The path to get the double list from
     * @return The double list at the specified path
     */
    @Override
    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    /**
     * Gets a float list from the specified path.
     *
     * @param path The path to get the float list from
     * @return The float list at the specified path
     */
    @Override
    public List<Float> getFloatList(String path) {
        return config.getFloatList(path);
    }

    /**
     * Gets a long list from the specified path.
     *
     * @param path The path to get the long list from
     * @return The long list at the specified path
     */
    @Override
    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    /**
     * Gets a byte list from the specified path.
     *
     * @param path The path to get the byte list from
     * @return The byte list at the specified path
     */
    @Override
    public List<Byte> getByteList(String path) {
        return config.getByteList(path);
    }

    /**
     * Gets a character list from the specified path.
     *
     * @param path The path to get the character list from
     * @return The character list at the specified path
     */
    @Override
    public List<Character> getCharacterList(String path) {
        return config.getCharacterList(path);
    }

    /**
     * Gets a short list from the specified path.
     *
     * @param path The path to get the short list from
     * @return The short list at the specified path
     */
    @Override
    public List<Short> getShortList(String path) {
        return config.getShortList(path);
    }

    /**
     * Gets a map list from the specified path.
     *
     * @param path The path to get the map list from
     * @return The map list at the specified path
     */
    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return config.getMapList(path);
    }

    /**
     * Checks if the specified path exists in the configuration.
     *
     * @param path The path to check
     * @return True if the path exists, false otherwise
     */
    @Override
    public boolean contains(String path) {
        return config.contains(path);
    }

    /**
     * Creates a new section at the specified path.
     *
     * @param path The path to create the section at
     */
    @Override
    public void createSection(String path) {
        config.createSection(path);
    }

    /**
     * Removes a section at the specified path.
     *
     * @param path The path of the section to remove
     */
    @Override
    public void removeSection(String path) {
        config.set(path, null);
    }

    /**
     * Gets all keys in the configuration.
     *
     * @param deep Whether to get keys from nested sections
     * @return Set of keys in the configuration
     */
    @Override
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    /**
     * Gets a configuration section at the specified path.
     *
     * @param path The path to get the section from
     * @return The configuration section at the specified path
     */
    @Override
    public IConfigableSection getConfigurationSection(String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            return null;
        }
        ConfigableSection configableSection = new ConfigableSection();
        for (String key : section.getKeys(true)) {
            configableSection.set(key, section.get(key));
        }
        return configableSection;
    }

    /**
     * Gets all values in the configuration.
     *
     * @param deep Whether to get values from nested sections
     * @return Map of all values in the configuration
     */
    @Override
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    /**
     * Adds a default value at the specified path.
     *
     * @param path The path to add the default value at
     * @param value The default value to add
     */
    @Override
    public void addDefault(String path, Object value) {
        config.addDefault(path, value);
    }

    /**
     * Sets default values from a map.
     *
     * @param defaults Map of default values to set
     */
    @Override
    public void setDefaults(Map<String, Object> defaults) {
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            config.addDefault(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sets configuration options.
     *
     * @param header The header to set for the configuration file
     * @param copyDefaults Whether to copy default values
     */
    @Override
    public void options(String header, boolean copyDefaults) {
        config.options().setHeader(header != null ? List.of(header.split("\n")) : null);
        config.options().copyDefaults(copyDefaults);
    }

    /**
     * Saves the configuration to a string.
     *
     * @return String representation of the configuration
     */
    @Override
    public String saveToString() {
        return config.saveToString();
    }

    /**
     * Loads the configuration from a string.
     *
     * @param contents String to load the configuration from
     */
    @Override
    public void loadFromString(String contents) {
        try {
            config.loadFromString(contents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a value at the specified path.
     *
     * @param path The path of the value to remove
     */
    @Override
    public void remove(String path) {
        config.set(path, null);
    }
}
