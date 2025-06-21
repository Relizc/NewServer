package net.itsrelizc.commands;

import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.SignInput;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHelp extends RelizcCommand {

	public CommandHelp() {
		super("help", "get help");
		this.setAliases(StringUtils.fromArgs("?"));
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		TextComponent a = new TextComponent("\n§7官方QQ群聊: ");
		TextComponent b = new TextComponent("§e§n941503698");
		StringUtils.attachHover(b, Locale.get(sender, "general.text.clicktocopy"));
		StringUtils.attachCopy(b, "941503698");
		TextComponent c = new TextComponent("§a§n打开链接");
		StringUtils.attachOpenURL(c, "https://qm.qq.com/q/myxpWyuJAA");
		StringUtils.attachHover(c, Locale.get(sender, "general.text.openurl", "https://qm.qq.com/q/myxpWyuJAA"));
		
		a.addExtra(b);
		a.addExtra(" ");
		a.addExtra(c);
		
		sender.spigot().sendMessage(a);
		
		TextComponent d = new TextComponent("\n§7服务器官网: ");
		TextComponent e = new TextComponent("§a§nsmp.itsrelizc.net§r");
		StringUtils.attachOpenURL(e, "https://smp.itsrelizc.net");
		StringUtils.attachHover(e, Locale.get(sender, "general.text.openurl", "https://smp.itsrelizc.net"));
		d.addExtra(e);
		
		sender.spigot().sendMessage(d);
		
		return true;
	}
	
	

}
