package net.itsrelizc.events;

import org.bukkit.Bukkit;

import net.itsrelizc.bundler.Main;

public class TaskDelay {
	
	public static void delayTask(Runnable runnable, long ticks) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(EventRegistery.main, runnable, ticks);
	}
	
}