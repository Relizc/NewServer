package net.itsrelizc.gunmod;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.gunmod.deathutils.DeathUtils;
import net.itsrelizc.health2.Body;
import net.itsrelizc.health2.ballistics.Bullet;
import net.itsrelizc.health2.ballistics.FragUtils;

public class CommandDamage extends RelizcCommand {

	public CommandDamage() {
		super("damage", "damages a player");
		this.setRelizcOp(true);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		does(args, player);
		return true;
		
	}
	
	@Override
	public boolean onConsoleExecute(ConsoleCommandSender sender, String[] args) {
		does(args, null);
		return true;
		
	}
	
	private boolean does(String[] args, Player p) {
		
		if (args[0].equalsIgnoreCase("kill")) {
			
			if (p == null) return true;
			
			DeathUtils.addPlayer(p);
			
			return true;
			
		} else if (args[0].equalsIgnoreCase("unkill")) {
			
			if (p == null) return true;
			
			Body.parts.get(p.getUniqueId().toString()).reset();
			DeathUtils.removePlayer(p);
			
			return true;
		} else if (args[0].equalsIgnoreCase("rays")) {
			
			int a = Integer.valueOf(args[1]);
			//double b = Double.valueOf(args[2]);
			//int c = Integer.valueOf(args[3]);
			
			Bullet.rays = a;
			//Main.speed = b;
			//Main.maxSteps = c;
			return true;
		} else if (args[0].equalsIgnoreCase("speed")) {
			//int a = Integer.valueOf(args[1]);
			double b = Double.valueOf(args[1]);
			//int c = Integer.valueOf(args[3]);
			
			//Main.rays = a;
			Bullet.speed = b;
			return true;
			//Main.maxSteps = c;
		} else if (args[0].equalsIgnoreCase("steps")) {
			//int a = Integer.valueOf(args[1]);
			//double b = Double.valueOf(args[2]);
			int c = Integer.valueOf(args[1]);
			
			//Main.rays = a;
			//Main.speed = b;
			Bullet.maxSteps = c;
			return true;
		} else if (args[0].equalsIgnoreCase("gravity")) {
			//int a = Integer.valueOf(args[1]);
			//double b = Double.valueOf(args[2]);
			double c = Double.valueOf(args[1]);
			
			//Main.rays = a;
			//Main.speed = b;
			Bullet.gravity = c;
			return true;
		} else if (args[0].equalsIgnoreCase("distance")) {
			double dist = Double.valueOf(args[1]);
			Bullet.maxDistance = dist;
			return true;
		} else if (args[0].equalsIgnoreCase("shoot")) {
			
			FragUtils.spawnSingleFragment(p.getEyeLocation(), p.getEyeLocation().getDirection(), Double.valueOf(args[1]));
			return true;
		}
		
		Player player = Bukkit.getPlayer(args[0]);
		Body body = Body.parts.get(player.getUniqueId().toString());
		
		body.damage(Integer.valueOf(args[1]), Long.valueOf(args[2]), null);
		return true;
		
	}

}
