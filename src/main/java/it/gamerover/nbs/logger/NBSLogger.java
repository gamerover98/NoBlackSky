package it.gamerover.nbs.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.gamerover.nbs.NoBlackSky;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 
 * @author gamerover98
 *
 */
public final class NBSLogger {

	private static final String ANSI_PACKAGE_NAME = "org.fusesource.jansi.Ansi";

	@Getter
	private static final Logger logger = NoBlackSky.getInstance().getLogger();

	@Getter
	private static boolean ansiSupported = false;

	private NBSLogger() {
		throw new IllegalStateException("This is a static class");
	}

	/**
	 * Check Ansi lib for spigot 1.12
	 */
	public static void init() {

		try {

			Class.forName(ANSI_PACKAGE_NAME);
			ansiSupported = true;

		} catch (ClassNotFoundException ex) {
			ansiSupported = false;
		}

	}
	
	/**
	 * Send to console an error message
	 */
	public static void error(@NotNull String error, @Nullable Throwable throwable) {

		if (isAnsiSupported()) {
			NBSAnsiLogger.error(error, throwable);
		} else {
			logger.log(Level.SEVERE, error, throwable);
		}

	}

	/**
	 * Send to console a warning message
	 */
	public static void warning(@NotNull String warning) {

		if (isAnsiSupported()) {
			NBSAnsiLogger.warning(warning);
		} else {	
			logger.log(Level.WARNING, warning);
		}

	}

	/**
	 * Send to console an info message
	 */
	public static void info(@NotNull String info) {

		if (isAnsiSupported()) {
			NBSAnsiLogger.info(info);
		} else {
			logger.log(Level.INFO, info);
		}

	}

}
