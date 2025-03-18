package net.itsrelizc.smp.corps.api;

import org.bukkit.entity.Player;

import net.itsrelizc.smp.corps.Contract.Party.PartyType;

public interface Business {
	
	public String getName();
	
	public String getName(Player player);
	
	public PartyType getType();
	
	public String getTypeName(Player player);
	
	public String getLiabilityName(Player player);
	
	public String getRegistrationName();
	
	public String getDisplayName(Player player);
	
	public String getLoreName(Player player);
	
	public String getLocale(Player player, String namespace);

}
