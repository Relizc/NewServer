package net.itsrelizc.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.string.StringUtils;

public class CommandDebugStats extends RelizcCommand {

    public CommandDebugStats() {
        super("debugstats", "Show runnable & listener stats");
        this.setRelizcOp(true);
    }

    @Override
    public boolean onPlayerExecute(Player sender, String[] args) {
        // 1️⃣ Count pending + active runnables
        BukkitScheduler sched = sender.getServer().getScheduler();
        int pending = sched.getPendingTasks().stream()
                .map(BukkitTask::getOwner)
                .filter(plugin -> plugin.getName().equals(EventRegistery.main.getName()))
                .mapToInt(t -> 1).sum();
        int active = sched.getActiveWorkers().size(); // includes any plugin tasks

        // 2️⃣ Count event listeners
        int listenerCount = 0;
        for (HandlerList hl : HandlerList.getHandlerLists()) {
            for (RegisteredListener rl : hl.getRegisteredListeners()) {
                if (rl.getPlugin().getName().equals(EventRegistery.main.getName())) {
                    listenerCount++;
                }
            }
        }

        sender.sendMessage(
            "§a[RUNTIME INFO] §e" + pending + " pending runnables, " +
            active + " active tasks, and §b" + listenerCount + " registered listeners.");

        return true;
    }
}
