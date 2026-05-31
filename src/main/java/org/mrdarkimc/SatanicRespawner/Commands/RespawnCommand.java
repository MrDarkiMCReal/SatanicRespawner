package org.mrdarkimc.SatanicRespawner.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mrdarkimc.SatanicRespawner.services.RespawnerService;

public class RespawnCommand implements CommandExecutor {
    private RespawnerService service;

    public RespawnCommand(RespawnerService service) {
        this.service = service;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player){
            if (!player.hasPermission("satanic.admin")){
                return true;
            }
        }
        if (strings.length < 1){
            commandSender.sendMessage("Укажите ник игрока");
            return true;
        }
        Player player = Bukkit.getPlayer(strings[0]);
        if (player!=null) {
            service.fakeKillAndRespawn(player);
            commandSender.sendMessage("Игрок " + player.getName() + " убит и возражден");
        }
        return true;
    }
}
