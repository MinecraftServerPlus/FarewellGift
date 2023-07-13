package io.github.gum4.farewellgift.listeners;

import io.github.gum4.farewellgift.handlers.GiftHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.naming.Name;
import java.util.Objects;

public class AttackListener implements Listener {
    private static Long combatStateTickLength = 100L;

    private NamespacedKey lastCombatTimeKey = null;

    private NamespacedKey isPlayerDroppedKey = null;

    private JavaPlugin plugin;

    private static BossBar bossBar = Bukkit.createBossBar("전투 중입니다! 나가면 모든 아이템을 떨어뜨립니다!", BarColor.RED, BarStyle.SEGMENTED_6);

    public AttackListener(JavaPlugin plugin) {
        lastCombatTimeKey = new NamespacedKey(plugin, "last-combat-time");
        isPlayerDroppedKey = new NamespacedKey(plugin, "is-dropped");
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {
        Entity damagingEntity = event.getDamager();
        Entity damagedEntity = event.getEntity();

        if ((damagingEntity instanceof Player) && (damagedEntity instanceof Player)) {
            Player attacker = (Player) damagingEntity;
            Player victim = (Player) damagedEntity;
            Long baseTimeStamp = System.currentTimeMillis();
            attacker.getPersistentDataContainer().set(lastCombatTimeKey, PersistentDataType.LONG, baseTimeStamp);
            victim.getPersistentDataContainer().set(lastCombatTimeKey, PersistentDataType.LONG, baseTimeStamp);
            bossBar.addPlayer(attacker);
            bossBar.addPlayer(victim);
            
            new BukkitRunnable() {
                Long timeSpan = 0L;
                public void run() {
                    Long currentTimeStamp = attacker.getPersistentDataContainer().getOrDefault(lastCombatTimeKey, PersistentDataType.LONG, 0L);
                    if (Objects.equals(timeSpan, combatStateTickLength)) {
                        bossBar.removePlayer(attacker);
                        attacker.getPersistentDataContainer().remove(lastCombatTimeKey);
                        this.cancel();
                    }
                    else if (!baseTimeStamp.equals(currentTimeStamp)) this.cancel();
                    else {
                        timeSpan += 1;
                        Bukkit.getConsoleSender().sendMessage(String.valueOf(timeSpan));
                    }

                }
            }.runTaskTimer(plugin, 0L, 1L);
            new BukkitRunnable() {
                Long timeSpan = 0L;
                public void run() {
                    Long currentTimeStamp = victim.getPersistentDataContainer().getOrDefault(lastCombatTimeKey, PersistentDataType.LONG, 0L);
                    if (Objects.equals(timeSpan, combatStateTickLength)) {
                        bossBar.removePlayer(victim);
                        victim.getPersistentDataContainer().remove(lastCombatTimeKey);
                        this.cancel();
                    }
                    else if (!baseTimeStamp.equals(currentTimeStamp)) this.cancel();
                    else {
                        timeSpan += 1;
                        Bukkit.getConsoleSender().sendMessage(String.valueOf(timeSpan));
                    }


                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        player.getPersistentDataContainer().remove(lastCombatTimeKey);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Long combatTime = player.getPersistentDataContainer().getOrDefault(
                lastCombatTimeKey,
                PersistentDataType.LONG,
                0L
        );
        Long currentTime = System.currentTimeMillis();
        if (currentTime - combatTime < 5000L) {
            GiftHandler.createFarewellGift(player);
            /*
            player.getPersistentDataContainer().set(isPlayerDroppedKey, PersistentDataType.BOOLEAN, true);
            Location location = player.getLocation();
            Inventory inventory = player.getInventory();

            for (ItemStack item : inventory.getContents()) {
                if (item != null) {
                    player.getWorld().dropItemNaturally(location, item);
                }
            }

            inventory.clear();
            return;
            */
        }

        player.getPersistentDataContainer().remove(isPlayerDroppedKey);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean isPlayerDropped = player.getPersistentDataContainer().getOrDefault(isPlayerDroppedKey, PersistentDataType.BOOLEAN, false);
        if (isPlayerDropped) {
            player.sendMessage(
                    Component.text("전투 중에 서버를 나갔었습니다! 모든 아이템을 떨구었습니다!", NamedTextColor.RED)
            );
        }

        player.getPersistentDataContainer().remove(isPlayerDroppedKey);
    }

}
