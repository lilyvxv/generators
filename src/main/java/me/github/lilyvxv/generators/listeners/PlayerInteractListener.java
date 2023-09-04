package me.github.lilyvxv.generators.listeners;

import me.github.lilyvxv.generators.Generators;
import me.github.lilyvxv.generators.utils.config.DataManager;
import me.github.lilyvxv.generators.utils.generators.Generator;
import me.github.lilyvxv.generators.utils.misc.PermissionUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.github.lilyvxv.generators.Generators.dataManager;
import static org.bukkit.event.EventPriority.LOWEST;

public class PlayerInteractListener implements Listener {

    @EventHandler(priority = LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            Location location = block.getLocation();

            // Check if the interacted block is a generator and if the player is allowed to break it
            Generator generator = dataManager.getGenerator(location);

            if (generator == null) {
                return;
            }

            if (generator.generatorOwner.equals(event.getPlayer().getUniqueId()) || player.hasPermission("generators.break-others")) {
                // Remove the generator from the world and the sqlite database
                block.setType(Material.AIR);
                dataManager.removeGenerator(generator);

                // Play a sound to the player
                player.playSound(location, Sound.ENTITY_ITEM_PICKUP, 6, 1);

                // If the player isn't in creative, give them the generator back
                if (player.getGameMode() != GameMode.CREATIVE) {
                    ItemStack generatorItem = generator.generatorType.getItemStack(1);
                    player.getInventory().addItem(generatorItem);
                }

                int maxGenerators = PermissionUtils.getPlayerMaxGens(player);
                int placedGenerators = dataManager.getAllOwnedGenerators(player);

                player.sendActionBar(Generators.prefix
                        .append(Generators.miniMessage.deserialize(
                                String.format("<white>Successfully removed generator</white> <gray>(<green>%d</green>/<red>%d</red>)</gray>", placedGenerators, maxGenerators))));
            } else {
                player.sendActionBar(Generators.prefix
                        .append(Generators.miniMessage.deserialize("<white>This is not your generator.</white>")));
            }
        }
    }
}