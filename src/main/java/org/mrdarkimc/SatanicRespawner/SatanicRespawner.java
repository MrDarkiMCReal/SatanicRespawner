package org.mrdarkimc.SatanicRespawner;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.SatanicLib.ConfigAPI.Config;
import org.mrdarkimc.SatanicLib.ConfigAPI.MessagesConfig;
import org.mrdarkimc.SatanicLib.SatanicLib;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicRespawner.Commands.ReloadCommand;
import org.mrdarkimc.SatanicRespawner.Commands.RespawnCommand;
import org.mrdarkimc.SatanicRespawner.listeners.PlayerDeathListener;
import org.mrdarkimc.SatanicRespawner.services.RespawnerService;

import java.util.Locale;

public class SatanicRespawner extends JavaPlugin implements Listener {
    private static SatanicRespawner instance;

    public static SatanicRespawner getInstance() {
        return instance;
    }

    private Config config;
    private MessagesConfig messagesRu;
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
        config = new Config(this,"config");
        messagesRu = new MessagesConfig(this,"config", Locale.forLanguageTag("ru"));
        messagesRu.load();

        respawnerService = new RespawnerService();
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(respawnerService), this);
        getServer().getPluginCommand("respawner").setExecutor(new ReloadCommand(config));
        getServer().getPluginCommand("fakekillandrespawn").setExecutor(new RespawnCommand(respawnerService));

    }

    public FileConfiguration getConfig() {
        return config.get();
    }

}
