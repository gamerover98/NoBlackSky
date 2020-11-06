package it.gamerover.nbs.logger;

import java.util.logging.Level;

import org.fusesource.jansi.Ansi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 
 * @author gamerover98
 *
 */
final class NBSAnsiLogger {

	private NBSAnsiLogger() {
		throw new IllegalStateException("This is a static class");
	}

	/**
	 * Send to console an error message
	 */
	public static void error(String error) {
		error(error, null);
	}

	/**
	 * Send to console an error message
	 */
	public static void error(@NotNull String error, @Nullable Throwable throwable) {

		String message = getAnsiColor(Ansi.Color.RED) + error + getAnsiColor(Ansi.Color.WHITE);
		NBSLogger.getLogger().log(Level.SEVERE, message, throwable);

	}

	/**
	 * Send to console a warning message
	 */
	public static void warning(@NotNull String warning) {

		String message = getAnsiColor(Ansi.Color.YELLOW) + warning + getAnsiColor(Ansi.Color.WHITE);
		NBSLogger.getLogger().log(Level.WARNING, message);

	}

	/**
	 * Send to console an info message
	 */
	public static void info(@NotNull String info) {

		String message = getAnsiColor(Ansi.Color.GREEN) + info + getAnsiColor(Ansi.Color.WHITE);
		NBSLogger.getLogger().log(Level.INFO, message);

	}

	/**
	 * Get the ansi color string.
	 *
	 * @param color The not null color enumeration.
	 * @return The not null string of the converted color.
	 */
	@NotNull
	private static String getAnsiColor(@NotNull Ansi.Color color) {
		return Ansi.ansi().fg(color).boldOff().toString();
	}

}
