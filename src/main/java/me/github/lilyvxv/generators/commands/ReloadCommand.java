package me.github.lilyvxv.generators.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import me.github.lilyvxv.generators.Generators;
import org.bukkit.entity.Player;

@Permission("generators.reload")
@Command("genreload")
public class ReloadCommand {
    @Default
    public static void reloadConfig(Player player) {
        Generators.configManager.load();
        Generators.INSTANCE.onConfigLoad();
        player.sendMessage(Generators.prefix
                .append(Generators.miniMessage.deserialize("The config has been reloaded.")));
    }

}
