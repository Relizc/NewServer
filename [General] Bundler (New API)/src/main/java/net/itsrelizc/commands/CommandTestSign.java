package net.itsrelizc.commands;

import org.bukkit.entity.Player;

import net.itsrelizc.players.SignInput;
import net.itsrelizc.players.locales.Locale;

public class CommandTestSign extends RelizcCommand {

	public CommandTestSign() {
		super("testsign");
		// TODO Auto-generated constructor stub
		this.setRelizcOp(true);
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		SignInput input = new SignInput(sender, TabCompleteType.TEXT, "Noob") {
			
		};
		
		input.open();
		
		return true;
	}

}
