package com.github.basedworks.aceu.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigableSection implements IConfigableSection {
    private final Map<String, Object> data;

    public ConfigableSection() {
        this.data = new HashMap<>();
    }

    @Override
    public Object get(String path) {
        return data.get(path);
    }

    @Override
    public String getString(String path) {
        Object obj = get(path);
        return obj != null ? obj.toString() : null;
    }

    @Override
    public String getString(String path, String def) {
        String val = getString(path);
        return val != null ? val : def;
    }

    @Override
    public int getInt(String path) {
        Object obj = get(path);
        return obj instanceof Number ? ((Number) obj).intValue() : 0;
    }

    @Override
    public int getInt(String path, int def) {
        Object obj = get(path);
        return obj instanceof Number ? ((Number) obj).intValue() : def;
    }

    @Override
    public boolean getBoolean(String path) {
        Object obj = get(path);
        return obj instanceof Boolean ? (Boolean) obj : false;
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        Object obj = get(path);
        return obj instanceof Boolean ? (Boolean) obj : def;
    }

    @Override
    public double getDouble(String path) {
        Object obj = get(path);
        return obj instanceof Number ? ((Number) obj).doubleValue() : 0.0;
    }

    @Override
    public double getDouble(String path, double def) {
        Object obj = get(path);
        return obj instanceof Number ? ((Number) obj).doubleValue() : def;
    }

    @Override
    public long getLong(String path) {
        Object obj = get(path);
        return obj instanceof Number ? ((Number) obj).longValue() : 0L;
    }

    @Override
    public long getLong(String path, long def) {
        Object obj = get(path);
        return obj instanceof Number ? ((Number) obj).longValue() : def;
    }

    @Override
    public List<?> getList(String path) {
        Object obj = get(path);
        return obj instanceof List ? (List<?>) obj : new ArrayList<>();
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        Object obj = get(path);
        return obj instanceof List ? (List<?>) obj : def;
    }

    @Override
    public List<String> getStringList(String path) {
        List<?> list = getList(path);
        List<String> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj != null) result.add(obj.toString());
        }
        return result;
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        List<?> list = getList(path);
        List<Integer> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) result.add(((Number) obj).intValue());
        }
        return result;
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        List<?> list = getList(path);
        List<Boolean> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Boolean) result.add((Boolean) obj);
        }
        return result;
    }

    @Override
    public List<Double> getDoubleList(String path) {
        List<?> list = getList(path);
        List<Double> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) result.add(((Number) obj).doubleValue());
        }
        return result;
    }

    @Override
    public List<Float> getFloatList(String path) {
        List<?> list = getList(path);
        List<Float> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) result.add(((Number) obj).floatValue());
        }
        return result;
    }

    @Override
    public List<Long> getLongList(String path) {
        List<?> list = getList(path);
        List<Long> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) result.add(((Number) obj).longValue());
        }
        return result;
    }

    @Override
    public List<Byte> getByteList(String path) {
        List<?> list = getList(path);
        List<Byte> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) result.add(((Number) obj).byteValue());
        }
        return result;
    }

    @Override
    public List<Character> getCharacterList(String path) {
        List<?> list = getList(path);
        List<Character> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Character) result.add((Character) obj);
        }
        return result;
    }

    @Override
    public List<Short> getShortList(String path) {
        List<?> list = getList(path);
        List<Short> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) result.add(((Number) obj).shortValue());
        }
        return result;
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        List<?> list = getList(path);
        List<Map<?, ?>> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Map) result.add((Map<?, ?>) obj);
        }
        return result;
    }

    @Override
    public void set(String path, Object value) {
        if (value == null) {
            data.remove(path);
        } else {
            data.put(path, value);
        }
    }

    @Override
    public boolean contains(String path) {
        return data.containsKey(path);
    }

    @Override
    public void createSection(String path) {
        data.put(path, new HashMap<String, Object>());
    }

    @Override
    public void removeSection(String path) {
        data.remove(path);
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return data.keySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public IConfigableSection getConfigurationSection(String path) {
        Object obj = get(path);
        if (!(obj instanceof Map)) {
            return null;
        }
        ConfigableSection section = new ConfigableSection();
        section.data.putAll((Map<String, Object>) obj);
        return section;
    }

    @Override
    public Map<String, Object> getValues(boolean deep) {
        return new HashMap<>(data);
    }

    @Override
    public void addDefault(String path, Object value) {
        if (!contains(path)) {
            set(path, value);
        }
    }

    @Override
    public void setDefaults(Map<String, Object> defaults) {
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            addDefault(entry.getKey(), entry.getValue());
        }
    }
}
