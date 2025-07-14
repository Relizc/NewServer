package net.itsrelizc.health2.fletching;

import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.LocaleSession;

public class FletchingMenu extends MenuTemplate2 {
	
	LocaleSession session;

	public FletchingMenu(String title) {
		super(title);
		session = new LocaleSession(getPlayer());
		
		// TODO Auto-generated constructor stub
	}

}
