package net.itsrelizc.gunmod.items.armor;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.Damageable;

import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.ItemUtils.MetadataPair;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.minecraft.nbt.CompoundTag;

// CLASS: double 
// RICOCHET: double percentage
// DURABILITY: int
// MAX_DURABILITY: int

public class RelizcDamageable extends RelizcItemStack {
	
	
	public static class RelizcDamageableRepairListener implements Listener {
		@EventHandler(priority=EventPriority.LOWEST)
		public void onPrepareCraft(PrepareItemCraftEvent e) {
		    HumanEntity p = e.getView().getPlayer();
		    ItemStack result = e.getInventory().getResult();
		    if (result != null && isCraftingRepair(e.getInventory())) {
		        e.getInventory().setResult(null);
		    }
		}
		
		@EventHandler(priority=EventPriority.HIGHEST)
		public void onPrepareCraft(CraftItemEvent e) {
		    HumanEntity p = e.getView().getPlayer();
		    ItemStack result = e.getInventory().getResult();
		    if (result != null && isCraftingRepair(e.getInventory())) {
		        e.setCancelled(true);
		    }
		}

		@EventHandler
		public void onPrepareAnvil(PrepareAnvilEvent e) {
		    ItemStack result = e.getResult();
		    if (result != null && isAnvilRepair(e.getInventory())) {
		        e.setResult(null);
		    }
		}
		
		@EventHandler
		public void onPrepareGrindstone(PrepareGrindstoneEvent e) {
		    ItemStack result = e.getResult();
		    if (result != null && isGrindstoneRepair(e.getInventory())) {
		        e.setResult(null);
		    }
		}
		
		@EventHandler
		public void onPrepareSmithing(PrepareSmithingEvent e) {
		    ItemStack result = e.getResult();
		    if (result != null && isSmithingUpgrade(e.getInventory())) {
		        e.setResult(null);
		    }
		}
		
		@EventHandler
		public void onRepairAttempt(InventoryClickEvent e) {
		    Inventory inv = e.getInventory();
		    InventoryType type = inv.getType();

		    // Only handle result slot click
		    if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

		    // Player is clicking the result of an anvil, grindstone, or smithing table
		    if (type == InventoryType.ANVIL) {
		        if (isAnvilRepair((AnvilInventory) inv)) {
		            e.setCancelled(true);
		            //e.getWhoClicked().sendMessage(ChatColor.RED + "Repairing with an anvil is disabled.");
		        }
		    } else if (type == InventoryType.GRINDSTONE) {
		        if (isGrindstoneRepair((GrindstoneInventory) inv)) {
		            e.setCancelled(true);
		            //e.getWhoClicked().sendMessage(ChatColor.RED + "Repairing or disenchanting with a grindstone is disabled.");
		        }
		    } else if (type == InventoryType.SMITHING) {
		        if (isSmithingUpgrade((SmithingInventory) inv)) {
		            e.setCancelled(true);
		            //e.getWhoClicked().sendMessage(ChatColor.RED + "Upgrading with a smithing table is disabled.");
		        }
		    }
		}
		
		@EventHandler
		public void mend(PlayerItemMendEvent event) {
			
			
			
			RelizcItemStack it = ItemUtils.castOrCreateItem(event.getItem());
			//Bukkit.broadcastMessage(it.toString());
			if (it instanceof RelizcDamageable) {
				RelizcDamageable piece = (RelizcDamageable) it;
				
				int amount = event.getRepairAmount();
				amount = Math.min(amount, piece.getTagInteger("MAX_DURABILITY") - piece.getTagInteger("DURABILITY"));
				event.setCancelled(true);
				
				repairWithQualityLoss(piece, event.getPlayer(), piece.getBukkitItem(), amount);
				
				
			}
			
		}
		
		public static void breakItem(Player player, ItemStack it) {
			Location loc = player.getLocation();
	        player.playSound(loc, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);

	        // 2. Spawn item break particles (like block fragments)
	        // We'll use ITEM_CRACK with the broken item's material
	        player.spawnParticle(
	            Particle.ITEM_CRACK,
	            loc.clone().add(0, 1.0, 0),
	            20, // count
	            0.3, 0.3, 0.3,
	            0.05,
	            it
	        );
	        
	        it.setAmount(0);
		}
		
		@EventHandler
		public void onItemDamage(PlayerItemDamageEvent e) {
			
			e.setCancelled(true);
			
		    ItemStack item = e.getItem();
		    int amount = e.getDamage();
		    
		    RelizcItemStack it = ItemUtils.castOrCreateItem(item);
		    if (it == null) return;
		    if (!(it instanceof RelizcDamageable)) return;
		    
		    
		    
		    //Bukkit.broadcastMessage(it + " ");
		    
		    RelizcDamageable armor = (RelizcDamageable) it;
		    
		    CompoundTag tag = NBT.getNBT(armor.getBukkitItem());
		    int current = tag.getInt("DURABILITY");
		    current -= amount;
		    
		    
		    
		    if (current <= 0) {
		    	breakItem(e.getPlayer(), armor.getBukkitItem());
		    	return;
		    	
		    	
		    }
		    
		    tag.putInt("DURABILITY", current);
		    ItemStack copy = NBT.setCompound(armor.getBukkitItem(), tag);
		    armor.getBukkitItem().setItemMeta(copy.getItemMeta());
		    
		    refreshDurabilityBar(current, tag.getInt("MAX_DURABILITY"), item);
		    
		    MetadataPair[] allPairs = getPairWhenDupe(armor, tag);
		    Class<? extends RelizcDamageable> handle = (Class<? extends RelizcDamageable>) ItemUtils.getHandler(armor);
		    
		    RelizcDamageable copy2 = ItemUtils.createItem(handle, e.getPlayer(), allPairs);
		    armor.getBukkitItem().getItemMeta().setLore(copy.getItemMeta().getLore());
		    
		    
		    // Cancel the durability damage
		    //Bukkit.broadcastMessage(armor + " ");

		    // Or reduce damage
		    // e.setDamage(e.getDamage() / 2);
		}

		public boolean isCraftingRepair(CraftingInventory inv) {
		    ItemStack[] matrix = inv.getMatrix();
		    ItemStack first = null;

		    for (ItemStack item : matrix) {
		        if (item != null && item.getType() != Material.AIR) {
		            if (first == null) {
		                first = item;
		            } else {
		            	
		            	
		                if (!(item.getType() == first.getType())) {
		                    return false; // Not the same item
		                }
		            }
		        }
		    }

		    // Must be exactly 2 matching damaged items
		    int count = 0;
		    for (ItemStack item : matrix) {
		        if (item != null && item.getType() != Material.AIR) {
		            count++;
		        }
		    }

		    return (first != null && first.getType().getMaxDurability() > 0 && count == 2);
		}
		
		public boolean isAnvilRepair(AnvilInventory inv) {
		    ItemStack first = inv.getItem(0);
		    ItemStack second = inv.getItem(1);

		    if (first == null || second == null) return false;

		    // Repair with material (e.g., iron + iron ingot)
		    if (!first.getType().equals(second.getType()) &&
		        second.getType().toString().endsWith("_INGOT") || second.getType() == Material.LEATHER || second.getType() == Material.NETHERITE_INGOT) {
		        return true;
		    }

		    // Repair by combining same item types
		    return first.isSimilar(second) && first.getType().getMaxDurability() > 0;
		}
		
		public boolean isGrindstoneRepair(GrindstoneInventory inv) {
		    ItemStack top = inv.getItem(0);
		    ItemStack bottom = inv.getItem(1);

		    if (top == null || bottom == null) return false;

		    // Repair: Two of the same item type
		    if (top.isSimilar(bottom) && top.getType().getMaxDurability() > 0) {
		        return true;
		    }

		    // Disenchant: One item is enchanted
		    if ((top != null && top.getEnchantments().size() > 0) || (bottom != null && bottom.getEnchantments().size() > 0)) {
		        return true;
		    }

		    return false;
		}

		
		public boolean isSmithingUpgrade(SmithingInventory inv) {
		    ItemStack base = inv.getItem(0); // base item (e.g., diamond armor)
		    ItemStack addition = inv.getItem(1); // upgrade item (e.g., netherite ingot)

		    if (base == null || addition == null) return false;

		    // Simple check for netherite upgrades
		    return addition.getType() == Material.NETHERITE_INGOT &&
		           base.getType().toString().startsWith("DIAMOND_");
		}

	}
	
	protected int getNewDurability() {return 1;}
	public String[] getCopyMetadata() {
		return new String[] {};
	}
	
	public static MetadataPair[] getPairWhenDupe(RelizcDamageable armor, CompoundTag tag) {
		int length = 2 + armor.getCopyMetadata().length; // durability + max_durability + ... specified
		
		MetadataPair[] allPairs = new MetadataPair[length];
		allPairs[0] = new MetadataPair("DURABILITY", tag.getInt("DURABILITY"));
		allPairs[1] = new MetadataPair("MAX_DURABILITY", tag.getInt("MAX_DURABILITY"));
		for (int i = 2; i < length; i ++) {
			String[] dat = armor.getCopyMetadata()[i - 2].split(";");
			String typ = dat[0];
			String key = dat[1];
			
			if (typ.equalsIgnoreCase("DOUBLE")) {
				allPairs[i] = new MetadataPair(key, tag.getDouble(key));
			} else if (typ.equalsIgnoreCase("INT")) {
				allPairs[i] = new MetadataPair(key, tag.getInt(key));
			} if (typ.equalsIgnoreCase("LONG")) {
				allPairs[i] = new MetadataPair(key, tag.getLong(key));
			} if (typ.equalsIgnoreCase("STRING")) {
				allPairs[i] = new MetadataPair(key, tag.getString(key));
			} 
		}
		
		return allPairs;
	}
	
	public static void refreshDurabilityBar(int newDurability, int currentMax, ItemStack stack) {
		double currentPercentage = (double) newDurability / currentMax;
		int damage = Math.min(stack.getType().getMaxDurability(), (int) ((1 - currentPercentage) * stack.getType().getMaxDurability()));
		if (damage == 0 && (newDurability != currentMax)) damage = 1;
		
		
		
		Damageable dmg = (Damageable) stack.getItemMeta();
		
		dmg.setDamage(damage);
		Bukkit.broadcastMessage(damage + " " + newDurability + " " + currentMax);
		stack.setItemMeta(dmg);
	}
	
	public static ItemStack repairWithQualityLoss(RelizcDamageable armor, Player player, ItemStack stack, int amount) {
		
		CompoundTag tag = NBT.getNBT(stack);
		
		int currentMax = NBT.getInteger(tag, "MAX_DURABILITY");
		//currentMax -= (amount / 2);
		int newDurability = NBT.getInteger(tag, "DURABILITY") + amount;
		
		newDurability = Math.min(currentMax, newDurability);
		NBT.setInteger(tag, "DURABILITY", newDurability);
		NBT.setInteger(tag, "MAX_DURABILITY", currentMax);
		ItemStack copy = NBT.setCompound(stack, tag);
		
		stack.setItemMeta(copy.getItemMeta());
		
		refreshDurabilityBar(newDurability, currentMax, stack);
		
//		RelizcNeoArmor copyArmor = ItemUtils.castOrCreateItem(stack);
//		Damageable meta2 = (Damageable) stack.getItemMeta();
//		
//		meta2.setLore(copyArmor.getBukkitItem().getItemMeta().getLore());
//		stack.setItemMeta(meta2);
		
		int length = 2 + armor.getCopyMetadata().length; // durability + max_durability + ... specified
		
		MetadataPair[] allPairs = getPairWhenDupe(armor, tag);
		
		Class<? extends RelizcItemStack> handle = ItemUtils.getHandler(armor);
		RelizcDamageable armorClone = (RelizcDamageable) ItemUtils.createItem(handle, player, allPairs);
		
		List<String> newLore = armorClone.getBukkitItem().getItemMeta().getLore();
		
		//Bukkit.broadcastMessage(String.join(", ", newLore));
		
		
		Damageable dmg2 = (Damageable) stack.getItemMeta();
		dmg2.setLore(newLore);
		stack.setItemMeta(dmg2);
//		
//		armor.getBukkitItem().setItemMeta(stack.getItemMeta());
		
		return stack;
	}
	
	public static ItemStack updateDurabilityBar(ItemStack stack) {
		
		CompoundTag tag = NBT.getNBT(stack);
		
		double currentPercentage = (double) NBT.getInteger(tag, "DURABILITY") / NBT.getInteger(tag, "MAX_DURABILITY");
		int damage = Math.min(stack.getType().getMaxDurability(), (int) ((1 - currentPercentage) * stack.getType().getMaxDurability()));
		
		Damageable dmg = (Damageable) stack.getItemMeta();
		
		dmg.setDamage(damage); 	
		
		stack.setItemMeta(dmg);
		
		//Bukkit.broadcastMessage(dmg.toString());
		
		return stack;
	}

	public RelizcDamageable(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
		
		if (this.getTagInteger("MAX_DURABILITY") == -1) {
			CompoundTag tag = NBT.getNBT(it);
			NBT.setInteger(tag, "DURABILITY", it.getType().getMaxDurability() - ((Damageable) it.getItemMeta()).getDamage());
			NBT.setInteger(tag, "MAX_DURABILITY", it.getType().getMaxDurability());
			ItemStack snd = NBT.setCompound(it, tag);
			
			it.setItemMeta(snd.getItemMeta());
		} else {
			
			updateDurabilityBar(it);

		}
	}
	
	protected static String convertChance(double chance) {
		if (chance < 0.2) return "§c";
		if (chance < 0.4) return "§6";
		if (chance < 0.6) return "§e";
		if (chance < 0.8) return "§2";
		if (chance <= 1.000005) return "§a";
		else return "§b§l";
	}
	
	public String renderDurability() {
		double percentLeft = (double) this.getTagInteger("DURABILITY") / this.getTagInteger("MAX_DURABILITY");
		return Locale.a(owner, "item.armor.durability").formatted(convertChance(percentLeft), this.getTagInteger("DURABILITY"), this.getTagInteger("MAX_DURABILITY"), this.getNewDurability());
	}

}
 