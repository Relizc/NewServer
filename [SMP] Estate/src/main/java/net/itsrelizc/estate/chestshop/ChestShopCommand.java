package net.itsrelizc.estate.chestshop;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class ChestShopCommand extends RelizcCommand {

	public ChestShopCommand() {
		super("chestshop", "opens the chest shop");
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		Player player = (Player) sender;
		
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
	       
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("BookUpdateBook");
        meta.setAuthor("Relizc");
        
        String[] pages = Locale.get(player, "chestshop.tutorial").split("\r");
        meta.addPage(pages);
        
        book.setItemMeta(meta);
        
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book);
        PacketContainer pc = pm.createPacket(PacketType.Play.Server.OPEN_BOOK);
        try
        {
            pm.sendServerPacket(player, pc);
        } catch (InvocationTargetException e)
        {
            throw new RuntimeException("Cannot send open book packet " + pc, e);
        }
        player.getInventory().setItem(slot, old);
        
        player.getInventory().setItem(slot, old);
		
		return true;
		
	}

}
