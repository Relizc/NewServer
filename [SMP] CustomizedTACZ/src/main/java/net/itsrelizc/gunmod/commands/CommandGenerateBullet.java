package net.itsrelizc.gunmod.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.gunmod.craft.CartridgeAssemblerListener;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.string.StringUtils;

public class CommandGenerateBullet extends RelizcCommand {


	public CommandGenerateBullet() {
		super("bullet");
		// TODO Auto-generated constructor stub
		this.setRelizcOp(true);
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		Material bullet = Material.getMaterial(args[0]);
		Material casing = Material.getMaterial(args[1]);
		Material primer = Material.getMaterial(args[2]);
		Material powder = Material.getMaterial(args[3]);
		
		ItemStack a = CartridgeAssemblerListener.createBullet(sender, ItemGenerator.generate(bullet, 1), ItemGenerator.generate(casing, 1), ItemGenerator.generate(primer, 1), ItemGenerator.generate(powder, 1));
		a.setAmount(Integer.valueOf(args[4]));
		
		sender.getInventory().addItem(a);
		
		return true;
		
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		
		if (args.length == 1) {
			Material[] mats = Material.values();
			List<String> names = StringUtils.fromNewList();
			for (Material m : mats) {
				if (m.toString().startsWith("RELIZC_BULLET")) names.add(m.toString());
			}
			return TabCompleteInfo.presetOption((Player) sender, "commands.bullet.arg0.description", names);
		} else if (args.length == 2) {
			Material[] mats = Material.values();
			List<String> names = StringUtils.fromNewList();
			for (Material m : mats) {
				if (m.toString().startsWith("RELIZC_CASE")) names.add(m.toString());
			}
			return TabCompleteInfo.presetOption((Player) sender, "commands.bullet.arg1.description", names);
		} else if (args.length == 3) {
			Material[] mats = Material.values();
			List<String> names = StringUtils.fromNewList();
			for (Material m : mats) {
				if (m.toString().startsWith("RELIZC_PRIMER")) names.add(m.toString());
			}
			return TabCompleteInfo.presetOption((Player) sender, "commands.bullet.arg2.description", names);
		} else if (args.length == 4) {
			List<String> names = StringUtils.fromArgs("GUNPOWDER");
			return TabCompleteInfo.presetOption((Player) sender, "commands.bullet.arg3.description", names);
		} else if (args.length == 5) {
			return TabCompleteInfo.presetNumber((Player) sender, "commands.bullet.arg4.description");
		} 
		
		return TabCompleteInfo.presetNothing((Player) sender);
		
	}


}
