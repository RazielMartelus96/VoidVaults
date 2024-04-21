package io.curiositycore.voidvaults.util;

import io.curiositycore.voidvaults.events.vault.VaultEventType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class that handles logging of events within the plugin. The main functionality of this class is to allow
 * a way to log events that occur within the plugin, but with the ability to toggle the logging of certain events on
 * and off via a specified config file for the logging.
 */
public class Logger {
    /**
     * Static instance of the Logger, as per the Singleton pattern.
     */
    private static Logger instance;

    /**
     * Gets the instance of the Logger, creating one if it does not exist, as per the Singleton pattern.
     * @return The instance of the Logger.
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Map of all the enabled logs within the plugin, keyed by the {@linkplain VaultEventType} that the log is for.
     */
    private Map<VaultEventType,Boolean> enabledLogs = new HashMap<>();

    /**
     * Whether the Logger is enabled or not.
     */
    private boolean enabled;

    /**
     * Private constructor for the Logger class, to prevent instantiation outside the class's static getInstance method.
     */
    private Logger() {
    }

    /**
     * Enables the logger, as defined by the "enabled" setting within the config file.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets the log for a specific {@linkplain VaultEventType} to be enabled or disabled.
     * @param type The type of event to enable or disable logging for.
     * @param enabled Whether the log should be enabled or disabled.
     */
    public void setLogEnabled(VaultEventType type, boolean enabled) {
        enabledLogs.put(type, enabled);
    }


    /**
     * Initialises the enabledLogs map, setting all logs to be disabled by default.
     */
    private void initEnabledLogs() {
        Arrays.stream(VaultEventType.values()).forEach(type -> enabledLogs.put(type, false));
    }

    /**
     * Logs a message to the console, if the log is enabled. This allows for the logging of the same event, but
     * with different messages, depending on the context. An example of this being that the contents of a Vault can
     * change, but be logged as either an addition or removal of contents.
     * @param type The type of event that the log is for.
     * @param message The message to log.
     */
    public void log(VaultEventType type, String message) {
        if(!enabled){
            return;
        }
        if (enabledLogs.get(type)) {
            System.out.println("[" + type.getEventName() + "] " + message);
        }
    }


}
