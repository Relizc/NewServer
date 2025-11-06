package net.itsrelizc.dimensions.end;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.market.TradeUtils;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.menus.Skull;
import net.itsrelizc.players.locales.Locale;

import net.itsrelizc.string.StringUtils;

public class ToTheEndMenu extends MenuTemplate2 {

	public ToTheEndMenu(Player title) {
		super(Locale.a(title, "menu.end.title"));
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void apply() {
		ItemStack head = Skull.getCustomSkull("https://textures.minecraft.net/texture/79c819b51b4b8389a9e5dfb891dc6476458a1e62929b95eef799586650d19baf");
		
		String[] headlore = Locale.a(getPlayer(), "menu.end.head").split("\n");
		
		
		ItemStack formattedHead = ItemGenerator.generate(head, 1, headlore[0], Arrays.copyOfRange(headlore, 1, headlore.length - 1));
		
		this.defaultPreset();
		this.setItem(4, formattedHead);
		
		String[] portallore = Locale.a(getPlayer(), "menu.end.portal").split("\n");
		
		String a = StringUtils.wrapWithColor(portallore[1], Locale.getLanguage(getPlayer()));
		a += "\n" + portallore[2];
		a += "\n" + portallore[3];
		a += "\n" + portallore[4];
		
		this.setItem(22, ItemGenerator.generate(Material.END_PORTAL_FRAME, 1, portallore[0], a.split("\n")));
		
		String buylore = Locale.a(getPlayer(), "menu.end.buylore");
		String[] loresplit = buylore.split("\n");
		String desc = loresplit[1];
		String desc2 = loresplit[2];
		String fin = StringUtils.wrapWithColor(desc, Locale.getLanguage(getPlayer())) + "\n\n" + StringUtils.wrapWithColor(desc2, Locale.getLanguage(getPlayer()));
		String payWithMeth = fin + TradeUtils.SHOP_DASH_OF(getPlayer()) + "\n" + StringUtils.quantify(2, ItemUtils.createItem(RelizcItemMeth.class, getPlayer()).getBukkitItem().getItemMeta().getDisplayName());
		String payWithEms = fin + TradeUtils.SHOP_DASH_OF(getPlayer()) + "\n" + StringUtils.quantifyCurrency(500);
		
		this.setItem(29, ItemGenerator.generate(Material.EMERALD, 1, loresplit[0], payWithMeth.split("\n")));
		
		this.setItem(33, ItemGenerator.generate(Material.EMERALD, 1, loresplit[0], payWithEms.split("\n")));
	}
	
	@Override
	public void onClick(InventoryClickEvent event) {
		
	}

}
