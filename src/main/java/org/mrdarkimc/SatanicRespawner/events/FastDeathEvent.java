package org.mrdarkimc.SatanicRespawner.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mrdarkimc.SatanicLib.NotifyAPI.MessageDispatcher;
import org.mrdarkimc.SatanicRespawner.SatanicRespawner;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FastDeathEvent extends Event implements Cancellable {


    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


    private final Player player;
    private final DamageSource damageSource;
    private final List<ItemStack> drops;
    private final int droppedLevels;
    private final String message;
    private final Optional<Player> killer;
    private Location respawn = null;

    private boolean canceled = false;


    private boolean disableClearItems = false;
    private boolean disableItemDrops = false;
    private boolean disableTeleport = false;
    private boolean disableHealing = false;
    private boolean disableStatisticUpdate = false;
    private boolean isThrowingOriginalEventDisabled = false;
    private MessageDispatcher title;


    public FastDeathEvent(@NotNull Player player, @NotNull DamageSource damageSource, @NotNull List<ItemStack> drops, int droppedExp, @Nullable String deathMessage, Optional<Player> killer, MessageDispatcher title) {
        this.player = player;
        this.damageSource = damageSource;
        this.drops = drops;
        this.droppedLevels = droppedExp;
        this.message = deathMessage;
        this.killer = killer;
        this.title = title;
    }

    public MessageDispatcher getDeathMessageDispatcher() {
        return title;
    }

    public Player getPlayer() {
        return player;
    }
    public boolean isThrowingOriginalEventDisabled(){
        return isThrowingOriginalEventDisabled;
    }
    public void disableThrowingOriginalEvent(){
        this.isThrowingOriginalEventDisabled = true;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public Optional<Player> getKiller() {
        return killer;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }


    public void start() {
        player.closeInventory();
        clearItems(player);
        dropItems(player);
        teleportPlayer(player);
        healPlayer(player);
        updateStats(player);
        clearCombat(player);
    }

    public void clearCombat(Player player){
        new BukkitRunnable(){

            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"clx untag " + player.getName());
            }
        }.runTaskLater(SatanicRespawner.getInstance(),1L);
    }
    private void clearItems(Player player) {
        if (disableClearItems) return;
        player.getInventory().clear();
    }

    private void dropItems(Player player) {
        if (disableItemDrops) return;
        Location loc = player.getLocation();
        drops.stream().filter(Objects::nonNull).forEach(itemStack -> loc.getWorld().dropItemNaturally(loc, itemStack));

    }


    private void teleportPlayer(Player player) {
        System.out.println("[Respawner] Teleporting..");
        if (disableTeleport) return;

        if (respawn != null) {
            System.out.println("[Respawner] custom resp not null");
            player.teleport(respawn);
            return;
        }

        FileConfiguration config = SatanicRespawner.getInstance().getConfig();
        String world = config.getString("modules.respawner.location.world");
        double x = config.getDouble("modules.respawner.location.x");
        double y = config.getDouble("modules.respawner.location.y");
        double z = config.getDouble("modules.respawner.location.z");
        double yaw = config.getDouble("modules.respawner.location.yaw");
        double pitch = config.getDouble("modules.respawner.location.pitch");
        System.out.println("[Respawner] Teleport to spawn");
        player.teleport(new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch)); //todo вынести аллокацию в кеш? или ноу? можно вынести аллокацию в кеш, а если потребуется вводить новую локацию через сеттер
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawn = respawnLocation;
    }


    private void healPlayer(Player player) {
        if (disableHealing) return;
        player.setLevel(0);
        player.setExp(0);
        player.setHealth(20);
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (!effect.getType().equals(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(effect.getType());
            }
        }
    }


    private void updateStats(Player player) {
        if (disableStatisticUpdate) return;

        player.incrementStatistic(Statistic.DEATHS);
        killer.ifPresent(k -> k.incrementStatistic(Statistic.PLAYER_KILLS));
    }


    public void disableClearItems(boolean disableClearItems) {
        this.disableClearItems = disableClearItems;
    }

    public void disableItemDrops(boolean disableItemDrops) {
        this.disableItemDrops = disableItemDrops;
    }

    public void disableTeleport(boolean disableTeleport) {
        this.disableTeleport = disableTeleport;
    }

    public void disableHealing(boolean disableHealing) {
        this.disableHealing = disableHealing;
    }

    public void disableStatisticUpdate(boolean disableStatisticUpdate) {
        this.disableStatisticUpdate = disableStatisticUpdate;
    }
}