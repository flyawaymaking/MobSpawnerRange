package com.flyaway.mobspawnerrange;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getWorld().getMinHeight(); y < chunk.getWorld().getMaxHeight(); y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.SPAWNER) {
                        setupSpawner(block);
                    }
                }
            }
        }
    }

    private void setupSpawner(Block spawnerBlock) {
        if (!(spawnerBlock.getState() instanceof CreatureSpawner spawner)) {
            return;
        }

        try {
            int range = plugin.getActivationRange();
            spawner.setRequiredPlayerRange(range);
            spawner.update(true, false);

            if (plugin.getConfig().getBoolean("debug-mode", false)) {
                plugin.getLogger().info("✅ Установлен радиус активации " + range + " для спавнера в " + spawnerBlock.getLocation());
            }

        } catch (Exception e) {
            plugin.getLogger().warning("Не удалось обновить спавнер в " + spawnerBlock.getLocation() + ": " + e.getMessage());
        }
    }

    public void updateAllSpawners() {
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                processChunk(chunk);
            }
        }
    }
}
