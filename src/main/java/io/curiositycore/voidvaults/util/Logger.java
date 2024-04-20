package io.curiositycore.voidvaults.util;

import io.curiositycore.voidvaults.events.vault.VaultEventType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Logger {
    private static Logger instance;
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    private Map<VaultEventType,Boolean> enabledLogs = new HashMap<>();
    private boolean enabled;
    private Logger() {
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setLogEnabled(VaultEventType type, boolean enabled) {
        enabledLogs.put(type, enabled);
    }


    private void initEnabledLogs() {
        Arrays.stream(VaultEventType.values()).forEach(type -> enabledLogs.put(type, false));
    }

    public void log(VaultEventType type, String message) {
        if(!enabled){
            return;
        }
        if (enabledLogs.get(type)) {
            System.out.println("[" + type.getEventName() + "] " + message);
        }
    }


}
