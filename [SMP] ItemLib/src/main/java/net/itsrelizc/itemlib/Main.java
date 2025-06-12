package net.itsrelizc.itemlib;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.items.RelizcOverridedWoodenSword;
import net.itsrelizc.items.RelizcTestSword;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		EventRegistery.register(this);
		EventRegistery.register(new OriginalItemOverrider());
		
		ItemUtils.register(RelizcTestSword.class);
		
		//Native mineraft weapons
		ItemUtils.register(RelizcOverridedWoodenSword.class);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		event.getPlayer().sendMessage("bitch");
		
		RelizcTestSword item = ItemUtils.createItem(RelizcTestSword.class, event.getPlayer());
		event.getPlayer().getInventory().addItem(item.getBukkitItem());
		
		TaskDelay.delayTask(new Runnable() {

			@Override
			public void run() {
				item.addCum();
				Bukkit.broadcastMessage("Cum!");
			}
			
		}, 40L);
		
		
		
		
	}
	
}
