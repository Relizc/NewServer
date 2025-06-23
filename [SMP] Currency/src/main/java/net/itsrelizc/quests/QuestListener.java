package net.itsrelizc.quests;

import java.io.File;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.Profile.NewPlayerJoinedEvent;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.quests.Quest.QuestObjective;
import net.itsrelizc.quests.Quest.QuestReward;
import net.itsrelizc.quests.QuestUtils.PlayerNewQuestStarted;
import net.md_5.bungee.api.chat.TextComponent;

public class QuestListener implements Listener {
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void join(NewPlayerJoinedEvent event) {
		event.getProfile().setMetadata("activeQuest", null);
		
	}
	
	@EventHandler
	public void start(PlayerNewQuestStarted event) {
		

			
		Song song = NBSDecoder.parse(new File(JSON.PREFIX + "sounds/quest_started.nbs")); // Preloaded song
		// Create RadioSongPlayer.
		RadioSongPlayer rsp = new RadioSongPlayer(song);
		// Add player to SongPlayer so he will hear the song.
		rsp.addPlayer(event.getPlayer());
		// Start RadioSongPlayer playback
		rsp.setPlaying(true);
		
		String message = "\n \n \n" +
				"§e§m--------------------------------§r\n" +
				" §6§l" + Locale.a(event.getPlayer(), "quest.new") + "§r §8- §f" + Locale.a(event.getPlayer(), event.getQuest().DISPLAY_NAME) + "\n" +
				"  §7§o" + Locale.a(event.getPlayer(), event.getQuest().DESCRIPTION) + "\n ";
		
		message += "\n §6§l" + Locale.a(event.getPlayer(), "quest.objectives") + "\n ";
		
		for (QuestObjective objectives : event.getQuest().OBJECTIVES) {
			message += " §e• " + objectives.getDescription(event.getPlayer()) + "\n ";
		}
		
		message += "\n";
		
		message += " §6§l" + Locale.a(event.getPlayer(), "quest.rewards") + "\n";
		
		event.getPlayer().sendMessage(message);
		
		if (event.getQuest().REWARDS == null || event.getQuest().REWARDS.length == 0) {
			event.getPlayer().sendMessage(" §7§o" + Locale.a(event.getPlayer(), "quest.rewards.none"));
		} else {
			for (QuestReward objectives : event.getQuest().REWARDS) {
				TextComponent a = new TextComponent("§e");
				a.addExtra(objectives.toString(event.getPlayer()));
				event.getPlayer().spigot().sendMessage(a);
			}
		}
		
		event.getPlayer().sendMessage("§e§m--------------------------------");

		
		
	}

}
