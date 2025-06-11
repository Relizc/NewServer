package net.itsrelizc.directrix;

import java.util.Map.Entry;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	@Override
    public void onEnable() {

        // BungeeCord already does so
        getLogger().info("Yay! It loads!");
        
        for (Entry<String, Command> ent : ProxyServer.getInstance().getPluginManager().getCommands()) {
        	if (ent.getKey().equals("bungee")) {
        		ProxyServer.getInstance().getPluginManager().unregisterCommand(ent.getValue());
        		ProxyServer.getInstance().getPluginManager().registerCommand(null, new BungeeValidationCommandWrapper());
        		break;
        	}
        }
	}
}
