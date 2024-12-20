package net.abrasminecraft.smp;

import net.abrasminecraft.smp.commands.RefundCommand;
import net.abrasminecraft.smp.events.EventEnchant;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class EnchantmentMods extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EventEnchant(),this);
        //COMMANDS
        Bukkit.getPluginCommand("refund").setExecutor(new RefundCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
