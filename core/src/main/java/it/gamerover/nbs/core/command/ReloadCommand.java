package it.gamerover.nbs.core.command;

import it.gamerover.nbs.config.ConfigManager;
import it.gamerover.nbs.CoreHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.spigot.api.command.CombinedCommand;
import xyz.tozymc.spigot.api.command.Command;
import xyz.tozymc.spigot.api.command.result.CommandResult;
import xyz.tozymc.spigot.api.command.result.TabResult;
import xyz.tozymc.spigot.api.util.bukkit.permission.PermissionWrapper;

public class ReloadCommand extends CombinedCommand {

    public static final String PERMISSION = PluginCommand.PERMISSION + ".reload";

    private static final String COMMAND = "reload";

    public ReloadCommand(@NotNull Command parent) {
        super(parent, COMMAND);
    }

    @NotNull
    @Override
    public CommandResult onCommand(@NotNull CommandSender sender, @NotNull String[] params) {

        CoreHandler coreHandler = CoreHandler.getInstance();
        Plugin plugin = coreHandler.getPlugin();

        ConfigManager.reload(plugin, CoreHandler.getServerVersion());
        sender.sendMessage("§aSuccessful reload");

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
        return "Reload the config file";
    }

}
