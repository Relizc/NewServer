package net.itsrelizc.modchecker.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;

import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.server.TerminalServer;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class DualPingPong extends SocketPacket implements ISocketPacket {
	
	public static final byte ID = (byte) 0x00;
	
	public DualPingPong() {
		
		writePacketId(ID);
		
	}

	public DualPingPong(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() {
		readByte();
		
		DualPingPong packet = new DualPingPong();
		TerminalClient.client.sendPacket(packet);
	}
	
	private static Map<ClientHandler, Long> lastPinged = new HashMap<ClientHandler, Long>();

	@Override
	public void handleServer(ClientHandler handler) {
		readByte();
		
		System.out.println("Recieved Ping Packet from " + handler);
		lastPinged.put(handler, System.currentTimeMillis());
	}
	
	public static void startKickingUnpinged() {
		
	}

}
