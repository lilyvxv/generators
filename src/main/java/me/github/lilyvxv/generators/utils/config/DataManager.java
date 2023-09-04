package me.github.lilyvxv.generators.utils.config;

import me.github.lilyvxv.generators.utils.generators.Generator;
import me.github.lilyvxv.generators.utils.generators.GeneratorType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.github.lilyvxv.generators.Generators.configManager;
import static me.github.lilyvxv.generators.Generators.INSTANCE;

public class DataManager {

    private final Logger logger = Logger.getLogger(DataManager.class.getName());
    private final Connection connection;

    public DataManager(String dbPath) throws SQLException {
        String url = "jdbc:sqlite:" + dbPath;
        connection = DriverManager.getConnection(url);
        createTable();
    }

    public void createTable() {
        try (PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS player_data (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "player_uuid TEXT NOT NULL," +
                        "generator_type TEXT NOT NULL," +
                        "world TEXT NOT NULL," +
                        "x REAL NOT NULL," +
                        "y REAL NOT NULL," +
                        "z REAL NOT NULL)")) {

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating table", e);
        }
    }

    public void addGenerator(Generator generator) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO player_data (player_uuid, generator_type, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, generator.generatorOwner.toString());
            stmt.setString(2, generator.generatorType.generatorType);
            Location location = generator.generatorLocation;
            stmt.setString(3, location.getWorld().getName());
            stmt.setDouble(4, location.getBlockX());
            stmt.setDouble(5, location.getBlockY());
            stmt.setDouble(6, location.getBlockZ());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding generator", e);
        }
    }

    public void removeGenerator(Generator generator) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM player_data WHERE world = ? AND x = ? AND y = ? AND z = ?")) {

            Location location = generator.generatorLocation;
            stmt.setString(1, location.getWorld().getName());
            stmt.setDouble(2, location.getBlockX());
            stmt.setDouble(3, location.getBlockY());
            stmt.setDouble(4, location.getBlockZ());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error removing generator", e);
        }
    }

    public Generator getGenerator(Location location) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM player_data WHERE world = ? AND x = ? AND y = ? AND z = ?")) {

            stmt.setString(1, location.getWorld().getName());
            stmt.setDouble(2, location.getBlockX());
            stmt.setDouble(3, location.getBlockY());
            stmt.setDouble(4, location.getBlockZ());

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    UUID generatorOwner = UUID.fromString(resultSet.getString("player_uuid"));
                    GeneratorType generatorType = configManager.getGeneratorByType(resultSet.getString("generator_type"));

                    Generator generator = new Generator(generatorType, location, generatorOwner);
                    generator.generatorLocation = location;
                    generator.generatorOwner = generatorOwner;

                    return generator;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting generator", e);
        }

        return null;
    }

    public int getAllOwnedGenerators(Player player) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM player_data WHERE player_uuid = ?")) {

            stmt.setString(1, player.getUniqueId().toString());

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting owned generators count", e);
        }

        return 0;
    }

    public ArrayList<Generator> getAllGenerators() {
        ArrayList<Generator> generators = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT generator_type, player_uuid, world, x, y, z FROM player_data");
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                GeneratorType generatorType = configManager.getGeneratorByType(resultSet.getString("generator_type"));
                UUID generatorOwner = UUID.fromString(resultSet.getString("player_uuid"));

                Location location = new Location(
                        INSTANCE.getServer().getWorld(resultSet.getString("world")),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("z")
                );

                Generator generator = new Generator(generatorType, location, generatorOwner);
                generator.generatorLocation = location;
                generator.generatorOwner = generatorOwner;

                generators.add(generator);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all generators", e);
        }

        return generators;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error closing connection", e);
        }
    }
}
