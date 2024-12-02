package com.github.basedworks.aceu.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IConfigableSection {    
    // Getters
    Object get(String path);
    String getString(String path);
    String getString(String path, String def);
    int getInt(String path);
    int getInt(String path, int def);
    boolean getBoolean(String path);
    boolean getBoolean(String path, boolean def);
    double getDouble(String path);
    double getDouble(String path, double def);
    long getLong(String path);
    long getLong(String path, long def);
    List<?> getList(String path);
    List<?> getList(String path, List<?> def);
    List<String> getStringList(String path);
    List<Integer> getIntegerList(String path);
    List<Boolean> getBooleanList(String path);
    List<Double> getDoubleList(String path);
    List<Float> getFloatList(String path);
    List<Long> getLongList(String path);
    List<Byte> getByteList(String path);
    List<Character> getCharacterList(String path);
    List<Short> getShortList(String path);
    List<Map<?, ?>> getMapList(String path);
    
    // Setters
    void set(String path, Object value);
    
    // Utility methods
    boolean contains(String path);
    void createSection(String path);
    void removeSection(String path);
    Set<String> getKeys(boolean deep);
    IConfigableSection getConfigurationSection(String path);
    Map<String, Object> getValues(boolean deep);
    void addDefault(String path, Object value);
    void setDefaults(Map<String, Object> defaults);
}
