package net.itsrelizc.players.locales;

import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;

public class CommandReloadLocale extends RelizcCommand {
	
	public CommandReloadLocale() {
		super("reloadlocale", "reloads the locales");
		this.setRelizcOp(true);
	}
	
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		Locale.load_all();
		
		sender.sendMessage("Reloaded Locales. ");
		
		return true;
		
	}

}
