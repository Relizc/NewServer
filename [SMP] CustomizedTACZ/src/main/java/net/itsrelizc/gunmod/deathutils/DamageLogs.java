package net.itsrelizc.gunmod.deathutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DamageLogs implements Iterable<DamageRecord> {
	
	private List<DamageRecord> records;
	
	public DamageLogs() {
		records = new ArrayList<DamageRecord>();
	}

	public void add(DamageRecord damageRecord) {
		// TODO Auto-generated method stub
		records.add(damageRecord);
	}

	public void debugBroadcastRecords(Player player) {
		for (DamageRecord str : records) {
			//(str.toString(player));
		}
	}

	public void clear() {
		records.clear();
	}

	@Override
	public Iterator<DamageRecord> iterator() {
		return records.iterator();
	}

	public DamageRecord getLatest() {
		return records.get(records.size() - 1);
	}
	
	public int getSize() {
		return records.size();
	}

}
