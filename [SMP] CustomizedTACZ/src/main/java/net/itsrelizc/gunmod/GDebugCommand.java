package net.itsrelizc.gunmod;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class GDebugCommand extends RelizcCommand {

	public GDebugCommand() {
		super("setballistic");
		// TODO Auto-generated constructor stub
		this.setRelizcOp(true);
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		String mode = args[0];
		double value = Double.valueOf(args[1]);
		
		if (mode.equalsIgnoreCase("g")) {
			Hit.g = value;
			StringUtils.systemMessage(sender, "Ballistics", "Set Hit.g to " + value);
		} else if (mode.equalsIgnoreCase("bs")) {
			Hit.bulletSpeed = value;
			StringUtils.systemMessage(sender, "Ballistics", "Set Hit.bulletSpeed to " + value);
		} else if (mode.equalsIgnoreCase("nbt")) {
			
			ItemStack hand = sender.getItemInHand();
			NBTTagCompound nbt = NBT.getNBT(hand);
			
			NBTTagCompound shitfuck = NBT.getCompound(nbt, "ShitFuck");
			NBT.setCompound(nbt, "Inventory", shitfuck);
			
			NBT.setInteger(nbt, "ShitFuck", 0);
			
			hand = NBT.setCompound(hand, nbt);
			
			sender.setItemInHand(hand);
			
		} else if (mode.equalsIgnoreCase("nbt2")) {
			
			ItemStack hand = sender.getItemInHand();
			NBTTagCompound nbt = NBT.getNBT(hand);

			NBT.setInteger(nbt, "Inventory", 0);

			hand = NBT.setCompound(hand, nbt);
			
			sender.setItemInHand(hand);
			
		}
		return true;
		
	}

}
