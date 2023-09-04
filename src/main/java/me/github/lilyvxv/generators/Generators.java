package me.github.lilyvxv.generators;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.github.lilyvxv.generators.commands.GiveGeneratorCommand;
import me.github.lilyvxv.generators.commands.ReloadCommand;
import me.github.lilyvxv.generators.listeners.BlockBreakListener;
import me.github.lilyvxv.generators.listeners.BlockPlaceListener;
import me.github.lilyvxv.generators.listeners.PlayerInteractListener;
import me.github.lilyvxv.generators.listeners.WorldLoadListener;
import me.github.lilyvxv.generators.utils.config.ConfigManager;
import me.github.lilyvxv.generators.utils.config.DataManager;
import me.github.lilyvxv.generators.utils.generators.GeneratorManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

public final class Generators extends JavaPlugin {

    public static DataManager dataManager;
    public static ConfigManager configManager;
    public static Logger LOGGER;
    public static MiniMessage miniMessage;
    public static Generators INSTANCE;
    public static Component prefix;

    @Override
    public void onLoad() {
        LOGGER = getLogger();
        INSTANCE = this;

        saveDefaultConfig();
        reloadConfig();

        FileConfiguration pluginConfig = getConfig();
        configManager = new ConfigManager(pluginConfig);
        onConfigLoad();

        try {
            dataManager = new DataManager(getDataFolder().getPath() + "/generators.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dataManager.createTable();

        CommandAPIBukkitConfig commandAPIConfig = new CommandAPIBukkitConfig(getPlugin(this.getClass()));
        CommandAPI.onLoad(commandAPIConfig);
    }

    public void onConfigLoad() {
        MiniMessage.Builder miniMessageBuilder = MiniMessage.builder();
        TagResolver.Builder tagResolverBuilder = TagResolver.builder()
                .resolver(TagResolver.standard());

        // Add tags from config
        for (Map.Entry<String, TextColor> entry : ConfigManager.colorScheme.entrySet()) {
            tagResolverBuilder.tag(entry.getKey(), Tag.styling(style -> style.color(entry.getValue())));
        }

        miniMessage = miniMessageBuilder.tags(tagResolverBuilder.build())
                .build();

        prefix = configManager.getMessage("prefix");
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        CommandAPI.registerCommand(GiveGeneratorCommand.class);
        CommandAPI.registerCommand(ReloadCommand.class);

        getServer().getPluginManager().registerEvents(new WorldLoadListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, GeneratorManager::spawnItems, 0, 200);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        dataManager.closeConnection();
    }
}

