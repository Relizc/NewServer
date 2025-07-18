package net.itsrelizc.smp.modsmp;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.PlayerAFKEvent;
import net.itsrelizc.players.locales.Locale;

public class NiceUtilities implements Listener {
	
	private static final String[] motds = {
			"臭名昭著的贾斯丁已被清算！",
			"臭狗",
			"我们不支持大贝塔",
	};
	
	public static final Random rand = new Random();
	
	public static void startSendingTips() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				int index = (int) (10 * Math.random());
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.sendMessage(Locale.get(player, "general.tips", Locale.get(player, "general.tips" + index)));
				}
			}
			
		}, 0, 20L * 60L * 10L);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void disc(PlayerKickEvent event) {
		Bukkit.getLogger().info(event.getReason());
	}
	
	@EventHandler
	public void stuffz(ServerListPingEvent event) {
		
		int index = (int) (motds.length * Math.random());
		
		event.setMotd("§eRelizc SMP §a第一季 §8(Fabric 1.20.1)\n§b" + motds[index]);
		
		Bukkit.getLogger().info("Reimplemented MOTD");
		
	}
	
	@EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getTo() != null && event.getTo().getWorld().getEnvironment() == Environment.THE_END) {
            event.setCancelled(true);
//            event.getPlayer().sendMessage("§cYou are not allowed to enter The End.");
        }
    }

	
	@EventHandler
	public void chat(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().split(" ")[0].contains(":")) {
			String[] bruh = event.getMessage().split(" ")[0].split(":");
			event.setCancelled(true);
			event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "commands.usenocolon", bruh[1]));
			event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 2f, 2f);
		}
	}
	
	private static Map<String, BukkitTask> prepareKickTasks = new HashMap<String, BukkitTask>();
	
	@EventHandler
	public void afk(PlayerAFKEvent event) {
		
		String k = " §7𝗓ᶻ";
		if (event.isAFK()) {
			Player player = event.getPlayer();
			
			Grouping.setSuffix(player, Grouping.getSuffix(player) + k);
			
			BukkitTask task = new BukkitRunnable() {

				@Override
				public void run() {
					player.kickPlayer(Locale.a(player, "kick.afk"));
				}
				
			}.runTaskLater(EventRegistery.main, 20 * 5 * 60); // 5 mins
			
			prepareKickTasks.put(player.getUniqueId().toString(), task);
		} else {
			
			
			
			Player player = event.getPlayer();
			if (prepareKickTasks.containsKey(player.getUniqueId().toString())) {
				prepareKickTasks.get(player.getUniqueId().toString()).cancel();
				prepareKickTasks.remove(player.getUniqueId().toString());
			};
			int g = Grouping.getSuffix(player).length();
			Grouping.setSuffix(player, Grouping.getSuffix(player).substring(0, g - k.length()));
		}
	}

}
