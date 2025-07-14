package net.itsrelizc.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.itsrelizc.bundler.Main;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.Rank;
import net.itsrelizc.players.locales.Locale;

import java.util.ArrayList;
import java.util.List;

public class RelizcScoreboard {

    private final Scoreboard bukkitScoreboard;
    private final Objective objective;
    protected final Player player;
    private final List<String> lines;

    public RelizcScoreboard(Player player) {
        this.player = player;
        this.bukkitScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = bukkitScoreboard.registerNewObjective("custom", Criteria.DUMMY, "SomeGame");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.lines = new ArrayList<>();

        
        
        this.addGameInfo();
        
        
        // Overriding orignal group in class Grouping
        
        /* *
         * 
         * Players must be in the sameboard to show their nametags.
         * 
         * */
        
//        String orgsuffix = Grouping.getSuffix(player);
//        Grouping.playersoleTeams.get(player).removeEntry(player.getName());
//        Grouping.playersoleTeams.get(player).unregister();
//        
//        String n = player.getUniqueId().toString();
//		Team t = bukkitScoreboard.registerNewTeam(n.substring(0, Math.min(n.length(), 15)));
//		
//		Profile p = Profile.findByOwner(player);
//		
//		
//		Rank r = Rank.findByPermission(p.permission);
//		t.setPrefix(r.rankColor());
//		ChatColor c = ChatColor.getByChar(r.displayName.substring(1, 2));
//		
//		t.setColor(c);
//		Grouping.playersoleTeams.put(player, t);
//		t.addEntry(player.getName());
//		
//		Grouping.setSuffix(player, orgsuffix);
//		Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {
//
//			@Override
//			public void run() {
//				//("bruh " + t.getPlayers().size() + " " + player.getName() + " " + t.getPrefix() + "");
//				
//				Grouping.setSuffix(player, ChatColor.AQUA + "noob");
//				t.setSuffix(ChatColor.AQUA + "noob");
//				
//				for (Player player : Bukkit.getOnlinePlayers()) {
//					player.setScoreboard(bukkitScoreboard);
//				}
//			}
//			
//		}, 0, 20L);
		
		player.setScoreboard(bukkitScoreboard);
    }
    
    public void setDisplayName(String name) {
    	this.objective.setDisplayName(name);
    }
    
    public void addGameInfo() {
    	addLine("");
    	setDisplayName("§e§l" + Locale.get(player, GameInfo.gameName));
    }

    public void addLine(String line) {
    	while (lines.contains(line)) {
    		line += " ";
    	}
        lines.add(line);
        
        updateScores();
    }
    
    public List<String> getLines() {
    	return this.lines;
    }
    
    public void addLine(int index, String line) {
    	while (lines.contains(line)) {
    		line += " ";
    	}
    	lines.add(index, line);
    	
    	updateScores();
    }

    public void editLine(int index, String newLine) {
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid line index.");
        }
        
        if (lines.get(index).equals(newLine)) return;

        int score = lines.size() - index;

        // Remove the old line from scoreboard
        String oldLine = lines.get(index);
        bukkitScoreboard.resetScores(oldLine);

        // Update internal list
        lines.set(index, newLine);

        // Set the new line at the correct score
        objective.getScore(newLine).setScore(score);
    }
    
    protected void addFootnote() {
		addLine(" ");
		addLine("§7" + Main.getVersion());
	}

    public void removeLine(int index) {
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid line index.");
        }

        // Remove the line from scoreboard
        String oldLine = lines.remove(index);
        bukkitScoreboard.resetScores(oldLine);

        // Re-score only the lines below the removed index
        for (int i = index; i < lines.size(); i++) {
            String line = lines.get(i);
            int score = lines.size() - i;
            bukkitScoreboard.resetScores(line); // Remove old score before re-setting
            objective.getScore(line).setScore(score);
        }
    }
    
    public void updateScores() {
        // Reset all scores to prevent duplicates
        for (String line : bukkitScoreboard.getEntries()) {
            bukkitScoreboard.resetScores(line);
        }

        // Assign scores in reverse so first line has highest score
        int score = lines.size();
        for (String line : lines) {
            objective.getScore(line).setScore(score--);
        }
    }
}
