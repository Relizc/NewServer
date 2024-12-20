package net.itsrelizc.smp.modsmp.menus;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.menus.ClassicMenu;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.MenuTemplate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Contract extends MenuTemplate implements Listener {
	
	private ClassicMenu menu;
	private boolean sign;
	
	@Override
	public void loadTemplate(ClassicMenu classicMenu) {
		
		
		classicMenu.fillEmpty();
		classicMenu.putClose();
		
		this.menu = classicMenu;
		
		classicMenu.setItem(13, book_item());
		
		classicMenu.setItem(29, no());
		classicMenu.setItem(33, yes());
		
		
	}
	
	@Override
	public boolean onClose(InventoryCloseEvent event) {
		
		if (!sign) {
			Player player = (Player) event.getPlayer();
			player.kickPlayer("§c您必须同意服务器的条款才能进行游戏！");
			
			
		}
		
		return false;
		
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		
		if (event.getRawSlot() == 29) this.menu.holder.closeInventory();
		if (event.getRawSlot() == 33) agree();
		
		return true;
		
	}
	
	private void agree() {
		JSONObject content = JSON.loadDataFromDataBase("contract_signed.json");
		JSONArray agrees = (JSONArray) content.get("agree");
		
		agrees.add(this.menu.holder.getUniqueId().toString());
		
		content.put("agree", agrees);
		
		JSON.saveDataFromDataBase("contract_signed.json", content);
		
		this.sign = true;
		this.menu.holder.closeInventory();
	}
	
	private static boolean signed(Player player) {
		JSONObject content = JSON.loadDataFromDataBase("contract_signed.json");
		JSONArray agrees = (JSONArray) content.get("agree");
		
		return agrees.contains(player.getUniqueId().toString());
	}
	
	

	private static ItemStack book_item() {
		String[] contents = {
				"§8§m-------------------------------",
				"§d1.§f §c严禁§f发布§e人身攻击§f、§e个人骚扰§f、以及§e不法言论§f。",
				"§d2.§f §c严禁§f使用§b外挂§f或任何§b破坏游戏平衡§f的§c第三方软件§f。",
				"§d3.§f §c严禁§f以§6恳求§f、§6道德绑架§f、§6贿赂§f、等§6腐败§f的方式向§a管理员§f获得§e利益§f。",
				"§d4.§f 尽量避免政治、宗教、以及种族等话题。",
				"§d5.§f 在发送任何链接前，请确定其是否安全。",
				" ",
				"§c请注意: 惩罚方式包括但不限于警告、禁言、以及封禁。请为您的行为负责。"
		};
		ItemStack item = ItemGenerator.generate(Material.ENCHANTED_BOOK, 1, "§a服务器条款", contents);
		
		return item;
	}
	
	private static ItemStack no() {
		ItemStack item = ItemGenerator.generate(Material.RED_CONCRETE, 1, "§c我不同意以上条款");
		
		return item;
	}
	
	private static ItemStack yes() {
		String[] contents = {
				" ",
				"§e请注意: 当您点击此处，则代表服务器管理员有权利以上方的条款决定您的惩罚方式。"
		};
		ItemStack item = ItemGenerator.generate(Material.LIME_CONCRETE, 1, "§a我同意以上条款", contents);
		
		return item;
	}
	
	public static void enable(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(new Contract(), plugin);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		if (!signed(event.getPlayer())) {
			ClassicMenu menu = new ClassicMenu(event.getPlayer(), 5, "服务器条款", new Contract());
			menu.show();
		}
		
		
	}

}
