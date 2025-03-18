package net.itsrelizc.modchecker.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.UUID;

import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.packets.ServerEnigmaSubmitServerInsights.EnigmaInsightType;
import net.itsrelizc.modchecker.server.TerminalServer;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class ClientSpigotSubmitServerInsights extends SocketPacket implements ISocketPacket {
	
	public static final byte ID = (byte) 0x23;
	
	public static enum SpigotInsightType {
		
		CONNECT((byte) 0x40),
		JOIN((byte) 0x00);
		
		private byte header;

		private SpigotInsightType(byte header) {
			this.header = header;
		}
		
	}
	
	public ClientSpigotSubmitServerInsights(SpigotInsightType type, String playerName, String uniqueId) {
		
		writePacketId(ID);
		writeByte(type.header);
		writeString(playerName);
		writeString(uniqueId);
		
	}
	
	public ClientSpigotSubmitServerInsights(SpigotInsightType type) {
		
		writePacketId(ID);
		writeByte(type.header);
		
	}

	public ClientSpigotSubmitServerInsights(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() {
		readByte();

	}
	
	public static ClientHandler spigotClient = null;

	@Override
	public void handleServer(ClientHandler handler) {
		readByte();
		
		byte type = readByte();
		
		if (type == SpigotInsightType.JOIN.header) {
			
			String name = readString();
			UUID uuid = UUID.fromString(readString());
			
			System.out.println(name + " " + uuid + " JOINED");
			boolean f = false;
			
			for (UUID u : ClientSubmitPlayerCredentials.clients.keySet()) {
//				System.out.println(u + " " + uuid);
				if (u.equals(uuid)) f = true;
			}
			
			if (!f) {
				System.out.println("Enigma Disconnected for " + name + " " + uuid);
				
				
				ServerEnigmaSubmitServerInsights packet = new ServerEnigmaSubmitServerInsights(EnigmaInsightType.KICK, name, uuid.toString());
				handler.sendPacket(packet);
			}
		} else if (type == SpigotInsightType.CONNECT.header && spigotClient == null) {
			
			spigotClient = handler;
			System.out.println("Spigot Client Connected");
			
		}
	}

}
