package net.itsrelizc.gunmod;

import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.string.ChatUtils;

public class GDebugCommand extends RelizcCommand {

	public GDebugCommand() {
		super("setballistic");
		// TODO Auto-generated constructor stub
		this.setRelizcOp(true);
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		String mode = args[0];
		double value = Double.valueOf(args[1]);
		
		if (mode.equalsIgnoreCase("g")) {
			Hit.g = value;
			ChatUtils.systemMessage(sender, "Ballistics", "Set Hit.g to " + value);
		} else if (mode.equalsIgnoreCase("bs")) {
			Hit.bulletSpeed = value;
			ChatUtils.systemMessage(sender, "Ballistics", "Set Hit.bulletSpeed to " + value);
		}
		return true;
		
	}

}
