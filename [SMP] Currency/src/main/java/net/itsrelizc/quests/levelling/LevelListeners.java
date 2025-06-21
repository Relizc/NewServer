package net.itsrelizc.quests.levelling;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import net.itsrelizc.players.Profile;
import net.itsrelizc.players.Profile.NewPlayerJoinedEvent;

public class LevelListeners implements Listener {
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void join(NewPlayerJoinedEvent event) {
		event.getProfile().setMetadata("level", 0);
		event.getProfile().setMetadata("exp", 0);
	}
	
	@EventHandler
	public void onExpChange(PlayerExpChangeEvent event) {
	    Player player = event.getPlayer();
	    int gainedXP = event.getAmount();
	    event.setAmount(0); // Cancel default XP gain

	    addCustomXP(player, gainedXP);
	}
	
	private void addCustomXP(Player player, int amount) {
		
		
		Profile prof = Profile.findByOwner(player);

		long currentXP = (long) prof.getMetadata("exp");
	    currentXP += amount;
	    
	    long level = (long) prof.getMetadata("level");
	    long level2 = level;

	    while (currentXP >= 1600) {
	        currentXP -= 1600;
	        
	        level ++;
	        
	        //player.sendMessage("§aYou leveled up! New level: " + level);
	    }
	    
	    
	    
	    prof.setMetadata("exp", currentXP);
	    if (level2 != level) {
	    	prof.setMetadata("level", level);;
	    	player.setLevel((int) level);
	    }
	    

	    updateExpBar(player, currentXP);
	}
	
	private void updateExpBar(Player player, long customXP) {
	    player.setExp(customXP / 1600f);
	}
}
