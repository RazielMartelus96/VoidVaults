package io.curiositycore.voidvaults.config;

import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Manager responsible for the initialisation, storage and updating of the logger configuration properties.
 * */
public class LoggerConfigManager {

    /**
     * Singleton instance of the LoggerConfigManager.
     */
    private static LoggerConfigManager instance;

    /**
     * Returns the singleton instance of the LoggerConfigManager.
     * @return The singleton instance.
     */
    public static LoggerConfigManager initInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new LoggerConfigManager(plugin);
        }
        return instance;
    }

    /**
     * Returns the singleton instance of the LoggerConfigManager.
     * @return The singleton instance.
     */
    public static LoggerConfigManager getInstance() {
        if(instance == null){

        }
        return instance;
    }

    /**
     * Instance of the VoidVaults plugin.
     */
    private final JavaPlugin plugin;

    /**
     * The file object that corresponds to the logging configuration file.
     */
    private File configFile;

    /**
     * The configuration object that corresponds to the logging configuration file.
     */
    private FileConfiguration logConfig;

    /**
     * Singleton instance of the Logger class.
     */
    private final Logger logger = Logger.getInstance();

    /**
     * The path to the logging configuration file.
     */
    private final String LOGGING_PATH = "logging.yml";

    /**
     * The status of the logger. If true, the logger is enabled.
     */
    private boolean loggerEnabled = false;


    /**
     * Private constructor to prevent instantiation of the LoggerConfigManager.
     */
    private LoggerConfigManager() {
        this.plugin = null;
        this.configFile = null;
        this.logConfig = null;
    }

    /**
     * Constructs a new LoggerConfigManager with the given {@linkplain JavaPlugin} object.
     * @param plugin Instance of the VoidVaults plugin.
     */
    private LoggerConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), LOGGING_PATH);
        initConfig();
    }

    /**
     * Initialises the logging configuration file. Without this file, the plugin will not be able to log events. Hence,
     * this method should be called during the plugin's initialisation.
     */
    private void initConfig() {
        try {
            if (!configFile.exists()) {
                plugin.saveResource(LOGGING_PATH, false);
            }
            logConfig = YamlConfiguration.loadConfiguration(configFile);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the logging configuration file. Typically called by command for the purposes of reloading altered
     * configuration properties during runtime.
     */
    public void reloadConfig() {
        logConfig = YamlConfiguration.loadConfiguration(configFile);
        updateLogger();
    }

    /**
     * Saves the logging configuration file. Typically called by command for the purposes of saving altered configuration
     * properties during runtime.
     */
    public void saveConfig() {
        try {
            logConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the logger with the configuration properties. Typically called during the plugin's initialisation.
     */
    public void updateLogger(){
        if(!isLoggerInitialized() || !isLoggerEnabled()){
            return;
        }
        this.logger.setEnabled(true);
        try{
            for(String key : Objects.requireNonNull(logConfig.getConfigurationSection("loggable_events")).getKeys(false)){
                if(logConfig.isBoolean(key)){
                    this.logger.setLogEnabled(VaultEventType.valueOf(key.toUpperCase()), logConfig.getBoolean(key));
                }
            }
        }
        catch (NullPointerException e) {
              e.printStackTrace();
        }
    }


    /**
     * Set the logging status of a specified {@linkplain VaultEventType Vault Event Type}. This is to be utilised for
     * in-game methods of changing the logging status of events, such as commands.
     * @param eventType The Event Type to set the logging status of.
     * @param enabled The status to set the logging to, with true meaning the event will be logged.
     */
    public void setLogEnabled(VaultEventType eventType, boolean enabled) {
        if(!isLoggerInitialized() || !this.loggerEnabled){
            return;
        }
        Objects.requireNonNull(logConfig.getConfigurationSection("loggable_events")).set(eventType.name().toLowerCase(), enabled);
        saveConfig();
    }

    /**
     * Checks to see if the logger has been initialised.
     * @return True if the logger has been initialised, false otherwise.
     */
    private boolean isLoggerInitialized() {
        return logConfig != null;
    }

    /**
     * Checks to see if the logger is enabled within the configuration properties.
     * @return True if the logger is enabled, false otherwise.
     */
    private boolean isLoggerEnabled() {
        return logConfig.getBoolean("enabled");
    }



}
