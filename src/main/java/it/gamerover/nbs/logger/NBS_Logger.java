package it.gamerover.nbs.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.gamerover.nbs.NBS;

/**
 * 
 * @author gamerover98
 *
 */
public final class NBS_Logger {

	private static Logger logger = NBS.getInstance().getLogger();
	private static boolean ansi = false;

	/**
	 * Check Ansi lib for spigot 1.12
	 */
	public static void init() {

		try {

			Class.forName("org.fusesource.jansi.Ansi");
			ansi = true;

		} catch (ClassNotFoundException ex) {
			ansi = false;
		}

	}
	
	/**
	 * Send to console an error message
	 */
	public static void error(Throwable throwable, String error) {

		if (ansi) {
			NBS_AnsiLogger.error(throwable, error);
		} else {
			logger.log(Level.SEVERE, error, throwable);
		}

	}

	/**
	 * Send to console a warning message
	 */
	public static void warning(String warning) {

		if (ansi) {
			NBS_AnsiLogger.warning(warning);
		} else {	
			logger.warning(warning);
		}

	}

	/**
	 * Send to console an info message
	 */
	public static void info(String info) {

		if (ansi) {
			NBS_AnsiLogger.info(info);
		} else {
			logger.info(info);
		}
	}

}
