package net.itsrelizc.smp.homeplugin.commands;

import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.menus.ClassicMenu;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.homeplugin.menus.HomeControlMenu;
import net.itsrelizc.string.StringUtils;

public class HomeCommand extends RelizcCommand {

	public HomeCommand() {
		super("home", "Bring back home", StringUtils.fromArgs("house", "h", "家"));
		
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
		//ChatUtils.systemMessage(player, "§c§lERROR", "This command cannot be run by Console. TESTHOMECOMMAND");
		
		player.sendMessage(Locale.get(player, "commands.home.notavaliable"));
		
//		ClassicMenu menu = new ClassicMenu(player, 6, "家园控制台", new HomeControlMenu());
//		menu.show();
		
		return true;
		
	}

}
