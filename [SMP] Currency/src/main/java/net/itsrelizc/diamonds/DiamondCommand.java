package net.itsrelizc.diamonds;

import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.players.locales.Locale;


public class DiamondCommand extends RelizcCommand {
	
	public DiamondCommand() {
		super("diamond");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		if (!sender.isOp()) {
			sender.sendMessage(Locale.get(sender, "general.commands.deny"));
			return true;
		}
		
		DiamondCounter.remaining = Long.valueOf(args[0]);
		
		return true;
		
	}
	
	

}
