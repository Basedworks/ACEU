package com.github.basedworks.aceu.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.*;
import java.util.*;

/**
 * XML configuration implementation of IConfigable interface.
 * Provides functionality to read, write and manage XML format configuration files.
 * Uses Jackson XML for efficient XML processing.
 */
public class XMLConfig implements IConfigable {
    private final File file;
    private final XmlMapper xmlMapper;
    private ObjectNode rootNode;

    /**
     * Constructs a new XMLConfig with the specified file.
     *
     * @param file The XML file to read from/write to
     */
    public XMLConfig(File file) {
        this.file = file;
        this.xmlMapper = new XmlMapper();
        this.rootNode = xmlMapper.createObjectNode();
    }

    /**
     * Loads the configuration from the XML file.
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
            rootNode = (ObjectNode) xmlMapper.readTree(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the configuration to the XML file.
     */
    @Override
    public void save() {
        try {
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(file, rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a value from the configuration.
     *
     * @param path The path to the value
     * @return The value at the path, or null if not found
     */
    @Override
    public Object get(String path) {
        JsonNode node = getNode(path);
        return node != null ? nodeToObject(node) : null;
    }

    /**
     * Sets a value in the configuration.
     *
     * @param path The path to set
     * @param value The value to set
     */
    @Override
    public void set(String path, Object value) {
        String[] parts = path.split("\\.");
        ObjectNode current = rootNode;
        
        for (int i = 0; i < parts.length - 1; i++) {
            JsonNode node = current.get(parts[i]);
            if (node == null || !node.isObject()) {
                current.putObject(parts[i]);
            }
            current = (ObjectNode) current.get(parts[i]);
        }

        if (value == null) {
            current.remove(parts[parts.length - 1]);
        } else {
            current.putPOJO(parts[parts.length - 1], value);
        }
    }

    /**
     * Gets a string from the configuration.
     *
     * @param path The path to the string
     * @return The string at the path, or null if not found
     */
    @Override
    public String getString(String path) {
        JsonNode node = getNode(path);
        return node != null ? node.asText() : null;
    }

    @Override
    public String getString(String path, String def) {
        String val = getString(path);
        return val != null ? val : def;
    }

    /**
     * Gets an integer from the configuration.
     *
     * @param path The path to the integer
     * @return The integer at the path, or 0 if not found
     */
    @Override
    public int getInt(String path) {
        JsonNode node = getNode(path);
        return node != null ? node.asInt() : 0;
    }

    @Override
    public int getInt(String path, int def) {
        JsonNode node = getNode(path);
        return node != null ? node.asInt() : def;
    }

    /**
     * Gets a boolean from the configuration.
     *
     * @param path The path to the boolean
     * @return The boolean at the path, or false if not found
     */
    @Override
    public boolean getBoolean(String path) {
        JsonNode node = getNode(path);
        return node != null && node.asBoolean();
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        JsonNode node = getNode(path);
        return node != null ? node.asBoolean() : def;
    }

    /**
     * Gets a double from the configuration.
     *
     * @param path The path to the double
     * @return The double at the path, or 0.0 if not found
     */
    @Override
    public double getDouble(String path) {
        JsonNode node = getNode(path);
        return node != null ? node.asDouble() : 0.0;
    }

    @Override
    public double getDouble(String path, double def) {
        JsonNode node = getNode(path);
        return node != null ? node.asDouble() : def;
    }

    /**
     * Gets a long from the configuration.
     *
     * @param path The path to the long
     * @return The long at the path, or 0L if not found
     */
    @Override
    public long getLong(String path) {
        JsonNode node = getNode(path);
        return node != null ? node.asLong() : 0L;
    }

    @Override
    public long getLong(String path, long def) {
        JsonNode node = getNode(path);
        return node != null ? node.asLong() : def;
    }

    /**
     * Gets a list from the configuration.
     *
     * @param path The path to the list
     * @return The list at the path, or an empty list if not found
     */
    @Override
    public List<?> getList(String path) {
        JsonNode node = getNode(path);
        if (node == null || !node.isArray()) {
            return new ArrayList<>();
        }
        List<Object> result = new ArrayList<>();
        node.forEach(n -> result.add(nodeToObject(n)));
        return result;
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        List<?> val = getList(path);
        return !val.isEmpty() ? val : def;
    }

    @Override
    public List<String> getStringList(String path) {
        List<?> list = getList(path);
        List<String> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj != null) {
                result.add(obj.toString());
            }
        }
        return result;
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        List<?> list = getList(path);
        List<Integer> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) {
                result.add(((Number) obj).intValue());
            }
        }
        return result;
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        List<?> list = getList(path);
        List<Boolean> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Boolean) {
                result.add((Boolean) obj);
            }
        }
        return result;
    }

    @Override
    public List<Double> getDoubleList(String path) {
        List<?> list = getList(path);
        List<Double> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) {
                result.add(((Number) obj).doubleValue());
            }
        }
        return result;
    }

    @Override
    public List<Float> getFloatList(String path) {
        List<?> list = getList(path);
        List<Float> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) {
                result.add(((Number) obj).floatValue());
            }
        }
        return result;
    }

    @Override
    public List<Long> getLongList(String path) {
        List<?> list = getList(path);
        List<Long> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) {
                result.add(((Number) obj).longValue());
            }
        }
        return result;
    }

    @Override
    public List<Byte> getByteList(String path) {
        List<?> list = getList(path);
        List<Byte> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) {
                result.add(((Number) obj).byteValue());
            }
        }
        return result;
    }

    @Override
    public List<Character> getCharacterList(String path) {
        List<?> list = getList(path);
        List<Character> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Character) {
                result.add((Character) obj);
            } else if (obj instanceof String && !((String) obj).isEmpty()) {
                result.add(((String) obj).charAt(0));
            }
        }
        return result;
    }

    @Override
    public List<Short> getShortList(String path) {
        List<?> list = getList(path);
        List<Short> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Number) {
                result.add(((Number) obj).shortValue());
            }
        }
        return result;
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        List<?> list = getList(path);
        List<Map<?, ?>> result = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Map) {
                result.add((Map<?, ?>) obj);
            }
        }
        return result;
    }

    @Override
    public IConfigableSection getConfigurationSection(String path) {
        JsonNode node = getNode(path);
        if (node != null && node.isObject()) {
            ConfigableSection section = new ConfigableSection();
            node.fields().forEachRemaining(entry -> 
                section.set(entry.getKey(), convertJsonValue(entry.getValue()))
            );
            return section;
        }
        return null;
    }

    private Object convertJsonValue(JsonNode value) {
        if (value.isTextual()) return value.asText();
        if (value.isInt()) return value.asInt();
        if (value.isLong()) return value.asLong();
        if (value.isDouble()) return value.asDouble();
        if (value.isBoolean()) return value.asBoolean();
        if (value.isNull()) return null;
        if (value.isArray()) {
            List<Object> list = new ArrayList<>();
            value.elements().forEachRemaining(element -> 
                list.add(convertJsonValue(element))
            );
            return list;
        }
        if (value.isObject()) {
            Map<String, Object> map = new HashMap<>();
            value.fields().forEachRemaining(entry -> 
                map.put(entry.getKey(), convertJsonValue(entry.getValue()))
            );
            return map;
        }
        return null;
    }

    @Override
    public boolean contains(String path) {
        return getNode(path) != null;
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

    @Override
    public void options(String path, boolean value) {
        set(path, value);
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        Set<String> keys = new HashSet<>();
        collectKeys(rootNode, "", keys, deep);
        return keys;
    }

    private void collectKeys(JsonNode node, String prefix, Set<String> keys, boolean deep) {
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                String newPrefix = prefix.isEmpty() ? fieldName : prefix + "." + fieldName;
                keys.add(newPrefix);
                if (deep) {
                    collectKeys(node.get(fieldName), newPrefix, keys, true);
                }
            }
        }
    }

    @Override
    public Map<String, Object> getValues(boolean deep) {
        Map<String, Object> values = new HashMap<>();
        collectValues(rootNode, "", values, deep);
        return values;
    }

    private void collectValues(JsonNode node, String prefix, Map<String, Object> values, boolean deep) {
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                String newPrefix = prefix.isEmpty() ? fieldName : prefix + "." + fieldName;
                JsonNode childNode = node.get(fieldName);
                if (!childNode.isObject() || !deep) {
                    values.put(newPrefix, nodeToObject(childNode));
                }
                if (deep && childNode.isObject()) {
                    collectValues(childNode, newPrefix, values, true);
                }
            }
        }
    }

    @Override
    public void createSection(String path) {
        set(path, new HashMap<>());
    }

    @Override
    public void removeSection(String path) {
        set(path, null);
    }

    @Override
    public void remove(String path) {
        set(path, null);
    }

    @Override
    public void delete() {
        if (file.exists()) {
            file.delete();
        }
        rootNode = xmlMapper.createObjectNode();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public String saveToString() {
        try {
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void loadFromString(String contents) {
        try {
            rootNode = (ObjectNode) xmlMapper.readTree(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonNode getNode(String path) {
        String[] parts = path.split("\\.");
        JsonNode current = rootNode;
        
        for (String part : parts) {
            current = current.get(part);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private Object nodeToObject(JsonNode node) {
        if (node.isTextual()) return node.asText();
        if (node.isInt()) return node.asInt();
        if (node.isLong()) return node.asLong();
        if (node.isDouble()) return node.asDouble();
        if (node.isBoolean()) return node.asBoolean();
        if (node.isNull()) return null;
        if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            node.forEach(n -> list.add(nodeToObject(n)));
            return list;
        }
        return node.toString();
    }
}
