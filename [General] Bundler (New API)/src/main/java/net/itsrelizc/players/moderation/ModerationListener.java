package net.itsrelizc.players.moderation;

import java.util.UUID;
import net.itsrelizc.bundler.JSON;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ModerationListener implements Listener {
  @EventHandler(priority=EventPriority.HIGHEST)
  public void login(PlayerLoginEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    JSONObject obj = JSON.loadDataFromDataBase("banned.json");
    if (obj.containsKey(uuid.toString())) {
      String made = null;
      JSONObject info = (JSONObject)obj.get(uuid.toString());
      long expires = ((Long)info.get("expires")).longValue();
      long diff = expires - System.currentTimeMillis();
      long[] delta = StringUtils.convertMillisToTime(diff);
      JSONObject reason = (JSONObject)info.get("reason");
      JSONArray meta = (JSONArray)reason.get("meta");
      if (reason.get("type").equals("general"))
        made = String.valueOf(Locale.get(player, "general.tempban.title")) + "\n\n" + 
          Locale.get(player, "general.tempban.time").formatted(new Object[] { Long.valueOf(delta[0]), Long.valueOf(delta[1]), Long.valueOf(delta[2]), Long.valueOf(delta[3]) }) + "\n\n" + 
          Locale.get(player, "general.ban.reason").formatted(new Object[] { Locale.get(player, "general.ban_reasons.general").formatted(meta.toArray()) }) + "\n" + 
          Locale.get(player, "general.ban.sum").formatted(new Object[] { Long.valueOf(14L) }) + "\n\n" + 
          Locale.get(player, "general.ban.directions").formatted(new Object[] { player.getName(), info.get("token") }) + "\n\n" + 
          Locale.get(player, "general.ban.website") + "https://smp.itsrelizc.net/"; 
      event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
      event.setKickMessage(made);
    } 
  }
}
