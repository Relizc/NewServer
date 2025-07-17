package net.itsrelizc.players;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.itsrelizc.events.EventRegistery;

public class PlaytimeTracker {
	
	public class ProfileKeys {
	    public static final String LAST_ACTIVE_TIME = "lastActiveTime";       // Long (ms)
	    public static final String TOTAL_PLAYTIME = "totalActivePlaytime";    // Long (ms)
	    public static final String LAST_WARNED_HOUR = "lastWarnedHour";       // Integer
	}

	
	public static class PlayerTimeTrackerListeners implements Listener {
		@EventHandler
		public void onPlayerAFK(PlayerAFKEvent event) {
		    Player player = event.getPlayer();
		    if (event.isAFK()) {
		        PlaytimeTracker.setAFK(player);
		    } else {
		        PlaytimeTracker.setActive(player);
		    }
		}

		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event) {
		    PlaytimeTracker.setActive(event.getPlayer());
		}

		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent event) {
		    PlaytimeTracker.clear(event.getPlayer());
		}
		

	}
	
	public static void startTimerToWarnForPlaytime() {
		Bukkit.getScheduler().runTaskTimer(EventRegistery.main, () -> {
		    for (Player player : Bukkit.getOnlinePlayers()) {
		        long millis = PlaytimeTracker.getTotalPlaytimeMillis(player);
		        int hoursPlayed = (int) (millis / (60 * 60 * 1000L));

		        if (hoursPlayed > 0 && hoursPlayed > PlaytimeTracker.getLastWarnedHour(player)) {
		            player.sendMessage(ChatColor.GOLD + "You've been actively playing for " + hoursPlayed + " hour"
		                    + (hoursPlayed > 1 ? "s" : "") + "!");
		            player.sendMessage(ChatColor.GRAY + "Remember to take breaks to rest your eyes and body.");
		            PlaytimeTracker.setLastWarnedHour(player, hoursPlayed);
		        }
		    }
		}, 0L, 20L * 60); // Check every minute

	}
	
	private static final Map<UUID, Long> lastActiveTime = new HashMap<>();
    private static final Map<UUID, Integer> lastWarnedHour = new HashMap<>();


    public static void clear(Player player) {
        setAFK(player); // finalize session
        lastActiveTime.remove(player.getUniqueId());
        lastWarnedHour.remove(player.getUniqueId());
        
        
    }

    public static void setActive(Player player) {
        Profile profile = Profile.findByOwner(player);
        profile.setMetadata(ProfileKeys.LAST_ACTIVE_TIME, System.currentTimeMillis());
    }

    public static void setAFK(Player player) {
        Profile profile = Profile.findByOwner(player);
        Object last = profile.getMetadata(ProfileKeys.LAST_ACTIVE_TIME);
        if (last instanceof Long) {
            long lastTime = (Long) last;
            long elapsed = System.currentTimeMillis() - lastTime;
            long total = getTotalPlaytimeMillis(player);
            profile.setMetadata(ProfileKeys.TOTAL_PLAYTIME, total + elapsed);
        }
        profile.setMetadata(ProfileKeys.LAST_ACTIVE_TIME, null); // clear
    }

    public static long getTotalPlaytimeMillis(Player player) {
        Profile profile = Profile.findByOwner(player);
        long base = 0L;
        Object stored = profile.getMetadata(ProfileKeys.TOTAL_PLAYTIME);
        if (stored instanceof Long) base = (Long) stored;

        Object last = profile.getMetadata(ProfileKeys.LAST_ACTIVE_TIME);
        if (last instanceof Long) {
            base += (System.currentTimeMillis() - (Long) last);
        }

        return base;
    }

    public static void resetPlaytime(Player player) {
        Profile profile = Profile.findByOwner(player);
        profile.setMetadata(ProfileKeys.TOTAL_PLAYTIME, 0L);
        profile.setMetadata(ProfileKeys.LAST_ACTIVE_TIME, null);
        profile.setMetadata(ProfileKeys.LAST_WARNED_HOUR, 0);
    }

    public static int getLastWarnedHour(Player player) {
        Profile profile = Profile.findByOwner(player);
        Object value = profile.getMetadata(ProfileKeys.LAST_WARNED_HOUR);
        return value instanceof Integer ? (Integer) value : 0;
    }

    public static void setLastWarnedHour(Player player, int hour) {
        Profile profile = Profile.findByOwner(player);
        profile.setMetadata(ProfileKeys.LAST_WARNED_HOUR, hour);
    }
}

