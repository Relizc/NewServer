package net.itsrelizc.directrix;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.command.CommandBungee;

public class BungeeValidationCommandWrapper extends CommandBungee {
	
	@Override
    public void execute(CommandSender sender, String[] args)
    {
        sender.sendMessage( ChatColor.YELLOW + "This server runs a modified version of §9BungeeCord§e. §8§o" + ProxyServer.getInstance().getVersion() + " 1.20.1");
    }

}
