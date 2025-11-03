package net.itsrelizc.dimensions.end;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.quests.QuestUtils;

public class PreventEndListener implements Listener {
	
	private final Map<UUID, Long> lastMessageTime = new ConcurrentHashMap<>();

    // Cool-down in milliseconds
    private static final long MESSAGE_COOLDOWN_MS = 5_000;
	
	@EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getCause() == TeleportCause.END_PORTAL) {
            Player player = event.getPlayer();
            Location toLoc = event.getTo();
            World.Environment env = (toLoc != null ? toLoc.getWorld().getEnvironment() : null);

            if (env == World.Environment.THE_END) {
                // Cancel teleport
                event.setCancelled(true);

                // Send message if cooldown passed
                UUID uuid = player.getUniqueId();
                long now = System.currentTimeMillis();
                Long last = lastMessageTime.get(uuid);
                if (last == null || now - last >= MESSAGE_COOLDOWN_MS) {
                	
                	Profile prof = Profile.findByOwner(player);
                	if (prof.getMetadata("quest.brown.endportal", null) == null) {
                		
                		new BukkitRunnable() {

							@Override
							public void run() {
								QuestUtils.startQuest(player, QuestToTheEnd.INSTANCE);
								QuestUtils.setActiveQuest(player, QuestToTheEnd.INSTANCE);
							}
                			
                		}.runTaskLater(EventRegistery.main, 60L);
                		
                		prof.setMetadata("quest.brown.endportal", true);
                	}
                	
                	
                	
                    player.sendMessage(Locale.a(player, "dimensions.end.disallowed"));
                    
                    player.playSound(player, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2f, 0f);
                    
                    lastMessageTime.put(uuid, now);
                }

                // Apply velocity to shoot player outwards
                // Example: reverse direction (i.e., send them back where they came from) or outward from portal
                Location from = player.getLocation();
                Vector dir = from.getDirection().normalize(); // direction player is facing
                // Option: take horizontal direction and add upward
                Vector launch = new Vector(dir.getX(), 0.5, dir.getZ()).normalize().multiply(2.0);
                player.setVelocity(launch);
            }
        }
    }

}
