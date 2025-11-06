package net.itsrelizc.menus;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.api.RelizcInteractiveMenu;
import net.itsrelizc.api.RelizcInteractiveMenuTemplate;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.minecraft.nbt.CompoundTag;

public class Menu2 implements Listener, RelizcInteractiveMenu {
	
	public static boolean isDuped(Player player, ItemStack it) {
		
		if (it == null || player == null) return false;
		
		CompoundTag tag = NBT.getNBT(it);
		if (tag == null) return false;
		if (!tag.contains("generatedMenu")) return false;
		
		return true;
		
	}
	
	public static boolean isMenuItem(ItemStack it) {
		
		CompoundTag tag = NBT.getNBT(it);
		////(tag.toString());
		return tag.contains("generatedMenu");
		
	}
	
	
	
	private Inventory inventory;
	private MenuTemplate2 currentTemplate;
	private Player player;
	
	
	public Menu2(Player player, int rows, MenuTemplate2 template) {
		
		this.player = player;
		this.currentTemplate = template;
		this.inventory = Bukkit.createInventory(player, rows * 9, template.getTitle());
		this.currentTemplate.apply_wrapper(this);
		
		EventRegistery.register(this);
		
	}
	
	public void open() {
		
		////(this.player.getOpenInventory().toString());
		
		if (this.player.getOpenInventory() != null && !(this.getPlayer().getOpenInventory() instanceof CraftInventoryView)) {
			this.player.getOpenInventory().close();
		}
		
		this.currentTemplate.apply_wrapper(this);
		this.player.openInventory(inventory);
	}
	
	public int getSize() {
		return this.inventory.getSize();
	}
	
	public int getRows() {
		return this.getSize() / 9;
	}
	
	public void setItem(int slot, ItemStack item) {
		
		CompoundTag tag = NBT.getNBT(item);
		tag.putBoolean("generatedMenu", true);
		item = NBT.setCompound(item, tag);
		
		this.inventory.setItem(slot, item);
	}
	
	public void clearItem(int slot) {
		this.inventory.clear(slot);
	}
	
	public void close() {
		this.player.closeInventory();
	}
	
	public RelizcInteractiveMenuTemplate getTemplate() {
		return this.currentTemplate;
	}

	public Player getPlayer() {
		// TODO Auto-generated method stub
		return this.player;
	}
	
	@EventHandler
	public void event_onClick(InventoryClickEvent event) {
		
		
		
		if (((Player) event.getWhoClicked()) != player) {
			return;
		}
		
		
		
		event.setCancelled(true);
		
		if (event.getClick() == ClickType.DOUBLE_CLICK) return;
		
		this.currentTemplate.onClick(event);
		
//		//("invclick by " + event.getWhoClicked().getName());
	}
	
	public static int removeAllDupedItems(Player player) {
		int count = 0;
		for (ItemStack item : player.getInventory()) {
			
			if (isDuped(player, item)) {
				count += item.getAmount();
				item.setAmount(0);
				
			}
			
		}
		
		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (isDuped(player, item)) {
				count += item.getAmount();
				item.setAmount(0);
			}
		}
		
		ItemStack off = player.getInventory().getItemInOffHand();
		if (isDuped(player, off)) {
			count += off.getAmount();
			off.setAmount(0);
		}
		
		return count;
	}
	
	public static void startTimedDupeChecking() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					int count = removeAllDupedItems(player);
					if (count != 0) {
						player.sendMessage(Locale.a(player, "inventory.duped"));
					}
					
				}
			}
			
		}.runTaskTimer(EventRegistery.main, 10 * 20l, 10 * 20l);
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent event) {
		removeAllDupedItems(event.getPlayer());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		removeAllDupedItems(event.getPlayer());
	}
	
	
	
	@EventHandler
	public void event_onClose(InventoryCloseEvent event) {
		if (((Player) event.getPlayer()) != player) {
			return;
		}
		removeAllDupedItems((Player) event.getPlayer());
		this.currentTemplate.onClose(event);
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
		
		
		
//		//("invclose by " + event.getPlayer().getName());
	}
	

}
