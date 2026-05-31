package org.mrdarkimc.SatanicRespawner.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mrdarkimc.SatanicRespawner.events.TotemPopEvent;
import org.mrdarkimc.SatanicRespawner.services.RespawnerService;


public class PlayerDeathListener implements Listener {
    private RespawnerService service;

    public PlayerDeathListener(RespawnerService service) {
        this.service = service;
    }

    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        if (e.isCancelled())
            return;
        if (!(e.getEntity() instanceof Player player)) return;

        //if (!player.getWorld().getName().equals("world_the_end")) return;

        if (player.getHealth() > e.getFinalDamage()) return;
        //check if dmg type is void
        if (e.getDamageSource().getDamageType().equals(DamageType.OUT_OF_WORLD)) {
            service.fakeKillAndRespawn(player, e);
            return;
        }

        if (service.hasTotems(player)) {
            TotemPopEvent totemPopEvent = new TotemPopEvent(player);
            Bukkit.getPluginManager().callEvent(totemPopEvent);
            if (!totemPopEvent.isCancelled()) {
                return;
            }
        }
        e.setDamage(0.01);
        //e.setCancelled(true);

        service.fakeKillAndRespawn(player, e);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (isInPvpMode(player)) {
            service.fakeKillAndRespawn(player);
        }
    }

    public boolean isInPvpMode(Player player) {
        String s = PlaceholderAPI.setPlaceholders(player, "%combatlogx_in_combat%");
        return s.toLowerCase().contains("да");

    }
}
