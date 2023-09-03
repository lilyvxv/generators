package me.github.lilyvxv.generators;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.github.lilyvxv.generators.commands.GiveGeneratorCommand;
import me.github.lilyvxv.generators.listeners.BlockBreakListener;
import me.github.lilyvxv.generators.listeners.BlockPlaceListener;
import me.github.lilyvxv.generators.listeners.PlayerInteractListener;
import me.github.lilyvxv.generators.listeners.WorldLoadListener;
import me.github.lilyvxv.generators.utils.config.ConfigManager;
import me.github.lilyvxv.generators.utils.config.DataManager;
import me.github.lilyvxv.generators.utils.generators.GeneratorManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.logging.Logger;

public final class Generators extends JavaPlugin {

    public static DataManager dataManager;
    public static ConfigManager configManager;
    public static Logger logger;
    public static MiniMessage miniMessage = MiniMessage.miniMessage();
    public static Generators plugin;

    @Override
    public void onLoad() {
        logger = getLogger();
        plugin = this;

        saveDefaultConfig();
        reloadConfig();

        FileConfiguration pluginConfig = getConfig();
        configManager = new ConfigManager(pluginConfig);

        try {
            dataManager = new DataManager(getDataFolder().getPath() + "/generators.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dataManager.createTable();

        CommandAPIBukkitConfig commandAPIConfig = new CommandAPIBukkitConfig(getPlugin(this.getClass()));
        CommandAPI.onLoad(commandAPIConfig);
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        CommandAPI.registerCommand(GiveGeneratorCommand.class);

        getServer().getPluginManager().registerEvents(new WorldLoadListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, GeneratorManager::spawnItems, 0, 200);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();

        dataManager.closeConnection();
    }
}

