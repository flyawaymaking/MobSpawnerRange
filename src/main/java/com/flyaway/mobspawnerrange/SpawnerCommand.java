package com.flyaway.mobspawnerrange;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SpawnerCommand implements CommandExecutor, TabCompleter {

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
                    sender.sendMessage(ChatColor.RED + "У вас нет прав на эту команду!");
                    return true;
                }
                plugin.reloadPluginConfig();
                sender.sendMessage(ChatColor.GREEN + "Конфигурация MobSpawnerRange перезагружена!");
                sender.sendMessage(ChatColor.GREEN + "Все спавнеры обновлены с новым радиусом: " +
                    plugin.getActivationRange() + " блоков");
                break;

            case "info":
                if (!sender.hasPermission("mobspawnerrange.info")) {
                    sender.sendMessage(ChatColor.RED + "У вас нет прав на эту команду!");
                    return true;
                }
                sender.sendMessage(ChatColor.YELLOW + "=== MobSpawnerRange ===");
                sender.sendMessage(ChatColor.GREEN + "Текущая дистанция активации: " +
                    plugin.getActivationRange() + " блоков");
                sender.sendMessage(ChatColor.GREEN + "Режим отладки: " +
                    (plugin.getConfig().getBoolean("debug-mode") ? "включен" : "выключен"));
                break;

            case "setrange":
                if (!sender.hasPermission("mobspawnerrange.setrange")) {
                    sender.sendMessage(ChatColor.RED + "У вас нет прав на эту команду!");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Использование: /spawnerrange setrange <дистанция>");
                    return true;
                }
                try {
                    int newRange = Integer.parseInt(args[1]);
                    if (newRange < 1 || newRange > 128) {
                        sender.sendMessage(ChatColor.RED + "Дистанция должна быть от 1 до 128 блоков!");
                        return true;
                    }
                    plugin.getConfig().set("activation-range", newRange);
                    plugin.saveConfig();
                    plugin.reloadPluginConfig();
                    sender.sendMessage(ChatColor.GREEN + "Дистанция активации установлена на " + newRange + " блоков!");
                    sender.sendMessage(ChatColor.GREEN + "Все спавнеры обновлены!");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Пожалуйста, введите корректное число!");
                }
                break;

            case "updateall":
                if (!sender.hasPermission("mobspawnerrange.admin")) {
                    sender.sendMessage(ChatColor.RED + "У вас нет прав на эту команду!");
                    return true;
                }
                new SpawnerListener().updateAllSpawners();
                sender.sendMessage(ChatColor.GREEN + "Все спавнеры обновлены с текущими настройками!");
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "=== MobSpawnerRange Помощь ===");
        if (sender.hasPermission("mobspawnerrange.info")) {
            sender.sendMessage(ChatColor.GREEN + "/spawnerrange info - Информация о плагине");
        }
        if (sender.hasPermission("mobspawnerrange.reload")) {
            sender.sendMessage(ChatColor.GREEN + "/spawnerrange reload - Перезагрузить конфиг");
        }
        if (sender.hasPermission("mobspawnerrange.setrange")) {
            sender.sendMessage(ChatColor.GREEN + "/spawnerrange setrange <дистанция> - Установить дистанцию");
        }
        if (sender.hasPermission("mobspawnerrange.admin")) {
            sender.sendMessage(ChatColor.GREEN + "/spawnerrange updateall - Обновить все спавнеры");
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
}
