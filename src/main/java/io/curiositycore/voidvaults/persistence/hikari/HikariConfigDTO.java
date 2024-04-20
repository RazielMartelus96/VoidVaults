package io.curiositycore.voidvaults.persistence.hikari;

import com.zaxxer.hikari.HikariConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class HikariConfigDTO {
    private HikariConfig hikariConfig;

    public HikariConfigDTO(FileConfiguration config) {

        this.hikariConfig = new HikariConfig();
        this.hikariConfig.setJdbcUrl(config.getString("database.jdbcUrl"));
        this.hikariConfig.setUsername(config.getString("database.username"));
        this.hikariConfig.setPassword(config.getString("database.password"));
        this.hikariConfig.setMaximumPoolSize(config.getInt("database.maximumPoolSize"));
        this.hikariConfig.setConnectionTimeout(config.getLong("database.connectionTimeout"));
        this.hikariConfig.setIdleTimeout(config.getLong("database.idleTimeout"));
        this.hikariConfig.setMaxLifetime(config.getLong("database.maxLifetime"));

    }

    // Getters and Setters
    public HikariConfig getHikariConfig() {
        return hikariConfig;
    }
}
