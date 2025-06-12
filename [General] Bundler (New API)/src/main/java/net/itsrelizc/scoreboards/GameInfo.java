package net.itsrelizc.scoreboards;

import net.itsrelizc.events.EventRegistery;

public class GameInfo {
	
	public static String gameName = "gamename.smp";

	public static void init() {
		EventRegistery.register(new GameInfoBoardHandler());
	}

}
