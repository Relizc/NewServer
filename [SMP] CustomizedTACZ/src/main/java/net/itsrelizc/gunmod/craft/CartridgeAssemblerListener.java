package net.itsrelizc.gunmod.craft;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class CartridgeAssemblerListener implements Listener {
	
	public static JSONObject database = JSON.loadDataFromDataBase("ballistics.json");
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		
		if (inventory instanceof AnvilInventory) {
			if (event.getRawSlot() == 2) {
				
				ItemStack im = event.getCurrentItem();
				
				if (im != null) {
					
					if (im.getItemMeta().getDisplayName().equalsIgnoreCase("CartridgeAssembler")) {
						StringUtils.systemMessage(player, Locale.get(player, "inventory.anvil"), Locale.get(player, "inventory.anvil.fail.badname"));
						event.setCancelled(true);
					}
					
				}
				
			}
		}
		
//		Bukkit.broadcastMessage(String.valueOf(event.getRawSlot()));
		
		if (player.getOpenInventory().getTitle().equalsIgnoreCase("CartridgeAssembler")) {
			
			if (event.getRawSlot() == 3) {
				ItemStack bullet = event.getInventory().getItem(0);
				ItemStack casing = event.getInventory().getItem(1);
				ItemStack primer = event.getInventory().getItem(2);
				ItemStack powder = event.getInventory().getItem(4);
				
				if (event.getCurrentItem() != null && bullet != null && casing != null && primer != null && powder != null) {
					event.getInventory().getItem(0).setAmount(bullet.getAmount() - 1);
					event.getInventory().getItem(1).setAmount(casing.getAmount() - 1);
					event.getInventory().getItem(2).setAmount(primer.getAmount() - 1);
					event.getInventory().getItem(4).setAmount(powder.getAmount() - 1);
				}
			}
			
			
			
			TaskDelay.delayTask(new Runnable() {

				@Override
				public void run() {
					ItemStack bullet = event.getInventory().getItem(0);
					ItemStack casing = event.getInventory().getItem(1);
					ItemStack primer = event.getInventory().getItem(2);
					ItemStack powder = event.getInventory().getItem(4);
					
					// TODO Auto-generated method stub
//					ChatUtils.systemMessage(player, bullet + " " + casing + " " + primer + " " + powder);
				
					event.getInventory().setItem(3, createBullet(player, bullet, casing, primer, powder));
				}
				
			}, 2L);
			
		}
		
		
		
	}
	
	public static ItemStack createBullet(Player player, ItemStack bullet, ItemStack casing, ItemStack primer, ItemStack powder) {
		
		if (bullet == null || casing == null || primer == null) return null;
		
		String bulletId = bullet.getType().toString();
		String caseId = casing.getType().toString();
		String primerId = primer.getType().toString();
		
		
		System.out.println(bulletId + " " + caseId + " " + primerId);
		System.out.println(bulletId.startsWith("RELIZC_BULLET") + " " + caseId.startsWith("RELIZC_CASE") + " " + primerId.startsWith("RELIZC_PRIMER"));
		
		if (!(bulletId.startsWith("RELIZC_BULLET") && caseId.startsWith("RELIZC_CASE") && primerId.startsWith("RELIZC_PRIMER"))) return null;
		
		// Head Check
		String bullet_caliber = bulletId.split("_")[2];
		String case_caliber = caseId.split("_")[2];
		String primer_caliber = primerId.split("_")[2];
		
		System.out.println(bullet_caliber + " " + case_caliber + " " + primer_caliber + " " + (bullet_caliber == primer_caliber && primer_caliber == case_caliber));
		
		String case_texture = caseId.split("_")[3];
		String bullet_texture = bulletId.split("_")[3];
		String primer_texture = primerId.split("_")[3];
		
		if (!(bullet_caliber.equals(primer_caliber) && primer_caliber.equals(case_caliber))) return null;
		
		double propellent_requirement = (double) ((JSONObject) database.get(primer_caliber)).get("base");
		if (powder.getAmount() < propellent_requirement) return null;
		
		JSONObject propel_info = (JSONObject) database.getOrDefault(powder.getType().toString(), null);
		if (propel_info == null) return null;
		double powder_j = (double) propel_info.get("base");
		
		double primer_j = (double) ((JSONObject) database.get(primerId)).get("base");
		
		JSONObject bullet_info = (JSONObject) database.get(bulletId);
		
		
//		double final_j = (powder_j * powder.getAmount()) * primer_j;
		double final_j = (powder_j * 1) * primer_j;
		double min_j = (double) bullet_info.get("min_j");
		double max_j = (double) ((JSONObject) database.get(caseId)).get("base");
		
		
		
		String created = "RELIZC_CARTRIDGE_" + bullet_caliber + "_" + case_texture + "_" + bullet_texture;
		
		
		
		Material wanted = Material.getMaterial(created);
		
//		Bukkit.broadcastMessage(wanted.toString());
		
		if (wanted == null) return null;
		
		ItemStack generated = generateBullet(player, wanted, bullet_caliber, case_texture, bullet_texture, primer_texture, (int) min_j, (int) max_j, (int) final_j);
		
		
		
		return generated;
	}
	
	private static ItemStack generateBullet(Player player, Material mat, String bullet_caliber, String case_texture, String bullet_texture, String primer_texture, int min_j, int max_j, int final_j) {
		
		String bulletName = Locale.get(player, "bullet." + bullet_texture.toLowerCase());
		String primerName = Locale.get(player, "primer." + primer_texture.toLowerCase());
		String caseName = Locale.get(player, "case." + case_texture.toLowerCase());
		
		ItemStack it = ItemGenerator.generateLore(mat, 1, 
				"§f" + Locale.get(player, "cartridge.model").formatted(bulletName, caseName, primerName),
				"§f" + Locale.get(player, "cartridge.energy").formatted((float) final_j),
				" ",
				"§f" + Locale.get(player, "bullet.description." + bullet_caliber + "." + bullet_texture.toLowerCase()),
				" ",
				"§7§o\"马上就要发射了!!!\"");
		
		NBTTagCompound nbt = NBT.getNBT(it);
		NBT.setInteger(nbt, "energy", final_j);
		
		
		return NBT.setCompound(it, nbt);
		
	}

}
