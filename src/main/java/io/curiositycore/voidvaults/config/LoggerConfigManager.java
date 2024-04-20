package io.curiositycore.voidvaults.config;

import io.curiositycore.voidvaults.events.vault.VaultEventType;
import io.curiositycore.voidvaults.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LoggerConfigManager {
    private static LoggerConfigManager instance;
    public static LoggerConfigManager initInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new LoggerConfigManager(plugin);
        }
        return instance;
    }

    public static LoggerConfigManager getInstance() {
        if(instance == null){

        }
        return instance;
    }
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration logConfig;
    private final Logger logger = Logger.getInstance();
    private final String LOGGING_PATH = "logging.yml";
    private boolean loggerEnabled = false;

    private LoggerConfigManager() {
        this.plugin = null;
        this.configFile = null;
        this.logConfig = null;
    }

    private LoggerConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), LOGGING_PATH);
        initConfig();
    }

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
    public void reloadConfig() {
        logConfig = YamlConfiguration.loadConfiguration(configFile);
        updateLogger();
    }

    public void saveConfig() {
        try {
            logConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


    private boolean isLoggerInitialized() {
        return logConfig != null;
    }
    private boolean isLoggerEnabled() {
        return logConfig.getBoolean("enabled");
    }



}
