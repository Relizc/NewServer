package net.itsrelizc.bundler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSON {
	
//	public static String PREFIX = "F:/";
	public static String PREFIX = "/home/ubuntu/minecraft-data/";
	
	public static JSONObject pathLoadData(String path) {
		path = path.replace("\\", "/");
		JSONParser parser = new JSONParser();
		Object object = null;
		try {
			String s = Files.readString(Path.of(path), StandardCharsets.UTF_8);
			object = parser.parse(s);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (JSONObject) object;
	}
	
	public static JSONObject loadDataFromDataBase(String name) {
		return pathLoadData(PREFIX + name);
	}
	
	public static void pathSaveData(String path, JSONObject data) {
		path = path.replace("\\", "/");
		FileWriter file;
		try {
			file = new FileWriter(path);
			file.write(data.toJSONString());
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveDataFromDataBase(String name, JSONObject data) {
		pathSaveData(PREFIX + name, data);
	}
	
	public static boolean checkAccountExsists(Player player) {
		try {

			JSONObject players = JSON.loadDataFromDataBase("player.json");
			return players.containsKey(player.getUniqueId().toString());
			
			
		} catch (Exception e) {
			Bukkit.getLogger().warning("Cannot find true UUID of player " + player.getDisplayName());
			e.printStackTrace();
		}
		
		return false;
	}
}
