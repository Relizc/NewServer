package net.itsrelizc.dimensions.end;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.quests.Quest;
import net.itsrelizc.quests.QuestUtils;

public class QuestToTheEnd extends Quest {
	
	public static class Listeners implements Listener {
		
		@EventHandler(ignoreCancelled=true)
		public void craft(CraftItemEvent event) {
			
			if (!(QuestUtils.getActiveQuest((Player) event.getView().getPlayer()) instanceof QuestToTheEnd)) return;
			QuestObjective obj = QuestUtils.getActiveObjectives((Player) event.getView().getPlayer()).get(0);
			if (!(obj.getID().equals("CRAFT_TABLE"))) return;
			
			Player player = (Player) event.getView().getPlayer();
			
			if (event.getRecipe().getResult().getType() == Material.CRAFTING_TABLE) {
				//QuestUtils.setActiveQuestObjectiveMetadata(player, obj, true);
				obj.complete(player);
			}
			
		}
		
	}
	
	public static final QuestToTheEnd INSTANCE = new QuestToTheEnd();
	
	private static class QuestFindBrown extends QuestObjective {


		public QuestFindBrown(Quest parent) {
			super("FIND_BROWN", false, true, parent);
			
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
			
			return Locale.a(player, "quest.FIND_BROWN.objective.find").formatted(mark);
			
		}
		
		@Override
		public String getDescription(Player player) {
			return Locale.a(player, "quest.FIND_BROWN.objective.find_neutral");
		}
		
		@Override
		public void complete(Player player) {
			super.complete(player);
			
			QuestUtils.setActiveQuestObjectiveMetadata(player, this, true);
			
			//QuestUtils.setActiveQuestObjective(player, parent.OBJECTIVES[1], false);
//			
//			new BukkitRunnable() {
//
//				@Override
//				public void run() {
//					QuestUtils.questStatusChanged(player);
//					parent.OBJECTIVES[1].start(player);
//				}
//				
//			}.runTaskLater(EventRegistery.main, 40L);
		}
		
	}

	
	public QuestToTheEnd() {
		
		ItemStack it = ItemUtils.createItem(RelizcItemMeth.class, null).getBukkitItem();
		it.setAmount(4);
		
		this.ID = "TO_THE_END";
		this.DESCRIPTION = "quest.TO_THE_END.description";
		this.DISPLAY_NAME = "quest.TO_THE_END.name";
		this.OBJECTIVES = new QuestObjective[]{
				new QuestFindBrown(this),
		};
		this.REWARDS = new QuestReward[] {
				new QuestRewardItem(it)
		};
	}

}
