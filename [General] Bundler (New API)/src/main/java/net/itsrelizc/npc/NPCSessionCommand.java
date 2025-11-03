package net.itsrelizc.npc;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.commands.RelizcCommand.TabCompleteInfo;
import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class NPCSessionCommand extends RelizcCommand {

	public NPCSessionCommand() {
		super("npcsession", "npc session");
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		return TabCompleteInfo.presetNothing((Player) sender);
		
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		if (args.length < 2) return true;
		
		String uid = args[0];
		String params = args[1];
		
		NPCDialogueSession ses = NPCDialogueSession.sess.getOrDefault(uid, null);
		
		if (ses == null) return true;
		if (ses.getEndTime() < System.currentTimeMillis()) return true;
		
		ses.recieveResponse(sender, params);
		
		return true;
		
		
		
	}
	

}
