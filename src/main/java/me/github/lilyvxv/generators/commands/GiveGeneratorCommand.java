package me.github.lilyvxv.generators.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import me.github.lilyvxv.generators.utils.generators.GeneratorType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.github.lilyvxv.generators.Generators.configManager;
import static me.github.lilyvxv.generators.Generators.miniMessage;

@Permission("generators.give-generator")
@Command("give-generator")
public class GiveGeneratorCommand {

    @Default
    public static void giveGenerator(Player player, @APlayerArgument Player targetPlayer, @AStringArgument String generatorType, @AIntegerArgument Integer quantity) {
        GeneratorType generator = configManager.getGeneratorByType(generatorType);

        if (generator == null) {
            player.sendMessage(miniMessage.deserialize("<red>Could not find a generator by that name in the config.</red>"));
            return;
        }

        ItemStack generatorItem = generator.getItemStack(quantity);
        targetPlayer.getInventory().addItem(generatorItem);
    }
}
