package net.itsrelizc.estate.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.nbt.ChunkMetadata;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class CommandEstateMap extends RelizcCommand {
	
	
	
	private static class MenuTemplateGlobalEstate extends MenuTemplate2 {
		
		private int page = 0;
		private Chunk setting = null;
		
		private void playModSound() {
			
			for (int i = 0; i < 3; i ++) {
				
				TaskDelay.delayTask(new Runnable() {

					@Override
					public void run() {
						getPlayer().playSound(getPlayer(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 2f);
					}
					
				}, 4 * i);
				
			}
			
		}

		public MenuTemplateGlobalEstate(String title) {
			super(title);
		}
		
		@Override
		public void onClick(InventoryClickEvent event) {
			
			ItemStack item = event.getCurrentItem();
			
			if (page == 0) {
				if (event.getSlot() == 44) {
					playModSound();
					return;
					
				}
				
				if (item != null && item.getItemMeta().getDisplayName().startsWith("§7(")) {
					
					String[] pos = item.getItemMeta().getDisplayName().split(", ");
					
					Integer x = Integer.valueOf(pos[0].substring(3));
					Integer y = Integer.valueOf(pos[1].substring(0, pos[1].length() - 1));
					
					Chunk selected = getPlayer().getLocation().getWorld().getChunkAt(x, y);
					
					if (getPlayer().isOp()) {
						if (event.getClick() == ClickType.SWAP_OFFHAND) {
							
							JSONObject obj = JSON.loadDataFromDataBase("chunk_claims/" + ChunkMetadata.get(selected, "relizcPurchasedOwner", PersistentDataType.STRING) + ".json");
							obj.remove(selected.getWorld().getName() + "," + selected.getX() + "," + selected.getZ());
							JSON.saveDataFromDataBase("chunk_claims/" + ChunkMetadata.get(selected, "relizcPurchasedOwner", PersistentDataType.STRING) + ".json", obj);
							
							ChunkMetadata.remove(selected, "relizcPurchasedOwner");
							ChunkMetadata.remove(selected, "chunkPermissionList");
							ChunkMetadata.remove(selected, "chunkPermissionDigList");
							ChunkMetadata.remove(selected, "chunkPermission");
							ChunkMetadata.remove(selected, "chunkPermissionDig");
							
							
							this.menu.getTemplate().apply();
							return;
						}
					}
					
					if (ChunkMetadata.get(selected, "relizcPurchasedOwner", PersistentDataType.STRING)!=null && ChunkMetadata.get(selected, "relizcPurchasedOwner", PersistentDataType.STRING).equals(getPlayer().getUniqueId().toString())) {
						if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
							getPlayer().playSound(getPlayer(), Sound.ENTITY_WOLF_WHINE, 1f, 1f);
							getPlayer().sendMessage("§e" + Locale.get(getPlayer(), "globalestate.purchase.disown"));
							ChunkMetadata.remove(selected, "relizcPurchasedOwner");
							ChunkMetadata.remove(selected, "chunkPermissionList");
							ChunkMetadata.remove(selected, "chunkPermissionDigList");
							ChunkMetadata.remove(selected, "chunkPermission");
							ChunkMetadata.remove(selected, "chunkPermissionDig");
							EstateListener.chunkmaps.get(getPlayer()).remove(selected.getWorld().getName() + "," + selected.getX() + "," + selected.getZ());
							
							this.menu.getTemplate().apply();
						} else {
							playModSound();
							
							page = 1;
							setting = selected;
							this.menu.getTemplate().apply();
							
							return;
						}
						
						return;
					} else if (ChunkMetadata.get(selected, "relizcPurchasedOwner", PersistentDataType.STRING) != null) {
						getPlayer().playSound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0f);
						getPlayer().sendMessage("§c" + Locale.get(getPlayer(), "globalestate.purchase.fail.alreadyowned"));
						return;
					} else {
						ChunkMetadata.set(selected, "relizcPurchasedOwner", PersistentDataType.STRING, event.getWhoClicked().getUniqueId().toString());
						ChunkMetadata.set(selected, "chunkPermissionList", PersistentDataType.STRING, "");
						ChunkMetadata.set(selected, "chunkPermissionDigList", PersistentDataType.STRING, "");
						ChunkMetadata.set(selected, "chunkPermission", PersistentDataType.INTEGER, 0);
						ChunkMetadata.set(selected, "chunkPermissionDig", PersistentDataType.INTEGER, 0);
						
						JSONObject add = new JSONObject();
						add.put("chunkPermissionList", new JSONArray());
						add.put("chunkPermissionDigList", new JSONArray());
						add.put("chunkPermission", 0);
						add.put("chunkPermissionDig", 0);
						EstateListener.chunkmaps.get(getPlayer()).put(selected.getWorld().getName() + "," + selected.getX() + "," + selected.getZ(), add);
						
						getPlayer().playSound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
						getPlayer().sendMessage("§a" + Locale.get(getPlayer(), "globalestate.purchase.sucess"));
						
						this.menu.getTemplate().apply();
						
						return;
					}
					
					
					
				}
			} else if (page == 1) {
				
				if (event.getSlot() == 36) {
					
					getPlayer().playSound(getPlayer(), Sound.BLOCK_CHEST_CLOSE, 1f, 1f);
					
					this.page = 0;
					this.menu.getTemplate().apply();
				} else if (event.getSlot() == 20) {
					if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
						page = 2;
						getPlayer().playSound(getPlayer(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
					} else {
						getPlayer().playSound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
						if (ChunkMetadata.get(setting, "chunkPermission", PersistentDataType.INTEGER) == null) {
							ChunkMetadata.set(setting, "chunkPermission", PersistentDataType.INTEGER, 0);
						}
						int perm = (int) ChunkMetadata.get(setting, "chunkPermission", PersistentDataType.INTEGER);
						ChunkMetadata.set(setting, "chunkPermission", PersistentDataType.INTEGER, (perm + 1) % 4);
						
						JSONObject obj = (JSONObject) EstateListener.chunkmaps.get(getPlayer()).get(setting.getWorld().getName() + "," + setting.getX() + "," + setting.getZ());
						obj.put("chunkPermission", (perm + 1) % 4);
					}
					this.menu.getTemplate().apply();
				} else if (event.getSlot() == 21) {
					if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
						page = 3;
						getPlayer().playSound(getPlayer(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
					} else {
						if (ChunkMetadata.get(setting, "chunkPermissionDig", PersistentDataType.INTEGER) == null) {
							ChunkMetadata.set(setting, "chunkPermissionDig", PersistentDataType.INTEGER, 0);
						}
						getPlayer().playSound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
						int perm = (int) ChunkMetadata.get(setting, "chunkPermissionDig", PersistentDataType.INTEGER);
						ChunkMetadata.set(setting, "chunkPermissionDig", PersistentDataType.INTEGER, (perm + 1) % 4);
						
						JSONObject obj = (JSONObject) EstateListener.chunkmaps.get(getPlayer()).get(setting.getWorld().getName() + "," + setting.getX() + "," + setting.getZ());
						obj.put("chunkPermissionDig", (perm + 1) % 4);
					}
					
					this.menu.getTemplate().apply();
				}
				
			}
			
		}
		
		public void apply_page_0() {
			this.fillAllWith(MenuTemplate2.DIM_GLASS());
			
			Chunk current = this.getPlayer().getLocation().getChunk();
			
			int cx = current.getX();
			int cz = current.getZ();
			
			this.setItem(44, ItemGenerator.generate(Material.COMPASS, 1, Locale.get(getPlayer(), "globalestate.webstore.interactive.settings_all")));
			
			for (int x = -2; x <= 2; x ++) {
				
				for (int y = -2; y <= 2; y ++) {
					
					int dx = x + 2;
					int dy = y + 2;
					
					int slot = 2 + 9 * dy + dx;
					
					int newcx = cx + x;
					int newcz = cz + y;
					
					Chunk next = this.getPlayer().getLocation().getWorld().getChunkAt(newcx, newcz);
					
					String occupier = next.getPersistentDataContainer().getOrDefault(new NamespacedKey(EventRegistery.main, "relizcPurchasedOwner"), PersistentDataType.STRING, null);
					
					boolean abc = false;
					
					if (occupier == null) {
						abc = true;
						occupier = Locale.get(getPlayer(), "globalestate.unoccupied");
					} else {
						OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(occupier));
						occupier = Locale.get(getPlayer(), "globalestate.occupied").formatted(player.getName());
					}
					
					
					
					ItemStack place = null;
					
					if (abc) {
						place = ItemGenerator.generate(Material.GRAY_CONCRETE, 1, Locale.get(getPlayer(), "globalestate.webstore.chunkinfo").formatted(newcx, newcz), 
								occupier,
								" ",
								Locale.get(getPlayer(), "globalestate.webstore.interactive.buy")	
								);
					} else {
						place = ItemGenerator.generate(Material.LIME_CONCRETE, 1, Locale.get(getPlayer(), "globalestate.webstore.chunkinfo").formatted(newcx, newcz), 
								occupier
								);
					}
					
					
					ItemMeta im = place.getItemMeta();
					List<String> lore = im.getLore();
					im.addItemFlags(ItemFlag.values());
					
					if (ChunkMetadata.get(next, "relizcPurchasedOwner", PersistentDataType.STRING) != null) {
						
						if (ChunkMetadata.get(next, "relizcPurchasedOwner", PersistentDataType.STRING).equals(getPlayer().getUniqueId().toString())) {
							lore.add(" ");
							lore.add(Locale.get(getPlayer(), "globalestate.webstore.interactive.settings"));
							lore.add(Locale.get(getPlayer(), "globalestate.webstore.interactive.disown"));
							im.setLore(lore);
						} else {
							place.setType(Material.RED_CONCRETE);
						}
						
						
					} 
					
					
					
					if (x == 0 && y == 0) {
						place.setItemMeta(im);
						
						place.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
						im = place.getItemMeta();
						
						List<String> a = im.getLore();
						
						a.add(" ");
						a.add("§b" + Locale.get(getPlayer(), "globalestate.webstore.current"));
						
						im.setLore(a);
						
						
						
					}
					
					
					
					place.setItemMeta(im);
					
					this.setItem(slot, place);
					
				}
				
			}
		}
		
		private ItemStack perm(String set, ItemStack prev) {
			
			ItemMeta im = prev.getItemMeta();
			List<String> lore = im.getLore();
			
			String[] colors = {"§7", "§7", "§7", "§7"};
			
			if (ChunkMetadata.get(setting, set, PersistentDataType.INTEGER) == null) {
				ChunkMetadata.set(setting, set, PersistentDataType.INTEGER, 0);
			}
			
			int perm = (int) ChunkMetadata.get(setting, set, PersistentDataType.INTEGER);
			colors[perm] = "§a";
			
			lore.add(" ");
			lore.add(Locale.get(getPlayer(), "globalestate.webstore.settings").formatted(colors));
			lore.add(Locale.get(getPlayer(), "globalestate.webstore.interactives.cycle"));
			if (perm == 1) {
				lore.add(Locale.get(getPlayer(), "globalestate.webstore.interactives.list").formatted(Locale.get(getPlayer(), "globalestate.webstore.blacklist")));
			} else if (perm == 2) {
				lore.add(Locale.get(getPlayer(), "globalestate.webstore.interactives.list").formatted(Locale.get(getPlayer(), "globalestate.webstore.whitelist")));
			}
			im.setLore(lore);
			im.addItemFlags(ItemFlag.values());
			prev.setItemMeta(im);
			
			return prev;
		
		}
		
		@Override
		public void apply() {
			
			
			
			if (page == 0) apply_page_0();
			else if (page == 1) apply_page_1();
			
			
		}

		private void apply_page_1() {
			
			this.defaultPreset();
			
			this.setItem(36, ItemGenerator.generate(Material.ARROW, 1, Locale.get(getPlayer(), "menu.back").formatted(Locale.get(getPlayer(), "globalestate.webstore.title"))));
			
			this.setItem(20, perm("chunkPermission", ItemGenerator.generate(Material.CHEST, 1, Locale.get(getPlayer(), "globalestate.webstore.setpermission").formatted(Locale.get(getPlayer(), "globalestate.webstore.interactives")), Locale.get(getPlayer(), "globalestate.webstore.interactives.lore").split("\n"))));
			this.setItem(21, perm("chunkPermissionDig", ItemGenerator.generate(Material.IRON_PICKAXE, 1, Locale.get(getPlayer(), "globalestate.webstore.setpermission").formatted(Locale.get(getPlayer(), "globalestate.webstore.blockbreak")), Locale.get(getPlayer(), "globalestate.webstore.blockbreak.lore").split("\n"))));

			
			
		}
		
	}
	
	public CommandEstateMap() {
		super("estate", "lets u to sit down!");
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
		Menu2 menu = new Menu2(player, 5, new MenuTemplateGlobalEstate(Locale.get(player, "globalestate.webstore.title")));
		menu.open();
		
		
		return true;
		
		
			
	}

}
