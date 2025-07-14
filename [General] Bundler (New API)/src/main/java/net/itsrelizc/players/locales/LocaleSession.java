package net.itsrelizc.players.locales;

import org.bukkit.entity.Player;

public class LocaleSession {
	
	private Player owner;

	public LocaleSession(Player player) {
		this.owner = player;
	}
	
	public String get(String namespace) {
		return Locale.a(owner, namespace);
	}
	
	public String a(String b) {
		return get(b);
	}
	
}
