package net.itsrelizc.itemlib;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

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

}
