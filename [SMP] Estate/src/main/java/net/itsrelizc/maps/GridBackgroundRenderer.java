package net.itsrelizc.maps;

import java.awt.Color;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class GridBackgroundRenderer extends MapRenderer {

	private final Color light = new Color(200, 200, 200);
    private final Color dark = new Color(240, 240, 240);
    private final int cellSize = 16;

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        // Draw grid across entire map
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                boolean isDark = ((x / cellSize) + (y / cellSize)) % 2 == 0;
                canvas.setPixelColor(x, y, isDark ? dark : light);
            }
        }
        // Remove after first draw
        view.removeRenderer(this);
    }
}
