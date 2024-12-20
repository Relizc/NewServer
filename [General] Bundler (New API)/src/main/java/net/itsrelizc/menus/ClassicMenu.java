package net.itsrelizc.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.menus.MenuTemplate;

public class ClassicMenu implements Listener {
	
	public Inventory menu;
	private int row;
	public Player holder;
	private MenuTemplate template;

	public ClassicMenu(Player holder, int row, String name) {
		this.menu = Bukkit.createInventory(holder, row * 9, name);
		this.holder = holder;
		this.row = row;
		this.template = null;
	}
	
	public ClassicMenu(Player holder, int row, String name, MenuTemplate template) {
		this(holder, row, name);
		this.template = template;
	}
	
	public void setItem(int slot, ItemStack item) {
		this.menu.setItem(slot, item);
	}
	
	public void fillEmpty() {
		ItemStack it = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName(" ");
		it.setItemMeta(im);
		for (int i = 0; i < this.row * 9; i ++) {
			this.menu.setItem(i, it);
		}
	}
	
	public void leaveMiddleArea() {
		for (int i = 10; i < this.row * 9 - 10; i ++) {
			if ((i - 8) % 9 == 0 || (i - 8) % 9 == 1) continue;
			this.menu.clear(i);
		}
	}
	
	public void leaveMiddleAreaWith(ItemStack is) {
		for (int i = 10; i < this.row * 9 - 10; i ++) {
			if ((i - 8) % 9 == 0 || (i - 8) % 9 == 1) continue;
			this.menu.setItem(i, is);
		}
	}
	
	public void leaveMiddleAreaWithDimGlass() {
		ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(" ");
		i.setItemMeta(im);
		leaveMiddleAreaWith(i);
	}
	
	public void leaveMiddleAreaWithBlackGlass() {
		ItemStack i = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(" ");
		i.setItemMeta(im);
		leaveMiddleAreaWith(i);
	}
	
	public void putClose() {
		ItemStack it = new ItemStack(Material.BARRIER, 1);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName("§cClose");
		it.setItemMeta(im);
		this.menu.setItem(this.row * 9 - 5, it);
	}
	
	
	public void show() {
		
		this.fillEmpty();
		this.putClose();
		this.holder.openInventory(this.menu);
		if (this.template != null) {
			this.template.loadTemplate(this);
		}
		Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("RelizcSMP"));
	}
	
	public void close() {
		this.holder.closeInventory();
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void click(InventoryClickEvent event) {
		
		if (event.getClick() == ClickType.DOUBLE_CLICK) {
			return;
		};
		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getItemMeta() == null) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cClose")) {
			this.close();
			return;
		}
		
		event.setCancelled(this.template.onClick(event));
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void invclose(InventoryCloseEvent event) {
		
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
		if (this.template.onClose(event)) return;
	}
	
}
