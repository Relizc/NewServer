package net.itsrelizc.npc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPC.Metadata;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.string.StringUtils;

public class RelizcNPC {
	
	public final NPC npc;
	public boolean triggerTerminateMove;
	public NPCActions actionHandler;
	
	public RelizcNPC() {
		this.npc = CitizensAPI.getNPCRegistry().createNPC(getType(), StringUtils.randomString(12));
		this.npc.data().set(Metadata.NAMEPLATE_VISIBLE, false);
		this.triggerTerminateMove = false;
    	
	}
	
	public void spawn(Location loc) {
		npc.spawn(loc);
		addToTeam("npcs");
		
	}
	
	
	
	protected EntityType getType() {
		return EntityType.PLAYER;
	}
	
	private void addToTeam(String teamName) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(teamName);
        if (team == null) {
            team = board.registerNewTeam(teamName);
            team.setColor(ChatColor.DARK_GRAY);
            team.setPrefix("NPC ");
        }

        // Make sure to add the correct name — must match entity's scoreboard name
        if (npc.isSpawned()) {
            String entry = npc.getEntity().getName();
            if (!team.hasEntry(entry)) {
                team.addEntry(entry);
            }
        }
    }
	
	public void sayDialogue(String message) {
		
		String[] splitted = message.split("\\\\");
		
		
		
		new BukkitRunnable() {
			
			int line = 0;

			@Override
			public void run() {
				String msg = splitted[line];
				
				if (npc.getEntity() == null) {
					cancel();
					return;
				}
				
				PlayerChatEvent chatEvent = new PlayerChatEvent((Player) npc.getEntity(), msg);
				Bukkit.getPluginManager().callEvent(chatEvent);
				
				line ++;
				if (line >= splitted.length) {
					cancel();
					return;
				}
			}
			
		}.runTaskTimer(EventRegistery.main, 0, 30l);
	}
	
	public void despawn() {
		this.npc.despawn();
	}

	public void event(String string) {
		// TODO Auto-generated method stub
		
	}
}
