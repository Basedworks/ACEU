package com.github.basedworks.aceu;

import java.io.File;

import com.github.basedworks.aceu.config.INIConfig;
import com.github.basedworks.aceu.config.PropertiesConfig;
import com.github.basedworks.aceu.config.XMLConfig;
import com.github.basedworks.aceu.config.YamlConfig;
import com.github.basedworks.aceu.config.JsonConfig;

/**
 * Factory class for creating different types of configuration objects.
 */
public class ACEU {
    
  /**
   * Creates a new INI configuration object from the specified file.
   *
   * @param file The INI configuration file
   * @return A new INIConfig instance
   */
  public static INIConfig createINIConfig(File file) {
    return new INIConfig(file);
  }

  /**
   * Creates a new Properties configuration object from the specified file.
   *
   * @param file The properties configuration file
   * @return A new PropertiesConfig instance
   */
  public static PropertiesConfig createPropertiesConfig(File file) {
    return new PropertiesConfig(file);
  }

  /**
   * Creates a new JSON configuration object from the specified file.
   *
   * @param file The JSON configuration file
   * @return A new JsonConfig instance
   */
  public static JsonConfig createJsonConfig(File file) {
    return new JsonConfig(file);
  }

  /**
   * Creates a new YAML configuration object from the specified file.
   *
   * @param file The YAML configuration file
   * @return A new YamlConfig instance
   */
  public static YamlConfig createYamlConfig(File file) {
    return new YamlConfig(file);
  }

  /**
   * Creates a new XML configuration object from the specified file.
   *
   * @param file The XML configuration file
   * @return A new XMLConfig instance
   */
  public static XMLConfig createXMLConfig(File file) {
    return new XMLConfig(file);
  }
  
}