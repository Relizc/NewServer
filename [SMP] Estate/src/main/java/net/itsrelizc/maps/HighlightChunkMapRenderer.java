package net.itsrelizc.maps;

import java.awt.Color;

import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.itsrelizc.events.EventRegistery;

public class HighlightChunkMapRenderer extends MapRenderer {
    private final NamespacedKey key;

    public HighlightChunkMapRenderer(Plugin plugin) {
        super(true);  // contextual
        this.key = new NamespacedKey(plugin, "relizcPurchasedOwner");
    }

    @Override
    public void initialize(MapView map) {
        map.setScale(MapView.Scale.CLOSE);
        map.setTrackingPosition(true);
        map.setUnlimitedTracking(false);
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
    	
    	
        int cx = player.getLocation().getBlockX();
        int cz = player.getLocation().getBlockZ();
        
        view.setCenterX(cx);
        view.setCenterZ(cz);
        
        World world = player.getWorld();
        int radius = 8, size = 16;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Chunk chunk = world.getChunkAt(cx / size + dx, cz / size + dz);
                if (!chunk.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) continue;

                for (int x = 0; x < size; x++) for (int z = 0; z < size; z++) {
                    int wx = chunk.getX() * size + x;
                    int wz = chunk.getZ() * size + z;
                    int mx = wx - cx + 64;
                    int mz = wz - cz + 64;
                    if (mx < 0 || mx > 127 || mz < 0 || mz > 127) continue;

                    Color base = canvas.getBasePixelColor(mx, mz);
                    // blend 20% red:
                    int nr = (int) (base.getRed() * 0.8 + 255 * 0.2);
                    int ng = (int) (base.getGreen() * 0.8);
                    int nb = (int) (base.getBlue() * 0.8);
                    canvas.setPixelColor(cx, size, new Color(nr, ng, nb));
                }
            }
        }
    }
}



