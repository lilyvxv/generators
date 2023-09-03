package me.github.lilyvxv.generators.utils.config;

import me.github.lilyvxv.generators.utils.generators.Generator;
import me.github.lilyvxv.generators.utils.generators.GeneratorType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    public static final List<GeneratorType> AVAILABLE_GENERATOR_TYPES = new ArrayList<>();
    private final FileConfiguration config;

    public static int maxDefaultGenerators;

    public ConfigManager(FileConfiguration config) {
        this.config = config;
        loadConfig();
        loadGenerators();
    }

    private void loadConfig() {
        maxDefaultGenerators = config.getInt("max_default_generators");
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

    public List<String> getAllGeneratorTypes() {
        List<String> generatorTypes = new ArrayList<>();
        for (GeneratorType generatorType : AVAILABLE_GENERATOR_TYPES) {
            generatorTypes.add(generatorType.generatorName);
        }
        return generatorTypes;
    }
}