package crs.notification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// Central logger for notification-related actions; writes to data/notification.log.
public final class NotificationLogger {
    private static final Logger LOGGER = Logger.getLogger("crs.notification");
    private static volatile boolean initialized = false;
    private static final String LOG_FILE = "data/notification.log";

    private NotificationLogger() {
    }

    // Returns a lazily initialized logger instance shared across the notification
    // module.
    public static Logger getLogger() {
        if (!initialized) {
            synchronized (NotificationLogger.class) {
                if (!initialized) {
                    setupLogger();
                    initialized = true;
                }
            }
        }
        return LOGGER;
    }

    // Configures the logger to write to disk, falling back to console if setup
    // fails.
    private static void setupLogger() {
        try {
            Path logPath = Paths.get(LOG_FILE).toAbsolutePath().normalize();
            Files.createDirectories(logPath.getParent());
            FileHandler handler = new FileHandler(logPath.toString(), true);
            handler.setFormatter(new SimpleFormatter());
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(handler);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException ex) {
            // Fallback to console if file setup fails.
            LOGGER.log(Level.WARNING, "Failed to initialize notification log file: " + ex.getMessage(), ex);
        }
    }
}
