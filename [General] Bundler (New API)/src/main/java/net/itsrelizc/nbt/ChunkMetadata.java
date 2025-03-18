package net.itsrelizc.nbt;

import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import net.itsrelizc.events.EventRegistery;

public class ChunkMetadata {
	
	public static void set(Chunk chunk, String key, PersistentDataType type, Object value) {
		
		chunk.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, key), type, value);
		
	}
	
	public static Object get(Chunk chunk, String key, PersistentDataType type) {
		
		return chunk.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, key), type);
		
	}
	
	public static void remove(Chunk chunk, String key) {
		
		try {
			chunk.getPersistentDataContainer().remove(new NamespacedKey(EventRegistery.main, key));
		} catch (NullPointerException exception) {
			
			System.out.print("Forcefully removed key " + key + " from chunk " + chunk + " persistentdatacontainer!");
			
		}
		
	}

}
