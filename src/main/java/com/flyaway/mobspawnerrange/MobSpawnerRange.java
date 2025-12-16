package com.flyaway.mobspawnerrange;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MobSpawnerRange extends JavaPlugin {

    private static MobSpawnerRange instance;
    private int activationRange;
    private FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private SpawnerListener spawnerListener;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        loadConfig();

        SpawnerCommand spawnerCommand = new SpawnerCommand();

        getCommand("spawnerrange").setExecutor(spawnerCommand);
        getCommand("spawnerrange").setTabCompleter(spawnerCommand);

        spawnerListener = new SpawnerListener();
        Bukkit.getPluginManager().registerEvents(spawnerListener, this);

        getLogger().info("MobSpawnerRange включен! Дистанция активации: " + activationRange + " блоков");
    }

    @Override
    public void onDisable() {
        getLogger().info("MobSpawnerRange выключен");
    }

    private void loadConfig() {
        config = getConfig();
        activationRange = config.getInt("activation-range", 32);
    }

    public void reloadPluginConfig() {
        reloadConfig();
        loadConfig();
    }

    public void sendMessage(CommandSender sender, String configPath, String... placeholders) {
        String message = config.getString(configPath, "<red>Message not found: " + configPath);

        if (placeholders.length > 0) {
            for (int i = 0; i < placeholders.length; i += 2) {
                if (i + 1 < placeholders.length) {
                    message = message.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
                }
            }
        }

        sender.sendMessage(miniMessage.deserialize(message));
    }

    public static MobSpawnerRange getInstance() {
        return instance;
    }

    public int getActivationRange() {
        return activationRange;
    }

    public SpawnerListener getSpawnerListener() {
        return spawnerListener;
    }
}
