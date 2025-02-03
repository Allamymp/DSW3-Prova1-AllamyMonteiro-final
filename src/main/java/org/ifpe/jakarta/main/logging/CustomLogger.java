package org.ifpe.jakarta.main.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogger.class);

    /**
     * Logs an informational message.
     *
     * @param message The message to be logged.
     */
    public static void info(String message) {
        logger.info(formatMessage(message));
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to be logged.
     */
    public static void warn(String message) {
        logger.warn(formatMessage(message));
    }

    /**
     * Logs an error message.
     *
     * @param message The message to be logged.
     * @param exception The exception to log with the message.
     */
    public static void error(String message, Exception exception) {
        logger.error(formatMessage(message), exception);
    }

    /**
     * Logs a debug message.
     *
     * @param message The message to be logged.
     */
    public static void debug(String message) {
        logger.debug(formatMessage(message));
    }

    /**
     * Formats the log message with thread name, timestamp, class name, and method name.
     *
     * @param message The original log message.
     * @return The formatted log message with thread, timestamp, class name, and method name.
     */
    private static String formatMessage(String message) {
        String threadName = Thread.currentThread().getName();
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String className = getClassName();
        String methodName = getMethodName();

        return String.format("[%s] [Thread-%s] [%s::%s] %s", timestamp, threadName, className, methodName, message);
    }

    /**
     * Gets the class name from the current stack trace.
     *
     * @return The class name.
     */
    private static String getClassName() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    /**
     * Gets the method name from the current stack trace.
     *
     * @return The method name.
     */
    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }
}
