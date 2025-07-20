package net.itsrelizc.gunmod.items.armor;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

public class RelizcNeoArmor extends RelizcDamageable {
	
	
	
	protected int getNewDurability() {return 1;}
	public String[] getCopyMetadata() {
		return new String[]{"DOUBLE;RICOCHET"};
	}

	public RelizcNeoArmor(Player owner, ItemStack it) {
		super(owner, it);
	}

	@Override
	public List<String> renderInternalLore() {
		
		ItemStack item = this.getBukkitItem(); // your tool/armor
		//int vanillaMax = item.getType().getMaxDurability();  
		
		//short damageValue = (short) ((1.0 - percentLeft) * vanillaMax);
		
		//double percentOfMax = (double) this.getTagInteger("DURABILITY") / getNewDurability();
		//double percentOfCurrent = (double) this.getTagInteger("DURABILITY") / getNewDurability();
		
		//item.setDurability(damageValue);
		
		String[] prod = {
				Locale.a(owner, "item.armor.description"),
				" ",
				renderDurability(),
				" ",
				Locale.a(owner, "item.armor.class") + " " + ArrowUtils.convertPenetrationToSymbol(this.getTagInteger("CLASS")),
				Locale.a(owner, "item.armor.ricochet") + " " + convertChance(this.getTagDouble("RICOCHET")) + "%.1f%%".formatted(this.getTagDouble("RICOCHET") * 100d)
		};
		return StringUtils.fromArgs(prod);
		
	}

}
 