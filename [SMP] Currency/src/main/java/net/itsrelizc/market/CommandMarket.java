package net.itsrelizc.market;

import org.bukkit.command.CommandSender;

import net.itsrelizc.commands.RelizcCommand;

public class CommandMarket extends RelizcCommand {

	public CommandMarket() {
		super("market", "open market");
		// TODO Auto-generated constructor stub
		this.setRelizcOp(false);
	}
	
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		return true;
	}
}
