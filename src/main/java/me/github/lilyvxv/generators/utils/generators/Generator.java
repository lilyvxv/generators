package me.github.lilyvxv.generators.utils.generators;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.UUID;

import static me.github.lilyvxv.generators.Generators.miniMessage;

public class Generator {

    public GeneratorType generatorType;
    public Location generatorLocation;
    public UUID generatorOwner;

    public Generator(GeneratorType generatorType, Location generatorLocation, UUID generatorOwner) {
        this.generatorType = generatorType;
        this.generatorLocation = generatorLocation;
        this.generatorOwner = generatorOwner;
    }

    public void summonDrop() {
        World world = generatorLocation.getWorld();
        Location dropLocation = generatorLocation.add(0.5,1.2, 0.5);

        ItemStack drop = new ItemStack(this.generatorType.dropItem, 1);
        ItemMeta dropMeta = drop.getItemMeta();

        dropMeta.displayName(miniMessage.deserialize(this.generatorType.dropName));
        drop.setItemMeta(dropMeta);

        Item generatorDrop = world.dropItem(dropLocation, drop);
        generatorDrop.setVelocity(new Vector(0, 0, 0));
        generatorDrop.setPickupDelay(5);
    }
}
