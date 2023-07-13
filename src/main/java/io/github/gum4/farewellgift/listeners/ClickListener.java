package io.github.gum4.farewellgift.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ClickListener implements Listener {
    @EventHandler
    public void onLeftClick(PlayerInteractEntityEvent event) {
        Bukkit.getConsoleSender().sendMessage(event.getRightClicked().getType().name());
        if (event.getRightClicked() instanceof ItemDisplay) {
            event.getPlayer().sendMessage("맞았다!");
        }
    }
}
