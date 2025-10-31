package net.itsrelizc.gunmod.items.armor;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

@RelizcNativeMinecraftItem(material = Material.TOTEM_OF_UNDYING)
public class RelizcOverridenTotem extends RelizcNeoArmor {
	
	public RelizcOverridenTotem(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<String> renderInternalLore() {
		String a = StringUtils.wrapWithColor(Locale.a(owner, "item.TOTEM_OF_UNDYING.description"), Locale.getLanguage(owner));
		String b = a + Locale.a(owner, "item.TOTEM_OF_UNDYING.description.forced");
		String[] prod = b.split("\n");
		return StringUtils.fromArgs(prod);
		
	}
	
	public static class NoTotemDropListener implements Listener {

	    @EventHandler
	    public void onEvokerDeath(EntityDeathEvent event) {
	        // Check if the entity is an Evoker
	        if (event.getEntityType() == EntityType.EVOKER) {
	            // Remove Totem of Undying from drops
	            event.getDrops().removeIf(item -> item.getType() == Material.TOTEM_OF_UNDYING);
	        }
	    }
	}
}
 