package me.github.lilyvxv.generators.commands;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import me.github.lilyvxv.generators.menus.GeneratorListMenu;
import me.github.lilyvxv.generators.utils.generators.GeneratorType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.github.lilyvxv.generators.Generators.configManager;
import static me.github.lilyvxv.generators.Generators.miniMessage;

@Permission("generators.generator-list")
@Command("generator-list")
@Alias("genlist")
public class GeneratorListCommand {

    @Default
    public static void generatorList(Player player) {
        new GeneratorListMenu(player).open();
    }
}
