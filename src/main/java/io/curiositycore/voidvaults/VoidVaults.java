package io.curiositycore.voidvaults;

import io.curiositycore.voidvaults.config.DatabaseConfigManager;
import io.curiositycore.voidvaults.config.LoggerConfigManager;
import io.curiositycore.voidvaults.listeners.InventoryListener;
import io.curiositycore.voidvaults.listeners.PlayerListener;
import io.curiositycore.voidvaults.listeners.VaultListener;
import io.curiositycore.voidvaults.persistence.DatabaseManager;
import io.curiositycore.voidvaults.persistence.hikari.HikariConfigDTO;
import io.curiositycore.voidvaults.persistence.serializer.ItemStackSerializer;
import io.curiositycore.voidvaults.persistence.serializer.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public final class VoidVaults extends JavaPlugin {

    private DatabaseManager databaseManager;
    private LoggerConfigManager loggerConfigManager;
    private DatabaseConfigManager databaseConfigManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initialiseConfigManagers();
        databaseManager = initializeDatabase();
        initialisePlayerVaults();
        initListeners();
    }

    @Override
    public void onDisable() {
    }

    private void initialiseConfigManagers(){
        initLoggerConfigManager();
        initDatabaseConfigManager();
    }

    private void initLoggerConfigManager(){
        this.loggerConfigManager = LoggerConfigManager.initInstance(this);
        this.loggerConfigManager.updateLogger();
    }

    private void initDatabaseConfigManager(){
        this.databaseConfigManager = DatabaseConfigManager.getInstance();
        this.databaseConfigManager.initDatabaseConfig(this);
    }

    private DatabaseManager initializeDatabase(){
        Serializer<ItemStack> vaultSerializer = new ItemStackSerializer(Bukkit.getServer().getUnsafe());
        HikariConfigDTO config = new HikariConfigDTO(this.databaseConfigManager.getDatabaseConfig());
        return DatabaseManager.getInitialisedInstance(this, vaultSerializer, config);
    }
    private void initialisePlayerVaults(){
        databaseManager.initialiseAllPlayerVaults(
                Bukkit.getOnlinePlayers().stream()
                        .map(Entity::getUniqueId)
                        .collect(Collectors.toSet()));
    }


    private void initListeners(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new VaultListener(this), this);
        pluginManager.registerEvents(new InventoryListener(), this);
    }

}
