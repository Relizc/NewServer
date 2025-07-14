package net.itsrelizc.smp.insurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.commands.RelizcCommand;

public class PlayerStashManager {
	
	public static class GiveMyStashBackCommand extends RelizcCommand {

		public GiveMyStashBackCommand() {
			super("fuckugivemebackmystuff", "");
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean onPlayerExecute(Player player, String[] args) {
			
			PlayerStashManager manager = new PlayerStashManager();
			
			List<ItemStack> stash = manager.loadStash(player.getUniqueId());
			
			for (ItemStack it : stash) {
				player.getWorld().dropItem(player.getLocation(), it);
			}
			
			manager.clear(player.getUniqueId());
			
			return true;
			
		}
		
	}

    private final Map<UUID, List<ItemStack>> stashCache = new HashMap<>();

    // Load a stash for the player
    private List<ItemStack> loadStash(UUID uuid) {
        String path = "stash/" + uuid + ".json";
        JSONObject data = JSON.loadDataFromDataBase(path);
        List<ItemStack> items = new ArrayList<>();

        if (data == null || !data.containsKey("items")) return items;

        JSONArray array = (JSONArray) data.get("items");
        for (int i = 0; i < array.size(); i++) {
            JSONObject itemJson = (JSONObject) array.get(i);
            items.add(ItemStackSerializer.deserialize(itemJson));
        }

        return items;
    }

    // Save the stash
    private void saveStash(UUID uuid, List<ItemStack> items) {
        JSONArray array = new JSONArray();
        for (ItemStack item : items) {
            array.add(ItemStackSerializer.serialize(item));
        }

        JSONObject data = new JSONObject();
        data.put("items", array);

        String path = "stash/" + uuid + ".json";
        JSON.saveDataFromDataBase(path, data);
    }

    private List<ItemStack> getStash(UUID uuid) {
        if (!stashCache.containsKey(uuid)) {
            stashCache.put(uuid, loadStash(uuid));
        }
        return stashCache.get(uuid);
    }

 // Add item by UUID
    public void addItem(UUID uuid, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;
        List<ItemStack> stash = getStash(uuid);
        stash.add(item.clone());
        saveStash(uuid, stash);
    }

    // Add item by Player
    public void addItem(Player player, ItemStack item) {
        addItem(player.getUniqueId(), item);
    }

    // Get items by UUID
    public List<ItemStack> getItems(UUID uuid) {
        return new ArrayList<>(getStash(uuid));
    }

    // Get items by Player
    public List<ItemStack> getItems(Player player) {
        return getItems(player.getUniqueId());
    }

    // Remove item by UUID
    public boolean removeItem(UUID uuid, ItemStack target) {
        List<ItemStack> stash = getStash(uuid);
        Iterator<ItemStack> iterator = stash.iterator();
        while (iterator.hasNext()) {
            ItemStack item = iterator.next();
            if (item.isSimilar(target)) {
                iterator.remove();
                saveStash(uuid, stash);
                return true;
            }
        }
        return false;
    }

    // Remove item by Player
    public boolean removeItem(Player player, ItemStack target) {
        return removeItem(player.getUniqueId(), target);
    }

    // Clear stash by UUID
    public void clear(UUID uuid) {
        stashCache.put(uuid, new ArrayList<>());
        saveStash(uuid, new ArrayList<>());
    }

    // Clear stash by Player
    public void clear(Player player) {
        clear(player.getUniqueId());
    }
}

