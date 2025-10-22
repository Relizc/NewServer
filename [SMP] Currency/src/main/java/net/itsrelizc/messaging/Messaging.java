package net.itsrelizc.messaging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.itemlib.ItemStackSerializer;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class Messaging {

    private static final String MESSAGE_DIR = "messages/";

    // Inner message object abstraction
    public static class Message {
        public String id;
        public String category;
        public String sender;
        public String title;
        public String content;
        public long timestamp;
        public boolean read;               // 🆕 has player read this mail
        public boolean collected;          // 🆕 were items collected
        public List<ItemStack> items;
        public List<ItemStack> originalItems; // 🆕 immutable original item record

        public Message(String id, String category, String sender, String title, String content,
                       long timestamp, List<ItemStack> items, List<ItemStack> originalItems, boolean read, boolean collected) {
            this.id = id;
            this.category = category;
            this.sender = sender;
            this.title = title;
            this.content = content;
            this.timestamp = timestamp;
            this.items = (items != null ? new ArrayList<>(items) : new ArrayList<>());
            this.originalItems = (originalItems != null ? new ArrayList<>(originalItems) : new ArrayList<>());
            this.read = read;
            this.collected = collected;
        }
        
        public Message(String id, String category, String sender, String title, String content,
                long timestamp, List<ItemStack> items, boolean read, boolean collected) {
     this.id = id;
     this.category = category;
     this.sender = sender;
     this.title = title;
     this.content = content;
     this.timestamp = timestamp;
     this.items = (items != null ? new ArrayList<>(items) : new ArrayList<>());
     this.originalItems = new ArrayList<>();
     this.read = read;
     this.collected = collected;
 }

        public JSONObject toJSON() {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("category", category);
            obj.put("sender", sender);
            obj.put("title", title);
            obj.put("content", content);
            obj.put("timestamp", timestamp);
            obj.put("read", read);
            obj.put("collected", collected);

            JSONArray itemsArray = new JSONArray();
            for (ItemStack i : items)
                itemsArray.add(ItemStackSerializer.serialize(i));
            obj.put("items", itemsArray);

            JSONArray originalItemsArray = new JSONArray();
            for (ItemStack i : originalItems)
                originalItemsArray.add(ItemStackSerializer.serialize(i));
            obj.put("originalItems", originalItemsArray);

            return obj;
        }

        public static Message fromJSON(JSONObject obj) {
            String id = (String) obj.get("id");
            String category = (String) obj.getOrDefault("category", "default");
            String sender = (String) obj.getOrDefault("sender", "system");
            String title = (String) obj.getOrDefault("title", "Untitled");
            String content = (String) obj.getOrDefault("content", "");
            long timestamp = (long) obj.getOrDefault("timestamp", System.currentTimeMillis());
            boolean read = (boolean) obj.getOrDefault("read", false);
            boolean collected = (boolean) obj.getOrDefault("collected", false);

            List<ItemStack> items = new ArrayList<>();
            if (obj.containsKey("items")) {
                JSONArray arr = (JSONArray) obj.get("items");
                for (Object o : arr)
                    items.add(ItemStackSerializer.deserialize((JSONObject) o));
            }

            List<ItemStack> originalItems = new ArrayList<>();
            if (obj.containsKey("originalItems")) {
                JSONArray arr = (JSONArray) obj.get("originalItems");
                for (Object o : arr)
                    originalItems.add(ItemStackSerializer.deserialize((JSONObject) o));
            } else {
                // Backward compatibility: if missing, assume same as items
                originalItems.addAll(items);
            }

            return new Message(id, category, sender, title, content, timestamp, items, originalItems, read, collected);
        }

        public boolean hasItems() {
            return !items.isEmpty();
        }
        
        private static void sortItemsByAmountDescending(List<ItemStack> items) {
            if (items == null || items.isEmpty()) return;

            items.sort(Comparator.comparingInt(ItemStack::getAmount).reversed());
        }


		public ItemStack convertToItemStack(Player player) {
			// TODO Auto-generated method stub
			
			Material mat;
			ItemStack it;
			
			if (this.originalItems.size() > 0 || this.hasItems()) {
				if (this.hasItems()) {
					mat = Material.CHEST_MINECART;
				} else {
					mat = Material.MINECART;
				}
			} else {
				if (this.read) {
					mat = Material.BOOK;
				} else {
					mat = Material.ENCHANTED_BOOK;
				}
			}
			
			String name = "§r§f" + parseMessageLang(player, this.title);
			String pre = "§8" + Locale.convertDate(timestamp, Locale.getLanguage(player)) + " " + Locale.a(player, "messages.sender") + parseMessageLang(player, this.sender);
			String content = StringUtils.wrapWithColor("§f" + parseMessageLang(player, this.content), Locale.getLanguage(player));
			
			if (this.hasItems() || this.originalItems.size() > 0) {
				content += "\n\n" + Locale.a(player, "messages.items.contains") + "\n§7§m---------------";
				sortItemsByAmountDescending(this.items);
				for (ItemStack it1 : this.items) {
					ItemStack d = it1.clone();
					d.setAmount(1);
					RelizcItemStack it2 = ItemUtils.castOrCreateItem(d);
					content += "\n§7×" + it1.getAmount() + " " + it2.getBukkitItem().getItemMeta().getDisplayName();
				}
				
				
				if (this.originalItems.size() > 0) {
					sortItemsByAmountDescending(this.originalItems);
					for (ItemStack it1 : this.originalItems) {
						ItemStack d = it1.clone();
						d.setAmount(1);
						RelizcItemStack it2 = ItemUtils.castOrCreateItem(d);
						content += "\n§8§m×" + it1.getAmount() + " " + it2.getBukkitItem().getItemMeta().getDisplayName().replaceAll("§.", "");
					}
				}
				
				if (this.hasItems()) {
					content += "\n\n" + Locale.a(player, "messages.items.clickclaim");
				} else {
					content += "\n\n" + Locale.a(player, "messages.items.allclaimed");
				}
			}
			
			
			it = ItemGenerator.generate(mat, 1, name,  (pre + "\n\n" + content).split("\n"));
			return it;
		}
		
		private static String parseMessageLang(Player player, String msg) {
			if (msg.startsWith("§§")) {
				String loc = msg.substring(2);
				return Locale.a(player, loc);
			}
			return msg;
		}
    }


    // Utility to ensure message folder exists
    private static Path getPlayerFile(Player player) {
        Path dir = Paths.get(JSON.PREFIX, MESSAGE_DIR);
        Path file = dir.resolve(player.getUniqueId().toString() + ".json");

        try {
            if (!Files.exists(dir))
                Files.createDirectories(dir);
            if (!Files.exists(file))
                Files.writeString(file, "{}", StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    // Load player data JSON
    private static JSONObject loadPlayerData(Player player) {
        Path file = getPlayerFile(player);
        try {
            String content = Files.readString(file);
            if (content.isEmpty()) content = "{}";
            return (JSONObject) new JSONParser().parse(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    // Save player data JSON
    private static void savePlayerData(Player player, JSONObject data) {
        Path file = getPlayerFile(player);
        try {
            Files.writeString(file, data.toJSONString(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ========================
    // 1. Add Message
    // ========================
    public static void addMessage(Player player, Message message) {
        JSONObject data = loadPlayerData(player);
        JSONArray category = (JSONArray) data.getOrDefault(message.category, new JSONArray());
        category.add(message.toJSON());
        data.put(message.category, category);
        savePlayerData(player, data);
        
        if (player.isOnline()) {
        	player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 0.890899f);
        	
        	TextComponent comp = new TextComponent("§e📧 §7您收到了一条消息");
        	player.sendMessage("");
        }
        
    }

    // ========================
    // 2. Get Messages
    // ========================
    public static List<Message> getMessages(Player player, String category) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());
        List<Message> messages = new ArrayList<>();

        for (Object o : arr) {
            messages.add(Message.fromJSON((JSONObject) o));
        }

        // Sort by timestamp descending (most recent first)
        messages.sort((m1, m2) -> Long.compare(m2.timestamp, m1.timestamp));

        return messages;
    }

    // ========================
    // 3. Delete Message (by index, id, or object)
    // ========================
    public static void deleteMessage(Player player, String category, Object identifier) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());
        JSONArray newArr = new JSONArray();

        if (identifier instanceof Integer) {
            int idx = (int) identifier;
            for (int i = 0; i < arr.size(); i++)
                if (i != idx)
                    newArr.add(arr.get(i));
        } else if (identifier instanceof String) {
            String id = (String) identifier;
            for (Object o : arr) {
                JSONObject msg = (JSONObject) o;
                if (!msg.get("id").equals(id))
                    newArr.add(msg);
            }
        } else if (identifier instanceof Message) {
            String id = ((Message) identifier).id;
            for (Object o : arr) {
                JSONObject msg = (JSONObject) o;
                if (!msg.get("id").equals(id))
                    newArr.add(msg);
            }
        }

        data.put(category, newArr);
        savePlayerData(player, data);
    }

    // ========================
    // 4. Manage Inventory Items in Message
    // ========================
    public static ItemStack popItemFromMessage(Player player, String category, String messageId) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());
        ItemStack result = null;
        JSONArray newArr = new JSONArray();

        for (Object o : arr) {
            JSONObject msgJson = (JSONObject) o;
            if (msgJson.get("id").equals(messageId)) {
                Message m = Message.fromJSON(msgJson);
                if (!m.items.isEmpty()) {
                    result = m.items.remove(0);
                    m.originalItems.add(result); // add to original items
                }
                if (m.items.isEmpty()) m.collected = true;
                newArr.add(m.toJSON());
            } else {
                newArr.add(msgJson);
            }
        }

        data.put(category, newArr);
        savePlayerData(player, data);
        return result;
    }


    public static void removeItemFromMessage(Player player, String category, String messageId, int index) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());
        JSONArray newArr = new JSONArray();

        for (Object o : arr) {
            JSONObject msgJson = (JSONObject) o;
            if (msgJson.get("id").equals(messageId)) {
                Message m = Message.fromJSON(msgJson);
                if (index >= 0 && index < m.items.size()) {
                    ItemStack removed = m.items.remove(index);
                    m.originalItems.add(removed);
                }
                if (m.items.isEmpty()) m.collected = true;
                newArr.add(m.toJSON());
            } else {
                newArr.add(msgJson);
            }
        }

        data.put(category, newArr);
        savePlayerData(player, data);
    }


    public static void clearItemsFromMessage(Player player, String category, String messageId) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());
        JSONArray newArr = new JSONArray();

        for (Object o : arr) {
            JSONObject msgJson = (JSONObject) o;
            if (msgJson.get("id").equals(messageId)) {
                Message m = Message.fromJSON(msgJson);
                // move all remaining items to originalItems
                m.originalItems.addAll(m.items);
                m.items.clear();
                m.collected = true;
                newArr.add(m.toJSON());
            } else {
                newArr.add(msgJson);
            }
        }

        data.put(category, newArr);
        savePlayerData(player, data);
    }



    
    
    public static int getMessageCount(Player player, String category) {
        Path file = getPlayerFile(player);
        try {
            if (!Files.exists(file)) return 0;

            String content = Files.readString(file);
            if (content.isEmpty()) return 0;

            // Parse only the root and the category array
            JSONObject root = (JSONObject) new JSONParser().parse(content);
            Object categoryData = root.get(category);

            if (categoryData instanceof JSONArray)
                return ((JSONArray) categoryData).size();
            else
                return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // ========================
    // Get Original Items (Before Collection)
    // ========================
    public static List<ItemStack> getOriginalItems(Player player, String category, String messageId) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());

        for (Object o : arr) {
            JSONObject msg = (JSONObject) o;
            if (msg.get("id").equals(messageId)) {
                Message m = Message.fromJSON(msg);
                return new ArrayList<>(m.originalItems);
            }
        }

        return new ArrayList<>();
    }
    
    // ========================
    // Mark as Read
    // ========================
    public static void markAsRead(Player player, String category, String messageId) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());
        JSONArray newArr = new JSONArray();

        for (Object o : arr) {
            JSONObject msg = (JSONObject) o;
            if (msg.get("id").equals(messageId)) {
                Message m = Message.fromJSON(msg);
                m.read = true;
                newArr.add(m.toJSON());
            } else {
                newArr.add(msg);
            }
        }

        data.put(category, newArr);
        savePlayerData(player, data);
    }


    
    public static List<ItemStack> getItemsFromMessage(Player player, String category, String messageId) {
        JSONObject data = loadPlayerData(player);
        JSONArray arr = (JSONArray) data.getOrDefault(category, new JSONArray());

        for (Object o : arr) {
            JSONObject msg = (JSONObject) o;
            if (msg.get("id").equals(messageId)) {
                Message m = Message.fromJSON(msg);
                return new ArrayList<>(m.items); // return a copy to avoid mutating original
            }
        }

        return new ArrayList<>(); // return empty list if message not found
    }

    // ========================
    // Utility to generate message IDs
    // ========================
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}

