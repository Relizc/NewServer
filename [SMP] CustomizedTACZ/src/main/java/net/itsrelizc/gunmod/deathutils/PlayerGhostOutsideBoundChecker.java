package net.itsrelizc.gunmod.deathutils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.itsrelizc.gunmod.deathutils.DeathUtils.PlayerGhostEvent;

public class PlayerGhostOutsideBoundChecker implements Listener {
	
	public static class GhostManager {
	    public static class GhostData {
	        public final Location deathLocation;
	        public int violations = 0;

	        public GhostData(Location deathLocation) {
	            this.deathLocation = deathLocation;
	        }
	    }

	    private static final Map<UUID, GhostData> ghostPlayers = new HashMap<>();

	    public static void addGhost(Player player, Location deathLocation) {
	        ghostPlayers.put(player.getUniqueId(), new GhostData(deathLocation));
	    }

	    public static void removeGhost(Player player) {
	        ghostPlayers.remove(player.getUniqueId());
	    }

	    public static GhostData getGhostData(Player player) {
	        return ghostPlayers.get(player.getUniqueId());
	    }

	    public static boolean isGhost(Player player) {
	        return ghostPlayers.containsKey(player.getUniqueId());
	    }
	}

	
	@EventHandler
	public void onPlayerGhost(PlayerGhostEvent event) {
	    if (event.isGhost()) {
	        Player player = event.getPlayer();
	        Location deathLoc = player.getLocation();
	        GhostManager.addGhost(player, deathLoc);
	    } else {
	        GhostManager.removeGhost(event.getPlayer());
	    }
	}


}
