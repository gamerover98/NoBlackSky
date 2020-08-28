package it.gamerover.nbs.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.jansi.Ansi;

import it.gamerover.nbs.NBS;

/**
 * 
 * @author gamerover98
 *
 */
public final class NBS_AnsiLogger {

	private static Logger logger = NBS.getInstance().getLogger();

	/**
	 * Send to console an error message
	 */
	public static void error(Throwable throwable, String error) {
		logger.log(Level.SEVERE, (Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString()) + error + (Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString()), throwable);
	}

	/**
	 * Send to console a warning message
	 */
	public static void warning(String warning) {
		logger.warning((Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString()) + warning + (Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString()));
	}

	/**
	 * Send to console an info message
	 */
	public static void info(String info) {
		logger.info((Ansi.ansi().fg(Ansi.Color.GREEN).boldOff().toString()) + info + (Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString()));
	}

}
