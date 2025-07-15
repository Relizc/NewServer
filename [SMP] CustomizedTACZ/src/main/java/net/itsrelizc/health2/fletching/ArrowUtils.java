package net.itsrelizc.health2.fletching;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowFletching;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowPoint;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowShaft;
import net.itsrelizc.health2.penetration.ArrowHitListeners;
import net.itsrelizc.health2.penetration.ArrowHitListeners.Vec2;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.CompoundTag;

public class ArrowUtils {
	
	public static enum WeightUnit {
		SEED, NUGGET;
	}
	
	private static Map<Integer, ArrowPoint> points = new HashMap<Integer, ArrowPoint>();
	private static Map<Integer, ArrowShaft> shafts = new HashMap<Integer, ArrowShaft>();
	private static Map<Integer, ArrowFletching> fletchings = new HashMap<>();
	
	public static class ArrowPoint {
		public ArrowPoint(long id2, double weight2, WeightUnit unit2, long seedWeight2, double nuggetWeight2,
				long penetration2, long damage2) {
			id = id2;
			weight = weight2;
			unit = unit2;
			seedWeight = seedWeight2;
			nuggetWeight = nuggetWeight2;
			penetration = penetration2;
			damage = damage2;
		}
		public long id;
		double weight;
		WeightUnit unit;
		public long seedWeight;
		double nuggetWeight;
		public long penetration;
		public long damage;
		
		
	}
	
	public static class ArrowFletching {
		public ArrowFletching(long id, double weight, WeightUnit unit, long seedWeight, double nuggetWeight,
				double stability, double resistance) {
			this.id = id;
			this.weight = weight;
			this.unit = unit;
			this.seedWeight = seedWeight;
			this.nuggetWeight = nuggetWeight;
			this.stability = stability;
			this.resistance = resistance;
		}

		long id;
		double weight;
		WeightUnit unit;
		public long seedWeight;
		double nuggetWeight;
		public double stability;
		public double resistance;
	}
	
	public static class ArrowShaft {
		public ArrowShaft(long id2, double weight2, WeightUnit unit2, long seedWeight2, double nuggetWeight2,
				double releaseBreak2, double flightBreak2) {
			id = id2;
			weight = weight2;
			unit = unit2;
			seedWeight = seedWeight2;
			nuggetWeight = nuggetWeight2;
			releaseBreak = releaseBreak2;
			flightBreak = flightBreak2;
		}
		long id;
		double weight;
		WeightUnit unit;
		public long seedWeight;
		double nuggetWeight;
		double releaseBreak;
		public double flightBreak;
		
		
	}
	
	private static String penToColor(int lvl) {
		if (lvl == 0) {
			return "§4";
		} else if (lvl == 1) {
			return "§4";
		} else if (lvl == 2) {
			return "§e";
		} else if (lvl == 3) {
			return "§f";
		} else if (lvl == 4) {
			return "§b";
		} else if (lvl == 5) {
			return "§5";
		} else if (lvl >= 6) {
			return "§d";
		}
		return "";
	}
	
	private static String strengthToColor(int overflow) {
		if (overflow < 20) return "§4";
		else if (overflow < 50) return "§e";
		else if (overflow < 70) return "§6";
		else if (overflow < 90) return "§2";
		else return "§a";
	}
	
	static String breakChanceToColor(double chance) {
		if (chance <= 0.01) return "§a";
		else if (chance <= 0.05) return "§2";
		else if (chance <= 0.1) return "§e";
		else if (chance <= 0.15) return "§6";
		else if (chance <= 0.2) return "§c";
		else return "§4";
	}
	
	public static String convertPenetrationToSymbol(int penetration) {
		
		int lvl = penetration / 10;
		int overflow = penetration % 10;
		
		if (lvl == 0 && overflow == 0) {
			return "§40% §4I ⛊";
		}
		
		String build = "";
		
		build += penToColor(lvl);
		
		if (lvl >= 1) {
			build += StringUtils.intToRoman(lvl) + " ⛊";
		} else {
			
		}
		
		if (overflow > 0) {
			if (lvl >= 1) {
				build += " §7+§r ";
			}
			build += strengthToColor(overflow * 10) + overflow * 10 + "% " + penToColor(lvl + 1) + StringUtils.intToRoman(lvl + 1) + " ⛊";
		}
		
		
		
		return build;
		
	}
	
	public static void loadAllArrowPoints() {
		JSONArray array = (JSONArray) JSON.loadDataFromDataBase("ballistics/arrow_points.json").get("data");
		
		int count = 0;
		
		for (Object object : array) {
			JSONObject arrow = (JSONObject) object;
			
			JSONObject name = (JSONObject) arrow.get("name");
			String engName = (String) name.get("EN_US");
			if (engName.length() <= 0) continue;
			int id = joaat(engName);
			
			JSONObject description = (JSONObject) arrow.get("description");
			
			for (Object a : name.keySet()) {
				String lang = (String) a;
				String val = (String) name.get(a);
				
				Locale.addEntry(lang, "item.arrow_point.name." + id, val);
			}
			
			for (Object a : description.keySet()) {
				String lang = (String) a;
				String val = (String) name.get(a);
				
				Locale.addEntry(lang, "item.arrow_point.description." + id, val);
			}
			
			double weight = Double.valueOf((String) arrow.get("weight"));
			WeightUnit unit = (boolean) arrow.get("weightType") ? WeightUnit.NUGGET : WeightUnit.SEED;
			long seedWeight = unit == WeightUnit.SEED ? (long) weight : (long) (weight * 22.18392);
			double nuggetWeight = unit == WeightUnit.NUGGET ? (double) weight : (double) (weight / 22.18392);
			
			long penetration = (long) arrow.get("penetration");
			long damage = (long) arrow.get("damage");
			
			System.out.println("PT " + engName + " -> " + id);
			
			
			ArrowPoint point = new ArrowPoint(id, weight, unit, seedWeight, nuggetWeight, penetration, damage);
			points.put(id, point);
			
			count ++;
		}
		
		System.out.println("Loaded " + count + " arrow points");
	}
	
	public static void loadAllArrowFletchings() {
		JSONArray array = (JSONArray) JSON.loadDataFromDataBase("ballistics/arrow_fletching.json").get("data");

		int count = 0;

		for (Object object : array) {
			JSONObject fletching = (JSONObject) object;

			JSONObject name = (JSONObject) fletching.get("name");
			String engName = (String) name.get("EN_US");
			if (engName.length() <= 0) continue;
			int id = joaat(engName);

			JSONObject description = (JSONObject) fletching.get("description");

			for (Object a : name.keySet()) {
				String lang = (String) a;
				String val = (String) name.get(a);

				Locale.addEntry(lang, "item.arrow_fletching.name." + id, val);
			}

			for (Object a : description.keySet()) {
				String lang = (String) a;
				String val = (String) description.get(a);

				Locale.addEntry(lang, "item.arrow_fletching.description." + id, val);
			}

			double weight = Double.valueOf((String) fletching.get("weight"));
			WeightUnit unit = (boolean) fletching.get("weightType") ? WeightUnit.NUGGET : WeightUnit.SEED;
			long seedWeight = unit == WeightUnit.SEED ? (long) weight : (long) (weight * 22.18392);
			double nuggetWeight = unit == WeightUnit.NUGGET ? weight : weight / 22.18392;

			double stability = ((Number) fletching.get("stability")).doubleValue();
			double resistance = ((Number) fletching.get("resistance")).doubleValue();
			
			System.out.println("FL " + engName + " -> " + id);

			ArrowFletching fletch = new ArrowFletching(id, weight, unit, seedWeight, nuggetWeight, stability, resistance);
			fletchings.put(id, fletch);

			count++;
		}

		System.out.println("Loaded " + count + " arrow fletchings");
	}
	
	public static void loadAllArrowShafts() {
		JSONArray array = (JSONArray) JSON.loadDataFromDataBase("ballistics/arrow_shafts.json").get("data");
		
		int count = 0;
		
		for (Object object : array) {
			JSONObject arrow = (JSONObject) object;
			
			JSONObject name = (JSONObject) arrow.get("name");
			String engName = (String) name.get("EN_US");
			if (engName.length() <= 0) continue;
			int id = joaat(engName);
			
			JSONObject description = (JSONObject) arrow.get("description");
			
			for (Object a : name.keySet()) {
				String lang = (String) a;
				String val = (String) name.get(a);
				
				Locale.addEntry(lang, "item.arrow_shaft.name." + id, val);
			}
			
			for (Object a : description.keySet()) {
				String lang = (String) a;
				String val = (String) name.get(a);
				
				Locale.addEntry(lang, "item.arrow_shaft.description." + id, val);
			}
			
			double weight = Double.valueOf((String) arrow.get("weight"));
			WeightUnit unit = (boolean) arrow.get("weightType") ? WeightUnit.NUGGET : WeightUnit.SEED;
			long seedWeight = unit == WeightUnit.SEED ? (long) weight : (long) (weight * 22.18392);
			double nuggetWeight = unit == WeightUnit.NUGGET ? (double) weight : (double) (weight / 22.18392);
			
			double releaseBreak = (double) arrow.get("releaseBreak");
			double flightBreak = (double) arrow.get("flightBreak");
			
			System.out.println("SH " + engName + " -> " + id);
			
			
			ArrowShaft point = new ArrowShaft(id, weight, unit, seedWeight, nuggetWeight, releaseBreak, flightBreak);
			shafts.put(id, point);
			
			count ++;
		}
		
		System.out.println("Loaded " + count + " arrow shafts");
	}
	
	/**
     * Computes JOAAT (Jenkins One-at-a-Time) hash.
     * 
     * @param input The input string to hash.
     * @return A signed 32-bit hash value.
     */
    public static int joaat(String input) {
        int hash = 0;

        for (int i = 0; i < input.length(); i++) {
            hash += input.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >>> 6);
        }

        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);

        return hash;
    }
    
    public static void shootArrowNaturally(LivingEntity shooter, ItemStack usedArrow, ItemStack bow, Location loc, Vector vector, double multiplier) {
    	
    	
    	bow = ItemUtils.castOrCreateItem(bow).getBukkitItem();
	    
	    net.minecraft.world.item.ItemStack bowit = CraftItemStack.asNMSCopy(bow);
	    //System.out.println(bowit.getOrCreateTag());
	    CompoundTag bowtag = bowit.getOrCreateTag();
	    
	    long force = bowtag.getLong("force");
	    
	    double accuracy = bowtag.getDouble("accuracy");
	    //System.out.println(force + " " +  accuracy);
	     
	    
	    double dForce = (double) force;
    	
    	net.minecraft.world.item.ItemStack it = CraftItemStack.asNMSCopy(usedArrow);
	    CompoundTag tag = it.getOrCreateTag();
	    
	    
	    ArrowPoint point = ArrowUtils.getPoint(tag.getInt("point"));
	    ArrowShaft shaft = ArrowUtils.getShaft(tag.getInt("shaft"));
	    ArrowFletching fletching = ArrowUtils.getFletching(tag.getInt("fletching"));
	    
	    double dWeight = point.seedWeight + shaft.seedWeight + fletching.seedWeight;
	    
	    double forceRatio = dForce / dWeight;
	    double actualSpeed = 4 * forceRatio;
	    actualSpeed *= (1- fletching.resistance);
	    
	    //System.out.println(actualSpeed);
	    
	    Arrow arrow = shooter.launchProjectile(Arrow.class);
	    arrow.setShooter(shooter);
	    
	    double finalAccuracy = Math.max(accuracy + fletching.stability, 0d);
	    Vec2 offset = ArrowHitListeners.getRandomAimOffset(finalAccuracy);
	    Vector newVelocity = vector.normalize().multiply(actualSpeed);
	    ArrowHitListeners.applyPitchYawOffsetToVelocity(newVelocity, offset.pitchOffset, offset.yawOffset);
	    
	    //arrow.setTicksLived(-5);
	    
	    //FragUtils.spawnArrowFragment(arrow.getLocation(), newVelocity.normalize(), velocity * actualSpeed);
	    
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "penetration"), PersistentDataType.LONG, point.penetration);
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "damage"), PersistentDataType.LONG, point.damage);
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "breakChance"), PersistentDataType.DOUBLE, shaft.flightBreak);
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "point"), PersistentDataType.LONG, point.id);
	    
	    arrow.setVelocity(newVelocity.clone().multiply(multiplier));
	    
	    ArrowHitListeners.arrowLocationStorage.put(arrow, arrow.getLocation());
	    ArrowHitListeners.getLastTicksLived.put(arrow, Integer.MIN_VALUE);
    }
    
    public static void shootArrowWithSameVelocityInDirection(Arrow originalArrow, Vector newDirection, ProjectileSource shooter) {
        // Normalize new direction
        Vector dir = newDirection.clone().normalize();

        // Get original speed (magnitude of velocity vector)
        double speed = originalArrow.getVelocity().length();

        // Compute new velocity vector
        Vector newVelocity = dir.multiply(speed);

        // Offset position slightly backward (to avoid hitting the shooter)
        Location spawnLoc = originalArrow.getLocation().clone().add(dir.multiply(1)); // move 0.5 blocks back

        // Spawn new arrow
        World world = originalArrow.getWorld();
        Arrow newArrow = world.spawnArrow(spawnLoc, newVelocity, (float) speed, 0);
        newArrow.setShooter(shooter);

        // Transfer persistent data
        PersistentDataContainer originalData = originalArrow.getPersistentDataContainer();
        PersistentDataContainer newData = newArrow.getPersistentDataContainer();

        NamespacedKey keyPenetration = new NamespacedKey(EventRegistery.main, "penetration");
        NamespacedKey keyDamage = new NamespacedKey(EventRegistery.main, "damage");
        NamespacedKey keyPoint = new NamespacedKey(EventRegistery.main, "point");

        if (originalData.has(keyPenetration, PersistentDataType.LONG)) {
            newData.set(keyPenetration, PersistentDataType.LONG, originalData.get(keyPenetration, PersistentDataType.LONG));
        }
        if (originalData.has(keyDamage, PersistentDataType.LONG)) {
            newData.set(keyDamage, PersistentDataType.LONG, originalData.get(keyDamage, PersistentDataType.LONG));
        }
        if (originalData.has(keyPoint, PersistentDataType.LONG)) {
            newData.set(keyPoint, PersistentDataType.LONG, originalData.get(keyPoint, PersistentDataType.LONG));
        }
        
        ArrowHitListeners.arrowLocationStorage.put(newArrow, newArrow.getLocation());
	    ArrowHitListeners.getLastTicksLived.put(newArrow, Integer.MIN_VALUE);
    }

	public static ArrowPoint getPoint(Integer tagInteger) {
		
		return points.getOrDefault(tagInteger, points.getOrDefault(-992184946, null));
	}
	
	public static ArrowShaft getShaft(Integer tagInteger) {
		
		return shafts.getOrDefault(tagInteger, shafts.getOrDefault(-1478126976, null));
	}
	
	public static ArrowFletching getFletching(Integer tagInteger) {
		
		return fletchings.getOrDefault(tagInteger, fletchings.getOrDefault(-488142409, null));
	}


}
