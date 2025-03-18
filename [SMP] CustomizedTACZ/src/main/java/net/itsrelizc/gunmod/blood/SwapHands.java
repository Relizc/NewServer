package net.itsrelizc.gunmod.blood;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;

public class SwapHands implements Listener {
	
	public static Material TACZ_MODERN_KINETIC_GUN = Material.getMaterial("TACZ_MODERN_KINETIC_GUN");
	public static Material RELIZC_MAGAZINE_55645STANAG_30 = Material.getMaterial("RELIZC_MAGAZINE_30_55645_STANAG");
	public static final Material RELIZC_MAGAZINE_17_919_GLOCK_17ROUND = Material.getMaterial("RELIZC_MAGAZINE_17_919_GLOCK_17ROUND");
	
	public static Material TACZ_AMMO = Material.getMaterial("TACZ_AMMO");
	
	private static Map<Player, Map<Integer, ItemStack>> replacer = new HashMap<Player, Map<Integer, ItemStack>>();
	private static Map<Player, Integer> check_id = new HashMap<Player, Integer>();
	
	
	private static Map<Player, Map<Integer, NBTTagList>> data = new HashMap<Player, Map<Integer, NBTTagList>>();
	private static Map<Player, ItemStack> weapon = new HashMap<Player, ItemStack>();
	private static Map<Player, Integer> weapon_slot = new HashMap<Player, Integer>();
	
	private static Map<Player, Boolean> check_replaced = new HashMap<Player, Boolean>();
	
	public static ItemStack getAmmo(Player player, String id) {
		ItemStack item = ItemGenerator.generate(TACZ_AMMO, 60, Locale.get(player, "bullet.dummyammo"), Locale.get(player, "bullet.dummyammo.lore"));
		NBTTagCompound tag = NBT.getNBT(item);
		NBT.setString(tag, "AmmoId", id);
		
		return NBT.setCompound(item, tag);
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
	    
		ItemStack previous = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
		ItemStack current = event.getPlayer().getInventory().getItem(event.getNewSlot());
		
		if (previous != null && previous.getType() == TACZ_MODERN_KINETIC_GUN) {
			
			undoAndDeleteMap(event.getPlayer());
			stopCheck(event.getPlayer());
			
		}
		
		if (current != null && current.getType() == TACZ_MODERN_KINETIC_GUN) {
			
			NBTTagCompound nbt = NBT.getNBT(current);
			String gunId = NBT.getString(nbt, "GunId");
				
			replaceAndPutMap(event.getPlayer(), gunId);
			startCheck(event.getPlayer());
			
		}
		
//		if (current != null && current.getType() == RELIZC_MAGAZINE_55645STANAG_30) {
//			
//			NBTTagCompound shitfuck = NBT.getNBT(current);
//			
//		}
	    
	}
	
	public static void startCheck(Player player) {
		
		int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				check(player);
			}
			
		}, 0, 5L);
		
		check_id.put(player, id);
		
	}
	
	public static void stopCheck(Player player) {
		
		Bukkit.getScheduler().cancelTask(check_id.get(player));
		
	}
	
	public static void check(Player player) {
		
		if (!replacer.containsKey(player)) {
			stopCheck(player);
			return;
		}
		
		for (int slot : replacer.get(player).keySet()) {
			
			ItemStack ammo = player.getInventory().getItem(slot);
//			Bukkit.broadcastMessage("Checking " + slot + " Amount " + ammo.getAmount());
			
			if (ammo.getAmount() != 60) {
//				System.out.println("Found change " + slot);
				undoAndDeleteMap(player, slot);
				complete(player);
				break;
			}
	
			
		}
		
	}
	
	public static void complete(Player player) {
		if (replacer.containsKey(player)) {
			replacer.remove(player);
		};
		
		ItemStack i = player.getItemInHand();
		NBTTagCompound data = NBT.getNBT(i);
		if (NBT.getNBTArray(data, "AmmoContent", NBTTagType.TAG_Compound).size() == 0) {
			NBT.setBoolean(data, "HasBulletInBarrel", false);
		} else {
			NBT.setBoolean(data, "HasBulletInBarrel", true);
		}
		
		NBT.setInteger(data, "GunCurrentAmmoCount", 0);
		i = NBT.setCompound(i, data);
		player.setItemInHand(i);
		
		
		stopCheck(player);
	}
	
	private static class BulletInfo {
		
		public Material magazine;
		public String taczAmmoId;
		
		public BulletInfo(Material mat, String id) {
			magazine = mat;
			taczAmmoId = id;
		}
		
		
	}
	
	private static Map<String, BulletInfo> gunIdToMagazine = new HashMap<String, BulletInfo>();
	
	static {
		
		
		
		gunIdToMagazine.put("tacz:m4a1", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("tacz:hk416d", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("tacz:m16a1", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("tacz:m16a4", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("tacz:scar_l", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("tacz:aug", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:sr16", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:sr15", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:mod2", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:m4urgi", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:m4urgi10", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:m4", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:m727bhd", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:m733", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:m723", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:m727", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:xm177", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:ak101", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:ak102", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:ak19", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:ak202", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		gunIdToMagazine.put("mk16:ak201", new BulletInfo(RELIZC_MAGAZINE_55645STANAG_30, "tacz:556x45"));
		
		gunIdToMagazine.put("tacz:glock_17", new BulletInfo(RELIZC_MAGAZINE_17_919_GLOCK_17ROUND, "tacz:9mm"));
		
	}
	
	public static void replaceAndPutMap(Player player, String gunId) {
		
		Map<Integer, ItemStack> map = new HashMap<Integer, ItemStack>();
		replacer.put(player, map);
		
		
		
		for (int slot = 0; slot < 36; slot ++) {
			
			ItemStack item = player.getInventory().getItem(slot);
			
			if (item != null) {
				
				BulletInfo magtype = gunIdToMagazine.get(gunId);
				
				if (item.getType() == magtype.magazine) {
					replacer.get(player).put(slot, item);
					
					player.getInventory().setItem(slot, getAmmo(player, magtype.taczAmmoId));
				}
				
				
			}
			
		}
		
	}
	
	
	public static void undoAndDeleteMap(Player player) {
		
		undoAndDeleteMap(player, -1);
		
	}
	
	public static void undoAndDeleteMap(Player player, int slot) {
		
		if (!replacer.containsKey(player)) return;
		
		for (Entry<Integer, ItemStack> each : replacer.get(player).entrySet()) {
			if (each.getKey() == slot) {
				player.getInventory().clear(each.getKey());
				giveBackAmmo(player);
				modifyGun(player, each.getValue());
				
			} else {
				player.getInventory().setItem(each.getKey(), each.getValue());
			}
			
		}
		
		
		
		replacer.remove(player);
	}
	
	private static void giveBackAmmo(Player player) {
		
		ItemStack i = player.getItemInHand();
		NBTTagCompound tag = NBT.getNBT(i);
		NBTTagList list = NBT.getNBTArray(tag, "AmmoContent", NBTTagType.TAG_Compound);
		
		int ammoindex = NBT.getInteger(tag, "AmmoIndex");
		
		NBTTagList mag_final = new NBTTagList();
		
		
		if (list.size() == 0) {
			
		} else {
			
			for (int bullet = 0; bullet < ammoindex; bullet ++) {
//				Bukkit.broadcastMessage("ADDING: " + NBT.getCompound(list, bullet));
				NBT.addItem(mag_final, NBT.getCompound(list, bullet));
			}
			
			NBTTagCompound bar = NBT.getCompound(list, list.size() - 1);
			NBT.setInteger(bar, "Count", ammoindex);
			
			NBT.addItem(mag_final, bar);
			
		}
		
		ItemStack mag = new ItemStack(RELIZC_MAGAZINE_55645STANAG_30, 1);
		
		NBTTagCompound item = new NBTTagCompound();
		NBTTagCompound final_tag = new NBTTagCompound();
		
		NBT.setCompound(final_tag, "Items", mag_final);
		NBT.setCompound(item, "Inventory", final_tag);
		
		mag = NBT.setCompound(mag, item);
		
//		Bukkit.broadcastMessage(item.toString());
		
		org.bukkit.util.Consumer<Item> func = (im) -> {
			
//			Bukkit.broadcastMessage(item.getItemStack().getType().toString());
			
			
			NBTTagCompound a1 = NBT.getCompound(im);
			NBTTagCompound a2 = NBT.getCompound(a1, "Item");
			NBTTagCompound a3 = NBT.getCompound(a2, "tag");
			NBTTagCompound a4 = NBT.getCompound(a3, "Inventory");
			NBTTagList a5 = NBT.getNBTArray(a4, "Items", NBTTagType.TAG_Compound);
			
//			Bukkit.broadcastMessage("Before:" + a1.toString());

			// Set Forge -> Item.ForgeCaps.Parent.Items
			NBTTagCompound b1 = new NBTTagCompound();
			NBT.setCompound(b1, "Items", a5);
			
			NBTTagCompound b2 = new NBTTagCompound();
			NBT.setCompound(b2, "Parent", b1);

			NBT.setCompound(a2, "ForgeCaps", b2);
			
			NBT.setCompound(a2, "tag", a3);
			NBT.setCompound(a1, "Item", a2);
			
//			
//			Bukkit.broadcastMessage("After:" + a1.toString());
			
			NBT.setCompound(im, a1);
			
			
			
		};
		
		Item im = player.getWorld().dropItemNaturally(player.getLocation(), mag, func);
		
		// Get Vanilla -> Item.tag.Inventory.Items
		
		
//		NBTTagCompound final_tag = new NBTTagCompound();
		
//		NBT.setInteger(final_tag, "Size", 33);
		
		
//		Bukkit.broadcastMessage(item.toString());
		
//		
//		
//		mag = NBT.setCompound(mag, item);
		
//		Bukkit.broadcastMessage(NBT.getNBT(mag).toString());
		
//		player.getInventory().addItem(mag.clone());
		
	}
	
	private static void modifyGun(Player player, ItemStack loadedAmmo) {
		
		ItemStack i = player.getItemInHand();
		NBTTagCompound tag = NBT.getNBT(i);
		NBTTagCompound ammo = NBT.getNBT(loadedAmmo);
		NBTTagCompound ammo2 = NBT.getCompound(ammo, "Inventory");
		NBTTagList ammo3 = NBT.getNBTArray(ammo2, "Items", NBTTagType.TAG_Compound);
		
		NBTTagCompound ammodata = NBT.getCompound(ammo3, ammo3.size() - 1);
		
		NBT.setCompound(tag, "AmmoContent", ammo3);
		NBT.setInteger(tag, "AmmoIndex", NBT.getInteger(ammodata, "Count"));
		
//		Bukkit.broadcastMessage(ammo.toString());
//		Bukkit.broadcastMessage(ammo2.toString());
//		Bukkit.broadcastMessage(tag.toString());
		
		i = NBT.setCompound(i, tag);
		
		player.setItemInHand(i);
		
		
		
	}

	public static int checkWhichReplaced(Player player, EntitySpawnEvent event) {
//		
		return -1;
																			
	}
	
	public static String nextFire(Player player) {
		ItemStack gun = player.getItemInHand();
		
		NBTTagCompound nbt = NBT.getNBT(gun);
		System.out.println(nbt);
		int index = NBT.getInteger(nbt, "AmmoIndex");
		NBTTagCompound next = NBT.getCompound(NBT.getNBTArray(nbt, "AmmoContent", NBTTagType.TAG_Compound), index);
		NBT.setInteger(nbt, "AmmoIndex", index - 1);
//		NBT.setInteger(nbt, "GunCurrentAmmoCount", Math.max(index, 0));
//		NBT.setInteger(nbt, "HasBulletInBarrel", 0);
		
		ItemStack n = NBT.setCompound(gun, nbt);
		

		player.setItemInHand(n);

		
		
		return next.toString();
	}
	
	@EventHandler
	public void onItemSwapOff(InventoryClickEvent event) {
		
		Player p = (Player) event.getWhoClicked();

	}

	private static void replaceWithDummyAmmo(Player player) {
		
	}
}
