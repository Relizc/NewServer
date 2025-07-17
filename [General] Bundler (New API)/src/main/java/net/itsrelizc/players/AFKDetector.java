package net.itsrelizc.players;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.events.EventRegistery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AFKDetector implements Listener {

    // Stores the last location + timestamp of players
    private final HashMap<UUID, Long> lastMoveTimestamps = new HashMap<>();
    
    private final Set<UUID> afkPlayers = new HashSet<>();		


    private final long AFK_TIMEOUT = 60 * 1000; // 60 seconds in milliseconds

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, EventRegistery.main);

        // Scheduler to check AFK status every 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    long lastMoved = lastMoveTimestamps.getOrDefault(uuid, now);
                    boolean isAFK = (now - lastMoved) >= AFK_TIMEOUT;

                    if (isAFK && !afkPlayers.contains(uuid)) {
                        afkPlayers.add(uuid);
                        Bukkit.getPluginManager().callEvent(new PlayerAFKEvent(player, true));

                    }
                }
            }
        }.runTaskTimer(EventRegistery.main, 0L, 200L); // every 10 seconds (20 ticks = 1 second)
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;

        boolean positionChanged = from.distanceSquared(to) >= 0.01;
        boolean cameraChanged = from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch();

        if (!positionChanged && !cameraChanged) return;

        if (afkPlayers.contains(uuid)) {
            afkPlayers.remove(uuid);
            Bukkit.getPluginManager().callEvent(new PlayerAFKEvent(player, false));
        }

        lastMoveTimestamps.put(uuid, System.currentTimeMillis());
    }

    
    

    public void onDisable() {

        lastMoveTimestamps.clear();
    }
}
