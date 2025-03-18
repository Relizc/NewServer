package net.itsrelizc.modchecker.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.server.TerminalServer;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class ClientSubmitPlayerCredentials extends SocketPacket implements ISocketPacket {
	
	public static final byte ID = (byte) 0x48;
	
	public static Map<UUID, ClientHandler> clients = new HashMap<UUID, ClientHandler>();
	
	public ClientSubmitPlayerCredentials(String name, String uuid) {
		
		writePacketId(ID);
		writeString(name);
		writeString(uuid);
		
	}

	public ClientSubmitPlayerCredentials(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() {
		readByte();

	}

	@Override
	public void handleServer(ClientHandler handler) {
		readByte();
		
		String name = readString();
		UUID uuid = UUID.fromString(readString());
		
		clients.put(uuid, handler);
		System.out.println("Player " + name + " logged in with minecraft UUID " + uuid);
		
		
	}

}
