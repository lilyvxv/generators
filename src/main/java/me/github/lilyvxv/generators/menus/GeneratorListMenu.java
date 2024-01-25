package me.github.lilyvxv.generators.menus;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.github.lilyvxv.generators.utils.generators.GeneratorType;
import me.github.lilyvxv.generators.utils.misc.LoreConverter;
import me.github.lilyvxv.generators.utils.misc.MiniMessageReplace;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.item.ItemStackReader;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.github.lilyvxv.generators.Generators.*;

public class GeneratorListMenu extends Gui {

    private static final MenuScheme BORDER = new MenuScheme()
            .mask("111111111")
            .mask("100000001")
            .mask("100000001")
//            .mask("100000001")
//            .mask("100000001")
            .mask("111111111");

    private static final MenuScheme GENERATORS = new MenuScheme()
            .mask("000000000")
            .mask("011111110")
            .mask("011111110")
//            .mask("011111110")
//            .mask("011111110")
            .mask("000000000");

    public GeneratorListMenu(Player player) {
        super(player, 4, "&8Available Generators");
    }

    @Override
    public void redraw() {
        if (isFirstDraw()) {
            MenuPopulator populator = BORDER.newPopulator(this);

            ItemStack borderItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta borderMeta = borderItem.getItemMeta();
            borderMeta.displayName(miniMessage.deserialize(""));
            borderMeta.setCustomModelData(1);
            borderItem.setItemMeta(borderMeta);

            while (populator.hasSpace()) {
                populator.accept(Item.builder(borderItem).build());
            }

            populator = GENERATORS.newPopulator(this);

            for (String generatorType : configManager.getAllGeneratorTypes()) {
                GeneratorType generator = configManager.getGeneratorByType(generatorType);

                NBTItem nbt = new NBTItem(new ItemStack(generator.generatorBlock, 1));

                ItemStack generatorItem = nbt.getItem();
                ItemMeta generatorMeta = generatorItem.getItemMeta();

                generatorMeta.displayName(miniMessage.deserialize(generator.generatorName));

                List<Component> loreList = new ArrayList<>();
                loreList.add(configManager.getMessage("menu.lore_1"));
                loreList.add(configManager.getMessage("menu.lore_2", Placeholder.unparsed("drop_value", String.valueOf(generator.dropValue))));
                loreList.add(configManager.getMessage("menu.lore_3", Placeholder.unparsed("upgrade_cost", String.valueOf(generator.upgradeCost))));
                generatorMeta.lore(loreList);

                generatorItem.setItemMeta(generatorMeta);

                populator.accept(ItemStackBuilder.of(generatorItem)
                        .build(() -> {
                            LOGGER.info(generator.generatorName);
                        }));
            }
        }
    }
}