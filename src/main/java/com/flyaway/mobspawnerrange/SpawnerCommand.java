package com.flyaway.mobspawnerrange;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SpawnerCommand implements CommandExecutor, TabCompleter {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final MobSpawnerRange plugin = MobSpawnerRange.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("mobspawnerrange.reload")) {
                    sendMessage(sender, "<red>У вас нет прав на эту команду!");
                    return true;
                }
                plugin.reloadPluginConfig();
                sendMessage(sender, "<green>Конфигурация MobSpawnerRange перезагружена!");
                sendMessage(sender, "<green>Все спавнеры обновлены с новым радиусом: " +
                        plugin.getActivationRange() + " блоков");
                break;

            case "info":
                if (!sender.hasPermission("mobspawnerrange.info")) {
                    sendMessage(sender, "<red>У вас нет прав на эту команду!");
                    return true;
                }
                sendMessage(sender, "<yellow>=== MobSpawnerRange ===");
                sendMessage(sender, "<green>Текущая дистанция активации: " +
                        plugin.getActivationRange() + " блоков");
                sendMessage(sender, "<green>Режим отладки: " +
                        (plugin.getConfig().getBoolean("debug-mode") ? "включен" : "выключен"));
                break;

            case "setrange":
                if (!sender.hasPermission("mobspawnerrange.setrange")) {
                    sendMessage(sender, "<red>У вас нет прав на эту команду!");
                    return true;
                }
                if (args.length < 2) {
                    sendMessage(sender, "<red>Использование: /spawnerrange setrange <дистанция>");
                    return true;
                }
                try {
                    int newRange = Integer.parseInt(args[1]);
                    if (newRange < 1 || newRange > 128) {
                        sendMessage(sender, "<red>Дистанция должна быть от 1 до 128 блоков!");
                        return true;
                    }
                    plugin.getConfig().set("activation-range", newRange);
                    plugin.saveConfig();
                    plugin.reloadPluginConfig();
                    sendMessage(sender, "<green>Дистанция активации установлена на " + newRange + " блоков!");
                    sendMessage(sender, "<green>Все спавнеры обновлены!");
                } catch (NumberFormatException e) {
                    sendMessage(sender, "<red>Пожалуйста, введите корректное число!");
                }
                break;

            case "updateall":
                if (!sender.hasPermission("mobspawnerrange.admin")) {
                    sendMessage(sender, "<red>У вас нет прав на эту команду!");
                    return true;
                }
                new SpawnerListener().updateAllSpawners();
                sendMessage(sender, "<green>Все спавнеры обновлены с текущими настройками!");
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sendMessage(sender, "<yellow>=== MobSpawnerRange Помощь ===");
        if (sender.hasPermission("mobspawnerrange.info")) {
            sendMessage(sender, "<green>/spawnerrange info - Информация о плагине");
        }
        if (sender.hasPermission("mobspawnerrange.reload")) {
            sendMessage(sender, "<green>/spawnerrange reload - Перезагрузить конфиг");
        }
        if (sender.hasPermission("mobspawnerrange.setrange")) {
            sendMessage(sender, "<green>/spawnerrange setrange <дистанция> - Установить дистанцию");
        }
        if (sender.hasPermission("mobspawnerrange.admin")) {
            sendMessage(sender, "<green>/spawnerrange updateall - Обновить все спавнеры");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("info");
            if (sender.hasPermission("mobspawnerrange.reload")) {
                completions.add("reload");
            }
            if (sender.hasPermission("mobspawnerrange.setrange")) {
                completions.add("setrange");
            }
            if (sender.hasPermission("mobspawnerrange.admin")) {
                completions.add("updateall");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setrange")) {
            completions.add("16");
            completions.add("24");
            completions.add("32");
            completions.add("48");
            completions.add("64");
        }

        return completions;
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(miniMessage.deserialize(message));
    }
}
