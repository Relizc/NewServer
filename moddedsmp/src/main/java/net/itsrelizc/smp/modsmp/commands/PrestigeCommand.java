package net.itsrelizc.smp.modsmp.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class PrestigeCommand extends RelizcCommand {
	
	private static class PrestigeMenu extends MenuTemplate2 {
		
		public ItemStack title() {
			
			return ItemGenerator.generate(Material.NETHER_STAR, 1, 
					Locale.get(this.getPlayer(), "commands.prestige.menu.title"), 
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore"),
					" ",
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore2"),
					"§7§m------------------------",
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore3"),
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore4"),
					" ",
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore5"),
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore6"),
					" ",
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore7"),
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore8"),
					" ",
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore9"),
					Locale.get(this.getPlayer(), "commands.prestige.menu.lore10"));
			
		}
		
		public ItemStack option(Material mat, String opt) {
			
			String[] stuff = Locale.get(getPlayer(), "commands.prestige." + "item" + ".lore").formatted(opt).split("\n");
			
			return ItemGenerator.generate(mat, 1, "§e" + Locale.get(getPlayer(), "commands.prestige." + "item" + ".name").formatted(opt), stuff);
			
		}

		public PrestigeMenu(String title) {
			super(title);
		}
		
		@Override
		public void apply() {
			this.defaultPreset();
			
			this.setItem(11, option(Material.GRAY_DYE, Locale.get(getPlayer(), "commands.prestige.helmet")));
			this.setItem(20, option(Material.GRAY_DYE, Locale.get(getPlayer(), "commands.prestige.chestplate")));
			this.setItem(29, option(Material.GRAY_DYE, Locale.get(getPlayer(), "commands.prestige.leggings")));
			this.setItem(38, option(Material.GRAY_DYE, Locale.get(getPlayer(), "commands.prestige.boots")));
		}
		
		@Override
		public void onClick(InventoryClickEvent event) {
			this.defaultPreset();
		}
		
	}

	public PrestigeCommand() {
		super("prestige", "allows u to prestige");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
//		Menu2 menu = new Menu2(player, 6, new PrestigeMenu(Locale.get(player, "commands.prestige.menu")));
//		menu.open();
		
		StringUtils.systemMessage(player, Locale.get(player, "commands.prestige"), Locale.get(player, "commands.prestige.disabled"));
		
		return true;
		
	}
	
	

}
