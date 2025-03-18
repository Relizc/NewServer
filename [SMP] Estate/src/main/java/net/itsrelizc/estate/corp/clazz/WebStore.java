package net.itsrelizc.estate.corp.clazz;

import net.itsrelizc.api.RelizcInteractiveMenu;
import net.itsrelizc.smp.corps.api.RelizcWebStore;

public class WebStore implements RelizcWebStore {

	@Override
	public void open(RelizcInteractiveMenu menu) {
		
		menu.getTemplate().defaultPreset();
		
	}

}
