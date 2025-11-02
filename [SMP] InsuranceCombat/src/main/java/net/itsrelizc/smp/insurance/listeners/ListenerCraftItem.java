package net.itsrelizc.smp.insurance.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.nbt.NBT;
import net.minecraft.nbt.CompoundTag;

public class ListenerCraftItem implements Listener {
	
	@EventHandler
	public void undoCraftItem(PlayerJoinEvent event) {
		Inventory inv = event.getPlayer().getInventory();
		
		for (ItemStack it: inv) {
			if (it == null) continue;
			
			CompoundTag tag = NBT.getNBT(it);
			if (tag == null) return;
			
			boolean crafted = tag.getBoolean("crafted");
			if (crafted) {
				tag.remove("crafted");
				
				ItemStack it2 = NBT.setCompound(it, tag);
				
				ItemMeta im2 = it2.getItemMeta();
				
				List<String> lore = im2.getLore();
				
				for (String s : lore) {
					if (s.startsWith("§b🔨")) {
						lore.remove(s);
					}
				}
				
				im2.setLore(lore);
				
				it.setItemMeta(im2);
			}
		}
	
//	@EventHandler
//    public void onItemCraft(PrepareItemCraftEvent event) {
//		
////		System.out.println(event.getInventory().getType());
//		if (event.getInventory().getType() != InventoryType.WORKBENCH) return;
//		
//		if (event.getRecipe() == null) return;
//		
//		if (event.getInventory().getItem(0) == null) return;
//		
//		ItemStack item = event.getInventory().getItem(0);	
//		
//		if (item.getType().getMaxStackSize() != 1) return;
//		
//		ItemMeta im = item.getItemMeta();
//		List<String> lore = im.getLore();
//		if (lore == null) lore = StringUtils.fromNewList();
//		lore.add("§b🔨 " + Locale.get((Player) event.getView().getPlayer(), "insurance.crafteditem"));
//		im.setLore(lore);
//		item.setItemMeta(im);
//		
//		CompoundTag tag = NBT.getNBT(item);
//		NBT.setBoolean(tag, "crafted", true);
//		ItemStack newitem = NBT.setCompound(item, tag);
//		
//		event.getInventory().setItem(0, newitem);
//		
//		
//        
//    }

}
