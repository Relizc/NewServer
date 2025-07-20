package net.itsrelizc.itemlib;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.commands.RelizcCommand.TabCompleteInfo;
import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.itemlib.ItemUtils.MetadataPair;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class CommandGenerateItem extends RelizcCommand {

	public CommandGenerateItem() {
		super("relizcgive", "makes an item");
		this.setRelizcOp(true);
		
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		
		Player player = (Player )sender;
		
		//return new TabCompleteInfo(new TabCompleteType[] {}, (Player) sender, "commands.general.tabcomplete.test");
		if (args.length == 1) {
			return TabCompleteInfo.presetOption(player, "Item ID", new ArrayList<String>(ItemUtils.getRegisteredIDs()));
		} else if (args.length >= 2) {
			List<String> possible = new ArrayList<String>();
			Class<? extends RelizcItemStack> clazz = ItemUtils.getHandler(args[0]);
			if (clazz != null) {
				RelizcItemMeta[] metas = clazz.getAnnotationsByType(RelizcItemMeta.class);
				for (RelizcItemMeta meta : metas) {
					if (meta.type()==NBTTagType.TAG_Int) {
						possible.add(meta.key() + "=int:" + meta.int_init());
						possible.add(meta.key() + "=int:");
					} else if (meta.type()==NBTTagType.TAG_String) {
						possible.add(meta.key() + "=str:\"" + meta.str_init() + "\"");
						possible.add(meta.key() + "=str:");
					} else if (meta.type()==NBTTagType.TAG_Long) {
						possible.add(meta.key() + "=long:" + meta.long_init());
						possible.add(meta.key() + "=long:");
					} else if (meta.type()==NBTTagType.TAG_Double) {
						possible.add(meta.key() + "=double:" + meta.double_init());
						possible.add(meta.key() + "=double:");
					}
				}
				possible.add("CUSTOM_NAME=str:\"\"");
				possible.add("CUSTOM_NAME=str:\"");
			}
			
			return TabCompleteInfo.presetOption(player, "Metadata Pair: <key>=<type(int/String/long/boolean)>:<value>", possible);
		}
		return TabCompleteInfo.presetNothing(player);
		
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		Class<? extends RelizcItemStack> clazz = ItemUtils.getHandler(args[0]);
		
		int size = args.length - 1;
		MetadataPair[] metas = new MetadataPair[size];
		
		for (int i = 1; i < args.length; i ++) {
			
			
			
			String[] arg = args[i].split("=");
			String key = arg[0];
			String[] afs = arg[1].split(":");
			String type = afs[0];
			String val = afs[1];
			Object value = null;
			
			if (key.equals("CUSTOM_NAME")) {
				metas[i - 1] = new MetadataPair(key, val.substring(1, val.length() - 1));
				continue;
			}
			
			if (type.equals("int")) value = Integer.valueOf(val);
			else if (type.equals("long")) value = Long.valueOf(val);
			else if (type.equals("str")) value = val.substring(1, val.length() - 1);
			else if (type.equals("double")) value = Double.valueOf(val);
			
			metas[i - 1] = new MetadataPair(key, value);
		}
		
		RelizcItemStack created = ItemUtils.createItem(clazz, sender, metas);
		
		sender.getInventory().addItem(created.getBukkitItem());
		
		return true;
		
	}

}
