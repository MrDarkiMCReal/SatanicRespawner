package org.mrdarkimc.SatanicRespawner;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.SatanicLib.ConfigAPI.Config;
import org.mrdarkimc.SatanicLib.ConfigAPI.MessageLoader;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicRespawner.Commands.ReloadCommand;
import org.mrdarkimc.SatanicRespawner.Commands.RespawnCommand;
import org.mrdarkimc.SatanicRespawner.listeners.PlayerDeathListener;
import org.mrdarkimc.SatanicRespawner.services.RespawnerService;

public class SatanicRespawner extends JavaPlugin implements Listener {
    private static SatanicRespawner instance;

    public static SatanicRespawner getInstance() {
        return instance;
    }

    private Config config;
    private RespawnerService respawnerService;

    public RespawnerService getRespawnerService() {
        return respawnerService;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public void onEnable() {
        Utils.startUp("SatanicRespawner private");
        instance = this;
        config = new Config(this, "config");
        MessageLoader messageLoader = new MessageLoader(this);
        messageLoader.loadAllLocales();

        respawnerService = new RespawnerService();
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(respawnerService), this);
        getServer().getPluginCommand("respawner").setExecutor(new ReloadCommand(config));
        getServer().getPluginCommand("fakekillandrespawn").setExecutor(new RespawnCommand(respawnerService));

    }

    public FileConfiguration getConfig() {
        return config.get();
    }

}
