package de.mojamgames.trio;

import de.mojamgames.trio.commands.cmdTrio;
import de.mojamgames.trio.listener.PlayerListener;
import de.mojamgames.trio.listener.SignListener;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Trio extends JavaPlugin {
    private static Trio instance;
    private static MySQL mysql;

    public static Trio getInstance() {
        return instance;
    }
    public static MySQL getMySQL() {
        return mysql;
    }
    public static String getPrefix() {
        return "§a§l[§5Trio§a§l] ";
    }

    @Override
    public void onDisable() {
        instance = null;
        getMySQL().closeConnection();
    }

    @Override
    public void onEnable() {
        instance = this;

        getConfig().addDefault("MySQL.Host", "localhost");
        getConfig().addDefault("MySQL.User", "root");
        getConfig().addDefault("MySQL.Password", "pass");
        getConfig().addDefault("MySQL.Port", 3306);
        getConfig().addDefault("MySQL.Database", "data");
        getConfig().addDefault("Points.Win", 20);
        getConfig().addDefault("Points.rightCombination", 2);
        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("trio").setExecutor(new cmdTrio());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);

        mysql = new MySQL();
        getMySQL().openConnection();
        getMySQL().update("CREATE TABLE IF NOT EXISTS `trio_signs` (" +
                "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                "`x` DOUBLE NOT NULL," +
                "`y` DOUBLE NOT NULL," +
                "`z` DOUBLE NOT NULL," +
                "`world` TEXT NOT NULL);");
        getMySQL().update("CREATE TABLE IF NOT EXISTS `trio_gamefields` (" +
                "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                "`name` TEXT NOT NULL," +
                "`game` TEXT NOT NULL," +
                "`world` TEXT NOT NULL," +
                "`maxX` INT NOT NULL," +
                "`maxY` INT NOT NULL," +
                "`maxZ` INT NOT NULL," +
                "`minX` INT NOT NULL," +
                "`minY` INT NOT NULL," +
                "`minZ` INT NOT NULL);");
        getMySQL().update("CREATE TABLE IF NOT EXISTS `trio_spawns` (" +
                "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                "`x` DOUBLE NOT NULL," +
                "`y` DOUBLE NOT NULL," +
                "`z` DOUBLE NOT NULL," +
                "`yaw` FLOAT NOT NULL," +
                "`pitch` FLOAT NOT NULL," +
                "`world` TEXT NOT NULL," +
                "`gamefield` TEXT NOT NULL);");
        getMySQL().update("CREATE TABLE IF NOT EXISTS `trio_users` (" +
                "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                "`uuid` TEXT NOT NULL," +
                "`wonGames` INT NOT NULL," +
                "`rightCombinations` INT NOT NULL," +
                "`gamesPlayed` INT NOT NULL," +
                "`points` INT NOT NULL);");
    }

    public static String firstCharToUpper(String str) {
        if(str.contains(" ")) {
            String[] strArray = str.split(" ");
            String finalStr = null;
            for(String s : strArray) {
                finalStr += Character.toUpperCase(s.charAt(0)) + s.substring(1) + " ";
            }
            return StringUtils.removeEnd(finalStr, " ");
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

}
