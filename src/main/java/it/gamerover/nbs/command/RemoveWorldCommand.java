package it.gamerover.nbs.command;

import it.gamerover.nbs.configuration.ConfigManager;
import it.gamerover.nbs.logger.PluginLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.spigot.api.command.CombinedCommand;
import xyz.tozymc.spigot.api.command.Command;
import xyz.tozymc.spigot.api.command.result.CommandResult;
import xyz.tozymc.spigot.api.command.result.TabResult;
import xyz.tozymc.spigot.api.util.bukkit.permission.PermissionWrapper;

import java.util.Set;

public class RemoveWorldCommand extends CombinedCommand {

    public static final String PERMISSION = PluginCommand.PERMISSION + ".remove";

    private static final String COMMAND = "remove";

    public RemoveWorldCommand(@NotNull Command parent) {
        super(parent, COMMAND);
    }

    @NotNull
    @Override
    public CommandResult onCommand(@NotNull CommandSender sender, @NotNull String[] params) {

        if (params.length == 0) {
            return CommandResult.WRONG_SYNTAX;
        }

        String worldName = params[0];
        boolean result = ConfigManager.removeWorld(worldName);

        if (result) {

            PluginLogger.info("Removed " + worldName + " from the config list");

            if (sender instanceof Player) {

                sender.sendMessage("§aRemoved world §e" + worldName + "§a from the config list");
                sender.sendMessage("§aTo see changes you must re-login, change world or suicide");

            }

        } else {
            sender.sendMessage("§cCannot remove §4" + worldName + "§c because of it already removed");
        }

        return CommandResult.SUCCESS;

    }

    @NotNull
    @Override
    public TabResult onTab(@NotNull CommandSender sender, @NotNull String[] params) {

        int length = params.length;

        if (length > 1) {
            return TabResult.EMPTY_RESULT;
        }

        Set<String> names = ConfigManager.getWorlds();

        String token = "";

        if (length != 0) {
            token = params[0];
        }

        return TabResult.of(token, names);

    }

    @NotNull
    @Override
    public PermissionWrapper getPermission() {
        return PermissionWrapper.of(PERMISSION);
    }

    @NotNull
    @Override
    public String getSyntax() {

        assert getParent() != null;
        Command parent = getParent();
        String parentCommand = parent.getAliases().stream().findAny().orElse(parent.getName());

        return "/" + parentCommand + " " + COMMAND + " <world-name>";

    }

    @NotNull
    @Override
    public String getDescription() {
        return "Remove a world from the world list";
    }

}
