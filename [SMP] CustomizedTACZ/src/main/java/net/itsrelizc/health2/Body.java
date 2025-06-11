package net.itsrelizc.health2;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.smp.modsmp.SMPScoreboard;

public class Body {
	
	public static Map<String, Body> parts = new HashMap<String, Body>();
	
	Limb head;
	Limb leftArm;
	Limb rightArm;
	Limb chest;
	Limb abs;
	Limb leftLegs;
	Limb rightLegs;
	Player owner;
	
	int cycleDisplay = 0; // 0-6: head, chest, abs, l/r arms, l/r legs
	int cycleEvent = 0;
	
	
	public Body ( Player player ) {
		head = new Limb(3, 3, "head");
		chest = new Limb(6, 6, "chest");
		leftArm = new Limb(8, 8, "leftArm");
		rightArm = new Limb(8, 8, "rightArm");
		abs = new Limb(4, 4, "abs");
		leftLegs = new Limb(6, 6, "leftLegs");
		rightLegs = new Limb(6, 6, "rightLegs");
		
		this.owner = player;
	}
	
	private Limb convert(int partId) {
		if (partId == 0) return head;
		else if (partId == 1) return chest;
		else if (partId == 2) return abs;
		else if (partId == 3) return leftArm;
		else if (partId == 4) return rightArm;
		else if (partId == 5) return leftLegs;
		else if (partId == 6) return rightLegs;
		else return null;
		
	}
	
	public void startNotify() {
		
		SMPScoreboard.boards.get(owner).addLine(2, SMPScoreboard.SECRET_BODYPARTS);
		
		cycleEvent = Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				int i = 0;
				for (;i < 7; i ++) {
					cycleDisplay = cycleDisplay % 7;
					
					Limb limb = convert(cycleDisplay);
					if (limb.isAbnormal()) {
						SMPScoreboard.boards.get(owner).editLine(2, SMPScoreboard.SECRET_BODYPARTS + limb.getCriticalColor());
						cycleDisplay ++;
						break;
					}
					
					
					cycleDisplay ++;
				}
				if (i == 7) {
					stopNotify();
				}
			}
			
			
		}, 0, 20L);
		
	}
	
	public void stopNotify() {
		
		SMPScoreboard.boards.get(owner).removeLine(2);
		Bukkit.getScheduler().cancelTask(cycleEvent);
	}

}
