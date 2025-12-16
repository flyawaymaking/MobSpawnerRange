package com.flyaway.mobspawnerrange;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class SpawnerListener implements Listener {

    private final MobSpawnerRange plugin = MobSpawnerRange.getInstance();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        processChunk(event.getChunk());
    }

    private void processChunk(Chunk chunk) {
        for (BlockState state : chunk.getTileEntities()) {
            if (!(state instanceof CreatureSpawner spawner)) {
                continue;
            }

            setupSpawner(spawner);
        }
    }

    private void setupSpawner(CreatureSpawner spawner) {
        try {
            int range = plugin.getActivationRange();
            spawner.setRequiredPlayerRange(range);

            spawner.update(false, false);

            if (plugin.getConfig().getBoolean("debug-mode", false)) {
                plugin.getLogger().info(
                        "✅ Установлен радиус активации " + range +
                                " для спавнера в " + spawner.getBlock().getLocation()
                );
            }
        } catch (Exception e) {
            plugin.getLogger().warning(
                    "Не удалось обновить спавнер в " +
                            spawner.getBlock().getLocation() + ": " + e.getMessage()
            );
        }
    }

    public void updateAllSpawners() {
        for (var world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                processChunk(chunk);
            }
        }
    }
}
