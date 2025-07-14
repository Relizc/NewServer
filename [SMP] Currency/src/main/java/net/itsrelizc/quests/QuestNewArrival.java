package net.itsrelizc.quests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.locales.Locale;

public class QuestNewArrival extends Quest {
	
	public static class Listeners implements Listener {
		
		@EventHandler(ignoreCancelled=true)
		public void craft(CraftItemEvent event) {
			
			if (!(QuestUtils.getActiveQuest((Player) event.getView().getPlayer()) instanceof QuestNewArrival)) return;
			QuestObjective obj = QuestUtils.getActiveObjectives((Player) event.getView().getPlayer()).get(0);
			if (!(obj.getID().equals("CRAFT_TABLE"))) return;
			
			Player player = (Player) event.getView().getPlayer();
			
			if (event.getRecipe().getResult().getType() == Material.CRAFTING_TABLE) {
				//QuestUtils.setActiveQuestObjectiveMetadata(player, obj, true);
				obj.complete(player);
			}
			
		}
		
	}
	
	public static final QuestNewArrival INSTANCE = new QuestNewArrival();
	
	private static class QuestCraftTable extends QuestObjective {

		public QuestCraftTable(Quest parent) {
			super("CRAFT_TABLE", false, true, parent);
			
		}
		
		@Override
		public String toString(Player player) {
			
			boolean claimed = (boolean) INSTANCE.getObjectiveValue(player, getID());
			
			String mark;
			if (claimed) {
				mark = "§a" + Locale.a(player, "quest.complete");
;			} else {
				mark = "§c" + Locale.a(player, "quest.incomplete");
			}
			
			return Locale.a(player, "quest.NEW_ARRIVAL.objective.craft_table").formatted(mark);
			
		}
		
		@Override
		public String getDescription(Player player) {
			return Locale.a(player, "quest.NEW_ARRIVAL.objective.craft_table_neutral");
		}
		
		@Override
		public void complete(Player player) {
			super.complete(player);
			
			QuestUtils.setActiveQuestObjectiveMetadata(player, this, true);
			QuestUtils.setActiveQuestObjective(player, parent.OBJECTIVES[1], false);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					QuestUtils.questStatusChanged(player);
					parent.OBJECTIVES[1].start(player);
				}
				
			}.runTaskLater(EventRegistery.main, 40L);
		}
		
	}
	
	private static class QuestClaimLand extends QuestObjective {

		public QuestClaimLand(Quest parent) {
			super("CLAIM_LAND", false, false, parent);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String toString(Player player) {
			
			boolean claimed = (boolean) INSTANCE.getObjectiveValue(player, getID());
			
			String mark;
			if (claimed) {
				mark = "§a" + Locale.a(player, "quest.complete");
;			} else {
				mark = "§c" + Locale.a(player, "quest.incomplete");
			}
			
			return Locale.a(player, "quest.NEW_ARRIVAL.objective.claim_land").formatted(mark);
			
		}
		
		@Override
		public String getDescription(Player player) {
			return Locale.a(player, "quest.NEW_ARRIVAL.objective.claim_land_neutral");
		}
		
		@Override
		public void complete(Player player) {
			super.complete(player);
			QuestUtils.setActiveQuestObjectiveMetadata(player, this, true);
		}
		
		@Override
		public void start(Player player) {
			super.start(player);
			
			
			
		}
		
	}

	
	public QuestNewArrival() {
		this.ID = "NEW_ARRIVAL";
		this.DESCRIPTION = "quest.NEW_ARRIVAL.description";
		this.DISPLAY_NAME = "quest.NEW_ARRIVAL.name";
		this.OBJECTIVES = new QuestObjective[]{
				new QuestCraftTable(this),
				new QuestClaimLand(this)
		};
		this.REWARDS = new QuestReward[] {
				new QuestRewardItem(new ItemStack(Material.OAK_PLANKS, 4)),
				new QuestRewardItem(new ItemStack(Material.CHEST, 1)),
				new QuestRewardExp(800)
		};
	}

}
