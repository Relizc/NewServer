package net.itsrelizc.smp.modsmp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.PlayerAFKEvent;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.scoreboards.GameInfo;
import net.itsrelizc.scoreboards.RelizcScoreboard;
import net.itsrelizc.smp.insurance.Insurance;

@Deprecated // useless
class PacketMonitor {
    private final Map<UUID, AtomicInteger> rxPackets = new HashMap<>();
    private final Map<UUID, AtomicInteger> txPackets = new HashMap<>();

    public void onEnable(Plugin plugin) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        // Incoming (from client)
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.MONITOR, PacketType.values()) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                rxPackets.computeIfAbsent(event.getPlayer().getUniqueId(), k -> new AtomicInteger(0)).incrementAndGet();
            }
        });

        // Outgoing (to client)
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.MONITOR, PacketType.values()) {
            @Override
            public void onPacketSending(PacketEvent event) {
                txPackets.computeIfAbsent(event.getPlayer().getUniqueId(), k -> new AtomicInteger(0)).incrementAndGet();
            }
        });
    }

    public int getRx(UUID playerId) {
        return rxPackets.getOrDefault(playerId, new AtomicInteger(0)).get();
    }

    public int getTx(UUID playerId) {
        return txPackets.getOrDefault(playerId, new AtomicInteger(0)).get();
    }
}

class Signal implements Listener {

	public static String getSignalStatus(Player player) {
		if (player.getPing() <= 50) return "§a📶 ";
		else if (player.getPing() <= 100) return "§2📶 ";
		else if (player.getPing() <= 150) return "§e📶 ";
		else if (player.getPing() <= 200) return "§6📶 ";
		else if (player.getPing() <= 250) return "§c📶 ";
		else return "§4📶 ";
	}
	
	
	
}

public class SMPScoreboard extends RelizcScoreboard {
	
	public static final String SECRET_DIAMOND_PURSE = "§a§b§c";
	public static final String SECRET_BODYPARTS = "§d§e§f";
	
	public static Map<Player, SMPScoreboard> boards = new HashMap<Player, SMPScoreboard>();

	public SMPScoreboard(Player player) {
		super(player);
		
		
		addLine("");  // consolidated: player status
		addLine(""); // spacing
		addLine(SECRET_DIAMOND_PURSE + Locale.get(player, "general.tablist.purse").formatted(DiamondPurse.getPurse(player)));
		
		addFootnote();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				updateGameInfo();
				updateStatus();
			}
        	
        }, 0, 100L);
		
		updateStatus();
		
		boards.put(player, this);
	}
	
	

	@Override
	public void addGameInfo() {
    	addLine(" §8" + this.player.getPing() + "ms ↑0 ↓0");
    	setDisplayName("§e§l" + Locale.get(this.player, GameInfo.gameName));
    }
	
	public void updateStatus() {
		editLine(1, 
				Signal.getSignalStatus(player) +
				SMPScoreboardListener.getAFKStatus(player) +
				Insurance.getInsuranceStatusColorCode(player)
				);
	}
    
	public void updateGameInfo() {
    	editLine(0, " §8" + this.player.getPing() + "ms          "
    			);
    }

}
