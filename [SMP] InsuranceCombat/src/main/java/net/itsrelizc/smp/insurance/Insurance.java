package net.itsrelizc.smp.insurance;

import org.bukkit.entity.Player;

import net.itsrelizc.smp.corps.CorporateBusiness;

public class Insurance {

	public static long RESOURCE_PRICE = 1942l;
	public static long CRAFT_PRICE = 783l;
	
	public static CorporateBusiness JUSTIN_INSURANCE = CorporateBusiness.loadCompany("justin_insurance");
	
	public static String getInsuranceStatusColorCode(Player player) {
		return "§6♻ ";
		
	}

}
