package me.github.lilyvxv.generators.listeners;

import me.github.lilyvxv.generators.utils.generators.Generator;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static me.github.lilyvxv.generators.Generators.dataManager;
import static org.bukkit.event.EventPriority.LOWEST;

public class BlockBreakListener implements Listener {
    @EventHandler(priority = LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();

        // Check if the interacted block is a generator
        Generator generator = dataManager.getGenerator(location);

        if (generator == null) {
            return;
        }

        // Since it is a generator, we cancel the event as we dont want generators
        // to disappear or be broken by other players due to lag or cheats :(
        event.setCancelled(true);
    }
}