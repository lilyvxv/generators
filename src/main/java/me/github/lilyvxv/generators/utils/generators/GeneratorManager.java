package me.github.lilyvxv.generators.utils.generators;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static me.github.lilyvxv.generators.Generators.dataManager;
import static me.github.lilyvxv.generators.Generators.plugin;

public class GeneratorManager {

    public static void spawnItems() {
        ArrayList<Generator> generators = dataManager.getAllGenerators();

        for (Generator generator : generators) {
            if (isOnline(generator.generatorOwner)){
                generator.summonDrop();
            }
        }
    }

    private static boolean isOnline(UUID playerUUID) {
        Player player = plugin.getServer().getPlayer(playerUUID);
        return player != null && player.isOnline();
    }
}