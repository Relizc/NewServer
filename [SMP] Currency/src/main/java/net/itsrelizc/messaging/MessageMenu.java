package net.itsrelizc.messaging;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.menus.Skull;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.LocaleSession;

public class MessageMenu extends MenuTemplate2 {
	
	private LocaleSession loc;
	private byte selection;
	private int page = 0;

	public MessageMenu(Player player) {
		
		super(Locale.a(player, "messages.menu"));
		loc = new LocaleSession(player);
		icon = new ItemStack[]{
				ItemGenerator.generate(Material.COOKIE, 1, loc.a("messages.general.icon"), loc.a("messages.general.icon.lore").split("\n")),
				ItemGenerator.generate(Material.EMERALD, 1, loc.a("messages.trades.icon"), loc.a("messages.trades.icon.lore").split("\n")),
				ItemGenerator.generate(Skull.getPlayerSkull(player.getName()), 1, loc.a("messages.social.icon"), loc.a("messages.social.icon.lore").split("\n")),
				ItemGenerator.generate(Material.ANVIL, 1, loc.a("messages.security.icon"), loc.a("messages.security.icon.lore").split("\n"))
		};
		// TODO Auto-generated constructor stub
		
	}
	
	private ItemStack[] icon;
	
	private String[] conv = {
		"general",
		"trades",
		"social",
		"security"
	};
	
	public void drawCurrentSelection() {
		for (byte i = 0; i <= 3; i ++) {
			int slot = 10 + i * 9;
			Material mat;
			if (i == selection) {
				mat = Material.LIME_STAINED_GLASS_PANE;
			} else {
				mat = Material.GRAY_STAINED_GLASS_PANE;
			}
			int amt = Messaging.getMessageCount(getPlayer(), conv[i]);
			ItemStack it = icon[i].clone();
			ItemMeta im = it.getItemMeta();
			im.setDisplayName(im.getDisplayName() + " §7(" + amt + ")");
			it.setItemMeta(im);
			this.setItem(slot - 1, it);
			this.setItem(slot, ItemGenerator.generate(mat, 1, " "));
		}
	}
	
	private void setSelection(byte b) {
		if (selection == b) return;
		selection = b;
		drawCurrentSelection();
		
		page = 0;
		
		this.getPlayer().playSound(getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
	}
	
	private int indexToSlot() {
		return 0;
	}
	
	private static int getMaxPages(int totalMessages) {
        int messagesPerPage = 24;
        // Divide and round up to handle remaining messages on the last page
        return (totalMessages + messagesPerPage - 1) / messagesPerPage;
    }
	
	private static int mapSlot(int logicalSlot) {
        if (logicalSlot < 0 || logicalSlot > 23) {
            throw new IllegalArgumentException("Slot must be between 0 and 23");
        }

        // Each row has 6 slots, starting at 11, 20, 29, 38
        int row = logicalSlot / 6; // which row
        int col = logicalSlot % 6; // which column in the row
        int startSlot = 11 + row * 9; // starting inventory slot of that row

        return startSlot + col;
    }
	
	public static int inverseMapSlot(int actualSlot) {
        if (actualSlot < 11 || actualSlot > 43) {
            return -1;
        }
        int row = (actualSlot - 11) / 9;
        int col = (actualSlot - 11) % 9;
        if (col > 5) { // invalid slot in our mapping
            return -1;
        }
        return row * 6 + col;
    }
	
	private void renderMails() {
		List<Messaging.Message> msg = Messaging.getMessages(getPlayer(), conv[selection]);
		
		//int pages = getMaxPages(msg.size());
		int n = 0;
		for (int i = 24 * page; i < Math.min(24 * (page +1), msg.size()); i ++) {
			Messaging.Message current = msg.get(i);
			ItemStack it = current.convertToItemStack(getPlayer());
			this.setItem(mapSlot(n), it);
			n ++;
		}
		
	}
	
	@Override
	public void onClick(InventoryClickEvent event) {
		
		int slot = event.getSlot();
		if (!(slot >= 0 && slot <= 6 * 9)) return;
		
		if (slot == 9 || slot == 10) {
			setSelection((byte) 0);
		} else if (slot == 18 || slot == 19) {
			setSelection((byte) 1);
		} else if (slot == 27 || slot == 28) {
			setSelection((byte) 2);
		} else if (slot == 36 || slot == 37) {
			setSelection((byte) 3);
		}
		
		int msgClicked = inverseMapSlot(slot);
		if (msgClicked != -1 ) {
			Messaging.Message msg = Messaging.getMessages(getPlayer(), conv[selection]).get(24 * page + msgClicked);
			
			if (msg.read) return;
			
			Messaging.markAsRead(getPlayer(), conv[selection], msg.id);
			msg.read = true;
			
			if (msg.hasItems()) {
				this.getPlayer().playSound(getPlayer(), Sound.BLOCK_CHEST_CLOSE, 1f, 1f);
				this.getPlayer().playSound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
				Messaging.clearItemsFromMessage(getPlayer(), conv[selection], msg.id);
			} else {
				this.getPlayer().playSound(getPlayer(), Sound.ITEM_BOOK_PAGE_TURN, 2f, 1f);
			}
			
			this.renderMails();
		}
	}
	
	@Override
	public void onClose(InventoryCloseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void apply() {
		
		
		
		this.stashPreset();
		this.setItem(4, ItemGenerator.generate(Material.WRITABLE_BOOK, 1, loc.a("messages.menu.icon.name"),loc.a("messages.menu.icon.lore").split("\n")));
		
		this.drawCurrentSelection();
		this.renderMails();
	}
	
	

}
