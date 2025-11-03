package net.itsrelizc.npc;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class NPCDialogueSession {
	
	public static class Response {
		private String name;
		private String id;

		public Response(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getLocalizedName(Player player) {
			return Locale.a(player, this.name);
		}
	}
	
	public static enum Status {
		TALKING,
		WAITING;
	}
	
	private Player target;
	private RelizcNPC npc;
	private long start;
	private long end;
	private long current;
	
	private Status status;
	private UUID responseQuery;
	
	public long getEndTime() {
		return this.end;
	}
	
	public long getStartTime() {
		return this.start;
	}
	
	public long getCurrentTime() {
		return this.current;
	}

	public NPCDialogueSession(Player target, RelizcNPC who, long expiresSeconds) {
		this.target = target;
		this.npc = who;
		this.start = System.currentTimeMillis();
		this.current = System.currentTimeMillis();
		this.end = System.currentTimeMillis() + 1000 * expiresSeconds;
		this.responseQuery = null;
	}
	
	public void refreshSession(long expiresSeconds) {
		this.current = System.currentTimeMillis();
		this.end = System.currentTimeMillis() + 1000 * expiresSeconds;
	}
	
	public Player getPlayer() {return this.target;}
	public RelizcNPC getNPC() {return this.npc;}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void sendMessage(Player player, String message, long refreshSeconds) {
		this.getNPC().sayDialoguePrivateChat(player, message);
		this.refreshSession(refreshSeconds);
	}
	
	public void sendLocalizedMessage(Player player, String message, long refreshSeconds) {
		this.getNPC().sayDialoguePrivateChat(player, Locale.a(player, message));
		this.refreshSession(refreshSeconds);
	}
	
	public static HashMap<String, NPCDialogueSession> sess = new HashMap<String, NPCDialogueSession>();
	
	
	public void waitForResponse(Player player, List<Response> responses, long refreshSeconds) {
		
		this.refreshSession(refreshSeconds);
		player.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 0.5f, 1f);
		
		this.responseQuery = UUID.randomUUID();
		sess.put(this.responseQuery.toString(), this);
		
		String da = "§8§m--------------------------------§r\n " + Locale.a(player, "npc.awaitresponse").formatted(Locale.a(player, this.npc.getRealName())) + "\n";
		TextComponent a = new TextComponent(da);
		
		for (Response res : responses) {
			TextComponent b = new TextComponent("  ");
			TextComponent c = new TextComponent(Locale.a(player, res.name));
			StringUtils.attachCommand(c, "npcsession " + responseQuery.toString() + " " + res.id, Locale.a(player, "npc.awaitresponse.hover"));
			b.addExtra(c);
			b.addExtra("\n");
			
			a.addExtra(b);
		}
		
		a.addExtra(" " + Locale.a(player, "npc.awaitresponse.canclick") + "\n§8§m--------------------------------");
		
		player.spigot().sendMessage(a);
		
	}

	public void recieveResponse(Player sender, String params) {
		sess.remove(this.responseQuery.toString());
	}
	
	public void endSession() {
		this.end = System.currentTimeMillis() - 100;
	
	}

}
