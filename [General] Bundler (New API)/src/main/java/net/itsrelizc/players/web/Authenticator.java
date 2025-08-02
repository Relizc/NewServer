package net.itsrelizc.players.web;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.itsrelizc.events.EventRegistery;

public class Authenticator {
	
	public static class AuthenticatorListener implements Listener {
		
		@EventHandler
		public void join(PlayerJoinEvent event) {
			sendTokenAsync(event.getPlayer());
		}
		
	}
	
	private static String apiHost;
	
	public static void init() {
		apiHost = System.getProperty("APIHost");
        if (apiHost == null || apiHost.isEmpty()) {
            apiHost = System.getenv("APIHost");
        }
        if (apiHost == null || apiHost.isEmpty()) {
            apiHost = "http://localhost:5000";  // default fallback
        }

        EventRegistery.main.getLogger().info("API Host set to: " + apiHost);
	}
	
	// Replace this with your actual JWT generation method
    private static String generateJwtForPlayer(Player player) {
        // TODO: implement JWT generation or get it from your Minecraft server plugin logic
        return UUID.randomUUID().toString();
    }
    
    private static Map<String, String> tokens = new HashMap<String, String>();
    
    public static String getToken(Player player) {
    	return tokens.get(player.getUniqueId().toString());
    }

    public static void sendTokenAsync(Player player) {
        CompletableFuture.runAsync(() -> {
            try {
                String jwt = generateJwtForPlayer(player);
                tokens.put(player.getUniqueId().toString(), jwt);
                URL url = new URL(apiHost + "/provide_token");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Authorization", "Bearer " + jwt);
                conn.setRequestProperty("UUID", player.getUniqueId().toString());
                conn.setRequestProperty("Name", player.getName());

                // Empty body
                byte[] out = new byte[0];
                conn.setFixedLengthStreamingMode(out.length);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connect();

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(out);
                }

                int responseCode = conn.getResponseCode();
                Bukkit.getScheduler().runTask(EventRegistery.main, () -> {
                    Bukkit.getLogger().info("Sent token for " + player.getName() + ", response code: " + responseCode);
                });

                conn.disconnect();
            } catch (Exception e) {
                Bukkit.getScheduler().runTask(EventRegistery.main, () -> {
                    Bukkit.getLogger().severe("Failed to send token for " + player.getName() + ": " + e.getMessage());
                });
                e.printStackTrace();
            }
        });
    }

}
