package net.itsrelizc.uptime;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TPSService {
	
	private static String str = "§8[刻速: %s%.2f§8] [刻延迟: %s (%s%s§8)] [客户端延迟: %dms]\n[实体数量: %s%d§8/%s%d§8/%s%d§8]";
	
	private static boolean ok = false;
	private static String n;
	
	private static String eval(double tps) {
		if (tps <= 5) return "§c";
		if (tps <= 10) return "§6";
		if (tps <= 18) return "§e";
		if (tps >= 35) return "§c";
		if (tps >= 32) return "§6";
		if (tps >= 38) return "§e";
		if (tps >= 25) return "§a";
		if (tps >= 21) return "§b";
		
		
		
		
		return "§8";
	}
	
	private static String eval2(long tps) {
		if (tps <= -20) return "§c";
		if (tps <= -15) return "§6";
		if (tps <= -10) return "§e";
		if (tps <= -8) return "§a";
		if (tps <= -5) return "§b";
		if (tps >= 200) return "§c";
		if (tps >= 100) return "§6";
		if (tps >= 20) return "§e";
		
		
		
		
		return "§8";
	}
	
	private static String evalEnts(int tps) {
		
		if (tps >= 2000) return "§c";
		if (tps >= 1500) return "§6";
		if (tps >= 1000) return "§e";
		
		return "§8";
		
		
	}
	
	public static String getTablistDisplayInfo(Player player) {
		
		//for (World w : Bukkit.getWorlds()) Bukkit.broadcastMessage(w.getName());
		
		double t = TPSUtils.getTPS();
		
//		int a = Bukkit.getWorld("world").getEntities().size();
//		World nether = Bukkit.getWorld("world_nether");
//		World nether_alt = Bukkit.getWorld("DIM-1");
//		int b = -2;
//		if (nether == null && nether_alt == null) b = -1;
//		if (nether == null) b = nether_alt.getEntities().size();
//		if (nether_alt == null) b = nether.getEntities().size();
//		World end = Bukkit.getWorld("world_the_end");
//		World end_alt = Bukkit.getWorld("DIM1");
//		int c = -2;
//		if (end == null && end_alt == null) c = -1;
//		if (end == null) c = end_alt.getEntities().size();
//		if (end_alt == null) c = end.getEntities().size();
		int a = -1, b = -1, c = -1;
		
		return String.format(str,
					eval(t), t,
					TPSUtils.getDelayAsFormattedString(),
					eval2(TPSUtils.getDelayYield()), TPSUtils.getDelayYieldAsFormattedString(),
					player.getPing(),
					
					evalEnts(a), a,
					evalEnts(b), b,
					evalEnts(c), c
				);
		
		
		//return "Tablist info is currently disabled";
		
		
	}
	
	public static String tick() {
		if (ok) n = "§7⬤§8⬤";
		else n = "§8⬤§7⬤";
		ok = !ok;
		
		return n;
	}

}
