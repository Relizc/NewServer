package net.itsrelizc.nbt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.json.simple.JSONObject;

import com.comphenix.protocol.wrappers.Pair;

import net.itsrelizc.bundler.JSON;

public class BlockMetadata<K, V> {
	
	private K key;
	private V value;
	
	public BlockMetadata(K key, V value) {
		
		this.key = key;
		this.value = value;
		
	}
	
	public void writeJSON(JSONObject obj) {
		
		obj.put(key, value);
		
	}
	
	
	
	public static void addData(Block block, String key, Object value) {
		File worldFolder = block.getWorld().getWorldFolder();
        
        // Get the POI folder
        File poiFolder = new File(worldFolder, "poi");
        
        // Get the blockmetadata folder inside the POI folder
        File blockMetadataFolder = new File(poiFolder, "blockmetadata");

        if (!blockMetadataFolder.exists()) {
            // If the blockmetadata folder doesn't exist, you can create it if necessary
            blockMetadataFolder.mkdirs();
        }
        
        File dat = new File(blockMetadataFolder, String.format("%d.%d.%d.relizc", block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()));
        
        JSONObject obj;
        JSONObject data;
        
        if (!dat.exists()) {
        	try {
				dat.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	obj = new JSONObject();
        	obj.put("version", 1l);
        	
        	data = new JSONObject();
        	
        	obj.put("data", data);
        	
        	
        	
        	
        } else {
        	
        	obj = JSON.pathLoadData(dat.getPath());
        	data = (JSONObject) obj.get("data");
        	
        }
        
        data.put(key, value);
        obj.put("data", data);
        
        JSON.pathSaveData(dat.getPath(), obj);
        
        
	}

}
