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
			} else if (event.getRawSlot() == 14) {
				event.getWhoClicked().sendMessage("§a귀하의 언어가 간체 한국어로 전환되었습니다!");
				event.getWhoClicked().sendMessage("§a부분에서는 새로 고침하려면 서버에 다시 연결해야 한다는 것을 보여줍니다!");
				Profile.findByOwner((Player) event.getWhoClicked()).lang = Language.KO_KR;
			} else if (event.getRawSlot() == 15) {
				event.getWhoClicked().sendMessage("§a¡Tu idioma ha sido cambiado a español!");
				event.getWhoClicked().sendMessage("§a¡La parte muestra que necesitas volver a conectarte al servidor para actualizar!");
				Profile.findByOwner((Player) event.getWhoClicked()).lang = Language.ES_ES;
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
			ItemStack korean = Skull.getCustomSkull("http://textures.minecraft.net/texture/ca12913d7df640d18bcc7a45a8172c68ffa04756e84c6f0a2eda3da45e00dadd");
			ItemStack spanish = Skull.getCustomSkull("http://textures.minecraft.net/texture/c2d730b6dda16b584783b63d082a80049b5fa70228aba4ae884c2c1fc0c3a8bc");

			menu.setItem(10, ItemGenerator.generate(chinese, 1, "§a中文 (简体)", "§7语言完整性§8: §a100%"));
			
			
			menu.setItem(11, ItemGenerator.generate(chinese_trad, 1, "§a中文 (繁體)", "§7語言完整性§8: " + helper_coverage(Language.ZH_TW) + "%"));
			menu.setItem(12, ItemGenerator.generate(eng, 1, "§aEnglish (US)", "§7Language Coverage§8: "  + helper_coverage(Language.EN_US) + "%"));
			menu.setItem(13, ItemGenerator.generate(japan, 1, "§a日本語　(日本)", "§7言語の整合性§8: " + helper_coverage(Language.JA_JP) + "%"));
			menu.setItem(14, ItemGenerator.generate(korean, 1, "§a한국어(한국)", "§7언어 적용 범위§8: " + helper_coverage(Language.KO_KR) + "%"));
			menu.setItem(15, ItemGenerator.generate(spanish, 1, "§aEspañol(a) (españa)", "§7Cobertura lingüística§8: " + helper_coverage(Language.ES_ES) + "%"));

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
