package it.gamerover.nbs.command;


import java.io.IOException;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import it.gamerover.nbs.NoBlackSky;
import it.gamerover.nbs.configuration.NBS_Configuration;
import it.gamerover.nbs.logger.NBSLogger;

/**
 * 
 * @author gamerover98
 *
 */
public class NBS_Command implements CommandExecutor {

	private static final String ADD_COMMAND = "add";
	private static final String REMOVE_COMMAND = "remove";
	private static final String LIST_COMMAND = "list";
	private static final String RELOAD_COMMAND = "reload";

	private static final String NBS_PERMISSION = "nbs.";
	private static final String NBS_ADD_PERMISSION = NBS_PERMISSION + "add";
	private static final String NBS_REMOVE_PERMISSION = NBS_PERMISSION + "remove";
	private static final String NBS_LIST_PERMISSION = NBS_PERMISSION + "list";
	private static final String NBS_RELOAD_PERMISSION = NBS_PERMISSION + "reload";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		NBS_Configuration config = NoBlackSky.getConfiguration();
		
		if (args.length == 0) {
			this.helpCommand(label, sender);
		} else if (args.length == 1) {

			String sub_command = args[0];

			if (sub_command.equalsIgnoreCase(LIST_COMMAND)) { //LIST COMMAND
				
				if (this.hasPermission(sender, NBS_LIST_PERMISSION)) {

					List<String> blacklist = config.getBlacklist();

					if (blacklist.isEmpty()) {
						sender.sendMessage("§cBlacklist is empty");
					} else {

						sender.sendMessage("§5BlackListed worlds");

						for (String world_name : blacklist) {
							sender.sendMessage("§a - §e" + world_name);
						}

					}

				} else {
					noPermission(sender);
				}
				
			} else if (sub_command.equalsIgnoreCase(RELOAD_COMMAND)) { // RELOAD COMMAND
				
				if (this.hasPermission(sender, NBS_RELOAD_PERMISSION)) {
					
					try {
						
						config.reload();
						sender.sendMessage("§aReload successful, to see changes you must re-login, change world or suicide");
						
					} catch (IOException | InvalidConfigurationException ex) {
						
						sender.sendMessage("§cCan't reload the plugin");
						NBSLogger.error("Can't reload the plugin, check your plugin configurations", ex);
						
					}
					
				} else {
					noPermission(sender);
				}
				
			} else {
				helpCommand(label, sender);
			}


		} else if (args.length > 1) {

			String sub_command = args[0];
			String world_name = args[1];

			if (sub_command.equalsIgnoreCase(ADD_COMMAND)) { //ADD COMMAND
				
				if (this.hasPermission(sender, NBS_ADD_PERMISSION)) {

					try {

						if (config.add_blacklist_world(world_name)) {
							sender.sendMessage("§aAdded the world §e" + world_name + " §ain the blacklist, to see changes you must re-login, change world or suicide");
						} else {
							sender.sendMessage("§c" + world_name + " §aalready exists");
						}

					} catch (IOException ioex) {
						
						sender.sendMessage("The world " + world_name + " can't be added, does the file config.yml exists?");
						NBSLogger.error("The world " + world_name + " can't be added, does the file config.yml exists?", ioex);
						
					}

				} else {
					noPermission(sender);
				}

			} else if (sub_command.equalsIgnoreCase(REMOVE_COMMAND)) { //REMOVE COMMAND
				
				if (this.hasPermission(sender, NBS_REMOVE_PERMISSION)) {

					try {

						if (config.remove_blacklist_world(world_name)) {
							sender.sendMessage("§aWorld §e" + world_name + " §aremoved from the blacklist, to see changes you must re-login, change world or suicide");
						} else {
							sender.sendMessage("§cCan't remove the world " + world_name);
						}

					} catch (IOException ioex) {
						
						sender.sendMessage("The world " + world_name + " can't be removed, does the file config.yml exists?");
						NBSLogger.error("The world " + world_name + " can't be removed, does the file config.yml exists?", ioex);
						
					}

				} else {
					noPermission(sender);
				}

			} else {
				this.noPermission(sender);
			}

		}

		return true;

	}

	private void helpCommand(String label, CommandSender sender) {

		label = "§c/" + label + " §a";
		int permLength = 5;

		if (sender.hasPermission(NBS_ADD_PERMISSION)) sender.sendMessage(label + "add <world name> §8| §6Add a world in the blacklist"); else permLength--;
		if (sender.hasPermission(NBS_REMOVE_PERMISSION)) sender.sendMessage(label + "remove <world name> §8| §6Remove a world from the blacklist"); else permLength--;
		if (sender.hasPermission(NBS_LIST_PERMISSION)) sender.sendMessage(label + "list §8| §6Show blacklisted worlds"); else permLength--;
		if (sender.hasPermission(NBS_RELOAD_PERMISSION)) sender.sendMessage(label + "reload §8| §6Reload the config.yml"); else permLength--;

		if (permLength <= 0) {
			this.noPermission(sender);
		}

	}

	private boolean hasPermission(CommandSender sender, String permission) {
		return sender.hasPermission(permission);
	}

	private void noPermission(CommandSender sender) {
		sender.sendMessage("§cYou don't have permission to perform this command.");
	}

}
