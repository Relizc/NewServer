package net.itsrelizc.players;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.itsrelizc.events.EventRegistery;

public class Grouping implements Listener {
	
	public static Map<Rank, Team> rankedTeam = new HashMap<Rank, Team>();
	public static Scoreboard board;
	
	public static boolean showPrefix = true;
	
	public static class PostPlayerProfileGenerateEvent extends Event {
		
		private static final HandlerList handlers = new HandlerList();
		 
		public static HandlerList getHandlerList() {
		    return handlers;
		}

		public PlayerLoginEvent event;

		public PostPlayerProfileGenerateEvent(PlayerLoginEvent event) {
			this.event = event;
		}

		@Override
		public HandlerList getHandlers() {
			// TODO Auto-generated method stub
			return handlers;
		}
		
	}
	
	public static void initlizeRankGroups(Plugin plugin) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		
		for (Rank r : Rank.values()) {
			
			String name;
			
			if (r.toString().length() > 12) {
				name = r.toString().substring(0, 12);
			} else {
				name = r.toString();
			}
			
			String n = r.permission + name;
			
			Team team = board.registerNewTeam(n.substring(0, Math.min(n.length(), 15)));

			rankedTeam.put(r, team);
		}
		
		Bukkit.getPluginManager().registerEvents(new Grouping(), plugin);
		
	}
	
	@EventHandler
	public void _a(PlayerJoinEvent event) {
		event.getPlayer().setScoreboard(board);
		
		
	}
	
	public static Map<Player, Team> playersoleTeams = new HashMap<Player, Team>();
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void _aO(PlayerLoginEvent event) {
		
		
		Profile p = Profile.createProfile(event.getPlayer());
		
		Rank r = Rank.findByPermission(p.permission);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				if (showPrefix) {
					event.getPlayer().setPlayerListName(r.displayName + " " + event.getPlayer().getName());
				} else {
					event.getPlayer().setPlayerListName(r.displayName.substring(0, 2) + event.getPlayer().getName());
				}
			}
			
		}, 10L);
		
		String n = event.getPlayer().getUniqueId().toString();
		Team t = board.registerNewTeam(n.substring(0, Math.min(n.length(), 15)));
		
	
		
		ChatColor c = ChatColor.getByChar(r.displayName.substring(1, 2));
		t.setPrefix(r.rankColor());
		
		t.setColor(c);
		playersoleTeams.put(event.getPlayer(), t);
		t.addEntry(event.getPlayer().getName());
		
		event.getPlayer().setOp(r.useop);
		
		Bukkit.getPluginManager().callEvent(new PostPlayerProfileGenerateEvent(event));
	}
	
	public static void setSuffix(Player player, String str) {
		playersoleTeams.get(player).setSuffix(str);
	}
	
	public static String getSuffix(Player player) {
		return playersoleTeams.get(player).getSuffix();
	}
	
	@EventHandler
	public void _b(PlayerQuitEvent event) {
		Team t = playersoleTeams.remove(event.getPlayer());
		t.unregister();
		Profile.removeProfile(Profile.findByOwner(event.getPlayer()));
	}
	
	@EventHandler
	public void _b(AsyncPlayerChatEvent event) {
		Profile p = Profile.findByOwner(event.getPlayer());
		event.setFormat(Rank.findByPermission(p.permission).displayName + " " + event.getPlayer().getDisplayName() + "§7: §r" + event.getMessage());
	}
	
	public static Collection<Team> getTeams() {
		return rankedTeam.values();
	}
	
}
