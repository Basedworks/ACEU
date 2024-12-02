# ACEU (Another Configuration Example Utility) 🛠️

[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Spigot](https://img.shields.io/badge/Spigot-1.19.4-yellow.svg)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

ACEU is a powerful utility plugin for Spigot/Paper developers that simplifies configuration file management in their plugins. It provides a robust API for handling multiple configuration formats with minimal setup.

## ✨ Features

- 📁 Multi-format support (YAML, JSON, INI)
- 🔄 Dynamic configuration management
- 🛡️ Error handling and validation
- 🎯 Developer-friendly API
- ⚡ Lightweight and efficient
- 📚 Extensive documentation

## 📋 Version History

| Version | Release Date | Major Changes |
|---------|--------------|---------------|
| 1.0.0   | 2024-30-11   | - Added basic YAML, XML, Properties, JSON & INI support. |

## 🚀 For Developers

### Maven Integration

Add ACEU to your plugin's `pom.xml`:
```xml
<repository>
    <id>Jitpack</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.basedworks</groupId>
    <artifactId>ACEU</artifactId>
    <version>{ACEU_VERSION}</version>
    <scope>provided</scope>
</dependency>
```

Note: Replace {ACEU_VERSION} with the actual version of ACEU.

### 📝 Quick Start

```java

// Create or load a configuration file
YamlConfig config = ACEU.createYamlConfig("settings.yml");

// Save default values
config.setDefault("messages.welcome", "Welcome to the server!");
config.setDefault("settings.debug", false);

// Access values
String welcome = config.getString("messages.welcome");
boolean debug = config.getBoolean("settings.debug");

// Save changes
config.save();
```


## 📚 Documentation


For detailed documentation and examples, visit our [Wiki](https://github.com/basedworks/ACEU/wiki).


### Configuration Formats

ACEU supports multiple configuration formats out of the box:

- **YAML**: The default format, great for human-readable configs
- **JSON**: Perfect for data interchange and complex structures
- **INI**: Simple and lightweight for basic configurations
- **Properties**: Simple and lightweight for basic configurations
- **XML**: Structured format with strong validation capabilities

## 🤝 Contributing

We welcome contributions! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Thanks to all contributors who have helped shape ACEU
