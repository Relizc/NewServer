package net.itsrelizc.smp.insurance;

import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.players.locales.Locale;

public class InsuranceCommand extends RelizcCommand {

	public InsuranceCommand() {
		super("insurance", "Opens the insurance menu");
		this.setRelizcOp(false);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
		Menu2 menu = new Menu2(player, 5, new InsuranceMenu(Locale.get(player, "menu.insurance.name")));
		menu.open();
		
		return true;
		
	}
	
}