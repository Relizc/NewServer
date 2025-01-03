package net.itsrelizc.bungee;

import java.util.Map.Entry;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {
	
	@Override
    public void onEnable() {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
        getLogger().info("Yay! It loads!");
        
        getProxy().getPluginManager().registerListener(this, new Main());
    }
	
	@EventHandler
	public void pre(PreLoginEvent event) {
		
	}
	
	@EventHandler
	public void pre(PostLoginEvent event) {
		getLogger().info("modlist:");
		for (Entry<String, String> mod : event.getPlayer().getModList().entrySet()) {
			getLogger().info(mod.getKey() + " " + mod.getValue());
		}
	}

}
