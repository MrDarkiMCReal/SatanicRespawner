package org.mrdarkimc.SatanicRespawner;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class CustomTitle {
    String title;
    String subtitle;
    int fadeIn;
    int fadeOut;
    int stay;

    public CustomTitle(String title, String subtitle, int fadeIn, int fadeOut, int stay) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    public CustomTitle() {
        init();
    }

    private void init() {
        this.title = SatanicRespawner.getInstance().getConfig().getString("messages.titlebar.title");
        this.subtitle = SatanicRespawner.getInstance().getConfig().getString("messages.titlebar.subtitle");

        this.fadeIn = SatanicRespawner.getInstance().getConfig().getInt("messages.titlebar.fadeIn");
        this.fadeOut = SatanicRespawner.getInstance().getConfig().getInt("messages.titlebar.fadeOut");
        this.stay = SatanicRespawner.getInstance().getConfig().getInt("messages.titlebar.fadeStay");

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public void send(Player player) {
        player.sendTitle(PlaceholderAPI.setPlaceholders(player, title), PlaceholderAPI.setPlaceholders(player, subtitle), fadeIn, stay, fadeOut);
    }
}
