package net.itsrelizc.secretchat;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.events.EventRegistery;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		EventRegistery.register(new PlayerJoin());
		
	}
	
	@Override
	public void onDisable() {
		
		for (World world : Bukkit.getWorlds()) {
			for (CraftArmorStand entity : world.getEntitiesByClass(CraftArmorStand.class)) {
				if (entity.isInvisible()) entity.remove();
			}
		}
		
	}
	
}
