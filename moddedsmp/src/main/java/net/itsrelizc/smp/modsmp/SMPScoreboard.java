package net.itsrelizc.smp.modsmp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
import net.itsrelizc.health2.Body;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.quests.Quest.QuestObjective;
import net.itsrelizc.quests.QuestUtils;
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
	
	public static enum Pages {
		MAIN,
		DEATH,
		HEALTH;
	}
	
	public static Map<Player, SMPScoreboard> boards = new HashMap<Player, SMPScoreboard>();
	
	private Pages page;
	
	private void clearDisplay() {
		for (int i = this.getLines().size() - 3; i >= 3; i --) {
			this.removeLine(i);
		}
	}
	
	private void applyPage() {
		
		//Bukkit.broadcastMessage(page.toString());
		
		if (page == Pages.MAIN) {
			//addLine(2, "");
			addLine(3, SECRET_DIAMOND_PURSE + Locale.get(player, "general.tablist.purse").formatted(DiamondPurse.getPurse(player)));
			
			if (QuestUtils.getActiveQuest(player) != null) {
				addLine(4, " ");
				addLine(5, " " + Locale.a(player, "quest.tablist").formatted(Locale.a(player, QuestUtils.getActiveQuest(player).DISPLAY_NAME)));
				
				int i =5;
				for (QuestObjective obj : QuestUtils.getActiveQuest(player).OBJECTIVES) {
					i ++;
					if (!obj.isActive()) continue;
					addLine(i, " §e• " + obj.toString(player));
					
				}
			}
			
		} else if (page == Pages.HEALTH) {
			Body body = Body.parts.get(player.getUniqueId().toString());
			addLine(3, "  " + Profile.coloredName(player) + " §8- §a%d§8/§7%d §c❤".formatted(body.getHealth(),body.getMaxHealth()));
			addLine(4, " ");
			for (int i = 5; i < 7 + 5; i ++) {
				addLine(i, body.convert(i - 5).getCriticalColor(player));
			}
		} else if (page == Pages.DEATH) {
			
			addLine(3, Locale.a(player, "general.tablist.youdied"));
			addLine(4, " ");
			addLine(5, Locale.a(player, "general.tablist.youdied.revive0"));
			addLine(6, Locale.a(player, "general.tablist.youdied.revive1"));
			
		}
	}

	public SMPScoreboard(Player player) {
		super(player);
		page = Pages.MAIN;
		
		
		addLine("");  // consolidated: player status
		addLine("                  §d"); // spacing
//		addLine(SECRET_DIAMOND_PURSE + Locale.get(player, "general.tablist.purse").formatted(DiamondPurse.getPurse(player)));
		clearDisplay();
		applyPage();
		
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
	
	public Pages getPage() {
		return this.page;
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

	public void refreshDiamondPage() {
		if (page != Pages.MAIN) return;
		editLine(3, SECRET_DIAMOND_PURSE + Locale.get(player, "general.tablist.purse").formatted(DiamondPurse.getPurse(player)));
	}
	
	public void refreshMainPage() {
		if (page != Pages.MAIN) return;
		this.clearDisplay();
		this.applyPage();
	}

	public void changed(int partId) {
		
		if (page != Pages.HEALTH) return;
		
		Body body = Body.parts.get(player.getUniqueId().toString());
		//Bukkit.broadcastMessage("changededit" + page.toString() + " " + body.convert(partId).getCriticalColor(player));
		
		editLine(5 + partId, body.convert(partId).getCriticalColor(player));
		editLine(3, "  " + Profile.coloredName(player) + " §8- §a%d§8/§7%d §c❤".formatted(body.getHealth(),body.getMaxHealth()));
	}

	public void setPage(Pages page) {
		if (this.page == page) return;
		this.page = page;
		this.clearDisplay();
		this.applyPage();
		
	}

}
