package net.itsrelizc.quests.levelling;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.itsrelizc.players.Profile;
import net.itsrelizc.players.Profile.NewPlayerJoinedEvent;

public class LevelListeners implements Listener {
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void join(NewPlayerJoinedEvent event) {
		event.getProfile().setMetadata("level", -1l);
		event.getProfile().setMetadata("exp", 0l);
	}
	
	@EventHandler
	public void onExpChange(PlayerExpChangeEvent event) {
	    Player player = event.getPlayer();
	    int gainedXP = event.getAmount();
	    event.setAmount(0); // Cancel default XP gain
	    

	    addCustomXP(player, gainedXP);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		Profile prof = Profile.findByOwner(event.getPlayer());
		
		long level = (long) prof.getMetadata("level");
		long currentXP = (long) prof.getMetadata("exp");
		
		float fxp = (float) currentXP;
		
		event.getPlayer().setLevel(Math.max((int) level, 0));
		event.getPlayer().setExp(fxp / 1603f);
	}
	
	private void addCustomXP(Player player, int amount) {
		
		
		Profile prof = Profile.findByOwner(player);
		
		long level = (long) prof.getMetadata("level");
		
		if (level < 0) return;

		long currentXP = (long) prof.getMetadata("exp");
	    currentXP += amount;
	    
	    
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
