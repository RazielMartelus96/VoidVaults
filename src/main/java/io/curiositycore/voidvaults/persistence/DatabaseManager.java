package io.curiositycore.voidvaults.persistence;

import com.zaxxer.hikari.HikariDataSource;
import io.curiositycore.voidvaults.model.vault.Vault;
import io.curiositycore.voidvaults.model.vault.VaultManager;
import io.curiositycore.voidvaults.persistence.DAO.VaultDAO;
import io.curiositycore.voidvaults.persistence.hikari.DatabaseInitializer;
import io.curiositycore.voidvaults.persistence.hikari.HikariConfigDTO;
import io.curiositycore.voidvaults.persistence.serializer.Serializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DatabaseManager {
    private static volatile DatabaseManager instance;
    private static boolean initialised = false;
    private final VaultManager vaultManager = VaultManager.getInstance();
    private Serializer<ItemStack> vaultSerializer;
    private VaultDAO vaultDAO;
    private DataSource dataSource;
    private static final Object lock = new Object();


    public static DatabaseManager getInitialisedInstance(JavaPlugin plugin, Serializer<ItemStack> vaultSerializer, HikariConfigDTO configDTO) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseManager(plugin, configDTO, vaultSerializer);
                }
            }
        }
        return instance;
    }
    public static DatabaseManager getInstance() throws IllegalStateException {
        if (instance == null) throw new IllegalStateException("DatabaseManager has not been initialised");
        return instance;
    }

    private DatabaseManager(JavaPlugin plugin, HikariConfigDTO configDTO, Serializer<ItemStack> vaultSerializer) {
        instance = this;
        initialised = true;
        this.vaultSerializer = vaultSerializer;
        this.dataSource = new HikariDataSource(configDTO.getHikariConfig());
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(instance.dataSource);
        databaseInitializer.initializeDatabase();

    }



    public void initialisePlayerVault(UUID player){
        try{
            getVaultForPlayer(player).ifPresent(this::registerVault);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void initialiseAllPlayerVaults(Set<UUID> playerIds) {
        playerIds.forEach(this::initialisePlayerVault);
    }

    public void saveVaultsForPlayer(UUID playerUuid) throws SQLException {
        List<Vault> vaults = vaultManager.getVaultsForPlayer(playerUuid);
        for (Vault vault : vaults){
            saveVaultsForPlayer(playerUuid, vault);
        }
    }
    public void saveVaultsForPlayer(UUID playerUuid, Vault vault) throws SQLException {
        vaultDAO.saveVaultForPlayer(playerUuid, vault);
    }

    public void deleteVaultForPlayer(UUID playerUuid, Vault vault) throws SQLException {
        vaultDAO.deleteVaultForPlayer(playerUuid, vault);
    }

    private Optional<Vault> getVaultForPlayer(UUID playerUuid) throws SQLException {
        return vaultDAO.getVaultByPlayerUUID(playerUuid);

    }

    private void registerVault(Vault vault) {
        this.vaultManager.registerVault(vault);
    }

    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }




}
