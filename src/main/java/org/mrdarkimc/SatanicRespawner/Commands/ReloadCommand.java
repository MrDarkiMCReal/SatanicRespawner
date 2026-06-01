package org.mrdarkimc.SatanicRespawner.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mrdarkimc.SatanicLib.ConfigAPI.Config;

public class ReloadCommand implements CommandExecutor {
    private Config configCached;

    public ReloadCommand(Config configCached) {
        this.configCached = configCached;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player){
            if (!player.hasPermission("satanic.admin")) {return false;}
        }
        configCached.reloadConfig();
        commandSender.sendMessage("Respawner reloaded");
        return true;
    }
}
