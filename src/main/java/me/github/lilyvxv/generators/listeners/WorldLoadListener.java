package me.github.lilyvxv.generators.listeners;

import me.github.lilyvxv.generators.Generators;
import me.github.lilyvxv.generators.utils.generators.VerifyGenerators;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadListener implements Listener {

    public WorldLoadListener(Generators plugin) {
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        VerifyGenerators.verify(event.getWorld());
    }
}