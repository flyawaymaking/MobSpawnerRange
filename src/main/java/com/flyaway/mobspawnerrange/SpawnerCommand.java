package com.flyaway.mobspawnerrange;

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
                    plugin.sendMessage(sender, "messages.no-permission");
                    return true;
                }
                plugin.reloadPluginConfig();
                plugin.sendMessage(sender, "messages.reload-success");
                break;

            case "info":
                if (!sender.hasPermission("mobspawnerrange.info")) {
                    plugin.sendMessage(sender, "messages.no-permission");
                    return true;
                }
                plugin.sendMessage(sender, "messages.info",
                        "range", String.valueOf(plugin.getActivationRange()));

                if (plugin.getConfig().getBoolean("debug-mode")) {
                    plugin.sendMessage(sender, "messages.info-debug-enabled");
                } else {
                    plugin.sendMessage(sender, "messages.info-debug-disabled");
                }
                break;

            case "setrange":
                if (!sender.hasPermission("mobspawnerrange.setrange")) {
                    plugin.sendMessage(sender, "messages.no-permission");
                    return true;
                }
                if (args.length < 2) {
                    plugin.sendMessage(sender, "messages.setrange-usage");
                    return true;
                }
                try {
                    int newRange;
                    try {
                        newRange = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        plugin.sendMessage(sender, "messages.setrange-invalid");
                        return true;
                    }
                    plugin.getConfig().set("activation-range", newRange);
                    plugin.saveConfig();
                    plugin.reloadPluginConfig();
                    plugin.sendMessage(sender, "messages.setrange-success",
                            "range", String.valueOf(newRange));
                    plugin.getSpawnerListener().updateAllSpawners();
                    plugin.sendMessage(sender, "messages.setrange-updated");
                } catch (NumberFormatException e) {
                    plugin.sendMessage(sender, "messages.invalid-number");
                }
                break;

            case "updateall":
                if (!sender.hasPermission("mobspawnerrange.updateall")) {
                    plugin.sendMessage(sender, "messages.no-permission");
                    return true;
                }
                plugin.getSpawnerListener().updateAllSpawners();
                plugin.sendMessage(sender, "messages.updateall-success");
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        plugin.sendMessage(sender, "messages.help-header");

        if (sender.hasPermission("mobspawnerrange.info")) {
            plugin.sendMessage(sender, "messages.help-info");
        }
        if (sender.hasPermission("mobspawnerrange.reload")) {
            plugin.sendMessage(sender, "messages.help-reload");
        }
        if (sender.hasPermission("mobspawnerrange.setrange")) {
            plugin.sendMessage(sender, "messages.help-setrange");
        }
        if (sender.hasPermission("mobspawnerrange.updateall")) {
            plugin.sendMessage(sender, "messages.help-updateall");
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
            if (sender.hasPermission("mobspawnerrange.updateall")) {
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
