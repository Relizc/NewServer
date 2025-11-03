package net.itsrelizc.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import net.itsrelizc.dimensions.end.QuestToTheEnd;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.messaging.Messaging;
import net.itsrelizc.players.Profile;
import net.itsrelizc.quests.Quest.QuestObjective;
import net.itsrelizc.quests.Quest.QuestReward;
import net.itsrelizc.quests.Quest.QuestRewardItem;

public class QuestUtils {
	
	private static Map<String, Quest> HANDLERS = new HashMap<String, Quest>();
	
	public static Map<String, Quest> getHandler() {return HANDLERS;}
	
	public static class PlayerNewQuestStarted extends Event {
	    private static final HandlerList HANDLERS = new HandlerList();
	    private Player player;
		private Quest quest;


	    public PlayerNewQuestStarted(Player player, Quest instance) {
	        this.player = player;
	        this.quest = instance;
	    }

	    public Player getPlayer() {
	        return player;
	    }
	    
	    public Quest getQuest() {
	    	return this.quest;
	    }

	    @Override
	    public HandlerList getHandlers() {
	        return HANDLERS;
	    }

	    public static HandlerList getHandlerList() {
	        return HANDLERS;
	    }
	}
	
	public static class PlayerQuestStatusChangedEvent extends Event {
	    private static final HandlerList HANDLERS = new HandlerList();
	    private Player player;


	    public PlayerQuestStatusChangedEvent(Player player) {
	        this.player = player;
	    }

	    public Player getPlayer() {
	        return player;
	    }

	    @Override
	    public HandlerList getHandlers() {
	        return HANDLERS;
	    }

	    public static HandlerList getHandlerList() {
	        return HANDLERS;
	    }
	}
	
	public static class PlayerQuestCompletedEvent extends Event {
	    private static final HandlerList HANDLERS = new HandlerList();
	    private Player player;
		private Quest quest;


	    public PlayerQuestCompletedEvent(Player player, Quest instance) {
	        this.player = player;
	        this.quest = instance;
	    }

	    public Player getPlayer() {
	        return player;
	    }
	    
	    public Quest getQuest() {
	    	return this.quest;
	    }

	    @Override
	    public HandlerList getHandlers() {
	        return HANDLERS;
	    }

	    public static HandlerList getHandlerList() {
	        return HANDLERS;
	    }
	}
	
	public static void registerQuest(Quest quest) {
		HANDLERS.put(quest.ID, quest);
	}
	
	public static void startQuest(Player player, Quest instance) {

		Profile profile = Profile.findByOwner(player);
		
//			profile.setMetadata("ques", profile);
		JSONObject objectives = new JSONObject();
		QuestObjective[] objectivesList = instance.OBJECTIVES;
		
		for (QuestObjective obj : objectivesList) {
			objectives.put(obj.getID(), obj.getValue());
			objectives.put(obj.getID() + ".active", obj.isDefaultActive()); 
		}
		
		objectives.put("_COMPLETED", false);
		
		profile.setMetadata("quest." + instance.ID, objectives);
		
		PlayerNewQuestStarted event = new PlayerNewQuestStarted(player, instance);
		Bukkit.getPluginManager().callEvent(event);
		
		PlayerQuestStatusChangedEvent event2 = new PlayerQuestStatusChangedEvent(player);
		Bukkit.getPluginManager().callEvent(event2);

	}
	
	public static Quest getActiveQuest(Player player) {
		Profile profile = Profile.findByOwner(player);
		
		String active = (String) profile.getMetadata("activeQuest");
		if (active == null) return null;
		
		return HANDLERS.get(active);
	}
	
	public static List<QuestObjective> getActiveObjectives(Player player) {
		
		List<QuestObjective> arr = new ArrayList<QuestObjective>();
		
		Quest active = getActiveQuest(player);
		Profile profile = Profile.findByOwner(player);
		
		JSONObject obj = (JSONObject) profile.getMetadata("quest." + active.ID);
		
		for (QuestObjective nex : active.OBJECTIVES) {
			if ((boolean) obj.get(nex.getID() + ".active")) {
				arr.add(nex);
			}
		}
		
		return arr;
		
	}

	public static void setActiveQuest(Player player, Quest instance) {

		Profile profile = Profile.findByOwner(player);
		
		if (instance == null) {
			profile.setMetadata("activeQuest", null);
		} else {
			profile.setMetadata("activeQuest", instance.ID);
		}
		
//			profile.setMetadata("ques", profile);
		
		
		
		PlayerQuestStatusChangedEvent event2 = new PlayerQuestStatusChangedEvent(player);
		Bukkit.getPluginManager().callEvent(event2);

	}

	public static void setActiveQuestObjectiveMetadata(Player player, QuestObjective obj, Object result) {
		Profile profile = Profile.findByOwner(player);
		Quest active = getActiveQuest(player);
		
		JSONObject completed = (JSONObject) profile.getMetadata("quest." + active.ID);
		completed.put(obj.getID(), result);
		profile.setMetadata("quest." + active.ID, completed);
		
		PlayerQuestStatusChangedEvent event2 = new PlayerQuestStatusChangedEvent(player);
		Bukkit.getPluginManager().callEvent(event2);
	}

	public static void setActiveQuestObjective(Player player, QuestObjective objective) {
		setActiveQuestObjective(player, objective, true);
	}

	public static void setActiveQuestObjective(Player player, QuestObjective objective, boolean callEvent) {
		Profile profile = Profile.findByOwner(player);
		Quest active = getActiveQuest(player);
		JSONObject completed = (JSONObject) profile.getMetadata("quest." + active.ID);
		
		for (QuestObjective obj : getActiveObjectives(player)) {
			completed.put(obj.getID() + ".active", false);
		}
		
		
		completed.put(objective.getID() + ".active", true);
		profile.setMetadata("quest." + active.ID, completed);
		
		if (callEvent) {
			PlayerQuestStatusChangedEvent event2 = new PlayerQuestStatusChangedEvent(player);
			Bukkit.getPluginManager().callEvent(event2);
		}
	}

	public static void questStatusChanged(Player player) {
		PlayerQuestStatusChangedEvent event2 = new PlayerQuestStatusChangedEvent(player);
		Bukkit.getPluginManager().callEvent(event2);
	}

	public static void completeQuest(Player player, Quest instance) {
		
		Profile profile = Profile.findByOwner(player);
		JSONObject completed = (JSONObject) profile.getMetadata("quest." + instance.ID);
		completed.put("_COMPLETED", true);
		profile.setMetadata("quest." + instance.ID, completed);
		
		setActiveQuest(player, null);
		instance.complete(player);
		PlayerQuestCompletedEvent event = new PlayerQuestCompletedEvent(player, instance);
		Bukkit.getPluginManager().callEvent(event);
		
		ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
		
		for (QuestReward reward : instance.REWARDS) {
			if (reward instanceof QuestRewardItem) {
				QuestRewardItem r = (QuestRewardItem) reward;
				
				rewards.add((ItemStack) r.getValue());
			}
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Messaging.Message msg = new Messaging.Message(
					    Messaging.generateId(),
					    "general",
					    "§§quest.deliver",
					    "§§quest.completed.rewards.title",
					    "§§quest.completed.rewards.description", System.currentTimeMillis(),
					    rewards, false, false
					);
				
				Messaging.addMessage(player, msg);
			}
			
		}.runTaskLater(EventRegistery.main, new Random().nextLong(20 * 5) + 20 * 5);
	}

	public static boolean isQuestCompleted(Player player, QuestToTheEnd instance) {
		Profile profile = Profile.findByOwner(player);
		
		if (profile == null) return false;
		
		JSONObject completed = (JSONObject) profile.getMetadata("quest." + instance.ID);
		
		if (completed == null) return false;
		
		return (boolean) completed.getOrDefault("_COMPLETED", false);
	}

}
