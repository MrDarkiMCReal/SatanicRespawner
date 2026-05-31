package org.mrdarkimc.SatanicRespawner.services;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mrdarkimc.SatanicRespawner.CustomTitle;
import org.mrdarkimc.SatanicRespawner.events.FastDeathEvent;

import java.util.*;
import java.util.stream.Collectors;

public class RespawnerService {
    private final CustomTitle title = new CustomTitle();

    public boolean hasTotems(Player player) {
        PlayerInventory inv = player.getInventory();
        return inv.getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING ||
                inv.getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;
    }

    public void fakeKillAndRespawn(Player player, EntityDamageEvent originalEvent) {
        List<ItemStack> drops = Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(e -> {
                    Map<Enchantment, Integer> enchantments = e.getEnchantments();
                    return !enchantments.containsKey(Enchantment.VANISHING_CURSE);
                })
                .collect(Collectors.toList());
        String deathMessage = originalEvent instanceof EntityDamageByEntityEvent damageBy
                ? player.getName() + " был убит " + damageBy.getDamager().getName()
                : player.getName() + " умер";
        DamageSource source = originalEvent.getDamageSource();
        int xp = player.getLevel();
        Optional<Player> killer = Optional.empty();
        if (originalEvent.getDamageSource() != null && originalEvent.getDamageSource().getCausingEntity() != null && originalEvent.getDamageSource().getCausingEntity() instanceof Player) {
            killer = Optional.of((Player) originalEvent.getDamageSource().getCausingEntity());
        }
        FastDeathEvent fakeDeath = new FastDeathEvent(player, source, drops, xp, deathMessage, killer, title);
        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, source, drops, xp, deathMessage);
        Bukkit.getPluginManager().callEvent(fakeDeath);
        if (!fakeDeath.isThrowingOriginalEventDisabled()) {
            Bukkit.getPluginManager().callEvent(deathEvent);
        }

        if (!fakeDeath.isCancelled()) {
            //player.getInventory().clear();
            fakeDeath.start();
            title.send(player);

        }

//        player.teleport(Bukkit.getWorld("world") != null ? Bukkit.getWorld("world").getSpawnLocation() : player.getWorld().getSpawnLocation());
//        player.setHealth(player.getMaxHealth());
//
//        player.sendMessage("§c" + deathMessage);
    }

    public void fakeKillAndRespawn(Player player) {
        List<ItemStack> drops = Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(e -> {
                    Map<Enchantment, Integer> enchantments = e.getEnchantments();
                    return !enchantments.containsKey(Enchantment.VANISHING_CURSE);
                })
                .collect(Collectors.toList());
        int xp = player.getLevel();

        FastDeathEvent fakeDeath = new FastDeathEvent(player, DamageSource.builder(DamageType.GENERIC_KILL).build(), drops, xp, "Погиб, потому что ливнул в пвп", Optional.empty(), title);

        fakeDeath.start();
        title.send(player);

    }
}
