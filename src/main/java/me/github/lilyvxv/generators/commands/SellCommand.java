package me.github.lilyvxv.generators.commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import me.github.lilyvxv.generators.Generators;
import me.github.lilyvxv.generators.utils.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Locale;

import static me.github.lilyvxv.generators.Generators.configManager;
import static me.github.lilyvxv.generators.Generators.economy;

@Permission("generators.sell")
@Command("sell")
public class SellCommand {

    private static final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

    @Default
    public static void sell(Player player) {
        Inventory inventory = player.getInventory();
        Double totalValue = 0.0;

        // Loop every item inside the players inventory
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getAmount() > 0 && item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);

                if (nbtItem.getBoolean("generator_drop")) {
                    // Get the value of the drops based on the drop_value NBT tag and the amount in the ItemStack
                    double value = nbtItem.getDouble("drop_value") * item.getAmount();
                    economy.depositPlayer(player, value);

                    // Remove the item from the player's inventory
                    inventory.removeItem(item);

                    totalValue += value;
                }
            }
        }

        if (totalValue > 0.0) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0, 6);

            player.sendMessage(Generators.prefix
                    .append(configManager.getMessage("sell.success", Placeholder.component("value", Component.text(formatter.format(totalValue))))));
        } else {
            player.sendMessage(Generators.prefix
                    .append(configManager.getMessage("sell.no_items")));
        }
    }
}
