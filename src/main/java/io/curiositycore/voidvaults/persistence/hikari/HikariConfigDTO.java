package io.curiositycore.voidvaults.persistence.hikari;

import com.zaxxer.hikari.HikariConfig;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Data Transfer Object for the HikariConfig class, used to store the configuration for the HikariCP connection pool.
 */
public class HikariConfigDTO {
    /**
     * The HikariConfig object that stores the configuration for the HikariCP connection pool.
     */
    private final HikariConfig hikariConfig;

    /**
     * Constructor for the HikariConfigDTO class. This constructor takes a FileConfiguration object and uses it to
     * populate the HikariConfig object.
     * @param config The FileConfiguration object containing the configuration for the HikariCP connection pool.
     *              Typically, this is the plugin's database.yml file.
     */
    public HikariConfigDTO(FileConfiguration config) {

        this.hikariConfig = new HikariConfig();
        this.hikariConfig.setJdbcUrl(config.getString("database.jdbcUrl"));
        this.hikariConfig.setUsername(config.getString("database.user"));
        this.hikariConfig.setPassword(config.getString("database.password"));
        this.hikariConfig.setMaximumPoolSize(config.getInt("database.maxPoolSize"));
        this.hikariConfig.setConnectionTimeout(config.getLong("database.connectionTimeout"));
        this.hikariConfig.setIdleTimeout(config.getLong("database.timeout"));
        this.hikariConfig.setMaxLifetime(config.getLong("database.maxLifetime"));

    }

    /**
     * Getter for the HikariConfig object.
     * @return The HikariConfig object.
     */
    public HikariConfig getHikariConfig() {
        return hikariConfig;
    }
}
