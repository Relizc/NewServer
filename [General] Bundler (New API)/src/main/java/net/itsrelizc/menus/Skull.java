package net.itsrelizc.menus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;


/**
 * Represents some special mob heads, also support creating player skulls and custom skulls.
 *
 * @author xigsag, SBPrime
 */
public enum Skull {

    ARROW_LEFT("MHF_ArrowLeft"),
    ARROW_RIGHT("MHF_ArrowRight"),
    ARROW_UP("MHF_ArrowUp"),
    ARROW_DOWN("MHF_ArrowDown"),
    QUESTION("MHF_Question"),
    EXCLAMATION("MHF_Exclamation"),
    CAMERA("FHG_Cam"),

    ZOMBIE_PIGMAN("MHF_PigZombie"),
    PIG("MHF_Pig"),
    SHEEP("MHF_Sheep"),
    BLAZE("MHF_Blaze"),
    CHICKEN("MHF_Chicken"),
    COW("MHF_Cow"),
    SLIME("MHF_Slime"),
    SPIDER("MHF_Spider"),
    SQUID("MHF_Squid"),
    VILLAGER("MHF_Villager"),
    OCELOT("MHF_Ocelot"),
    HEROBRINE("MHF_Herobrine"),
    LAVA_SLIME("MHF_LavaSlime"),
    MOOSHROOM("MHF_MushroomCow"),
    GOLEM("MHF_Golem"),
    GHAST("MHF_Ghast"),
    ENDERMAN("MHF_Enderman"),
    CAVE_SPIDER("MHF_CaveSpider"),

    CACTUS("MHF_Cactus"),
    CAKE("MHF_Cake"),
    CHEST("MHF_Chest"),
    MELON("MHF_Melon"),
    LOG("MHF_OakLog"),
    PUMPKIN("MHF_Pumpkin"),
    TNT("MHF_TNT"),
    DYNAMITE("MHF_TNT2");

    private String id;

    private Skull(String id) {
        this.id = id;
    }

    /**
     * Return a skull that has a custom texture specified by url.
     *
     * @param url skin url
     * @return itemstack
     */
    public static ItemStack getCustomSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta == null) return head;

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        // Create the JSON and encode it
        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        String encodedData = Base64.getEncoder().encodeToString(json.getBytes());

        Property textureProperty = new Property("textures", encodedData);

        try {
            // Use reflection to get the properties map
            Method getProperties = GameProfile.class.getMethod("getProperties");
            Object propertyMap = getProperties.invoke(profile);

            // Use reflection to call the 'put' method on the property map
            Method putMethod = propertyMap.getClass().getMethod("put", Object.class, Object.class);
            putMethod.invoke(propertyMap, "textures", textureProperty);

            // Set the profile field in SkullMeta
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        head.setItemMeta(meta);
        return head;
    }
    
    public static void setSkullUrl(SkullMeta meta, String url) {

        if (meta == null) return;

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        // Create the JSON and encode it
        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        String encodedData = Base64.getEncoder().encodeToString(json.getBytes());

        Property textureProperty = new Property("textures", encodedData);

        try {
            // Use reflection to get the properties map
            Method getProperties = GameProfile.class.getMethod("getProperties");
            Object propertyMap = getProperties.invoke(profile);

            // Use reflection to call the 'put' method on the property map
            Method putMethod = propertyMap.getClass().getMethod("put", Object.class, Object.class);
            putMethod.invoke(propertyMap, "textures", textureProperty);

            // Set the profile field in SkullMeta
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getSkinUrl(Player player) {
        try {
            // Reflectively get the EntityPlayer
            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);

            // Try to find the GameProfile field reflectively
            Field profileField = null;
            for (Field field : craftPlayer.getClass().getDeclaredFields()) {
                if (field.getType().getSimpleName().equals("GameProfile")) {
                    profileField = field;
                    break;
                }
            }

            if (profileField == null) return null;

            profileField.setAccessible(true);
            GameProfile profile = (GameProfile) profileField.get(craftPlayer);

            // Reflectively get 'properties' field from GameProfile
            Field propertiesField = GameProfile.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            Object propertiesMap = propertiesField.get(profile); // Should be a com.google.common.collect.Multimap

            // Get the "textures" property collection from the multimap
            @SuppressWarnings("unchecked")
            Collection<Property> textures = ((Map<String, Collection<Property>>) propertiesMap).get("textures");

            if (textures == null || textures.isEmpty()) return null;

            Property texture = textures.iterator().next();

            // Decode Base64
            String json = new String(Base64.getDecoder().decode(texture.getValue()));

            // Extract URL manually
            int start = json.indexOf("http://textures.minecraft.net/texture/");
            if (start == -1) return null;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return a skull of a player.
     *
     * @param name player's name
     * @return itemstack
     */
    public static ItemStack getPlayerSkull(String name) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Return the skull's id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Return the skull of the enum.
     *
     * @return itemstack
     */
    public ItemStack getSkull() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(id);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
 
