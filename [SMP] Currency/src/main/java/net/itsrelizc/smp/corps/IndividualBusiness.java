package net.itsrelizc.smp.corps;

import org.bukkit.entity.Player;

import net.itsrelizc.smp.corps.Contract.Party.PartyType;
import net.itsrelizc.smp.corps.api.Business;

public class IndividualBusiness implements Business {
	
	private Player player;

	public IndividualBusiness(Player player) {
		this.player = player;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartyType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTypeName(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLiabilityName(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRegistrationName() {
		return this.player.getUniqueId().toString();
	}

	@Override
	public String getDisplayName(Player player) {
		return this.player.getDisplayName();
	}

	@Override
	public String getLoreName(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocale(Player player, String namespace) {
		// TODO Auto-generated method stub
		return null;
	}

}
