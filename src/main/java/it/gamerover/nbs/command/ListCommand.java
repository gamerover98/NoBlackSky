package it.gamerover.nbs.command;

import it.gamerover.nbs.configuration.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.spigot.api.command.CombinedCommand;
import xyz.tozymc.spigot.api.command.Command;
import xyz.tozymc.spigot.api.command.result.CommandResult;
import xyz.tozymc.spigot.api.command.result.TabResult;
import xyz.tozymc.spigot.api.util.bukkit.permission.PermissionWrapper;

import java.util.Set;
import java.util.StringJoiner;

public class ListCommand extends CombinedCommand {

    public static final String PERMISSION = PluginCommand.PERMISSION + ".list";

    private static final String COMMAND = "list";

    public ListCommand(@NotNull Command parent) {
        super(parent, COMMAND);
    }

    @NotNull
    @Override
    public CommandResult onCommand(@NotNull CommandSender sender, @NotNull String[] params) {

        Set<String> worlds = ConfigManager.getWorlds();

        if (worlds.isEmpty()) {
            sender.sendMessage("§aThere are no worlds into the config list");
        } else {

            StringJoiner joiner = new StringJoiner("§a, §e");
            worlds.forEach(joiner::add);

            sender.sendMessage("§aList of world from config: §e" + joiner.toString());

        }

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

        assert getParent() != null;
        Command parent = getParent();
        String parentCommand = parent.getAliases().stream().findAny().orElse(parent.getName());

        return "/" + parentCommand + " " + COMMAND;

    }

    @NotNull
    @Override
    public String getDescription() {
        return "List of config worlds";
    }

}
