package com.github.basedworks.aceu;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ACEULogger {
  private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");
  public static final String PREFIX = "&#00bfffＡＣＥＵ&#ffffff ";
  
  public static String color(String message) {
    if (message == null) return "";
    
    Matcher matcher = HEX_PATTERN.matcher(message);
    StringBuilder builder = new StringBuilder(message);
    
    while (matcher.find()) {
      String hexCode = builder.substring(matcher.start() + 1, matcher.end());
      ChatColor chatColor = ChatColor.of(hexCode);
      
      builder.replace(matcher.start(), matcher.end(), chatColor.toString());
      matcher = HEX_PATTERN.matcher(builder);
    }
    
    return ChatColor.translateAlternateColorCodes('&', builder.toString());
  }

  public static void log(String message) {
    if (message == null) return;
    Bukkit.getConsoleSender().sendMessage(color(PREFIX + message));
  }

  public static void debug(String message) {
    log("&#8c8c8c⚙ " + message);
  }

  public static void warn(String message) {
    log("&#ffcc00⚠ " + message); 
  }

  public static void error(String message) {
    log("&#ff3333✖ " + message);
  }

  public static void line(int length) {
    log("&m" + " ".repeat(length));
  }

  public static void line(int length, String centerMessage) {
    String line = " ".repeat(length).substring(0, (length - centerMessage.length()) / 2);
    log("&m" + line + centerMessage + line);
  }

  public static void line(int length, String centerMessage, String color) {
    String line = " ".repeat(length).substring(0, (length - centerMessage.length()) / 2);
    log(color + "&m" + line + centerMessage + line);
  }

  public static void line(int length, String centerMessage, String color, String lineColor) {
    String line = " ".repeat(length).substring(0, (length - centerMessage.length()) / 2);
    log(lineColor + "&m" + line + color + centerMessage + lineColor + "&m" + line);
  }

  public static void doubleLine(int length) {
    line(length);
    line(length);
  }

  public static void doubleLine(int length, String centerMessage) {
    line(length);
    line(length, centerMessage);
    line(length);
  }

  public static void dashedLine(int length) {
    log("&m" + "-".repeat(length));
  }

  public static void dashedLine(int length, String centerMessage) {
    String dashes = "-".repeat(length).substring(0, (length - centerMessage.length()) / 2);
    log("&m" + dashes + centerMessage + dashes);
  }
}
