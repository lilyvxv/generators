package me.github.lilyvxv.generators.commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import me.github.lilyvxv.generators.Generators;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Locale;

import static me.github.lilyvxv.generators.Generators.economy;

@Command("sell")
public class SellCommand {

    private static final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

    @Default
    @Permission("generators.sell")
    public static void sell(Player player) {
        Inventory inventory = player.getInventory();
        Double totalValue = 0.0;

        // Loop every item inside the players inventory
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
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
            player.sendMessage(Generators.prefix
                    .append(Generators.miniMessage.deserialize(
                            String.format("<gray>Sold all of your generator drops for <color:#72fb00>%s</color>!</gray>", formatter.format(totalValue)))));
        } else {
            player.sendMessage(Generators.prefix
                    .append(Generators.miniMessage.deserialize("<gray>You do not have any items to sell.</gray>")));
        }
    }
}
