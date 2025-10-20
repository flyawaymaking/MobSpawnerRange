package com.flyaway.mobspawnerrange;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MobSpawnerRange extends JavaPlugin {

    private static MobSpawnerRange instance;
    private int activationRange;

    @Override
    public void onEnable() {
        instance = this;

        // Сохраняем конфиг по умолчанию
        saveDefaultConfig();

        // Загружаем настройки
        loadConfig();

        // Регистрируем слушатель
        Bukkit.getPluginManager().registerEvents(new SpawnerListener(), this);

        // Регистрируем команду
        this.getCommand("spawnerrange").setExecutor(new SpawnerCommand());

        getLogger().info("MobSpawnerRange включен! Дистанция активации: " + activationRange + " блоков");
    }

    @Override
    public void onDisable() {
        getLogger().info("MobSpawnerRange выключен");
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();

        // Устанавливаем значения по умолчанию
        config.addDefault("activation-range", 32);
        config.addDefault("debug-mode", false);

        config.options().copyDefaults(true);
        saveConfig();

        activationRange = config.getInt("activation-range", 32);
    }

    public void reloadPluginConfig() {
        reloadConfig();
        loadConfig();

        // Обновляем все спавнеры с новыми настройками
        new SpawnerListener().updateAllSpawners();
    }

    public static MobSpawnerRange getInstance() {
        return instance;
    }

    public int getActivationRange() {
        return activationRange;
    }
}
