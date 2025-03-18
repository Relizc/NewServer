package net.itsrelizc.diamonds.commands;

import java.util.List;

import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.diamonds.DiamondJar;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class GiveDiamondPotion extends RelizcCommand {

	public GiveDiamondPotion() {
		super("diamondpotion");
		// TODO Auto-generated constructor stub
		this.setRelizcOp(true);
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		long value = Long.valueOf(args[0]);
		DiamondJar.createFor(sender, value);
		
		return true;
		
	}

}
