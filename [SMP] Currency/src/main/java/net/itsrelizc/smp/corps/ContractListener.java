package net.itsrelizc.smp.corps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.menus.Menu2;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.corps.Contract.Agreement;
import net.itsrelizc.smp.corps.Contract.Party;
import net.itsrelizc.smp.corps.menus.MenuContractSign;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class ContractListener implements Listener {
	
	@EventHandler
	public void pickup(PlayerPickupItemEvent event) {
		
		generate(event.getItem().getItemStack(), event.getPlayer());
		
	}
	
	@EventHandler
	public void rightClick(PlayerInteractEvent event) {
		
		if (event.getHand() == EquipmentSlot.OFF_HAND) return;
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (event.getPlayer().getItemInHand() != null) {
				
				CompoundTag tag = NBT.getNBT(event.getPlayer().getItemInHand());
				
				if (tag == null) return;
				
				
				if (NBT.getInteger(tag, "partyAmount") != 0) {
					
					
					ListTag parties = NBT.getNBTArray(tag, "party", NBTTagType.TAG_Compound);
					for (int i = 0; i < parties.size(); i ++) {
						String content = NBT.getString(NBT.getCompound(parties, i), "party");
						
//						System.out.println(content);
//						System.out.println(event.getPlayer().getUniqueId().toString());
						
						if (content.equals(event.getPlayer().getUniqueId().toString())) {
							StringUtils.systemMessage(event.getPlayer(), Locale.get(event.getPlayer(), "contract.name"), "§a" + Locale.get(event.getPlayer(), "contract.already_signed"));
							return;
						}
					}
					
					Menu2 signMenu = new Menu2(event.getPlayer(), 5, new MenuContractSign(Locale.get(event.getPlayer(), "menu.contract.title"), event.getPlayer().getItemInHand()));
					signMenu.open();
				}
				
			}
			
		}
	}
	
	public static void generate(ItemStack item, Player player) {
		
		CompoundTag tag = NBT.getNBT(item);
		if (tag == null) return;
		
		if (NBT.getInteger(tag, "partyAmount") == null) return;

		CorporateBusiness creator = CorporateBusiness.getByID(NBT.getString(tag, "creator"));

		ItemMeta im = item.getItemMeta();
		
		
		
		long number = NBT.getLong(tag, "number");
		
		im.setDisplayName("§e"+Locale.get(player, "item.contract.name").formatted(number));
		
		List<String> lore = StringUtils.fromNewList();
		int p = NBT.getInteger(tag, "partyAmount");
		
		ListTag party = NBT.getNBTArray(tag, "party", NBTTagType.TAG_Compound);
		lore.add(Locale.get(player, "contract.parties").formatted(party.size(), p));	
		
		lore.add(" ");
		
		lore.add("§b" + Locale.get(player, "contract.partymembers"));
		
		Map<String, CorporateBusiness> currentbusinesses = new HashMap<String, CorporateBusiness>();
		Map<String, Integer> order = new HashMap<String, Integer>();
		
		
		for (int i = 1; i <= party.size(); i ++) {
			
			CompoundTag party2 = (CompoundTag) party.get(i - 1);
			int type = NBT.getInteger(party2, "type");
			boolean sealed = NBT.getBoolean(party2, "sealed");
			
			if (type == 0) {
				String name = NBT.getString(party2, "party");
				name = Bukkit.getOfflinePlayer(UUID.fromString(name)).getName();
				lore.add("§7• [" + Locale.get(player, "contract.individual") + "] " + name + " " + Party.getLocalizedSealedString(sealed, player, type));
				
				currentbusinesses.put(name, null);
				order.put(name, i);
				
			} else {
				String name = NBT.getString(party2, "party");
//				Bukkit.broadcastMessage(party2.toString());
				CorporateBusiness current = CorporateBusiness.getByID(name);
				
				currentbusinesses.put(name, current);
				order.put(name, i);
				
				lore.add("§7• " + current.getLoreName(player) + " " + Party.getLocalizedSealedString(sealed, player, type));
			}
			
			
		}
		
		lore.add(" ");
		lore.add("§b" + Locale.get(player, "contract.expires"));
		
		ListTag expire = NBT.getNBTArray(tag, "expire", NBTTagType.TAG_Compound);
		
		for (int i = 0; i < expire.size(); i ++) {
			CompoundTag party1 = (CompoundTag) expire.get(i);
			int type = NBT.getInteger(party1, "type");
			String value = NBT.getString(party1, "value");
			
			if (type == 0) {
				lore.add("§7• " + Locale.get(player, "contract.force_majeure"));
			} else if (type == 1) {
				lore.add("§7• " + Locale.get(player, "contract.expire.either_breach"));
			} else {
				lore.add("§7• " + creator.getLocale(player, value));
			}
		}
		
		lore.add(" ");
		lore.add("§b" + Locale.get(player, "contract.agreement"));
		ListTag agreements = NBT.getNBTArray(tag, "agreement", NBTTagType.TAG_Compound);
		for (int i = 0; i < agreements.size(); i ++) {
			CompoundTag agree = (CompoundTag) agreements.get(i);
			
			String who = NBT.getString(agree, "party");
			String content = NBT.getString(agree, "value");
			
			String promise = Agreement.parseParties(creator.getLocale(player, content), player, p, currentbusinesses, order);
			lore.add("§7• " + promise);
			
//			if (type == 0) {
//				lore.add("§7• " + Locale.get(player, "contract.force_majeure"));
//			} else if (type == 1) {
//				lore.add("§7• " + Locale.get(player, "contract.expire.either_breach"));
//			} else {
//				lore.add("§7• " + creator.getLocale(player, value));
//			}
		}
		
		im.setLore(lore);
		item.setItemMeta(im);
	}

}
