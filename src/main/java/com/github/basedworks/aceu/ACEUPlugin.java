package com.github.basedworks.aceu;

import org.bukkit.plugin.java.JavaPlugin;

public class ACEUPlugin extends JavaPlugin {
  
  @Override
  public void onEnable() {
    ACEULogger.line(50, ACEULogger.PREFIX, "&a");
    ACEULogger.log("&aEnabled ACEU v" + getDescription().getVersion());
    ACEULogger.log("This is just a message that the plugin actually works. Other plugins just uses the utility which isn't included with the plugin.");
    ACEULogger.log("This is to maintain the plugin's integrity with other plugins without having the hassle to wait for ACEU to be loaded fully.");
    ACEULogger.log("");
    ACEULogger.log("Thanks for using ACEU!");
    ACEULogger.log("By BasedWorks Team");
    ACEULogger.line(50, ACEULogger.PREFIX, "&a");
  }

  @Override
  public void onDisable() {
    ACEULogger.line(50, ACEULogger.PREFIX, "&c");
    ACEULogger.log("&cDisabled ACEU v" + getDescription().getVersion() + " by " + getDescription().getAuthors());
    ACEULogger.line(50, ACEULogger.PREFIX, "&c");
  }
}
