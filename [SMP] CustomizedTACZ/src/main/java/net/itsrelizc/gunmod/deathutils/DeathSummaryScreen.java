package net.itsrelizc.gunmod.deathutils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.deathutils.DeathUtils.PlayerGhostEvent;
import net.itsrelizc.health2.Body;
import net.itsrelizc.health2.Limb;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.minecraft.nbt.CompoundTag;

public class DeathSummaryScreen extends MenuTemplate2 {
	
	public static class DeathSummaryListeners implements Listener {
		
		public static Map<String, BukkitTask> showtimer = new HashMap<String, BukkitTask>();
		
		@EventHandler
		public void death(PlayerGhostEvent event) {
			if (event.isGhost()) {
				
				event.getPlayer().getInventory().setHeldItemSlot(4);
				ItemStack it = ItemGenerator.generate(Material.RED_BED, 1, "§a§d§b§c§2" + Locale.a(event.getPlayer(), "menu.death.revive") + " " + Locale.a(event.getPlayer(), "general.interact.rightclick"));
				CompoundTag tag = NBT.getNBT(it);
				tag.putString("interactid", "menu");
				it = NBT.setCompound(it, tag);
				event.getPlayer().getInventory().setItem(4, it);
				
				BukkitTask task = new BukkitRunnable() {

					@Override
					public void run() {
						
						if (event.getPlayer().getOpenInventory().getTitle().startsWith("§5§3§b§d")) return;
						
						Menu2 menu = new Menu2(event.getPlayer(),6 , new DeathSummaryScreen(event.getPlayer()));
						menu.open();
					}
					
				}.runTaskLater(EventRegistery.main, 20 * 3);
				
				showtimer.put(event.getPlayer().getUniqueId().toString(), task);
				
			}
		}
		
		@EventHandler
		public void interact(PlayerInteractEvent event) {
			if (event.getItem() == null) return;
			//Bukkit.broadcastMessage(event.getItem().getItemMeta().getDisplayName().replace("§", "&") + " ");
			if (!(NBT.getString(NBT.getNBT(event.getItem()), "interactid").equals("menu"))) return;
			
			//Bukkit.broadcastMessage("LOL");
			Menu2 menu = new Menu2(event.getPlayer(),6 , new DeathSummaryScreen(event.getPlayer()));
			menu.open();
		}
		
	}
	
	public static class CommandRevive extends RelizcCommand {

		public CommandRevive() {
			super("revive", "opens the revive menu");
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean onPlayerExecute(Player player, String[] args) {
			
			if (!DeathUtils.isDead(player)) return true;
			
			Menu2 menu = new Menu2(player,6 , new DeathSummaryScreen(player));
			menu.open();
			
			return true;
			
		}
		
	}

	public DeathSummaryScreen(Player player) {
		super("§5§3§b§d§r" + Locale.get(player, "menu.death.title"));
		// TODO Auto-generated constructor stub
	}
	
	private static Color getGradientColor(double percentage) {
        // Clamp value to 0.0 - 1.0
        percentage = Math.max(0.0, Math.min(1.0, percentage));

        int red = (int) ((1.0 - percentage) * 512);
        red = Math.min(255, red);
        int green = (int) (percentage * 512);
        green = Math.min(255, green);
        int blue = 0;

        return Color.fromRGB(red, green, blue);
    }
	
	private String getLore(int part) {
		
		Body body = Body.parts.get(getPlayer().getUniqueId().toString());
		Limb head = body.convert(part);
		
		
		
		// head
		String headdesc = "§b§l" + Locale.get(getPlayer(), "combat.limb." + head.getName().toLowerCase() +".name") + Locale.get(getPlayer(), "menu.damage.insights") + " §r§7- %s%d§8/§7%d §c❤".formatted(head.getCriticalColor(getPlayer()).substring(0, 2), head.getHealth(), head.getMaxHealth()) +"\n";
		
		if (head.getDamageLogs().getSize() == 0) {
			return headdesc + "§7§o" + Locale.a(getPlayer(), "menu.damage.none");
		}
		
		headdesc += "§8╥ §7" + Locale.get(getPlayer(), "menu.damage.earliest") + "\n";
		for (DamageRecord rec : head.getDamageLogs() ) {
			
			headdesc += rec.toString(getPlayer()) + "\n";
			
		}
		
		headdesc += "§8╨ §7" + Locale.get(getPlayer(), "menu.damage.latest");
		
		return headdesc;
	}
	
	@Override
	public void apply() {
		this.fillAllWith(BLACK_GLASS());
		
		Body body = Body.parts.get(getPlayer().getUniqueId().toString());
		
		Limb head = body.convert(0);
		
		ItemStack headcap = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta headmeta = (LeatherArmorMeta) headcap.getItemMeta();
		headmeta.setColor(getGradientColor(((double) head.getHealth())/head.getMaxHealth()));
		headmeta.addItemFlags(ItemFlag.values());
		headcap.setItemMeta(headmeta);
		
		String[] headlore = getLore(0).split("\n");
		
		this.setItem(11, ItemGenerator.generate(headcap, 1, headlore[0], Arrays.copyOfRange(headlore, 1, headlore.length)));
		
		
		
		///
		
		Limb chest = body.convert(1);
		Limb larm = body.convert(3);
		Limb rarm = body.convert(4);
		
		ItemStack chestcap = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta chestmeta = (LeatherArmorMeta) chestcap.getItemMeta();
		
		double tot = chest.getHealth() + larm.getHealth() + rarm.getHealth();
		double max = chest.getMaxHealth() + larm.getMaxHealth() + rarm.getMaxHealth();
		
		chestmeta.setColor(getGradientColor(tot/max));
		chestmeta.addItemFlags(ItemFlag.values());
		chestcap.setItemMeta(chestmeta);
		
		String chestlore = getLore(1) + "\n\n" + getLore(3) + "\n\n" + getLore(4);
		String[] chestlorelist = chestlore.split("\n");
		
		this.setItem(20, ItemGenerator.generate(chestcap, 1, chestlorelist[0], Arrays.copyOfRange(chestlorelist, 1, chestlorelist.length)));
		
		
		///
		
		Limb penis = body.convert(2);
		
		ItemStack peniscap = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta penismeta = (LeatherArmorMeta) peniscap.getItemMeta();
		
		tot = penis.getHealth();
		max = penis.getMaxHealth();
		
		penismeta.setColor(getGradientColor(tot/max));
		penismeta.addItemFlags(ItemFlag.values());
		peniscap.setItemMeta(penismeta);
		
		String penislore = getLore(2);
		String[] penislorelist = penislore.split("\n");
		
		this.setItem(29, ItemGenerator.generate(peniscap, 1, penislorelist[0], Arrays.copyOfRange(penislorelist, 1, penislorelist.length)));
		
		///
		
		Limb penis2 = body.convert(5);
		Limb penis3 = body.convert(6);
		
		ItemStack boobcap = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta boobmeta = (LeatherArmorMeta) boobcap.getItemMeta();
		
		tot = penis2.getHealth() + penis3.getHealth();
		max = penis2.getMaxHealth() + penis3.getMaxHealth();
		
		boobmeta.setColor(getGradientColor(tot/max));
		boobmeta.addItemFlags(ItemFlag.values());
		boobcap.setItemMeta(boobmeta);
		
		String booblore = getLore(5) + "\n\n" + getLore(6);
		String[] booblorelist = booblore.split("\n");
			
		this.setItem(38, ItemGenerator.generate(boobcap, 1, booblorelist[0], Arrays.copyOfRange(booblorelist, 1, booblorelist.length)));
	}
	
	@Override
	public void onClick(InventoryClickEvent event) {
		
	}
	
	@Override
	public void onClose(InventoryCloseEvent event) {
		// TODO Auto-generated method stub
		
	}

}
