package net.itsrelizc.smp.modsmp.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.entity.EntityDismountEvent;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.commands.RelizcCommand.TabCompleteInfo;
import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.players.CustomPlayerTeleportEvent;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class SitCommand extends RelizcCommand implements Listener {
	
	public static Map<Player, ArmorStand> map = new HashMap<Player, ArmorStand>();
	public static Map<Player, Location> map2 = new HashMap<Player, Location>();
	public static Map<Player, LivingEntity> map3 = new HashMap<Player, LivingEntity>();

	public SitCommand() {
		super("sit", "lets u to sit down!");
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
		if (map.keySet().contains(player)) {
			player.sendMessage("§c你已经坐下了！");
			return true;
		}
		
		if (!player.isOnGround()) {
			player.sendMessage("§c你必须站稳才能坐下！");
			return true;
		}
		
		if (args.length >= 1) {
			
			String name = args[0];
			
			Player p = Bukkit.getPlayer(name);
			
			if (player.getName().equalsIgnoreCase(name)) {
				player.sendMessage("§c你不能做你自己身上！");
				return true;
			}
			
			if (p == null) {
				player.sendMessage("§c" + name + "不在线！");
				return true;
			}
			
			if (p.getLocation().distance(player.getLocation()) > 5) {
				player.sendMessage("§c" + name + " 必须在你 5 格之内！");
				return true;
			}
			
			LivingEntity ent = (LivingEntity) player.getWorld().spawnEntity(player.getLocation().add(0, -1.65, 0), EntityType.SILVERFISH);
			ent.setInvisible(true);
			ent.setAI(false);
			ent.setInvulnerable(true);
			ent.setGravity(false);
			
			ent.setPassenger(player);
			
			
			p.setPassenger(ent);
			
			map3.put(player, ent);
			
		} else {
			ArmorStand ent = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, -1.65, 0), EntityType.ARMOR_STAND);
			ent.setVisible(false);
			ent.setInvulnerable(true);
			ent.setGravity(false);
			
			
			
			map.put(player, ent);
			map2.put(player, player.getLocation());
			
			ent.setPassenger(player);
		}
		
		
		
		return true;
		
	}
	
	public static void enable(Plugin plugin) {
		
		Bukkit.getPluginManager().registerEvents(new SitCommand(), plugin);
		
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		if (args.length == 1) {
			return TabCompleteInfo.presetPlayer((Player) sender, Locale.get((Player) sender, "commands.sit.arg0.description"));
		} else {
			return TabCompleteInfo.presetNothing((Player) sender);
		}
		
		
	}
	
	@EventHandler
	public void exit(EntityDismountEvent event) {
		
		if (event.getDismounted() instanceof ArmorStand) {
			
			if (event.getEntity() instanceof Player) {
				
				Player player = (Player) event.getEntity();
				
				if (map.keySet().contains(player)) {
					ArmorStand entity = (ArmorStand) event.getDismounted();
					entity.remove();
					
					map.remove(player);
					Location loc = map2.get(player);
					//player.teleport(loc);
					CustomPlayerTeleportEvent.teleport(player, loc);
					map2.remove(player);
				}
				
			}
			
		} else if (event.getDismounted() instanceof Silverfish) {
			
			if (event.getEntity() instanceof Player) {
				
				Player player = (Player) event.getEntity();
				
				if (map3.keySet().contains(player)) {
					Silverfish entity = (Silverfish) event.getDismounted();
					entity.remove();
					
					map3.remove(player);
				}
				
			}
			
		}
		
	}

}
