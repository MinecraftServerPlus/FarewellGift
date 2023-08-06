package io.github.gum4.farewellgift.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GiftHandler {

    public static void createFarewellGift(Player player) {
        ItemDisplay skullDisplay;
        Location location = player.getLocation();
        {
            ItemStack playerSkull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playerSkullMeta = ((SkullMeta) playerSkull.getItemMeta());
            playerSkullMeta.setOwningPlayer(player);
            playerSkull.setItemMeta(playerSkullMeta);
            skullDisplay = player.getWorld().spawn(location.clone().add(0, 0.5, 0), ItemDisplay.class);
            skullDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            skullDisplay.setItemStack(playerSkull);
            skullDisplay.customName(
                    Component.text(player.getName(), NamedTextColor.GOLD)
                            .append(Component.text("의 인벤토리", NamedTextColor.WHITE))
            );
            skullDisplay.setCustomNameVisible(true);

        }
        Interaction skullHitBox;
        {
            skullHitBox = player.getWorld().spawn(location, Interaction.class);
            skullHitBox.setInteractionWidth(0.5f);
            skullHitBox.setInteractionHeight(0.5f);
        }
    }
}
