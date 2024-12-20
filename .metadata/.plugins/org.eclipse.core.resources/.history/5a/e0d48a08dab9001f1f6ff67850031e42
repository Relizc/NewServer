package net.abrasminecraft.smp.commands;

import net.abrasminecraft.smp.utils.Configs;
import net.abrasminecraft.smp.utils.EnchantmentRefundCooldowns;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RefundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equals("refund")){
            Player p = (Player) commandSender;
            String uuid = String.valueOf(p.getUniqueId());
            if (!EnchantmentRefundCooldowns.map.containsKey(uuid)) return false;
            int elapsed = (int) (System.currentTimeMillis() - EnchantmentRefundCooldowns.map.get(uuid));
            if(elapsed <= Configs.REFUND_COOLDOWN && p.getInventory().contains(Material.ENCHANTING_TABLE)){
                Map<Integer,ItemStack> map = p.getInventory().addItem(new ItemStack(Material.OBSIDIAN,4),new ItemStack(Material.BOOK,1),new ItemStack(Material.DIAMOND,2));
                for(ItemStack item:map.values()){
                    p.getWorld().dropItemNaturally(p.getLocation(),item);
                }
                EnchantmentRefundCooldowns.map.remove(uuid);
                p.getInventory().remove(new ItemStack(Material.ENCHANTING_TABLE,1));
                return true;

            }

        }
        return false;
    }
}
