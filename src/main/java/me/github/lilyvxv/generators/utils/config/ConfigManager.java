package me.github.lilyvxv.generators.utils.config;

import me.github.lilyvxv.generators.Generators;
import me.github.lilyvxv.generators.utils.generators.Generator;
import me.github.lilyvxv.generators.utils.generators.GeneratorType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public static final List<GeneratorType> AVAILABLE_GENERATOR_TYPES = new ArrayList<>();
    private final FileConfiguration config;

    public static int maxDefaultGenerators;
    public static Map<String, TextColor> colorScheme = new HashMap<>();

    public ConfigManager(FileConfiguration config) {
        this.config = config;
        load();
    }

    public void load() {
        loadConfig();
        loadGenerators();
    }

    private void loadConfig() {
        maxDefaultGenerators = config.getInt("max_default_generators");
        for (String key : config.getConfigurationSection("colors").getKeys(false)) {
            String value = config.getString("colors." + key);
            colorScheme.put(key, TextColor.fromHexString(value));
        }
    }

    private void loadGenerators() {
        // Get the generators section on the config.yml file
        ConfigurationSection generatorsSection = config.getConfigurationSection("generators");

        if (generatorsSection != null) {
            // Loop all of the generators in the generators section
            for (String generatorKey : generatorsSection.getKeys(false)) {
                ConfigurationSection generatorConfig = generatorsSection.getConfigurationSection(generatorKey);

                if (generatorConfig != null) {
                    // Extract the generator data from the config
                    String generatorName = generatorConfig.getString("generator_name");
                    String generatorBlock = generatorConfig.getString("generator_block");
                    String generatorLore = generatorConfig.getString("generator_lore");
                    String dropName = generatorConfig.getString("drop_name");
                    String dropItem = generatorConfig.getString("drop_item");
                    int dropValue = generatorConfig.getInt("drop_value");
                    int upgradeCost = generatorConfig.getInt("upgrade_cost");

                    // Create a generator instance from the data
                    GeneratorType generatorType = new GeneratorType(generatorKey, generatorName, generatorBlock, generatorLore, dropName, dropItem, dropValue, upgradeCost);
                    AVAILABLE_GENERATOR_TYPES.add(generatorType);
                }
            }
        }
    }

    public GeneratorType getGeneratorByType(String type) {
        for (GeneratorType generatorType : AVAILABLE_GENERATOR_TYPES) {
            if (generatorType.generatorType.equalsIgnoreCase(type)) {
                return generatorType;
            }
        }
        return null;
    }

    public Component getMessage(String key, @NotNull TagResolver... tagResolvers) {
        return Generators.miniMessage
                .deserialize(config.getString("messages." + key), tagResolvers);
    }
    public List<String> getAllGeneratorTypes() {
        List<String> generatorTypes = new ArrayList<>();
        for (GeneratorType generatorType : AVAILABLE_GENERATOR_TYPES) {
            generatorTypes.add(generatorType.generatorName);
        }
        return generatorTypes;
    }
}