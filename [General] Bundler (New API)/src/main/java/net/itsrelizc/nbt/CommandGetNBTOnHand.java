package net.itsrelizc.nbt;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.commands.RelizcCommand.TabCompleteInfo;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

public class CommandGetNBTOnHand extends RelizcCommand {

	public CommandGetNBTOnHand() {
		super("getnbt", "nbt commands");
		this.setRelizcOp(true);
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		if (args.length == 1) {
			return TabCompleteInfo.presetOption((Player) sender, "MODE", StringUtils.fromArgs("offhand", "mainhand"));
		} else {
			return TabCompleteInfo.presetNothing((Player) sender);
		}
		
	}
	
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		net.minecraft.world.item.ItemStack item = CraftItemStack.asNMSCopy(sender.getItemInHand());
		
		CompoundTag tag = item.getTag();
		MutableComponent displayComponent = item.getDisplayName().copy()
			    .withStyle(style -> style.withHoverEvent(
			        new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(item))
			    ));
		
		CraftPlayer player = (CraftPlayer) sender;
		player.getHandle().sendSystemMessage(displayComponent.append("has NBT: " + tag.getAsString()));
		
		return true;
		
	}

}
