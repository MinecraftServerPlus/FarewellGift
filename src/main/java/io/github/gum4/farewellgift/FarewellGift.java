package io.github.gum4.farewellgift;

import io.github.gum4.farewellgift.listeners.AttackListener;
import io.github.gum4.farewellgift.listeners.ClickListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FarewellGift extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new AttackListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
