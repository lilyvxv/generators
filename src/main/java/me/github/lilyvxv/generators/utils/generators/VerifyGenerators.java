package me.github.lilyvxv.generators.utils.generators;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;

import static me.github.lilyvxv.generators.Generators.dataManager;
import static me.github.lilyvxv.generators.Generators.logger;

public class VerifyGenerators {

    public static void verify(World world) {
        ArrayList<Generator> generators = dataManager.getAllGenerators();
        int removedCount = 0;

        for (Generator generator: generators) {
            World generatorWorld = generator.generatorLocation.getWorld();

            if (generatorWorld == world) {
                Block worldBlock = generatorWorld.getBlockAt(generator.generatorLocation);

                if (worldBlock.getType() != generator.generatorType.generatorBlock) {
                    dataManager.removeGenerator(generator);
                    removedCount++;
                }
            }
        }

        if (removedCount > 0) {
            logger.info("Removed " + removedCount + " de-synced generators");
        }
    }
}
