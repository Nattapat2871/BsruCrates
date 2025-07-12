package com.bsru;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CratePlaceholders extends PlaceholderExpansion {

    private final bsruCrates plugin;

    public CratePlaceholders(bsruCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bsrucrates";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Nattapat2871";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        if (params.startsWith("physicalkeys_")) {
            String crateType = params.substring("physicalkeys_".length());
            int keyCount = plugin.countPhysicalKeys(player, crateType);
            return String.valueOf(keyCount);
        }

        if (params.startsWith("points_")) {
            String crateType = params.substring("points_".length());
            int points = plugin.getPoints(player.getUniqueId(), crateType);
            return String.valueOf(points);
        }

        return null;
    }
}