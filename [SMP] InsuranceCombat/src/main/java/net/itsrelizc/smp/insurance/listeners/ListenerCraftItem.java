package net.itsrelizc.smp.insurance.listeners;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class ListenerCraftItem implements Listener {
	
	@EventHandler
    public void onItemCraft(PrepareItemCraftEvent event) {
		
//		System.out.println(event.getInventory().getType());
		if (event.getInventory().getType() != InventoryType.WORKBENCH) return;
		
		if (event.getRecipe() == null) return;
		
		if (event.getInventory().getItem(0) == null) return;
		
		ItemStack item = event.getInventory().getItem(0);	
		
		if (item.getType().getMaxStackSize() != 1) return;
		
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		if (lore == null) lore = StringUtils.fromNewList();
		lore.add("§b🔨 " + Locale.get((Player) event.getView().getPlayer(), "insurance.crafteditem"));
		im.setLore(lore);
		item.setItemMeta(im);
		
		NBTTagCompound tag = NBT.getNBT(item);
		NBT.setBoolean(tag, "crafted", true);
		ItemStack newitem = NBT.setCompound(item, tag);
		
		event.getInventory().setItem(0, newitem);
		
		
        
    }

}
