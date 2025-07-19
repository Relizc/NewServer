package net.itsrelizc.gunmod.items.armor;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.Damageable;

import net.itsrelizc.health2.fletching.ArrowUtils;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.ItemUtils.MetadataPair;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.CompoundTag;

// CLASS: double 
// RICOCHET: double percentage
// DURABILITY: int
// MAX_DURABILITY: int

public class RelizcNeoArmor extends RelizcItemStack {
	
	
	public static class RelizcNeoArmorDamageRepairListener implements Listener {
		@EventHandler
		public void onPrepareCraft(PrepareItemCraftEvent e) {
		    HumanEntity p = e.getView().getPlayer();
		    ItemStack result = e.getInventory().getResult();
		    if (result != null && isCraftingRepair(e.getInventory())) {
		        e.getInventory().setResult(null);
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
		public void onItemDamage(PlayerItemDamageEvent e) {
		    ItemStack item = e.getItem();
		    
		    Object it = ItemUtils.castOrCreateItem(item);
		    if (it == null) return;
		    
		    Bukkit.broadcastMessage(it + " ");
		    RelizcNeoArmor armor = (RelizcNeoArmor) it;
		    
		    // Cancel the durability damage
		    Bukkit.broadcastMessage(armor + " ");

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
		                if (!first.isSimilar(item)) {
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
	
	public RelizcNeoArmor(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
		
		if (this.getTagInteger("MAX_DURABILITY") == -1) {
			CompoundTag tag = NBT.getNBT(it);
			NBT.setInteger(tag, "DURABILITY", it.getType().getMaxDurability() - ((Damageable) it.getItemMeta()).getDamage());
			NBT.setInteger(tag, "MAX_DURABILITY", it.getType().getMaxDurability());
			ItemStack snd = NBT.setCompound(it, tag);
			
			it.setItemMeta(snd.getItemMeta());
		} else {
			double currentPercentage = (double) this.getTagInteger("DURABILITY") / this.getTagInteger("MAX_DURABILITY");
			
			int damage = Math.min(it.getType().getMaxDurability(), (int) (1 - currentPercentage) * it.getType().getMaxDurability());
			
			Damageable dmg = (Damageable) it.getItemMeta();
			
			dmg.setDamage(damage);
			
			it.setItemMeta(dmg);
		}
	}
	
	private static String convertChance(double chance) {
		if (chance < 0.2) return "§c";
		if (chance < 0.4) return "§6";
		if (chance < 0.6) return "§e";
		if (chance < 0.8) return "§2";
		if (chance <= 1.000005) return "§a";
		else return "§b§l";
	}

	@Override
	public List<String> renderInternalLore() {
		
		ItemStack item = this.getBukkitItem(); // your tool/armor
		int vanillaMax = item.getType().getMaxDurability();  
		double percentLeft = (double) this.getTagInteger("DURABILITY") / this.getTagInteger("MAX_DURABILITY");
		short damageValue = (short) ((1.0 - percentLeft) * vanillaMax);
		
		double percentOfMax = (double) this.getTagInteger("DURABILITY") / getNewDurability();
		
		//item.setDurability(damageValue);
		
		String[] prod = {
				Locale.a(owner, "item.armor.description"),
				" ",
				Locale.a(owner, "item.armor.durability").formatted(convertChance(percentOfMax), this.getTagInteger("DURABILITY"), this.getTagInteger("MAX_DURABILITY"), this.getNewDurability()),
				" ",
				Locale.a(owner, "item.armor.class") + " " + ArrowUtils.convertPenetrationToSymbol(this.getTagInteger("CLASS")),
				Locale.a(owner, "item.armor.ricochet") + " " + convertChance(this.getTagDouble("RICOCHET")) + "%.1f%%".formatted(this.getTagDouble("RICOCHET") * 100d)
		};
		return StringUtils.fromArgs(prod);
		
	}

}
 