package net.itsrelizc.itemlib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.menus.Menu2;
import net.itsrelizc.nbt.NBT;
import net.minecraft.nbt.CompoundTag;

public class OriginalItemOverrider implements Listener {
	
	@EventHandler(ignoreCancelled=true)
	public void craft(PrepareItemCraftEvent event) {
		CraftingInventory inventory = event.getInventory();
	    ItemStack result = inventory.getResult();

	    if (result == null) return;
	    if (!(event.getView().getPlayer() instanceof Player)) return;

	    RelizcItemStack res = ItemUtils.castOrCreateItem((Player) event.getView().getPlayer(), result);
	    inventory.setResult(res.getBukkitItem());
	}
	
	@EventHandler(ignoreCancelled=true)
	public void pickup(PlayerPickupItemEvent event) {
		
		if (event.isCancelled()) return;
		
		Player p = event.getPlayer();
		
		RelizcItemStack result = ItemUtils.castOrCreateItem(p, event.getItem().getItemStack());
		
		
		
		event.getItem().setItemStack(result.getBukkitItem());
	}
	
	@EventHandler(ignoreCancelled=true) 
	public void pickup(BlockPlaceEvent event) {
		
		if (event.isCancelled()) return;
		
		Player p = event.getPlayer();
		
		RelizcItemStack result = ItemUtils.castOrCreateItem(p, event.getItemInHand());
		
		if (result.getTagString("id") == null) return;
		if (ItemUtils.getHandler(result.getTagString("id")) == null) return;
		
		RelizcItem annotation = ItemUtils.getHandler(result.getTagString("id")).getAnnotation(RelizcItem.class);

		if (!annotation.placeable()) event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
	public void inventoryOpenedMenu(InventoryOpenEvent event) {
		
		////("opened" + event.getInventory().getSize());
		
		for (int i = 0; i < event.getInventory().getSize(); i ++) {
			
			if (event.getInventory().getItem(i) == null) continue;
			
			////("opened " + event.getInventory().getItem(i) + " " + Menu2.isMenuItem(event.getInventory().getItem(i)));
			if (Menu2.isMenuItem(event.getInventory().getItem(i))) continue;
			
			RelizcItemStack stack = ItemUtils.castOrCreateItem((Player) event.getPlayer(), event.getInventory().getItem(i));
			event.getInventory().setItem(i, stack.getBukkitItem());
		}
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
	public void inventoryOpenedMenu(PlayerJoinEvent event) {
		
		////("opened" + event.getInventory().getSize());
		
		for (int i = 0; i < event.getPlayer().getInventory().getSize(); i ++) {
			
			if (event.getPlayer().getInventory().getItem(i) == null) continue;
			
			RelizcItemStack stack = ItemUtils.castOrCreateItem((Player) event.getPlayer(), event.getPlayer().getInventory().getItem(i));
			event.getPlayer().getInventory().setItem(i, stack.getBukkitItem());
		}
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
	public void anvilCheck(PrepareAnvilEvent event) {
		
		////("opened" + event.getInventory().getSize());
		
		AnvilInventory inv = event.getInventory();
        ItemStack result = event.getResult();
        if (result == null) return;

        String newName = inv.getRenameText();
        
        if (newName == null || newName.strip().trim().length() == 0) return;
        
        //Bukkit.broadcastMessage(newName);
        
        CompoundTag tag = NBT.getNBT(inv.getItem(0));
        tag.putString("CUSTOM_NAME", newName);
        ItemStack before = NBT.setCompound(inv.getItem(0), tag);
        
        RelizcItemStack copy = ItemUtils.castOrCreateItem(before);
        event.setResult(copy.getBukkitItem());
        
		
	}
}
