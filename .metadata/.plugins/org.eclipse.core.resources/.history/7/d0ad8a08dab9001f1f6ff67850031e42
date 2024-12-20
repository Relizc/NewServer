package net.abrasminecraft.smp.events;

import net.abrasminecraft.smp.utils.EnchantmentRefundCooldowns;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.world.level.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Objects;


public class EventEnchant implements Listener {
    @EventHandler
    public static void playerRightClickBlock(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.ENCHANTING_TABLE)){
                Player p = e.getPlayer();
                p.sendMessage("Enchantment tables are disabled. All enchantments can only spawn in treasure chests.");
                e.setCancelled(true);

            }
        }
    }
    @EventHandler
    public static void playerCraftTable(CraftItemEvent e){
        System.out.println("Hi");
        if(e.getRecipe().getResult().getType().equals(Material.ENCHANTING_TABLE)){
            Player p = (Player) e.getWhoClicked();
            EnchantmentRefundCooldowns.map.put(String.valueOf(p.getUniqueId()), System.currentTimeMillis());
            TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"&bEnchantment tables are disabled. &3Click &bwithin the next 10s to get a refund of your items.")));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/refund"));
            p.spigot().sendMessage(component);

        }
    }

}
