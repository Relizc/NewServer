package net.itsrelizc.modchecker.packets;

import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public interface ISocketPacket {
	
	
	
	public void handleClient();
	public void handleServer();
	public void handleServer(ClientHandler handler);

}
