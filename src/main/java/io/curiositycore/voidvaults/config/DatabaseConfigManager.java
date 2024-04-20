package io.curiositycore.voidvaults.config;

import io.curiositycore.voidvaults.persistence.DatabaseManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DatabaseConfigManager {
    private static DatabaseConfigManager instance;
    public static DatabaseConfigManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConfigManager();
        }
        return instance;
    }
    private final String DATABASE_PATH = "database.yml";

    public void initDatabaseConfig(JavaPlugin plugin) {
        this.configFile = new File(plugin.getDataFolder(), DATABASE_PATH);
        if (!configFile.exists()) {
            plugin.saveResource(DATABASE_PATH, false);
        }
        this.databaseConfig = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getDatabaseConfig() {
        return this.databaseConfig;
    }

    private File configFile;
    private FileConfiguration databaseConfig;

    private DatabaseConfigManager() {

    }

    private DatabaseConfigManager(JavaPlugin plugin) {
    }


}
