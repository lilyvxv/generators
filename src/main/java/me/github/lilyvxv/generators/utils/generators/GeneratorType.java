package me.github.lilyvxv.generators.utils.generators;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.github.lilyvxv.generators.utils.misc.LoreConverter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.github.lilyvxv.generators.Generators.miniMessage;

public class GeneratorType {

    public final String generatorType;
    public final String generatorName;
    public final Material generatorBlock;
    public final String generatorLore;
    public final String dropName;
    public final Material dropItem;
    public final double dropValue;
    public final double upgradeCost;

    public GeneratorType(String generatorType, String generatorName, String generatorBlock, String generatorLore, String dropName, String dropItem, int dropValue, int upgradeCost) {
        this.generatorType = generatorType;
        this.generatorName = generatorName;
        this.generatorBlock = Material.valueOf(generatorBlock.toUpperCase());
        this.generatorLore = generatorLore;
        this.dropName = dropName;
        this.dropItem = Material.valueOf(dropItem.toUpperCase());
        this.dropValue = dropValue;
        this.upgradeCost = upgradeCost;
    }

    public ItemStack getItemStack(int quantity) {
        NBTItem nbt = new NBTItem(new ItemStack(this.generatorBlock, quantity));
        nbt.setBoolean("generator", true);
        nbt.setString("generator_type", this.generatorType);

        ItemStack generatorItem = nbt.getItem();
        ItemMeta generatorMeta = generatorItem.getItemMeta();

        generatorMeta.displayName(miniMessage.deserialize(this.generatorName));
        generatorMeta.lore(LoreConverter.deserialize(this.generatorLore));
        generatorItem.setItemMeta(generatorMeta);

        return generatorItem;
    }
}
