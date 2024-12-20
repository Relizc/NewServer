package net.itsrelizc.smp.modsmp.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.smp.modsmp.items.ItemLibrary;
import net.itsrelizc.string.ChatUtils;
import net.minecraft.world.item.Items;

public class Shop implements Listener {
	
	private Player player;
	private Inventory inventory;
	private static Map<Player, Shop> shops = new HashMap<Player, Shop>();

	public Shop(Player player) {
		this.player = player;
		shops.put(player, this);
	}
	
	public Shop() {
		
	}
	
	public void openShop() {
		this.inventory = Bukkit.createInventory(player, 54, "贾斯丁小卖部");
		this.createBorder();
		this.player.openInventory(inventory);
		this.loadItems();
	}
	
	public void loadItems() {
		this.inventory.setItem(10, ItemLibrary.item_itemduper_shopedition());
	}
	
	public void createBorder() {
		for (int i = 0; i < 9; i ++) {
			this.inventory.setItem(i, getEmptyGlass());
		}
		for (int i = 9; i < 44; i += 9) {
			this.inventory.setItem(i, getEmptyGlass());
			this.inventory.setItem(i + 8, getEmptyGlass());
		}
		for (int i = 45; i < 54; i ++) {
			this.inventory.setItem(i, getEmptyGlass());
		}
		this.inventory.setItem(49, getClose());
	}
	
	public void closeShop() {
		shops.remove(this.player);
	}
	
	public static ItemStack getEmptyGlass() {
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName(" ");
		item.setItemMeta(im);
		
		return item;
	}
	
	public static Integer countMats(Player player, Material material) {
		int amount = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null) continue;
			if (item.getType() == material) {
				amount += item.getAmount();
			}
		}
		return amount;
	}
	
	public static void subMat(Player player, Material material, Integer amount) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null) continue;
			if (item.getType() == material) {
				if (amount >= item.getAmount()) {
					amount -= item.getAmount();
					item.setAmount(0);
				} else {
					item.setAmount(item.getAmount() - amount);
					amount = 0;
				}
				if (amount == 0) {
					break;
				}
			}
		}
	}
	
	public static boolean checkEmpty(Player player) {
		if (!isEmpty(player)) {
			ChatUtils.systemMessage(player, "§6§lSHOP", "§cYou need some empty space in your inventory!");
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 2f);
			return false;
		}
		return true;
	}
	
	@EventHandler
	public static void clickEvent(InventoryClickEvent event) {
		if (event.getView().getTitle().equalsIgnoreCase("贾斯丁小卖部") && shops.keySet().contains((Player) event.getWhoClicked())) {
			Player player = (Player) event.getWhoClicked();
			if (event.getCurrentItem() != null) {
				if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cClose")) {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1f, 2f);
					ChatUtils.systemMessage(player, "§6§lSHOP", "§eSee you next time customer!");
					player.closeInventory();
					event.setCancelled(true);
					return;
				}
				
				
				
				// SHOP
				if (event.getSlot() == 10 && event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7×")) {
					if (!checkEmpty(player)) {
						event.setCancelled(true);
						return;
					}
					if (countMats(player, Material.DIAMOND) >= 10) {
						player.getInventory().addItem(ItemLibrary.item_itemduper());
						subMat(player, Material.NETHERITE_INGOT, 3);
						ChatUtils.systemMessage(player, "§6§lSHOP", "§eYou purchased §71× §aItem Duper!§e!");
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
					} else {
						ChatUtils.systemMessage(player, "§6§lSHOP", "§cYou can't afford the cost to purchase this item!");
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 2f);
					}
				}
				
				event.setCancelled(true);
			}
		}
	}
	
	public static ItemStack getDisabledBlock() {
		ItemStack item = new ItemStack(Material.RED_CONCRETE, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName("§cShop ain't open.");
		List<String> lore = new ArrayList<String>();
		lore.add("§7Fuck off");
		im.setLore(lore);
		item.setItemMeta(im);
		
		return item;
	}
	
	public static boolean isEmpty(Player player) {
		boolean empty = false;
		Inventory inv = player.getInventory();
		for (int i = 9; i < 36; i ++) {
			if (inv.getContents()[i] == null) {
				empty = true;
				break;
			}
		}
		return empty;
	}
	
	public static ItemStack getClose() {
		ItemStack item = new ItemStack(Material.BARRIER, 1);
		ItemMeta im = item.getItemMeta();
		
		im.setDisplayName("§cClose");
		item.setItemMeta(im);
		
		return item;
	}
}
