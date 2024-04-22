package io.curiositycore.voidvaults.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Manager responsible for handling the initialisation, storage, and retrieval of the database configuration.
 */
public class DatabaseConfigManager {
    /**
     * Singleton instance of the DatabaseConfigManager.
     */
    private static DatabaseConfigManager instance;

    /**
     * Returns the singleton instance of the DatabaseConfigManager.
     * @return The singleton instance.
     */
    public static DatabaseConfigManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConfigManager();
        }
        return instance;
    }


    /**
     * Private constructor to prevent instantiation of the DatabaseConfigManager.
     */
    private DatabaseConfigManager() {

    }

    /**
     * The path to the database configuration file.
     */
    private final String DATABASE_PATH = "database.yml";

    /**
     * The file object that corresponds to the database configuration file.
     */
    private File configFile;

    /**
     * The configuration object that corresponds to the database configuration file.
     */
    private FileConfiguration databaseConfig;

    /**
     * Initialises the database configuration file. Without this file, the plugin will not be able to connect to the
     * database. Hence, this method should be called during the plugin's initialisation.
     * @param plugin Instance of the VoidVaults plugin.
     */
    public void initDatabaseConfig(JavaPlugin plugin) {
        this.configFile = new File(plugin.getDataFolder(), DATABASE_PATH);
        if (!configFile.exists()) {
            plugin.saveResource(DATABASE_PATH, false);
        }
        this.databaseConfig = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Returns the database configuration file.
     * @return The database configuration file.
     */
    public FileConfiguration getDatabaseConfig() {
        return this.databaseConfig;
    }


}
