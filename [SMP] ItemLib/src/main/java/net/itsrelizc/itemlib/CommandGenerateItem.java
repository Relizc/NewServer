package net.itsrelizc.itemlib;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.commands.RelizcCommand.TabCompleteInfo;
import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class CommandGenerateItem extends RelizcCommand {

	public CommandGenerateItem() {
		super("makeitem", "makes an item");
		this.setRelizcOp(true);
		
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		//return new TabCompleteInfo(new TabCompleteType[] {}, (Player) sender, "commands.general.tabcomplete.test");
		return new TabCompleteInfo(new TabCompleteType[] {}, (Player) sender, "commands.general.tabcomplete.test");
		
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		StringUtils.systemMessage(sender, Locale.get(sender, "commands.general.error"), Locale.get(sender, "commands.general.error_noplayer"));
		
		return true;
		
	}

}
