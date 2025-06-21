package net.itsrelizc.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class Quest {
	
	public static class QuestObjective {
		private String id;
		private Object value;

		public QuestObjective(String id, Object value) {
			this.id = id;
			this.value = value;
		}
		
		public String getID() {
			return id;
		}
		
		public Object getValue() {
			return value;
		}

		public String toString(Player player) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getDescription(Player player) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public static class QuestRewardItem extends QuestReward {

		private ItemStack item;

		public QuestRewardItem(ItemStack item) {
			
			super("ITEM_" + item.getType().toString(), item);
			this.item = item;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public TextComponent toString(Player player) {
			TextComponent c = new TextComponent("  ");
			TextComponent a = new TextComponent("§r ×" + item.getAmount());
			
			RelizcItemStack stack = ItemUtils.castOrCreateItem(player, item);
			
			
			TextComponent b = new TextComponent("§7[%s§7]".formatted(stack.getBukkitItem().getItemMeta().getDisplayName()));
			
			
			String full = stack.getBukkitItem().getItemMeta().getDisplayName();
			for (String next : stack.getBukkitItem().getItemMeta().getLore()) {
				full += "\n" + next;
			}
			
			StringUtils.attachHover(b, full);
			
			
			b.addExtra(a);
			c.addExtra(b);
			
			return c;
		}
		
	}
	
	public static class QuestRewardExp extends QuestReward {

		private long amount;

		public QuestRewardExp(long amount) {
			
			super("EXP", amount);
			this.amount =amount;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public TextComponent toString(Player player) {
			return new TextComponent("  §a" + Locale.a(player, "quest.rewards.exp").formatted(this.amount));
		}
		
	}
	
	public static class QuestReward {
		private String id;
		private Object value;

		public QuestReward(String id, Object value) {
			this.id = id;
			this.value = value;
		}
		
		public String getID() {
			return id;
		}
		
		public Object getValue() {
			return value;
		}
		
		public TextComponent toString(Player player) {
			return new TextComponent(Locale.a(player, "quest.rewards.unknown"));
		}
	}
	
	public Object getObjectiveValue(Player player, String id) {
		Profile prof = Profile.findByOwner(player);
		
		if (prof.getMetadata("quest." + ID) != null) {
			JSONObject crit = (JSONObject) prof.getMetadata("quest." + ID);
			
			return crit.getOrDefault(id, null);
		}
		return null;
	}
	
	public Quest[] UNLOCK_NEXT = null;
	public String ID = null;
	public String DISPLAY_NAME = null;
	public String DESCRIPTION = null;
	public QuestObjective[] OBJECTIVES = null;
	public QuestReward[] REWARDS = null;

}
