package net.itsrelizc.commands;

import org.bukkit.entity.Player;

public class CSetTabListName extends RelizcCommand {

	public CSetTabListName() {
		super("settabname");
		// TODO Auto-generated constructor stub
		this.setRelizcOp(true);
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		sender.setPlayerListName(args[0]);
		return true;
		
	}

}
