package net.itsrelizc.itemlib;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.items.RelizcOverridedWoodenSword;
import net.itsrelizc.items.RelizcOverridenEnchantedBook;
import net.itsrelizc.items.RelizcTestSword;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		EventRegistery.register(this);
		EventRegistery.register(new OriginalItemOverrider());
		EventRegistery.register(new EnchantItemListener());
		
		ItemUtils.register(RelizcTestSword.class);
		
		//Native mineraft weapons
		ItemUtils.register(RelizcOverridedWoodenSword.class);
		ItemUtils.register(RelizcOverridenEnchantedBook.class);
		
		CommandRegistery.register(new CommandGenerateItem());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
//		event.getPlayer().sendMessage("bitch");
//		
//		RelizcTestSword item = ItemUtils.createItem(RelizcTestSword.class, event.getPlayer());
//		event.getPlayer().getInventory().addItem(item.getBukkitItem());
//		
//		TaskDelay.delayTask(new Runnable() {
//
//			@Override
//			public void run() {
//				item.addCum();
//				////("Cum!");
//			}
//			
//		}, 40L);
		
		
		
		
	}
	
}
