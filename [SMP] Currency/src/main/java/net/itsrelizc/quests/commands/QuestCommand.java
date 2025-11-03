package net.itsrelizc.quests.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.messaging.MessageMenu;
import net.itsrelizc.quests.Quest.QuestObjective;
import net.itsrelizc.quests.QuestUtils;
import net.itsrelizc.string.StringUtils;

public class QuestCommand extends RelizcCommand {
	
	private byte selection = 0; // 0 - general
	
	public QuestCommand() {
		super("quest", "quest options");
		this.setRelizcOp(true);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] ntargs) {
		
		Player target = Bukkit.getPlayer(ntargs[0]);
		String mode = ntargs[1];
		String query = ntargs[2];
		
		if (mode.equalsIgnoreCase("startquest")) {
			QuestUtils.startQuest(player, QuestUtils.getHandler().get(query));
		} else if (mode.equalsIgnoreCase("setobjective")) {
			QuestObjective obj2 = null;
			for (QuestObjective obj : QuestUtils.getActiveObjectives(player)) {
				if (obj.getID().equalsIgnoreCase(query)) {
					obj2 = obj;
					break;
				}
			}
			
			QuestUtils.setActiveQuestObjective(player, obj2);
		} 
		
		return true;
	}
	
	

	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] ntargs, Location location) {
		
		Player player = (Player) sender;
		
		if (ntargs.length == 1) {
			return TabCompleteInfo.presetPlayer(player, "select player");
		} else if (ntargs.length == 2) {
			return TabCompleteInfo.presetOption(player, "select mode", StringUtils.fromArgs("startquest", "setobjective", "setobjectivemeta"));
		} else if (ntargs.length == 3) {
			String option = ntargs[1];
			
			if (option.equalsIgnoreCase("startquest")) {
				return TabCompleteInfo.presetOption(player, "questNames", new ArrayList<String>(QuestUtils.getHandler().keySet()));
			} else if (option.equalsIgnoreCase("setobjective")) {
				List<QuestObjective> obj = QuestUtils.getActiveObjectives(player);
				List<String> ids = obj.stream()
					    .map(QuestObjective::getID)
					    .collect(Collectors.toList());
				return TabCompleteInfo.presetOption(player, "questNames", ids);
			} 
			return TabCompleteInfo.presetNothing((Player) sender);
			
		}
		
		return TabCompleteInfo.presetNothing((Player) sender);
		
	}
	
	
	
}
