package it.gamerover.nbs.command;

import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;
import xyz.tozymc.spigot.api.command.CombinedCommand;
import xyz.tozymc.spigot.api.command.CommandController;
import xyz.tozymc.spigot.api.command.result.CommandResult;
import xyz.tozymc.spigot.api.command.result.TabResult;
import xyz.tozymc.spigot.api.util.bukkit.permission.PermissionWrapper;

/**
 * @author gamerover98
 * The plugin main command.
 */
public class PluginCommand extends CombinedCommand {

	public static final String PERMISSION = "nbs";

	private static final String COMMAND = "NoBlackSky";
	private static final String ALIAS   = "nbs";

	private final CommandController commandController;

	public PluginCommand(@NotNull CommandController commandController) {

		super(COMMAND, ALIAS);
		this.commandController = commandController;

		commandController.addCommand(this);
		commandController.addCommand(new AddWorldCommand(this));
		commandController.addCommand(new RemoveWorldCommand(this));
		commandController.addCommand(new ListCommand(this));
		commandController.addCommand(new ReloadCommand(this));

	}

	@NotNull
	@Override
	public CommandResult onCommand(@NotNull CommandSender sender, @NotNull String[] params) {

		StringBuilder messageBuilder = new StringBuilder();

		commandController.getCommands().computeIfPresent(this, (cmd, subCommand) -> {

			subCommand.forEach(c -> messageBuilder
					.append("§f").append(c.getSyntax())
					.append("§7").append(" - ")
					.append(c.getDescription())
					.append("\n"));

			return subCommand;

		});

		String message = messageBuilder.toString();

		sender.sendMessage("\n" + message);

		return CommandResult.SUCCESS;

	}

	@NotNull
	@Override
	public TabResult onTab(@NotNull CommandSender sender, @NotNull String[] params) {
		return TabResult.EMPTY_RESULT;
	}

	@NotNull
	@Override
	public PermissionWrapper getPermission() {
		return PermissionWrapper.of(PERMISSION);
	}

	@NotNull
	@Override
	public String getSyntax() {
		return "";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "No black sky in worlds";
	}

}
