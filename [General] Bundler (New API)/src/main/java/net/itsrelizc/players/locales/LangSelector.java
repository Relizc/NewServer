package net.itsrelizc.players.locales;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.menus.ClassicMenu;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.MenuTemplate;
import net.itsrelizc.menus.Skull;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.string.ChatUtils;

public class LangSelector extends RelizcCommand {
	
	private static String helper_coverage(Language code) {
		float res = Locale.getLinesAmount(code) / (float) Locale.getLinesAmount(Language.ZH_CN);
		res = res * 100;
		if (res > 80) {
			return "§a%.2f".formatted(res);
		} else if (res > 60) {
			return "§e%.2f".formatted(res);
		} else if (res > 40) {
			return "§6%.2f".formatted(res);
		} else {
			return "§c%.2f".formatted(res);
		}
	}
	
	private static class LangTemplate extends MenuTemplate {
		
		@Override
		public boolean onClick(InventoryClickEvent event) {
			
			if (event.getRawSlot() == 10) {
				event.getWhoClicked().sendMessage("§a您的语言以切换至简体中文！");
				event.getWhoClicked().sendMessage("§a部分显示需要重新连接服务器才能刷新！");
				Profile.findByOwner((Player) event.getWhoClicked()).lang = Language.ZH_CN;
				
			} else if (event.getRawSlot() == 11) {
				event.getWhoClicked().sendMessage("§a您的語言以切換至繁體中文！");
				event.getWhoClicked().sendMessage("§a部分顯示需要重新連接伺服器才能刷新！");
				Profile.findByOwner((Player) event.getWhoClicked()).lang = Language.ZH_TW;
			} else if (event.getRawSlot() == 12) {
				event.getWhoClicked().sendMessage("§aYour language has now been set to English (US)!");
				event.getWhoClicked().sendMessage("§aSome displays require re-logging in the server to change!");
				Profile.findByOwner((Player) event.getWhoClicked()).lang = Language.EN_US;
			} else if (event.getRawSlot() == 13) {
				event.getWhoClicked().sendMessage("§a言語が日本語に設定されました。");
				event.getWhoClicked().sendMessage("§a一部の表示を変更するには、サーバーに再度ログインする必要があります。");
				Profile.findByOwner((Player) event.getWhoClicked()).lang = Language.JA_JP;
			}
			
			event.getWhoClicked().closeInventory();
			
			Locale.changeLanguage((Player) event.getWhoClicked(), Profile.findByOwner((Player) event.getWhoClicked()).lang);
			
			return true;
			
			
		}

		@Override
		public boolean onClose(InventoryCloseEvent event) {
			// TODO Auto-generated method stub
			return true;
		}
		
		

		@Override
		public void loadTemplate(ClassicMenu menu) {
			menu.fillEmpty();
			menu.leaveMiddleArea();
			menu.putClose();
			
			ItemStack chinese = Skull.getCustomSkull("https://textures.minecraft.net/texture/7f9bc035cdc80f1ab5e1198f29f3ad3fdd2b42d9a69aeb64de990681800b98dc");
			ItemStack chinese_trad = Skull.getCustomSkull("https://textures.minecraft.net/texture/7f9bc035cdc80f1ab5e1198f29f3ad3fdd2b42d9a69aeb64de990681800b98dc");
			ItemStack eng = Skull.getCustomSkull("https://textures.minecraft.net/texture/cd91456877f54bf1ace251e4cee40dba597d2cc40362cb8f4ed711es50b0be5b3");
			ItemStack japan = Skull.getCustomSkull("https://textures.minecraft.net/texture/d640ae466162a47d3ee33c4076df1cab96f11860f07edb1f0832c525a9e33323");
			
			menu.setItem(10, ItemGenerator.generate(chinese, 1, "§a中文 (简体)", "§7语言完整性§8: §a100%"));
			
			
			menu.setItem(11, ItemGenerator.generate(chinese_trad, 2, "§a中文 (繁體)", "§7語言完整性§8: " + helper_coverage(Language.ZH_TW) + "%"));
			menu.setItem(12, ItemGenerator.generate(eng, 3, "§aEnglish (US)", "§7Language Coverage§8: "  + helper_coverage(Language.EN_US) + "%"));
			menu.setItem(13, ItemGenerator.generate(japan, 3, "§a日本語　(日本)", "§7言語の整合性§8: " + helper_coverage(Language.JA_JP) + "%"));
		}
		
	}

	public LangSelector() {
		super("lang", "select");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
		ClassicMenu menu = new ClassicMenu(player, 3, Locale.get(player, "general.selectlang"), new LangTemplate());
		
		
		
		
		menu.show();
		
		return true;
		
	}

}
