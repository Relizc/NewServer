package net.itsrelizc.dimensions.end;

import net.itsrelizc.events.EventRegistery;

public class EndDimensionRaid {
	
	public static void enable() {
		EventRegistery.register(new PreventEndListener());
		
		NPCProfBrown.spawnme();
	}

}
