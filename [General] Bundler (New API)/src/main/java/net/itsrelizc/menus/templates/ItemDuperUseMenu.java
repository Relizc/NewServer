package net.itsrelizc.menus.templates;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.EntitySongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import net.itsrelizc.menus.ClassicMenu;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.MenuTemplate;
import net.itsrelizc.string.ChatUtils;

public class ItemDuperUseMenu extends MenuTemplate {
	
	private boolean ok = true;
	private ClassicMenu menu;
	private int randomevent;
	
	private ItemStack item;
	private EntitySongPlayer esp;
	private int soundeffect;
	
	private int copyprogress = -1;
	private int update;
	
	private int took = 0;
	private boolean failed = false;
	private boolean started = false;
	private boolean cheater = false;
	
	@Override
	public void loadTemplate(ClassicMenu classicMenu) {
		
		
		classicMenu.fillEmpty();
		classicMenu.leaveMiddleAreaWithDimGlass();
		classicMenu.putClose();
		
		this.menu = classicMenu;
		
		ItemStack directions = ItemGenerator.generate(Material.OAK_SIGN, 1, "§a贾斯丁复制器™使用说明说", 
				"§7§m-------------------",
				"§71. §b将您要§e复制的物品§f放置于§d左侧§f的§6熔炉",
				"§72. §f点击§b中间§f的§e复制器§f标志",
				"§73. §f等待§e5-60秒§f即可复制完毕！",
				"",
				"§c注意：若在复制完成之前离开此页面，则复制无效，并无法退款！");
		
		classicMenu.setItem(4, directions);
		classicMenu.setItem(20, ItemGenerator.generate(Material.FURNACE, 1, "§b物品放置处"));
		classicMenu.setItem(22, ItemGenerator.generate(Material.SCULK_SHRIEKER, 1, "§a点我开始复制"));
		
		final Material[] l = {
				Material.RED_STAINED_GLASS_PANE,
				Material.ORANGE_STAINED_GLASS_PANE,
				Material.YELLOW_STAINED_GLASS_PANE,
				Material.LIME_STAINED_GLASS_PANE,
				Material.GREEN_STAINED_GLASS_PANE,
				Material.LIGHT_BLUE_STAINED_GLASS_PANE,
				Material.BLUE_STAINED_GLASS_PANE,
				Material.PURPLE_STAINED_GLASS_PANE,
				Material.MAGENTA_STAINED_GLASS_PANE,
		};
		
		this.randomevent = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				
				int i =new Random().nextInt(l.length);
				
				classicMenu.setItem(24, ItemGenerator.generate(l[i], 1, " "));
				
			}
			
		}, 0, 5L);
		
		
	}
	
	public void complete() {
		
		if (esp != null) {
			if (esp.isPlaying()) {
				esp.setPlaying(false);
				esp.destroy();
			}
		}
		
		this.menu.holder.playSound(this.menu.holder.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0f);
		ChatUtils.systemMessage(this.menu.holder, "§e§l物品复制器", "§a复制完毕！");
		
		
		this.menu.setItem(24, this.item);
		ok = false;
		
		Player holder = this.menu.holder;
		
		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 0.707107f);
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 0.890899f);
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 1.059463f);
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 1.414214f);
				
			}
			
		}, 12L);
		
		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 0.707107f);
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 0.890899f);
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 1.059463f);
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 10f, 1.414214f);
				
			}
			
		}, 14L);
	}
	
	public void fail() {
		
		if (esp != null) {
			if (esp.isPlaying()) {
				esp.setPlaying(false);
				esp.destroy();
			}
		}
		
		int slots[] = {5, 6, 7, 8, 17, 26, 35, 44, 43, 42, 41, 39, 38, 37, 36, 27, 18, 9, 0, 1, 2, 3};
		for (int i = 0; i < 22; i ++) {
			this.menu.setItem(slots[i], ItemGenerator.generate(Material.RED_STAINED_GLASS_PANE, 1, " "));
		}
		
		ChatUtils.systemMessage(this.menu.holder, "§e§l物品复制器", "§c复制失败！你当我是阿拉丁的精灵？？");
		ChatUtils.systemMessage(this.menu.holder, "§e§l物品复制器", "§c另外，您原来的§e物品复制器§c被没收了");
		
		
		this.menu.setItem(24, ItemGenerator.generate(Material.BARRIER, 1, "§c复制失败！"));
		this.menu.setItem(20, ItemGenerator.generate(Material.BARRIER, 1, "§c复制失败！"));
		failed = true;
		Player holder = this.menu.holder;
		
		holder.playSound(holder.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10f, 0f);
		
		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				
				holder.playSound(holder.getLocation(), Sound.ENTITY_WOLF_WHINE, 10f, 0.5f);
				
				
				
			}
			
		}, 20L);
		
	}
	
	public void start() {
		
		started = true;
		
		int slots[] = {5, 6, 7, 8, 17, 26, 35, 44, 43, 42, 41, 39, 38, 37, 36, 27, 18, 9, 0, 1, 2, 3};
		final Player holder = this.menu.holder;
		final ClassicMenu menu = this.menu;
		
		menu.setItem(24, ItemGenerator.generate(Material.FURNACE, 1, "§a正在复制..."));
		
		
		this.soundeffect = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				
				holder.playSound(holder.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.5f, (float) ((float) (copyprogress / 22.0) * 2 + 0.5));
				
			}
			
		}, 0, 2L);
		
		final ItemDuperUseMenu accessmenu = this;
		
		this.update = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				
				
				
				if (Math.random() < 0.3) {
					
					if (accessmenu.item.getItemMeta().getDisplayName().startsWith("§e物品复制器")) {
						if (Math.random() < (copyprogress / 20.0)) {
							
							Bukkit.getScheduler().cancelTask(update);
							Bukkit.getScheduler().cancelTask(soundeffect);
							fail();
							
							return;
							
						}
					}
					
					copyprogress ++;
					
					if (copyprogress == 22) {
						Bukkit.getScheduler().cancelTask(update);
						Bukkit.getScheduler().cancelTask(soundeffect);
						menu.setItem(3, ItemGenerator.generate(Material.LIME_STAINED_GLASS_PANE, 1, " "));
						complete();
						return;
					}
					
					menu.setItem(slots[copyprogress], ItemGenerator.generate(Material.GREEN_STAINED_GLASS_PANE, 1, " "));
					
					if (copyprogress > 0) {
						menu.setItem(slots[copyprogress - 1], ItemGenerator.generate(Material.LIME_STAINED_GLASS_PANE, 1, " "));
					}
					
					
				}
				
				
				
			}
			
		}, 0, 10L);
		
	}

	@Override
	public boolean onClick(InventoryClickEvent event) {
		
		if (failed) {
			return true;
		}
		
		if (!ok) {
			if (event.getRawSlot() != 20 && event.getRawSlot() != 24) {
				return true;
			}
			
			if (event.getCursor().getType() != Material.AIR) {
				return true;
			}
			
			took ++;
			
			return false;
		}
		
		if (this.esp != null && this.esp.isPlaying()) {
			event.setCancelled(true);
			return true;
		}
		
		System.out.println(event.getCursor());
		
		if (event.getRawSlot() > 44) {
			return false;
		}
		
		if (event.getRawSlot() == 20) {
			
			
			
			if (this.item == null) {
				if (event.getCursor().getType() == Material.AIR) return true;
				this.item = event.getCursor().clone();
				this.menu.setItem(20, item);
				
				event.getCursor().setAmount(0);
				
				((Player) event.getWhoClicked()).playSound(((Player) event.getWhoClicked()).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1);
			} else {
				event.getWhoClicked().getInventory().addItem(this.item);
				this.item = null;
				this.menu.setItem(20, ItemGenerator.generate(Material.FURNACE, 1, "§b物品放置处"));
				
				((Player) event.getWhoClicked()).playSound(((Player) event.getWhoClicked()).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 0);
			}
			
			return true;
		} else if (event.getRawSlot() == 22) {
			
			if (started) return true;
			
			try {
				Bukkit.getScheduler().cancelTask(randomevent);
			} catch (Exception e) {
				
			}
			
			ChatUtils.systemMessage((Player) event.getWhoClicked(), "§e§l物品复制器", "§a正在复制，请稍等！");
			
			String names[] = {"Sweet Sweet Canyon", "Rude Buster"};
			int name = new Random().nextInt(names.length);

			
			Song song = NBSDecoder.parse(new File("D:\\ServerData\\NBSFiles\\" + names[name] + ".nbs")); // Preloaded song
			// Create EntitySongPlayer.
			this.esp = new EntitySongPlayer(song);
			// Set entity which position will be used
			esp.setEntity(event.getWhoClicked());
			// Set distance from target location in which will players hear the SongPlayer
			esp.setDistance(16); // Default: 16
			// Add player to SongPlayer so he will hear the song.
			for (Player player : Bukkit.getOnlinePlayers()) {
				esp.addPlayer(player);
			}
			// Start RadioSongPlayer playback
			esp.setPlaying(true);
			
			start();
		}
		
		return true;
		
	}

	@Override
	public boolean onClose(InventoryCloseEvent event) {
		
		if (failed ) {
			return false;
		}
		
		if (ok) {
			
			ChatUtils.systemMessage(this.menu.holder, "§e§l物品复制器", "§7§o看起来你还没有使用复制器！不过不好意思，我不能给你退款...");
			try {
				Bukkit.getScheduler().cancelTask(randomevent);
			} catch (Exception e) {
				
			}
			
		} else {
			
			for (int i = 0; i < (2 - took); i ++) {
				this.menu.holder.getInventory().addItem(this.item);
			}
			
			if ((2 - took) > 0) {
				ChatUtils.systemMessage(this.menu.holder, "§e§l物品复制器", "§a您忘记拿了" + (2 - took) + "个物品，我帮你放进背包里了！");
			}
			
		}
		
		if (esp != null) {
			if (esp.isPlaying()) {
				esp.setPlaying(false);
				esp.destroy();
			}
		}
		
		
		return false;
	}
	
}