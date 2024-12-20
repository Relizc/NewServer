package net.itsrelizc.uptime;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TPSUtils {
	
	private static long last = System.currentTimeMillis();
	private static long delay = -1;
	private static double tps = 0;
	
	private static Double currenttps = 20.00;
	private static Long lastms = System.currentTimeMillis();
	
	
	public static void start(Plugin plugin) {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				long x = System.currentTimeMillis();
				
				delay = x - last;
				
				last = System.currentTimeMillis();
				
				TPSService.tick();
			}
			
		}, 0L, 0L);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				Long thisms = System.currentTimeMillis();
				Float gapms = (float) (thisms - lastms);
				Float gaps = (float) (gapms / 1000.00);
				if (gaps < 0.5) {
					return;
				}
				currenttps = (double) (20 / gaps);
				lastms = System.currentTimeMillis();
			}
			
		}, 0L, 20L);
	}
	
	public static long getDelay() {
		return delay;
	}
	
	public static String getDelayAsFormattedString() {
		return delay + "ms";
	}
	
	public static long getDelayYield() {
		
		return delay - 50;
		
	}
	
	public static String getDelayYieldAsFormattedString() {
		String k;
		long y = getDelayYield();
		k = y > 0 ? "+" : "";
		
		return k + getDelayYield() + "ms";
	}

	public static double getTPS() {
		return currenttps;
	}

}
