package me.github.lilyvxv.generators.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.github.lilyvxv.generators.Generators;
import me.github.lilyvxv.generators.utils.config.ConfigManager;
import me.github.lilyvxv.generators.utils.config.DataManager;
import me.github.lilyvxv.generators.utils.generators.Generator;
import me.github.lilyvxv.generators.utils.generators.GeneratorType;
import me.github.lilyvxv.generators.utils.misc.PermissionUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.event.EventPriority.LOWEST;

public class BlockPlaceListener implements Listener {

    private static final DataManager dataManager = Generators.dataManager;
    private static final ConfigManager configManager = Generators.configManager;
    @EventHandler(priority = LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        NBTItem nbt = new NBTItem(item);
        Location location = event.getBlockPlaced().getLocation();

        // Make sure that the placed item is a generator
        if (nbt.getBoolean("generator")) {
            // Make sure that the player has enough generator slots to place the generator
            int maxGenerators = PermissionUtils.getPlayerMaxGens(player);
            int placedGenerators = dataManager.getAllOwnedGenerators(player);

            if (placedGenerators >= maxGenerators) {
                player.sendActionBar(Generators.prefix
                        .append(configManager.getMessage("gens.no_slots")));

                event.setCancelled(true);
            } else {
                // Add the generator to the sqlite database
                GeneratorType generatorType = configManager.getGeneratorByType(nbt.getString("generator_type"));
                Generator generator = new Generator(generatorType, location, player.getUniqueId());
                dataManager.addGenerator(generator);

                // Play a sound and show a particle to the player
                player.playSound(location, Sound.ITEM_ARMOR_EQUIP_GOLD, 6, 1);
                player.spawnParticle(Particle.WAX_OFF, location.add(0.5, 1.2, 0.5), 3);

                player.sendActionBar(Generators.prefix
                        .append(configManager.getMessage("gens.place", Placeholder.unparsed("placed", String.valueOf(placedGenerators + 1)), Placeholder.unparsed("max", String.valueOf(maxGenerators)))));
            }
        }
    }
}
